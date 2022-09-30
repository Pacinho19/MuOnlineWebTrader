package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.ItemShop;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemShopRepository extends PagingAndSortingRepository<ItemShop, Long> {

    Optional<ItemShop> findByItemAndActive(String item, int active);
    Page<ItemShop> findAllByActive(int active, Pageable pageable);
    List<ItemShop> findAllByActiveOrderByIdDesc(int active);
    List<ItemShop> findAllByActiveOrderByAddDateDesc(int active, Pageable pageable);
    List<ItemShop> findAllByActiveOrderByViewsDesc(int active, Pageable pageable);
    List<ItemShop> findAllByActiveAndSellerAccountNameEqualsOrderByIdDesc(int i, String name);
    List<ItemShop> findAllByActiveAndSellerAccountNameNotOrderByIdDesc(int i, String name);
}