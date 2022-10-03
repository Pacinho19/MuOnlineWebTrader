package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Trade;
import pl.pacinho.muonlinewebtrader.model.dto.TradeDto;

@Component
@RequiredArgsConstructor
public class TradeDtoMapper {

    private final TradeOfferDtoMapper tradeOfferDtoMapper;

    public TradeDto parse(Trade trade) {
        return TradeDto.builder()
                .uuid(trade.getExtendedId())
                .offerDate(trade.getOfferDate())
                .status(trade.getStatus())
                .receiver(tradeOfferDtoMapper.parse(trade.getReceiverOffer()))
                .sender(tradeOfferDtoMapper.parse(trade.getSenderOffer()))
                .build();
    }
}