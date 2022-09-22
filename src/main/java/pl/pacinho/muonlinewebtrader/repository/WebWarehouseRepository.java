package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;

import java.util.List;

@Repository
public interface WebWarehouseRepository extends JpaRepository<WebWarehouse, Long> {
    List<WebWarehouse> findByAccountName(String accountName);
}