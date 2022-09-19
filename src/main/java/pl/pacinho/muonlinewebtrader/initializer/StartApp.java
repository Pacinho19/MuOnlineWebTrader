package pl.pacinho.muonlinewebtrader.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;
import pl.pacinho.muonlinewebtrader.service.ItemService;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;

import java.io.File;

@RequiredArgsConstructor
//@Component
public class StartApp {

    private final ItemService itemService;

    //@EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        FileUtils.readTxt(new File("items.csv"))
                .stream()
                .skip(1)
                .map(s -> s.split(";"))
                .map(arr -> new Item(
                        Integer.parseInt(arr[0]),
                        Integer.parseInt(arr[1]),
                        ItemType.getItemType(arr[3]),
                        arr[4]
                ))
                .forEach(itemService::save);
    }
}