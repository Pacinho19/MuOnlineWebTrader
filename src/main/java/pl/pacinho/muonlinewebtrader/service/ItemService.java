package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.repository.ItemRepository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}