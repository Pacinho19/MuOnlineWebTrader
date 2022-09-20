package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOptionsGroup;

public class ExcOptionParser {
    public static ExcOption parse(Integer i, ItemDto itemDto) {
        if (itemDto.getName().startsWith("Pendant")) return ExcOptionsGroup.WEAPONS.findByIdx(i);

        return itemDto.getItemType().getExcOptionsGroup()
                .findByIdx(i);
    }
}