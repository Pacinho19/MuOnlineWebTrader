package pl.pacinho.muonlinewebtrader.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.ItemIconsService;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@Component
public class StartApp {

    private final ItemService itemService;
    private final AccountService accountService;
    private final ItemIconsService itemIconsService;
    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        initItems();
        initItemsIcons();
        initUsers();
    }

    private void initItemsIcons() {
        itemIconsService.setIcons();
    }

    private void initUsers() {
        if(accountService.getCount()>0) return;
        accountService.save(new Account("Testowy", "test"));
    }

    private void initItems() {
        if (itemService.getCount() > 0) return;

        FileUtils.readTxt(new File("items.csv"))
                .stream()
                .skip(1)
                .map(s -> s.split(";"))
                .map(arr -> new Item(
                        Integer.parseInt(arr[1]),
                        Integer.parseInt(arr[0]),
                        Integer.parseInt(arr[2]),
                        ItemType.getItemType(arr[4]),
                        arr[5]
                ))
                .forEach(itemService::save);
    }
}