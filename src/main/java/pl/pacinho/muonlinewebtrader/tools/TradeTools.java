package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.ItemWareCellDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;

import javax.resource.spi.IllegalStateException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TradeTools {
    private final WebWarehouseItemService webWarehouseItemService;
    private final ItemDecoder itemDecoder;

    public void putItem(String name, List<WareCellDto> tradeItems, String itemCode) throws IllegalStateException {
        if (!webWarehouseItemService.checkItemExists(name, itemCode))
            throw new IllegalStateException("Selected item doesn't exists in Web Warehouse  !");

        if (checkJustExistInTradeItems(tradeItems, itemCode))
            throw new IllegalStateException("Selected item has already been added!");

        boolean success = addItem(itemCode, tradeItems);
        if (!success)
            throw new IllegalStateException("Not enough space in for put this item!");
    }

    private boolean checkJustExistInTradeItems(List<WareCellDto> tradeItems, String itemCode) {
        return tradeItems.stream()
                .filter(i -> i.getType() == CellType.ITEM)
                .map(i -> (ItemWareCellDto) i)
                .anyMatch(i -> i.getExtendedItemDto().getCode().equals(itemCode));
    }

    private boolean putItem(List<WareCellDto> tradeItems, WareCellDto cell, ExtendedItemDto item, Map<Integer, WareCellDto> cellsMap) {
        tradeItems.remove(cell.getNumber());
        tradeItems.add(cell.getNumber(), ItemWareCellDto.createItemCell(cell.getRowNumber(), cell.getColNumber(), cell.getNumber(), item));
        if (cellsMap != null) {
            cellsMap.put(cell.getNumber(), tradeItems.get(cell.getNumber()));
            WarehouseTools.blockingItemCells(cellsMap);
        }
        return true;
    }

    private boolean addItem(String itemCode, List<WareCellDto> tradeItems) {
        ExtendedItemDto item = itemDecoder.decode(itemCode, -1);

        List<WareCellDto> freeCells = tradeItems
                .stream()
                .filter(c -> c.getType() == CellType.FREE)
                .toList();

        if (freeCells.isEmpty()) return false;

        if (item.getWidth() == 1 && item.getHeight() == 1)
            return putItem(tradeItems, freeCells.get(0), item, null);

        Map<Integer, WareCellDto> cellsMap = freeCells.stream()
                .collect(Collectors.toMap(WareCellDto::getNumber, Function.identity()));

        for (WareCellDto cell : freeCells) {
            List<Integer> itemIndexes = CodeUtils.getCellsIndexesByItem(cell.getNumber(), item.getWidth(), item.getHeight());
            if (itemIndexes == null) continue;
            if (WarehouseTools.isFreeSpaceForItem(itemIndexes, cellsMap)) {
                return putItem(tradeItems, cell, item, cellsMap);
            }
        }
        return false;
    }

}