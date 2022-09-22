package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class SimpleItemDto {

    private int id;
    private int section;
    @Setter
    private String name;
    @Setter
    private ItemType itemType;

    public int getNumber() {
        return (this.getSection() * 512) + this.getId();
    }
}