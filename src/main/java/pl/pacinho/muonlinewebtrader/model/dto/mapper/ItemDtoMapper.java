package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.SimpleItemDto;

import java.util.List;

public class ItemDtoMapper {
    public static List<SimpleItemDto> parseList(List<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::parse)
                .toList();
    }

    private static SimpleItemDto parse(Item item) {
        return SimpleItemDto.builder()
                .name(item.getName())
                .itemType(item.getCategory())
                .id(item.getIdx())
                .section(item.getSection())
                .build();
    }
}