package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import pl.pacinho.muonlinewebtrader.entity.Trade;
import pl.pacinho.muonlinewebtrader.model.dto.TradeOfferDto;

public class TradeOfferDtoMapper {
    public static TradeOfferDto parse(Trade trade) {
        return TradeOfferDto.builder()
                .id(trade.getId())
                .offerDate(trade.getOfferDate())
                .status(trade.getStatus())
                .receiverName(trade.getReceiverOffer().getAccount().getName())
                .senderName(trade.getSenderOffer().getAccount().getName())
                .build();
    }
}