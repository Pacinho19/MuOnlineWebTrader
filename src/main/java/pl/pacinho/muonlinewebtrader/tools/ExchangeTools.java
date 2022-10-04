package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.pacinho.muonlinewebtrader.model.dto.CurrencyRateDto;
import pl.pacinho.muonlinewebtrader.model.dto.WebWalletDto;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;
import pl.pacinho.muonlinewebtrader.utils.MathUtils;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class ExchangeTools {

    private final WebWalletService webWalletService;
    private final CurrencyRateDto currencyRateDto = new CurrencyRateDto();

    @Transactional
    public void exchange(String name, Long count, PaymentMethod type) {
        if (count <= 0)
            throw new IllegalStateException("Invalid value ! Count must be positive number!");

        WebWalletDto webWallet = webWalletService.findByAccountName(name);
        if (!checkCount(count, type, webWallet))
            throw new IllegalStateException("Not enough " + type.getName() + " !");

        checkValue(count, type);

    }

    private void checkValue(Long count, PaymentMethod type) {
        switch (type) {
            case ZEN -> throw new IllegalStateException("Invalid type!");
            case BLESS -> {
                BigDecimal calculate = currencyRateDto.getBlessToSoulRate().multiply(BigDecimal.valueOf(count));
                if (MathUtils.getNumberOfDecimalPlaces(calculate) > 0)
                    throw new IllegalStateException("Invalid value! Bless count must be even number!");
            }
            case SOUL -> {
                BigDecimal calculate = currencyRateDto.getSoulToBlessRate().multiply(BigDecimal.valueOf(count));
                if (MathUtils.getNumberOfDecimalPlaces(calculate) > 0)
                    throw new IllegalStateException("Invalid value! Soul count must be divisible by 4");
            }
        }
    }

    private boolean checkCount(Long count, PaymentMethod type, WebWalletDto webWallet) {
        switch (type) {
            case ZEN -> {
                throw new IllegalStateException("Invalid type!");
            }
            case BLESS -> {
                return webWallet.getBlessCount() >= count;
            }
            case SOUL -> {
                return webWallet.getSoulCount() >= count;
            }
        }
        return false;
    }
}
