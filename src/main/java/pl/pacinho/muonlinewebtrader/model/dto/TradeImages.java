package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.utils.ImageUtils;

@Getter
public final class TradeImages {

    private final String ACCEPT;
    private final String DECLINE;
    private final String BACKGROUND;

    public TradeImages() {
        this.ACCEPT = ImageUtils.ACCEPT_TRADE_IMAGE;
        this.DECLINE = ImageUtils.DECLINE_TRADE_IMAGE;
        this.BACKGROUND = ImageUtils.BACKGROUND_TRADE_IMAGE;
    }
}