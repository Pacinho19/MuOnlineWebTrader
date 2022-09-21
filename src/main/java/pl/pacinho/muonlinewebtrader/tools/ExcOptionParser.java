package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOptionsGroup;

public class ExcOptionParser {
    public static ExcOption parse(Integer i, ExtendedItemDto extendedItemDto) {
        if (extendedItemDto.getName().startsWith("Pendant")) return ExcOptionsGroup.WEAPONS.findByIdx(i);

        return extendedItemDto.getItemType().getExcOptionsGroup()
                .findByIdx(i);
    }
}