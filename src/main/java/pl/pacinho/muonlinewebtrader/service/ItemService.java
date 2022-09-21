package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.repository.ItemRepository;
import pl.pacinho.muonlinewebtrader.utils.RandomUtils;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    @Cacheable("items")
    public List<Item> findAll() {
        return IterableUtils.toList(itemRepository.findAll())
                .stream()
                .sorted(Comparator.comparing(Item::getName))
                .toList();
    }

    public long getCount() {
        return itemRepository.count();
    }

    public List<Item> findRandomItems(int count) {
        return itemRepository.findAllByIdGreaterThan(
                        RandomUtils.randomLong(1, itemRepository.count()),
                        PageRequest.of(0, count, Sort.by(Sort.Order.asc("id"))))
                .getContent();
    }
}