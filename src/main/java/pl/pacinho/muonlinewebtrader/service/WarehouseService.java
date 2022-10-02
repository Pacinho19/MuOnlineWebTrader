package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.repository.WarehouseRepository;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;
import pl.pacinho.muonlinewebtrader.tools.WarehouseTools;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final AccountService accountService;

    public Warehouse getWarehouseByAccountName(String accountName) {
        Optional<Warehouse> wareOpt = warehouseRepository.findByAccountName(accountName);
        if (wareOpt.isPresent()) return wareOpt.get();
        return emptyWare(accountName);
    }

    private Warehouse emptyWare(String name) {
        return warehouseRepository.save(
                Warehouse.builder()
                        .account(accountService.findByLogin(name))
                        .zen(1000L)
                        .content(WarehouseTools.EMPTY_WARE)
                        .build()
        );
    }

    public void removeItem(String code, String name) {
        Warehouse ware = getWarehouseByAccountName(name);
        if (!ware.getContent().contains(code))
            throw new IllegalStateException("Selected item not found in game warehouse !");

        ware.setContent(ware.getContent().replace(code, CodeUtils.EMPTY_CODE));
        warehouseRepository.save(ware);
    }

    public void addItem(String name, String code, int startPosition) {
        Warehouse ware = getWarehouseByAccountName(name);
        if (!ware.getContent().contains(CodeUtils.EMPTY_CODE))
            throw new IllegalStateException("No free space in game warehouse!");

        ware.setContent(CodeUtils.addItemToWare(ware.getContent(), code, startPosition));
        warehouseRepository.save(ware);
    }

    public Long findZenByAccountName(String name) {
        return getWarehouseByAccountName(name)
                .getZen();
    }

    public void subtractZenValue(Long zen, String name) {
        addZenValue(-zen, name);
    }

    public void addZenValue(Long zen, String name) {
        Warehouse ware = getWarehouseByAccountName(name);
        ware.setZen(ware.getZen() + zen);
        warehouseRepository.save(ware);
    }
}