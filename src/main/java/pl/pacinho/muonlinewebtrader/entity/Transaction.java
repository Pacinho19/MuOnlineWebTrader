package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentMethod;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionDirection;
import pl.pacinho.muonlinewebtrader.model.enums.transactions.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Transaction {


    @Id
    @GenericGenerator(name = "transactionId", strategy = "increment")
    @GeneratedValue(generator = "transactionId")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "acc_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionDirection direction;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private PaymentMethod meansOfPayment;

    private Long amount;

    private String description;

    private LocalDateTime addDate;
}
