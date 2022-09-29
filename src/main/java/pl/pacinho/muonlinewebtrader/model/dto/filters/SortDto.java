package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SortDto {

    private List<SortType> sortTypes;
    private List<SortMethod> sortMethods;
    private SortType type;
    private SortMethod method;

    public SortDto() {
        this.type = SortType.DATE;
        this.method = SortMethod.DESC;
        this.sortTypes = List.of(SortType.values());
        this.sortMethods = List.of(SortMethod.values());
    }
}
