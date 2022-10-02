package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class TradeOfferDto {

    private String sellerName;
    private String buyerName;
    private String content;
}