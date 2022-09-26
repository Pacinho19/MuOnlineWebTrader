package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;
import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.PriceDto;
import pl.pacinho.muonlinewebtrader.tools.ItemDecoder;

@Component
@RequiredArgsConstructor
public class ItemShopDtoMapper {

    private final ItemDecoder itemDecoder;

    public ItemShopDto parse(ItemShop itemShop) {
        return new ItemShopDto(
                itemDecoder.decode(itemShop.getItem(), -1)
                , new PriceDto(itemShop.getZenPrice(), itemShop.getBlessPrice(), itemShop.getSoulPrice())
                , itemShop.getSellerAccount().getName()
                , itemShop.getViews()
                , itemShop.getAddDate()
        );
    }
}
