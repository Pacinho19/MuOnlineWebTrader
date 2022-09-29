package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortType {

    NAME("Name"),
    DATE("Add date"),
    PRICE_BLESS("Bless price"),
    PRICE_SOUL("Soul price"),
    PRICE_ZEN("Zen price"),
    CATEGORY("Category"),
    VIEWS("Views");

    @Getter
    private final String text;

}
