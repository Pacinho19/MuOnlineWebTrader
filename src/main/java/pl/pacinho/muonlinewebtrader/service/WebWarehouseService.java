package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.repository.WebWarehouseRepository;

import javax.resource.spi.IllegalStateException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebWarehouseService {

    private final WebWarehouseRepository webWarehouseRepository;

    public List<WebWarehouse> getWarehouseByAccountName(String accountName) {
        return webWarehouseRepository.findByAccountNameAndActive(accountName, 1);
    }

    public void addItem(Account account, String itemCode) {
        webWarehouseRepository.save(
                WebWarehouse.builder()
                        .item(itemCode)
                        .account(account)
                        .active(1)
                        .build());
    }

    @SneakyThrows
    public void removeItem(String name, String code) {
        Optional<WebWarehouse> webWareOpt = webWarehouseRepository.findByAccountNameAndItemAndActive(name, code, 1);
        if(webWareOpt.isEmpty())
            throw new IllegalStateException("Selected item not found in Web Warehouse!");

        WebWarehouse webWare = webWareOpt.get();
        webWare.setActive(0);
        webWarehouseRepository.save(webWare);
    }
}