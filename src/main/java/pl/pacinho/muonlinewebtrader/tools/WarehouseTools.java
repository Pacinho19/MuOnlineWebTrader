package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.PaymentItemsDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    private final Long GAME_WAREHOUSE_ZEN_LIMIT = 2_000_000_000L;
    private final WarehouseService warehouseService;
    private final WebWarehouseItemService webWarehouseItemService;
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
        webWarehouseItemService.removeItem(accountName, code);
        warehouseService.addItem(accountName, code, startPosition);
    }

    @Transactional
    public void transferToWeb(String accountName, String code) {
        warehouseService.removeItem(code, accountName);
        webWarehouseItemService.addItem(accountName, code);
    }

    @Transactional
    public void transferZenToWebWarehouse(String name, Long zen) throws IllegalStateException {
        if (zen == null)
            throw new IllegalStateException("Amount of zen cannot be empty! ");

        Long zenWare = warehouseService.findZenByAccountName(name);
        if (zen > zenWare)
            throw new IllegalStateException("Not enough zen in warehouse! Your value in warehouse " + zenWare);

        warehouseService.subtractZenValue(zen, name);
        webWarehouseService.addZen(name, zen);
    }

    public void transferZenToGameWarehouse(String name, Long zen) throws IllegalStateException {
        if (zen == null)
            throw new IllegalStateException("Amount of zen cannot be empty! ");

        Long zenWare = webWarehouseService.findZenByAccountName(name);
        if (zen > zenWare)
            throw new IllegalStateException("Not enough zen in web warehouse! Your value in web warehouse " + zenWare);

        Long zenGameWare = warehouseService.findZenByAccountName(name);
        if (zenGameWare + zen > GAME_WAREHOUSE_ZEN_LIMIT)
            throw new IllegalStateException("Amount zen is to big! Limit in game warehouse is " + GAME_WAREHOUSE_ZEN_LIMIT);

        warehouseService.addZenValue(zen, name);
        webWarehouseService.subtractZen(name, zen);
    }

    public PaymentItemsDto getPaymentsItem(String name) {
        Map<PaymentItem, List<ExtendedItemDto>> paymentItems = webWarehouseItemService.getWarehouseItemsByAccountName(name)
                .stream()
                .map(wwe -> itemDecoder.decode(wwe.getItem(), -1))
                .filter(i -> PaymentItem.checkNumber(i.getNumber()))
                .collect(Collectors.groupingBy(i -> PaymentItem.fromNumber(i.getNumber())));

        return PaymentItemsDto.builder()
                .zenAmount(webWarehouseService.findZenByAccountName(name))
                .blessCount(paymentItemSum(paymentItems, PaymentItem.BLESS)
                        + paymentItemSum(paymentItems, PaymentItem.BLESS_BUNDLE))
                .soulCount(paymentItemSum(paymentItems, PaymentItem.SOUL)
                        + paymentItemSum(paymentItems, PaymentItem.SOUL_BUNDLE))
                .build();
    }

    private Long paymentItemSum(Map<PaymentItem, List<ExtendedItemDto>> paymentItems, PaymentItem paymentItem) {
        List<ExtendedItemDto> items = paymentItems.get(paymentItem);
        if (items == null) return 0L;

        if (!paymentItem.isBundle())
            return items.stream().count();

        return items
                .stream()
                .map(i -> (i.getLevel() * 10) + 10)
                .reduce(0, Integer::sum)
                .longValue();
    }
}