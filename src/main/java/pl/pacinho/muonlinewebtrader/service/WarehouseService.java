package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.repository.WarehouseRepository;
import pl.pacinho.muonlinewebtrader.tools.CodeUtils;

@RequiredArgsConstructor
@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public Warehouse getWarehouseByAccountName(String accountName) {
        return warehouseRepository.findByAccountName(accountName);
    }

    public void removeItem(String code, String name) {
        Warehouse ware = warehouseRepository.findByAccountName(name);
        if (!ware.getContent().contains(code))
            throw new IllegalStateException("Selected item not found in game warehouse !");

        ware.setContent(ware.getContent().replace(code, CodeUtils.EMPTY_CODE));
        warehouseRepository.save(ware);
    }

    public void addItem(String name, String code, int startPosition) {
        Warehouse ware = warehouseRepository.findByAccountName(name);
        if (!ware.getContent().contains(CodeUtils.EMPTY_CODE))
            throw new IllegalStateException("No free space in game warehouse!");

        ware.setContent(CodeUtils.addItemToWare(ware.getContent(), code, startPosition));
        warehouseRepository.save(ware);
    }

    public Long findZenByAccountName(String name) {
        return warehouseRepository.findByAccountName(name)
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