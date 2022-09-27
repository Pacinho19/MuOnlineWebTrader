package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.WebWallet;

import java.util.Optional;

@Repository
public interface WebWalletRepository extends JpaRepository<WebWallet, Long> {

    Optional<WebWallet> findByAccountName(String name);
}