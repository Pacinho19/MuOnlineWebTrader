package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.*;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ItemDto {

    private int id;
    private int section;
    private String serialNumber;
    @Getter(AccessLevel.NONE)
    private int number;
    @Setter
    private String name;
    @Setter
    private ItemType itemType;
    private int level;
    private boolean luck;
    private boolean skill;
    private boolean exc;
    @Setter
    private List<ExcOption> excOptions;

    public int getNumber() {
        return (section * 512) + id;
    }
}