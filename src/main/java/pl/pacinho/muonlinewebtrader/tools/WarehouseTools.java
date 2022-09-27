package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.model.dto.ExtendedItemDto;
import pl.pacinho.muonlinewebtrader.model.dto.PaymentItemsDto;
import pl.pacinho.muonlinewebtrader.model.dto.TransferJewelsDto;
import pl.pacinho.muonlinewebtrader.model.dto.WareCellDto;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWalletService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    private final Long GAME_WAREHOUSE_ZEN_LIMIT = 2_000_000_000L;
    private final WarehouseService warehouseService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final WebWarehouseService webWarehouseService;
    private final WebWalletService webWalletService;
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
        Map<PaymentItem, List<ExtendedItemDto>> paymentItems =
                getPaymentsItemsFromWebWarehouse(name)
                        .stream()
                        .collect(Collectors.groupingBy(i -> PaymentItem.fromNumber(i.getNumber())));

        return PaymentItemsDto.builder()
                .zenAmount(webWarehouseService.findZenByAccountName(name))
                .blessCount(paymentItemSum(paymentItems, PaymentItem.BLESS)
                        + paymentItemSum(paymentItems, PaymentItem.BLESS_BUNDLE))
                .soulCount(paymentItemSum(paymentItems, PaymentItem.SOUL)
                        + paymentItemSum(paymentItems, PaymentItem.SOUL_BUNDLE))
                .build();
    }

    private List<ExtendedItemDto> getPaymentsItemsFromWebWarehouse(String name) {
        return webWarehouseItemService.getWarehouseItemsByAccountName(name)
                .stream()
                .map(wwe -> itemDecoder.decode(wwe.getItem(), -1))
                .filter(i -> PaymentItem.checkNumber(i.getNumber()))
                .toList();
    }

    private Long paymentItemSum(Map<PaymentItem, List<ExtendedItemDto>> paymentItems, PaymentItem paymentItem) {
        List<ExtendedItemDto> items = paymentItems.get(paymentItem);
        if (items == null) return 0L;

        if (!paymentItem.isBundle())
            return items.stream().count();

        return items
                .stream()
                .map(i -> ItemUtils.getItemCountFromBundle(i.getLevel()))
                .reduce(0, Integer::sum)
                .longValue();
    }

    public void transferBlessToWallet(String name, Integer blessCount) throws IllegalStateException {
        transferJewelToWallet(name, blessCount, PaymentMethod.BLESS);
    }

    public void transferSoulToWallet(String name, Integer soulCount) throws IllegalStateException {
        transferJewelToWallet(name, soulCount, PaymentMethod.SOUL);
    }

    private void transferJewelToWallet(String name, Integer count, PaymentMethod paymentMethod) throws IllegalStateException {
        if (count == null || count == 0)
            throw new IllegalStateException("Item count must be positive number!");

        PaymentItemsDto paymentsItem = getPaymentsItem(name);
        if (paymentsItem.getCountByType(paymentMethod) < count)
            throw new IllegalStateException("Not enough " + paymentMethod.getName() + " found in Web Warehouse !");

        removeJewelFromWebWarehouse(name, count, paymentMethod.getPaymentItems());
        webWalletService.addToWallet(name, count, paymentMethod);
    }

    private void removeJewelFromWebWarehouse(String name, Integer count, List<PaymentItem> paymentItemsTypes) {
        List<ExtendedItemDto> items = getPaymentsItemsFromWebWarehouse(name)
                .stream()
                .filter(ei -> paymentItemsTypes.contains(PaymentItem.fromNumber(ei.getNumber())))
                .sorted(Comparator.comparing(ExtendedItemDto::getNumber).reversed()
                        .thenComparing(ExtendedItemDto::getLevel))
                .toList();

        TransferJewelsDto tj = new TransferJewelsDto();
        AtomicInteger countI = new AtomicInteger(count);
        items.forEach(ei -> {
            if (countI.get() <= 0) return;

            if (!PaymentItem.fromNumber(ei.getNumber()).isBundle()) {
                tj.putItemToRemove(ei.getCode());
                countI.decrementAndGet();
                return;
            } else {
                tj.putItemToRemove(ei.getCode());
                countI.set(countI.get() - ItemUtils.getItemCountFromBundle(ei.getLevel()));
            }
        });

        IntStream.range(0, Math.abs(countI.get()))
                .forEach(i -> tj.putItemToAdd(ItemUtils.getItemCode(PaymentItem.SOUL)));

        tj.getItemsToRemove()
                .forEach(code -> webWarehouseItemService.removeItem(name, code));

        tj.getItemsToAdd()
                .forEach(code -> webWarehouseItemService.addItem(name, code));
    }

    public void transferZenToWallet(String name, Integer count) throws IllegalStateException {
        if (count == null || count == 0)
            throw new IllegalStateException("Zen ammount must be positive number!");

        Long webZen = webWarehouseService.findZenByAccountName(name);
        if (webZen < count)
            throw new IllegalStateException("Not enough zen found in Web Warehouse !");

        webWarehouseService.subtractZen(name, Long.valueOf(count));
        webWalletService.addToWallet(name, count, PaymentMethod.ZEN);
    }
}