package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Item;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;

import java.util.Optional;

@Repository
public interface ItemShopRepository extends JpaRepository<ItemShop, Long> {

    Optional<ItemShop> findByItemAndActive(String item, int active);
}