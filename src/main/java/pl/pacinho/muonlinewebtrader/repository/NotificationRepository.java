package pl.pacinho.muonlinewebtrader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.muonlinewebtrader.entity.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByAccountNameAndReadDateIsNull(String accountName);

    Optional<Notification> findByIdAndAccountName(long id, String name);
}