package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;
import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.PriceDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemShopDtoMapper;
import pl.pacinho.muonlinewebtrader.repository.ItemShopRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Service
public class ItemShopService {

    private final ItemShopRepository itemShopRepository;
    private final AccountService accountService;
    private final ReentrantLock lock = new ReentrantLock();
    private final ItemShopDtoMapper itemShopDtoMapper;


    public void addItem(String code, PriceDto priceDto, String accountName) {
        Account acc = accountService.findByLogin(accountName);
        itemShopRepository.save(
                ItemShop.builder()
                        .item(code)
                        .addDate(LocalDateTime.now())
                        .sellerAccount(acc)
                        .views(0)
                        .blessPrice(priceDto.getPriceBless())
                        .soulPrice(priceDto.getPriceSoul())
                        .zenPrice(priceDto.getPriceZen())
                        .active(1)
                        .build());
    }

    public void incrementItemViewCount(String code) {
        try {
            lock.lock();
            Optional<ItemShop> itemOpt = itemShopRepository.findByItemAndActive(code, 1);
            if (itemOpt.isEmpty()) return;
            ItemShop itemShop = itemOpt.get();
            itemShop.setViews(itemShop.getViews() + 1);
            itemShopRepository.save(itemShop);
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
    }

    public Optional<ItemShop> findByCodeAndActive(String code, int active) {
        return itemShopRepository.findByItemAndActive(code, active);
    }

    public List<ItemShopDto> findActiveOffers() {
        return itemShopRepository.findAllByActive(1)
                .stream()
                .map(itemShopDtoMapper::parse)
                .toList();
    }

    public List<ItemShopDto> findLastAdded(int count) {
        return itemShopRepository.findAllByActiveOrderByAddDateDesc(1, PageRequest.of(0, count))
                .stream()
                .map(itemShopDtoMapper::parse)
                .toList();
    }

    public List<ItemShopDto> findMostViewedItems(int count) {
        return itemShopRepository.findAllByActiveOrderByViewsDesc(1, PageRequest.of(0, count))
                .stream()
                .map(itemShopDtoMapper::parse)
                .toList();
    }

    public void closeOffer(ItemShop itemOffer, Account buyer) {
        itemOffer.setActive(0);
        itemOffer.setBuyerAccount(buyer);
        itemShopRepository.save(itemOffer);
    }
}