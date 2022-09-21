package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;

import java.util.List;

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
    @Setter
    private List<ExcOption> excOptions;

    private int level;
    private boolean luck;
    private boolean skill;
    private boolean exc;

    public int getNumber() {
        return (this.getSection() * 512) + this.getId();
    }
}