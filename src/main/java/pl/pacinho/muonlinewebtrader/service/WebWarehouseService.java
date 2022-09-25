package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.repository.WebWarehouseRepository;

@RequiredArgsConstructor
@Service
public class WebWarehouseService {
    private final WebWarehouseRepository webWarehouseRepository;
    private final AccountService accountService;

    public WebWarehouse getWarehouseByAccountName(String accountName) {
        return webWarehouseRepository.findByAccountName(accountName);
    }

    public void addZen(String accountName, Long zen) {
        WebWarehouse ware = getWarehouseByAccountName(accountName);
        if (ware == null)
            ware = WebWarehouse.builder()
                    .zen(0L)
                    .account(accountService.findByLogin(accountName))
                    .build();

        ware.setZen(ware.getZen() + zen);
        webWarehouseRepository.save(ware);
    }

    public Long findZenByAccountName(String name) {
        return webWarehouseRepository.findByAccountName(name)
                .getZen();
    }

    public void subtractZen(String name, Long zen) {
        addZen(name, -zen);
    }
}