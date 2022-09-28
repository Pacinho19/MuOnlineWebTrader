package pl.pacinho.muonlinewebtrader.tools.pageable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pacinho.muonlinewebtrader.tools.pageable.model.enums.PageItemType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {

    private PageItemType pageItemType;
    private int index;
    private boolean active;

}