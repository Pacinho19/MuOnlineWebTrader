package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Warehouse;

import java.math.BigInteger;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findByAccountName(String accountName);
}