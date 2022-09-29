package pl.pacinho.muonlinewebtrader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pacinho.muonlinewebtrader.entity.Account;
import pl.pacinho.muonlinewebtrader.entity.Notification;
import pl.pacinho.muonlinewebtrader.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountService accountService;

    public List<Notification> findUnreadByAccount(String name) {
        return notificationRepository.findAllByAccountNameAndReadDateIsNull(name);
    }

    public void add(String text, Account acc) {
        notificationRepository.save(Notification.builder()
                .addDate(LocalDateTime.now())
                .text(text)
                .account(acc)
                .build());
    }

    @Transactional
    public void setAllAsRead(String name) {
        notificationRepository.findAllByAccountNameAndReadDateIsNull(name)
                .forEach(n -> n.setReadDate(LocalDateTime.now()));
    }

    @Transactional
    public void read(long id, String name) {
        Optional<Notification> notificationOpt = notificationRepository.findByIdAndAccountName(id, name);
        if (notificationOpt.isEmpty()) return;
        Notification notification = notificationOpt.get();
        notification.setReadDate(LocalDateTime.now());
    }
}