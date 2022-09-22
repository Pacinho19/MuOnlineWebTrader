package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findByAccountName(String accountName);
}