package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebWarehouseItemRepository extends JpaRepository<WebWarehouseItem, Long> {
    List<WebWarehouseItem> findByAccountNameAndActive(String accountName, int active);

    Optional<WebWarehouseItem> findByAccountNameAndItemAndActive(String name, String code, int active);
}