package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import pl.pacinho.muonlinewebtrader.entity.WebWallet;
import pl.pacinho.muonlinewebtrader.model.dto.WebWalletDto;

public class WebWalletDtoMapper {

    public static WebWalletDto parse(WebWallet webWallet) {
        return WebWalletDto.builder()
                .blessCount(webWallet.getBlessCount())
                .soulCount(webWallet.getSoulCount())
                .zenCount(webWallet.getZenAmount())
                .build();
    }
}