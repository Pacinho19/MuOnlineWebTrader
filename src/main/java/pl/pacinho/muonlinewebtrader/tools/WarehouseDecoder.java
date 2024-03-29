package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.FreeWareCellDto;
import pl.pacinho.muonlinewebtrader.model.dto.ItemWareCellDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellLocation;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseDecoder {
    private final ItemDecoder itemDecoder;

    public List<ExtendedItemDto> decode(String wareContent) {
        if (wareContent.startsWith("0x")) wareContent = wareContent.substring(2);
        int maxSize = CodeUtils.ITEM_CHUNK_SIZE * CodeUtils.WAREHOUSE_CELLS_COUNT;
        if (wareContent.length() > maxSize) wareContent = wareContent.substring(0, CodeUtils.ITEM_CHUNK_SIZE * CodeUtils.WAREHOUSE_CELLS_COUNT);

        final String wareContentF = wareContent;
        String[] content = wareContentF.split("(?<=\\G.{" + CodeUtils.ITEM_CHUNK_SIZE + "})");
        return IntStream.range(0, content.length)
                .boxed()
                .map(i -> itemDecoder.decode(content[i], i * CodeUtils.ITEM_CHUNK_SIZE))
                .filter(Objects::nonNull)
                .toList();

    }

    public List<ExtendedItemDto> decodeWebItems(List<WebWarehouseItem> warehouseByAccountName) {
        return warehouseByAccountName
                .stream()
                .map(ww -> itemDecoder.decode(ww.getItem(), -1))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<WareCellDto> decodeExtended(String wareContent, CellLocation location) {
        if (wareContent.startsWith("0x")) wareContent = wareContent.substring(2);
        int maxSize = CodeUtils.ITEM_CHUNK_SIZE * CodeUtils.WAREHOUSE_CELLS_COUNT;
        if (wareContent.length() > maxSize) wareContent = wareContent.substring(0, maxSize);

        final String wareContentF = wareContent;
        String[] content = wareContentF.split("(?<=\\G.{" + CodeUtils.ITEM_CHUNK_SIZE + "})");
        List<List<String>> rows = ListUtils.partition(Arrays.asList(content), 8);

        Map<Integer, WareCellDto> cellMap = IntStream.range(0, rows.size())
                .boxed()
                .map(row -> decodeItemsByRow(row, rows.get(row), location))
                .flatMap(List::stream)
                .collect(Collectors.toMap(WareCellDto::getNumber, Function.identity()));

        WarehouseTools.blockingItemCells(cellMap);

//        printToConsole(cellMap.values());

        return cellMap.values()
                .stream()
                .toList();
    }

    private List<WareCellDto> decodeItemsByRow(Integer rowNumber, List<String> cells, CellLocation location) {
        return IntStream.range(0, cells.size())
                .boxed()
                .map(cell -> {
                    int cellNumber = (rowNumber * CodeUtils.WAREHOUSE_ROW_SIZE) + cell;
                    ExtendedItemDto itemDto = itemDecoder.decode(cells.get(cell),
                            cellNumber * CodeUtils.ITEM_CHUNK_SIZE);

                    if (itemDto == null) return FreeWareCellDto.createFreeCell(rowNumber, cell, location);
                    return ItemWareCellDto.createItemCell(rowNumber, cell, cellNumber, itemDto);
                })
                .toList();
    }

    private void printToConsole(Collection<WareCellDto> values) {
        ListUtils.partition(values.stream().toList(), CodeUtils.WAREHOUSE_ROW_SIZE)
                .stream()
                .map(rows -> rows.stream()
                        .map(r -> r.getType().getSign())
                        .collect(Collectors.joining(" ", "", "\n")))
                .forEach(System.out::println);
    }
}