package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Item;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    Page<Item> findAllByIdGreaterThan(long id, Pageable pageable);
}