package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@RequiredArgsConstructor
public enum CellLocation {

    WARE(ImageUtils.encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/wareCell.png")),
    TRADE(ImageUtils.encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/tradeCell.png"));

    @Getter
    private final String icon;
}