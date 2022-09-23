package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.WebWarehouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebWarehouseRepository extends JpaRepository<WebWarehouse, Long> {
    List<WebWarehouse> findByAccountNameAndActive(String accountName, int active);

    Optional<WebWarehouse> findByAccountNameAndItemAndActive(String name, String code, int active);
}