package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class FilterDto {

    private List<ClassFilterDto> classFilter;
    private PriceFilterDto priceFilter;
    private List<CategoryFilter> categoryFilter;

    public FilterDto() {
        this.classFilter = Stream.of(CharacterClass.values())
                        .map(ClassFilterDto::new)
                        .collect(Collectors.toList());
        this.categoryFilter =  Stream.of(ItemType.values())
                .map(CategoryFilter::new)
                .collect(Collectors.toList());
    }
}