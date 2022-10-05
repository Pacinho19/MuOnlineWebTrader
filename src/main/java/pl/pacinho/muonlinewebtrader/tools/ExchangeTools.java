package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.pacinho.muonlinewebtrader.entity.WebWallet;
import pl.pacinho.muonlinewebtrader.model.dto.CurrencyRateDto;
import pl.pacinho.muonlinewebtrader.model.dto.TransactionDto;
import pl.pacinho.muonlinewebtrader.model.dto.WebWalletDto;
import pl.pacinho.muonlinewebtrader.model.enums.ExchangeType;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDirection;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionType;
import pl.pacinho.muonlinewebtrader.service.TransactionService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;
import pl.pacinho.muonlinewebtrader.utils.MathUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ExchangeTools {

    private final TransactionService transactionService;
    private final WebWalletService webWalletService;
    private final CurrencyRateDto currencyRateDto = new CurrencyRateDto();

    @Transactional
    public void exchange(String name, Long count, ExchangeType type) {
        if (count <= 0)
            throw new IllegalStateException("Invalid value ! Count must be positive number!");

        WebWalletDto webWallet = webWalletService.findByAccountName(name);
        checkCount(count, type, webWallet);
        checkValue(count, type);
        doExchange(name, count, type);
        addToTransactionHistory(name, count, type);
    }

    private void addToTransactionHistory(String name, Long count, ExchangeType type) {
        transactionService.addTransaction(name,
                new TransactionDto(TransactionDirection.OUTGOING,
                        TransactionType.EXCHANGE,
                        type.getFrom(),
                        type.getFromValue(count),
                        LocalDateTime.now(),
                        "Exchange of " + type.getFrom().getName() + " into " + type.getTo().getName()));

        transactionService.addTransaction(name,
                new TransactionDto(TransactionDirection.INCOMING,
                        TransactionType.EXCHANGE,
                        type.getTo(),
                        type.getTargetValue(count),
                        LocalDateTime.now(),
                        "Exchange of " + type.getFrom().getName() + " into " + type.getTo().getName()));
    }

    private void doExchange(String name, Long count, ExchangeType type) {
        webWalletService.addToWallet(name, type.getFromValue(count).intValue(), type.getFrom());
        webWalletService.addToWallet(name, type.getTargetValue(count).intValue(), type.getTo());
    }

    private void checkValue(Long count, ExchangeType type) {
        switch (type) {
            case BTS -> {
                BigDecimal calculate = currencyRateDto.getBlessToSoulRate().multiply(BigDecimal.valueOf(count));
                if (MathUtils.getNumberOfDecimalPlaces(calculate) > 0)
                    throw new IllegalStateException("Invalid value! Bless count must be even number!");
            }
            case STB -> {
                BigDecimal calculate = currencyRateDto.getSoulToBlessRate().multiply(BigDecimal.valueOf(count));
                if (MathUtils.getNumberOfDecimalPlaces(calculate) > 0)
                    throw new IllegalStateException("Invalid value! Soul count must be divisible by 4");
            }
            default -> throw new IllegalStateException("Invalid type!");
        }

    }

    private void checkCount(Long count, ExchangeType type, WebWalletDto webWallet) {
        switch (type) {
            case BTS -> {
                if (webWallet.getBlessCount() < count)
                    throw new IllegalStateException("Not enough " + type.getFrom().getName() + " !");
            }
            case STB -> {
                if (webWallet.getSoulCount() < count)
                    throw new IllegalStateException("Not enough " + type.getFrom().getName() + " !");
            }
            default -> throw new IllegalStateException("Invalid type!");

        }
    }
}
