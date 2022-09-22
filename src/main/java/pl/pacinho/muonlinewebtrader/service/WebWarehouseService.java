package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.repository.WarehouseRepository;
import pl.pacinho.muonlinewebtrader.repository.WebWarehouseRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WebWarehouseService {

    private final WebWarehouseRepository webWarehouseRepository;

    public List<WebWarehouse> getWarehouseByAccountName(String accountName) {
        return webWarehouseRepository.findByAccountName(accountName);
    }

    public void addItem(Account account, String itemCode) {
        webWarehouseRepository.save(
                WebWarehouse.builder()
                        .item(itemCode)
                        .account(account)
                        .build());
    }
}