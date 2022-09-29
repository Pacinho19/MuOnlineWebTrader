package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Notification {

    @Id
    @GenericGenerator(name = "notificationId", strategy = "increment")
    @GeneratedValue(generator = "notificationId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    private Account account;

    private String text;
    private LocalDateTime addDate;
    @Setter
    private LocalDateTime readDate;


}