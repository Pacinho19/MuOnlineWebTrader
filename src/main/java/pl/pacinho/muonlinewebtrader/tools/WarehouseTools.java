package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.model.dto.*;
import pl.pacinho.muonlinewebtrader.model.enums.*;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDescription;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDirection;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionType;
import pl.pacinho.muonlinewebtrader.service.*;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    public static final String EMPTY_WARE = createEmptyWarehouse();
    private final Long GAME_WAREHOUSE_ZEN_LIMIT = 2_000_000_000L;
    private final WarehouseService warehouseService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final WebWarehouseService webWarehouseService;
    private final WebWalletService webWalletService;
    private final TransactionService transactionService;
    private final WarehouseDecoder warehouseDecoder;
    private final ItemDecoder itemDecoder;

    private int checkSpaceForPutItem(ExtendedItemDto item, Warehouse warehouseByAccountName, CellLocation location) {
        List<WareCellDto> freeCells = warehouseDecoder.decodeExtended(warehouseByAccountName.getContent(), location)
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

    @Transactional
    public void transferToGame(String accountName, String code) throws IllegalStateException {
        int startPosition = checkSpaceForPutItem(itemDecoder.decode(code, -1), warehouseService.getWarehouseByAccountName(accountName), CellLocation.WARE);
        if (startPosition == -1)
            throw new IllegalStateException("Not enough space in game warehouse for transfer selected item!");

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
                .zenCount(webWarehouseService.findZenByAccountName(name))
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

    @Transactional
    public void transferZenToWallet(String name, Integer count) throws IllegalStateException {
        if (count == null || count == 0)
            throw new IllegalStateException("Zen count must be positive number!");

        Long webZen = webWarehouseService.findZenByAccountName(name);
        if (webZen < count)
            throw new IllegalStateException("Not enough zen found in Web Warehouse !");

        webWarehouseService.subtractZen(name, Long.valueOf(count));
        webWalletService.addToWallet(name, count, PaymentMethod.ZEN);

        transactionService.addTransaction(
                name,
                new TransactionDto(TransactionDirection.INCOMING,
                        TransactionType.PAYMENT,
                        PaymentMethod.ZEN,
                        count.longValue(),
                        LocalDateTime.now(),
                        TransactionDescription.SELF_PAYMENT.getText()
                )
        );
    }

    @Transactional
    public void transferBlessToWallet(String name, Integer blessCount) throws IllegalStateException {
        transferJewelToWallet(name, blessCount, PaymentMethod.BLESS);
    }

    @Transactional
    public void transferSoulToWallet(String name, Integer soulCount) throws IllegalStateException {
        transferJewelToWallet(name, soulCount, PaymentMethod.SOUL);
    }

    private void transferJewelToWallet(String name, Integer count, PaymentMethod paymentMethod) throws IllegalStateException {
        if (count == null || count == 0)
            throw new IllegalStateException("Item count must be positive number!");

        PaymentItemsDto paymentsItem = getPaymentsItem(name);
        if (paymentsItem.getCountByType(paymentMethod) < count)
            throw new IllegalStateException("Not enough " + paymentMethod.getName() + " found in Web Warehouse !");

        removeJewelFromWebWarehouse(name, count, paymentMethod);
        webWalletService.addToWallet(name, count, paymentMethod);

        transactionService.addTransaction(
                name,
                new TransactionDto(TransactionDirection.INCOMING,
                        TransactionType.PAYMENT,
                        paymentMethod,
                        count.longValue(),
                        LocalDateTime.now(),
                        TransactionDescription.SELF_PAYMENT.getText()
                )
        );
    }

    private void removeJewelFromWebWarehouse(String name, Integer count, PaymentMethod paymentMethod) throws IllegalStateException {
        List<ExtendedItemDto> items = getPaymentsItemsFromWebWarehouse(name)
                .stream()
                .filter(ei -> paymentMethod.getPaymentItems().contains(PaymentItem.fromNumber(ei.getNumber())))
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

        for (String s : tj.getItemsToRemove()) {
            webWarehouseItemService.removeItem(name, s);
        }

        checkJewelsToPutBack(countI, paymentMethod, tj);
        tj.getItemsToAdd()
                .forEach(code -> webWarehouseItemService.addItem(name, code));
    }

    private void checkJewelsToPutBack(AtomicInteger countI, PaymentMethod paymentMethod, TransferJewelsDto tj) {
        int jewelsCount = Math.abs(countI.get());
        Map<Integer, Integer> bundleMap = bundleJewels(jewelsCount);
        int singleJewelsCount = jewelsCount - ItemUtils.bundleItemCount(bundleMap);

        IntStream.range(0, singleJewelsCount)
                .forEach(i -> tj.putItemToAdd(ItemUtils.getItemCode(
                        paymentMethod.getItem(false), 0
                )));

        bundleMap.forEach((level, count) ->
                IntStream.range(0, count)
                        .forEach(i -> tj.putItemToAdd(ItemUtils.getItemCode(
                                paymentMethod.getItem(true), level
                        ))));
    }

    private Map<Integer, Integer> bundleJewels(int count) {
        Map<Integer, Integer> bundleMap = new HashMap<>();
        int bundleLevel = 2;
        while (bundleLevel >= 0) {
            int bundleCount = count / ItemUtils.getItemCountFromBundle(bundleLevel);
            if (bundleCount > 0) {
                count -= bundleCount * ItemUtils.getItemCountFromBundle(bundleLevel);
                bundleMap.put(bundleLevel, bundleCount);
            }
            bundleLevel--;
        }
        return bundleMap;
    }

    @Transactional
    public void disbursementBlessFromWallet(String name, Integer blessCount) throws IllegalStateException {
        disbursementJewelsFromWallet(name, blessCount, PaymentMethod.BLESS);
    }

    @Transactional
    public void disbursementSoulFromWallet(String name, Integer soulCount) throws IllegalStateException {
        disbursementJewelsFromWallet(name, soulCount, PaymentMethod.SOUL);
    }

    @Transactional
    public void disbursementZenFromWallet(String name, Integer zenCount) throws IllegalStateException {
        if (zenCount == null || zenCount == 0)
            throw new IllegalStateException("Zen count must be positive number!");

        Long walletZen = webWalletService.findZenByAccountName(name);
        if (walletZen < zenCount)
            throw new IllegalStateException("Not enough zen found in Web Wallet !");

        webWarehouseService.addZen(name, Long.valueOf(zenCount));
        webWalletService.addToWallet(name, (-1 * zenCount), PaymentMethod.ZEN);

        transactionService.addTransaction(
                name,
                new TransactionDto(TransactionDirection.OUTGOING,
                        TransactionType.DISBURSEMENT,
                        PaymentMethod.ZEN,
                        (-1L * zenCount),
                        LocalDateTime.now(),
                        TransactionDescription.SELF_DISBURSEMENT.getText()
                )
        );
    }

    private void disbursementJewelsFromWallet(String name, Integer count, PaymentMethod paymentMethod) throws IllegalStateException {
        if (count == null || count == 0)
            throw new IllegalStateException("Item count must be positive number!");

        long jewelsInWallet = getJewelsCountInWallet(name, paymentMethod);
        if (jewelsInWallet < count)
            throw new IllegalStateException("Not enough " + paymentMethod.getName() + " found in Web Wallet !");

        webWalletService.addToWallet(name, (-1 * count), paymentMethod);
        transferJewelsFromWebWallet(name, count, paymentMethod);

        transactionService.addTransaction(
                name,
                new TransactionDto(TransactionDirection.OUTGOING,
                        TransactionType.DISBURSEMENT,
                        paymentMethod,
                        (-1L * count),
                        LocalDateTime.now(),
                        TransactionDescription.SELF_DISBURSEMENT.getText()
                )
        );
    }

    private void transferJewelsFromWebWallet(String name, Integer count, PaymentMethod paymentMethod) {
        Map<Integer, Integer> bundleMap = bundleJewels(count);
        int singleJewelsCount = count - ItemUtils.bundleItemCount(bundleMap);

        IntStream.range(0, singleJewelsCount)
                .forEach(i -> webWarehouseItemService.addItem(
                        name,
                        ItemUtils.getItemCode(paymentMethod.getItem(false), 0
                        )));

        bundleMap.forEach((level, bundleCount) ->
                IntStream.range(0, bundleCount)
                        .forEach(i -> webWarehouseItemService.addItem(
                                name,
                                ItemUtils.getItemCode(paymentMethod.getItem(true), level
                                ))));
    }

    private long getJewelsCountInWallet(String name, PaymentMethod paymentMethod) {
        WebWalletDto webWallet = webWalletService.findByAccountName(name);
        switch (paymentMethod) {
            case BLESS -> {
                return webWallet.getBlessCount();
            }
            case SOUL -> {
                return webWallet.getSoulCount();
            }
        }
        return 0;
    }

    private static String createEmptyWarehouse() {
        return "0x"
               + IntStream.range(0, CodeUtils.WAREHOUSE_CELLS_COUNT)
                       .boxed()
                       .map(i -> CodeUtils.EMPTY_CODE)
                       .collect(Collectors.joining());
    }

    public static boolean isFreeSpaceForItem(List<Integer> itemIndexes, Map<Integer, WareCellDto> cellsMap) {
        return itemIndexes.stream()
                .allMatch(i -> {
                    WareCellDto wareCellDto = cellsMap.get(i);
                    if (wareCellDto == null) return false;
                    return wareCellDto.getType() == CellType.FREE;
                });
    }

    public static void blockingItemCells(Map<Integer, WareCellDto> cellMap) {
        cellMap.values()
                .stream()
                .filter(i -> i.getType() == CellType.ITEM)
                .forEach(c -> {
                    ItemWareCellDto item = (ItemWareCellDto) c;
                    if (item.getCellsIdx() != null)
                        item.getCellsIdx()
                                .forEach(ci -> {
                                    if (ci != item.getNumber()) cellMap.get(ci).setType(CellType.BLOCKED);
                                });
                });
    }


    public static List<WareCellDto> unlockCells(List<WareCellDto> items2, List<Integer> cellsIdx) {
        List<WareCellDto> temp = new ArrayList<>(items2);
        items2.forEach(i -> {
            if (i.getType() == CellType.BLOCKED && cellsIdx.contains(i.getNumber())) {
                temp.remove(i.getNumber());
                temp.add(i.getNumber(), FreeWareCellDto.createFreeCell(i.getRowNumber(), i.getColNumber(), CellLocation.TRADE));
            }
        });
        return temp;
    }
}