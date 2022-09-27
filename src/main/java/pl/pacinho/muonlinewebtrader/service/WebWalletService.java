package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.WebWallet;
import pl.pacinho.muonlinewebtrader.model.dto.WebWalletDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.WebWalletDtoMapper;
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

    private WebWallet save(WebWallet webWallet) {
        return webWalletRepository.save(webWallet);
    }
}