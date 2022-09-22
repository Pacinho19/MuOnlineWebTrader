package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemIconsService {

    private final ItemService itemService;

    public void setIcons() {
        List<File> files = FileUtils.readDirectory(FileUtils.IMG_LOCATION);
        if(files==null) return;

        Map<Object, List<File>> icons = files
                .stream()
                .collect(Collectors.groupingBy(file -> file.getName().split("\\.")[0].trim()));

        itemService.findAll()
                .forEach(i -> {
                    List<File> file = icons.get(i.getName());
                    if (file == null) return;

                    i.setIconPath(file.get(0).getPath());
                    itemService.save(i);
                });

    }
}
