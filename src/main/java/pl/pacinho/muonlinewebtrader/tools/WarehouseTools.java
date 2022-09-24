package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    private final WarehouseService warehouseService;
    private final WebWarehouseService webWarehouseService;
    private final WarehouseDecoder warehouseDecoder;
    private final ItemDecoder itemDecoder;

    private int checkSpaceForPutItem(ExtendedItemDto item, Warehouse warehouseByAccountName) {
        List<WareCellDto> freeCells = warehouseDecoder.decodeExtended(warehouseByAccountName.getContent())
                .stream()
                .filter(c -> c.getType() == CellType.FREE)
                .toList();

        if (freeCells.isEmpty()) return -1;

        if (item.getWidth() == 1 && item.getHeight() == 1) return freeCells.get(0).getNumber();

        Map<Integer, WareCellDto> cellsMap = freeCells.stream()
                .collect(Collectors.toMap(WareCellDto::getNumber, Function.identity()));

        for (WareCellDto cell : freeCells) {
            List<Integer> itemIndexes = CodeUtils.getCellsIndexesByItem(cell.getNumber(), item.getWidth(), item.getHeight());
            if (itemIndexes == null) continue;
            if (isFreeSpaceForItem(itemIndexes, cellsMap)) return cell.getNumber();
        }
        return -1;
    }

    private boolean isFreeSpaceForItem(List<Integer> itemIndexes, Map<Integer, WareCellDto> cellsMap) {
        return itemIndexes.stream()
                .allMatch(i -> {
                    WareCellDto wareCellDto = cellsMap.get(i);
                    if (wareCellDto == null) return false;
                    return wareCellDto.getType() == CellType.FREE;
                });
    }

    @Transactional
    public void transferToGame(String accountName, String code) throws IllegalStateException {
        int startPosition = checkSpaceForPutItem(itemDecoder.decode(code, -1), warehouseService.getWarehouseByAccountName(accountName));
        if (startPosition == -1) {
            throw new IllegalStateException("Not enough space in game warehouse for transfer selected item!");
        }
        webWarehouseService.removeItem(accountName, code);
        warehouseService.addItem(accountName, code, startPosition);
    }

    @Transactional
    public void transferToWeb(String accountName, String code) {
        warehouseService.removeItem(code, accountName);
        webWarehouseService.addItem(accountName, code);
    }

}