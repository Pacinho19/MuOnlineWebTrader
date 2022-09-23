package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.service.WarehouseService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class WarehouseTools {

    private final WarehouseService warehouseService;
    private final WebWarehouseService webWarehouseService;

    public boolean checkSpaceForPutItem(String code) {
        return true;
    }

    @SneakyThrows
    @Transactional
    public void transferToGame(String accountName, String code) {
        if (!checkSpaceForPutItem(code)) {
            throw new IllegalStateException("Not enough space in game warehouse for transfer selected item!");
        }
        webWarehouseService.removeItem(accountName, code);
        warehouseService.addItem(accountName, code);
    }

    @Transactional
    public void transferToWeb(String accountName, String code) {
        warehouseService.removeItem(code, accountName);
        webWarehouseService.addItem(accountName, code);
    }

}
