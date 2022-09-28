package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.WebWallet;
import pl.pacinho.muonlinewebtrader.model.dto.PaymentItemsDto;
import pl.pacinho.muonlinewebtrader.model.dto.WebWalletDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.WebWalletDtoMapper;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.repository.WebWalletRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebWalletService {

    private final WebWalletRepository webWalletRepository;
    private final AccountService accountService;

    public WebWalletDto findByAccountName(String name) {
        Optional<WebWallet> webWalletOpt = webWalletRepository.findByAccountName(name);
        if (webWalletOpt.isEmpty())
            return WebWalletDtoMapper.parse(save(WebWallet.empty(accountService.findByLogin(name))));
        return WebWalletDtoMapper.parse(webWalletOpt.get());
    }

    public WebWallet findEntityByAccountName(String name) {
        return webWalletRepository.findByAccountName(name)
                .get();
    }

    private WebWallet save(WebWallet webWallet) {
        return webWalletRepository.save(webWallet);
    }

    private void addBless(String name, int blessCount) {
        WebWallet webWallet = webWalletRepository.findByAccountName(name).get();
        webWallet.setBlessCount(webWallet.getBlessCount() + blessCount);
        save(webWallet);
    }

    private void addSoul(String name, Integer soulCount) {
        WebWallet webWallet = webWalletRepository.findByAccountName(name).get();
        webWallet.setSoulCount(webWallet.getSoulCount() + soulCount);
        save(webWallet);
    }

    private void addZen(String name, Integer count) {
        WebWallet webWallet = webWalletRepository.findByAccountName(name).get();
        webWallet.setZenAmount(webWallet.getZenAmount() + count);
        save(webWallet);
    }

    public void addToWallet(String name, Integer count, PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ZEN -> addZen(name, count);
            case BLESS -> addBless(name, count);
            case SOUL -> addSoul(name, count);
        }
    }

    public Long findZenByAccountName(String name) {
        return findByAccountName(name)
                .getZenCount();
    }
}