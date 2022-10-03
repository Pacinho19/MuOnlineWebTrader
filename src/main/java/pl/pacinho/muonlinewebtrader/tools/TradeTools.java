package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.Trade;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;
import pl.pacinho.muonlinewebtrader.model.dto.*;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.TradeDtoMapper;
import pl.pacinho.muonlinewebtrader.model.enums.CellLocation;
import pl.pacinho.muonlinewebtrader.model.enums.CellType;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.TradeService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;

import javax.resource.spi.IllegalStateException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TradeTools {

    private final AccountService accountService;
    private final TradeService tradeService;
    private final WebWarehouseItemService webWarehouseItemService;
    private final ItemDecoder itemDecoder;
    private final TradeDtoMapper tradeDtoMapper;

    public List<WareCellDto> putItem(String name, List<WareCellDto> tradeItems, String itemCode) throws IllegalStateException {
        if (!webWarehouseItemService.checkItemExists(name, itemCode))
            throw new IllegalStateException("Selected item doesn't exists in Web Warehouse !");

        if (checkJustExistInTradeItems(tradeItems, itemCode))
            throw new IllegalStateException("Selected item has already been added!");

        boolean success = addItem(itemCode, tradeItems);
        if (!success)
            throw new IllegalStateException("Not enough space in for put this item!");

        return tradeItems;
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

    @Transactional
    public boolean sendOffer(HttpSession session, String name, String targetAccountName) throws IllegalStateException {
        if (targetAccountName == null)
            throw new IllegalStateException("Target Account not found!");

        if (targetAccountName.equals(name))
            throw new IllegalStateException("Stupid ! It's your account!");

        Account targetAccount = accountService.findByLogin(targetAccountName);
        if (targetAccount == null)
            throw new IllegalStateException(targetAccountName + " Account not found!");

        Object tradeItemsObj = session.getAttribute("tradeItems");
        if (tradeItemsObj == null)
            throw new IllegalStateException("No items for trade!");

        List<WareCellDto> items = (List<WareCellDto>) tradeItemsObj;
        if (checkEmpty(items))
            throw new IllegalStateException("No items for trade!");

        checkItemsForTrade(items, name, session);
        tradeService.sendOffer(accountService.findByLogin(name), hexTrade(items), targetAccount);
        removeWebWareItems(name, items);

        return true;
    }

    private void removeWebWareItems(String name, List<WareCellDto> items) {
        items.stream()
                .filter(i -> i.getType() == CellType.ITEM)
                .map(i -> (ItemWareCellDto) i)
                .forEach(i -> {
                    try {
                        webWarehouseItemService.removeItem(name, i.getExtendedItemDto().getCode());
                    } catch (IllegalStateException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private String hexTrade(List<WareCellDto> items) {
        return items.stream()
                .map(wc -> {
                    if (wc.getType() == CellType.FREE || wc.getType() == CellType.BLOCKED) return CodeUtils.EMPTY_CODE;
                    return ((ItemWareCellDto) wc).getExtendedItemDto().getCode();
                })
                .collect(Collectors.joining());
    }

    private void checkItemsForTrade(List<WareCellDto> items, String name, HttpSession session) throws IllegalStateException {
        List<WebWarehouseItem> webWare = webWarehouseItemService.getWarehouseItemsByAccountName(name);
        List<String> errors = new ArrayList<>();
        List<WareCellDto> items2 = new ArrayList<>(items);

        for (WareCellDto item : items) {
            if (item.getType() != CellType.ITEM) continue;
            if (item instanceof ItemWareCellDto itemCell) {
                if (webWare.stream().noneMatch(i -> i.getItem().equals(itemCell.getExtendedItemDto().getCode()))) {
                    errors.add(itemCell.getExtendedItemDto().getFullName());
                    items2.remove(item.getNumber());
                    items2.add(item.getNumber(), FreeWareCellDto.createFreeCell(item.getRowNumber(), item.getColNumber(), CellLocation.TRADE));
                    items2 = WarehouseTools.unlockCells(items2, itemCell.getCellsIdx());
                }
            }
        }
        if (errors.isEmpty()) return;

        String error = errors.stream().collect(Collectors.joining(", ", "", ""));
        session.setAttribute("tradeItems", items2);
        throw new IllegalStateException("Items : " +
                                        error
                                        + " doesn't exists in Web Warehouse!");
    }

    private boolean checkEmpty(List<WareCellDto> items) {
        return items.stream()
                .allMatch(i -> i.getType() == CellType.FREE);
    }

    public List<ExtendedItemDto> filterItems(List<WareCellDto> tradeItems, List<ExtendedItemDto> wareItems) {
        List<String> itemCodes = tradeItems.stream()
                .filter(ti -> ti.getType() == CellType.ITEM)
                .map(ti -> ((ItemWareCellDto) ti).getExtendedItemDto().getCode())
                .toList();
        return wareItems.stream()
                .filter(i -> !itemCodes.contains(i.getCode()))
                .toList();
    }

    public List<WareCellDto> removeItem(String code, Object tradeItemsObj) {
        if (tradeItemsObj == null) return ItemUtils.crateEmptyTrade();

        List<WareCellDto> items = (List<WareCellDto>) tradeItemsObj;
        List<WareCellDto> temp = new ArrayList<>();
        List<Integer> cellsIndex = null;
        for (WareCellDto item : items) {
            if (item.getType() != CellType.ITEM) {
                temp.add(item);
                continue;
            }

            ItemWareCellDto itemCell = (ItemWareCellDto) item;
            if (!itemCell.getExtendedItemDto().getCode().equals(code)) {
                temp.add(item);
                continue;
            }

            temp.add(FreeWareCellDto.createFreeCell(itemCell.getRowNumber(), itemCell.getColNumber(), CellLocation.TRADE));
            cellsIndex = itemCell.getCellsIdx();
        }
        return WarehouseTools.unlockCells(temp, cellsIndex);
    }

    public List<TradeDto> myOffers(String name) {
        return tradeService.findByName(name)
                .stream()
                .map(tradeDtoMapper::parse)
                .toList();
    }

    public TradeDto offerDetails(String name, String offerId) throws IllegalStateException {
        Optional<Trade> tradeOpt = tradeService.findByAccountAndOfferId(name, offerId);
        if (tradeOpt.isEmpty())
            throw new IllegalStateException("Not found selected offer!");

        return tradeDtoMapper.parse(tradeOpt.get());
    }
}