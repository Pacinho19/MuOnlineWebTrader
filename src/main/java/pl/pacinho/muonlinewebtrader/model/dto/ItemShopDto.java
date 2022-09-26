package pl.pacinho.muonlinewebtrader.model.dto;

import java.time.LocalDateTime;

public record ItemShopDto(ExtendedItemDto itemDto, PriceDto priceDto, String sellerAccount, int views,
                          LocalDateTime addDate) {
}
