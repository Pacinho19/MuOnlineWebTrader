package pl.pacinho.muonlinewebtrader.tools.filters;

import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.filters.SortDto;
import pl.pacinho.muonlinewebtrader.model.dto.filters.SortMethod;

import java.util.Comparator;

public class ComparatorUtils {

    public static Comparator<ItemShopDto> itemShopDtoComparator(SortDto sort) {
        Comparator<ItemShopDto> comparing = null;
        switch (sort.getType()) {
            case NAME -> comparing = Comparator.comparing(i -> i.itemDto().getName());
            case DATE -> comparing = Comparator.comparing(ItemShopDto::addDate);
            case PRICE_BLESS -> comparing = Comparator.comparing(i -> i.priceDto().getPriceBless());
            case PRICE_SOUL -> comparing = Comparator.comparing(i -> i.priceDto().getPriceSoul());
            case PRICE_ZEN -> comparing = Comparator.comparing(i -> i.priceDto().getPriceZen());
            case CATEGORY -> comparing = Comparator.comparing(i -> i.itemDto().getItemType().getName());
            case VIEWS -> comparing = Comparator.comparing(ItemShopDto::views);
        }
        return sort.getMethod() == SortMethod.DESC
                ? comparing.reversed()
                : comparing;
    }
}
