package pl.pacinho.muonlinewebtrader.model.dto;

import pl.pacinho.muonlinewebtrader.model.dto.filters.PaymentMethodFilter;

import java.time.LocalDateTime;

public record ItemShopDto(ExtendedItemDto itemDto, PriceDto priceDto, String sellerAccount, int views,
                          LocalDateTime addDate) {
    public long getPriceByType(PaymentMethodFilter paymentMethod) {
        switch (paymentMethod) {
            case NONE -> {
                return 0;
            }
            case BLESS -> {
                return priceDto().getPriceBless();
            }
            case SOUL -> {
                return priceDto().getPriceSoul();
            }
            case ZEN -> {
                return priceDto().getPriceZen();
            }
        }
        return 0;
    }
}
