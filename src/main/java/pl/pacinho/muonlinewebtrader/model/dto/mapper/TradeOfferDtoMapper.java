package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.TradeOffer;
import pl.pacinho.muonlinewebtrader.model.dto.TradeOfferDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellLocation;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.ItemUtils;
import pl.pacinho.muonlinewebtrader.tools.WarehouseDecoder;

@RequiredArgsConstructor
@Component
public class TradeOfferDtoMapper {

    private final WarehouseDecoder warehouseDecoder;

    public TradeOfferDto parse(TradeOffer tradeOffer) {
        return TradeOfferDto.builder()
                .accountName(tradeOffer.getAccount().getName())
                .content(tradeOffer.getContent())
                .cells(tradeOffer.getContent() == null
                        ? ListUtils.partition(ItemUtils.crateEmptyTrade(), CodeUtils.TRADE_COL_COUNT)
                        : ListUtils.partition(warehouseDecoder.decodeExtended(tradeOffer.getContent(), CellLocation.TRADE), CodeUtils.TRADE_COL_COUNT))
                .build();
    }
}
