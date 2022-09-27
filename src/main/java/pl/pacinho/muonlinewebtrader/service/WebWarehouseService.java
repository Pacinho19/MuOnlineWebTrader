package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.model.dto.PaymentItemsDto;
import pl.pacinho.muonlinewebtrader.repository.WebWarehouseRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebWarehouseService {
    private final WebWarehouseRepository webWarehouseRepository;
    private final AccountService accountService;

    public WebWarehouse getWarehouseByAccountName(String accountName) {
        Optional<WebWarehouse> webWareOpt = webWarehouseRepository.findByAccountName(accountName);
        if (webWareOpt.isEmpty())
            return save(WebWarehouse.builder()
                    .zen(0L)
                    .account(accountService.findByLogin(accountName))
                    .build());
        return webWareOpt.get();
    }

    private WebWarehouse save(WebWarehouse ware) {
        return webWarehouseRepository.save(ware);
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
        return getWarehouseByAccountName(name)
                .getZen();
    }

    public void subtractZen(String name, Long zen) {
        addZen(name, -zen);
    }
}