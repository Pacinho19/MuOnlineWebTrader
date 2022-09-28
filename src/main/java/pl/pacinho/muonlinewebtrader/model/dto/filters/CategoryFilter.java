package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;

@Getter
public class CategoryFilter {

    private ItemType category;
    private boolean selected;

    public CategoryFilter(ItemType category) {
        this.category = category;
        this.selected = true;
    }
}