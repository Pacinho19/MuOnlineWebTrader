package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;

@Getter
@Builder
@AllArgsConstructor
public class ItemDto {

    private int id;
    private int section;
    @Getter(AccessLevel.NONE)
    private int number;
    private String name;
    private ItemType itemType;
    private int level;
    private boolean luck;
    private boolean skill;
    private boolean exc;

    public int getNumber() {
        return (section * 512) + id;
    }
}