package pl.pacinho.muonlinewebtrader.tools.filters;

import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.filters.*;

import java.util.List;

public class FilterUtils {
    public static List<ItemShopDto> filterItems(FilterDto filterDto, List<ItemShopDto> items) {
        return items.stream()
                .filter(item -> filterClasses(filterDto.getClassFilter(), item)
                                && filterCategories(filterDto.getCategoryFilter(), item)
                                && filterPrice(filterDto.getPriceFilter(), item))
                .toList();
    }

    private static boolean filterPrice(PriceFilterDto priceFilter, ItemShopDto item) {
        if (priceFilter.getPaymentMethod() == PaymentMethodFilter.NONE) return true;

        return (priceFilter.getAmountMin() == 0 || (priceFilter.getAmountMin() > 0 && item.getPriceByType(priceFilter.getPaymentMethod()) >= priceFilter.getAmountMin()))
               && (priceFilter.getAmountMax() == 0 || (priceFilter.getAmountMax() > 0 && item.getPriceByType(priceFilter.getPaymentMethod()) <= priceFilter.getAmountMax()));
    }

    private static boolean filterCategories(List<CategoryFilter> categoryFilter, ItemShopDto item) {
        return categoryFilter.stream()
                .filter(CategoryFilter::isSelected)
                .anyMatch(cf -> cf.getCategory() == item.itemDto().getItemType());
    }

    private static boolean filterClasses(List<ClassFilterDto> classFilter, ItemShopDto item) {
        return classFilter.stream()
                .filter(ClassFilterDto::isSelected)
                .anyMatch(cf -> item.itemDto().getCharacterClasses().contains(cf.getCharacterClass()));
    }
}
