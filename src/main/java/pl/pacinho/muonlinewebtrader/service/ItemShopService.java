package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;
import pl.pacinho.muonlinewebtrader.model.dto.ItemShopDto;
import pl.pacinho.muonlinewebtrader.model.dto.PriceDto;
import pl.pacinho.muonlinewebtrader.model.dto.filters.FilterDto;
import pl.pacinho.muonlinewebtrader.model.dto.mapper.ItemShopDtoMapper;
import pl.pacinho.muonlinewebtrader.repository.ItemShopRepository;
import pl.pacinho.muonlinewebtrader.tools.filters.FilterUtils;
import pl.pacinho.muonlinewebtrader.tools.filters.ComparatorUtils;
import pl.pacinho.muonlinewebtrader.tools.pageable.Paging;
import pl.pacinho.muonlinewebtrader.tools.pageable.model.Paged;

import java.time.LocalDateTime;
import java.util.Collections;
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

    public Paged<ItemShopDto> findActiveOffers(Authentication authentication, Optional<Integer> page, FilterDto filterDto) {
        int pageNumber = page.orElse(1);
        int pageSize = filterDto.getPageSize();

//        Pageable request = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
//        Page<ItemShop> pageItems = itemShopRepository.findAllByActive(1, request);

        List<ItemShopDto> items = (!filterDto.isMyOffers() || (filterDto.isMyOffers() && authentication == null)
                ? itemShopRepository.findAllByActiveOrderByIdDesc(1)
                : itemShopRepository.findAllByActiveAndSellerAccountNameEqualsOrderByIdDesc(1, authentication.getName()))
                .stream()
                .map(itemShopDtoMapper::parse)
                .sorted(ComparatorUtils.itemShopDtoComparator(filterDto.getSort()))
                .toList();
        List<ItemShopDto> filteredItems = FilterUtils.filterItems(filterDto, items);
        List<List<ItemShopDto>> listPages = ListUtils.partition(filteredItems, pageSize);

        return new Paged<>(listPages.isEmpty() ? Collections.emptyList() : listPages.get(pageNumber - 1)
                , Paging.of(listPages.size(), pageNumber, pageSize));

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