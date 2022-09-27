package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouseItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebWarehouseRepository extends JpaRepository<WebWarehouse, Long> {
    Optional<WebWarehouse> findByAccountName(String accountName);
}