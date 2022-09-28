package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;
import pl.pacinho.muonlinewebtrader.repository.WebWarehouseItemRepository;

import javax.resource.spi.IllegalStateException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebWarehouseItemService {

    private final WebWarehouseItemRepository webWarehouseItemRepository;
    private final AccountService accountService;

    public List<WebWarehouseItem> getWarehouseItemsByAccountName(String accountName) {
        return webWarehouseItemRepository.findByAccountNameAndActive(accountName, 1)
                .stream()
                .sorted(Comparator.comparing(WebWarehouseItem::getId))
                .toList();
    }

    public void addItem(String accountName, String itemCode) {
        Account account = accountService.findByLogin(accountName);
        webWarehouseItemRepository.save(
                WebWarehouseItem.builder()
                        .item(itemCode)
                        .account(account)
                        .active(1)
                        .build());
    }

    @SneakyThrows
    public void removeItem(String accountName, String code) {
        Optional<WebWarehouseItem> webWareOpt = webWarehouseItemRepository.findByAccountNameAndItemAndActive(accountName, code, 1);
        if (webWareOpt.isEmpty())
            throw new IllegalStateException("Selected item not found in Web Warehouse!");

        WebWarehouseItem webWareItem = webWareOpt.get();
        webWareItem.setActive(0);
        webWarehouseItemRepository.save(webWareItem);
    }
}