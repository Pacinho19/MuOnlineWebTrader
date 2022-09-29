package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class FilterDto {

    private List<ClassFilterDto> classFilter;
    private PriceFilterDto priceFilter;
    private List<PaymentMethodFilter> paymentMethods;
    private List<CategoryFilter> categoryFilter;
    private int pageSize;
    private List<Integer> pageSizeOptions;
    private SortDto sort;

    private boolean myOffers;

    public FilterDto() {
        this.classFilter = Stream.of(CharacterClass.values())
                .map(ClassFilterDto::new)
                .collect(Collectors.toList());
        this.categoryFilter = Stream.of(ItemType.values())
                .map(CategoryFilter::new)
                .collect(Collectors.toList());
        this.paymentMethods = List.of(PaymentMethodFilter.values());
        this.priceFilter = new PriceFilterDto();
        this.pageSize = 5;
        pageSizeOptions = List.of(5, 10, 15, 20, 25);
        this.sort = new SortDto();
        this.myOffers = false;
    }
}