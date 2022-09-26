package pl.pacinho.muonlinewebtrader.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;
import pl.pacinho.muonlinewebtrader.exceptions.ItemNotFoundException;
import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.PriceDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemShopDtoMapper;
import pl.pacinho.muonlinewebtrader.service.AccountService;
import pl.pacinho.muonlinewebtrader.service.ItemShopService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseItemService;
import pl.pacinho.muonlinewebtrader.service.WebWarehouseService;

import javax.resource.spi.IllegalStateException;
import javax.transaction.Transactional;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemShopTools {

    private final WebWarehouseItemService webWarehouseItemService;
    private final ItemShopService itemShopService;
    private final ItemShopDtoMapper itemShopDtoMapper;

    @Transactional
    public void putForSale(String code, PriceDto priceDto, String accountName) throws IllegalStateException {
        validatePrice(priceDto);

        webWarehouseItemService.removeItem(accountName, code);
        itemShopService.addItem(code, priceDto, accountName);

    }

    private void validatePrice(PriceDto priceDto) throws IllegalStateException {
        if (priceDto == null)
            throw new IllegalStateException("Price Settings must be set!");

        if (priceDto.getPriceBless() <= 0
            && priceDto.getPriceSoul() <= 0
            && priceDto.getPriceZen() <= 0)
            throw new IllegalStateException("Minimum one price type mus be set!");
    }

    public ItemShopDto getByCode(String code) {
        Optional<ItemShop> itemOpt = itemShopService.findByCodeAndActive(code, 1);
        if (itemOpt.isEmpty())
            throw new ItemNotFoundException("Not found offers for selected item in shop!");

        ItemShop itemShop = itemOpt.get();
        return itemShopDtoMapper.parse(itemShop);
    }

    public void incrementItemViewCount(String code) {
        itemShopService.incrementItemViewCount(code);
    }
}
