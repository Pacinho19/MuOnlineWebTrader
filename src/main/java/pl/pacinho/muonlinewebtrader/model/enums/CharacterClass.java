package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@RequiredArgsConstructor
public enum CharacterClass {

    BK(ImageUtils.getCharacterClassImages("bk")),
    SM(ImageUtils.getCharacterClassImages("sm")),
    DL(ImageUtils.getCharacterClassImages("dl")),
    MG(ImageUtils.getCharacterClassImages("mg")),
    ELF(ImageUtils.getCharacterClassImages("elf")),
    SUM(ImageUtils.getCharacterClassImages("sum")),
    RF(ImageUtils.getCharacterClassImages("rf"));

    @Getter
    private final String icon;
}