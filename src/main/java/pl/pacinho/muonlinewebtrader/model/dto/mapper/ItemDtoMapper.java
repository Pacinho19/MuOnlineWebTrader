package pl.pacinho.muonlinewebtrader.model.dto.mapper;

import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.dto.ItemDto;

import java.util.List;

public class ItemDtoMapper {
    public static List<ItemDto> parseList(List<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::parse)
                .toList();
    }

    private static ItemDto parse(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .itemType(item.getCategory())
                .id(item.getIdx())
                .section(item.getSection())
                .build();
    }
}
