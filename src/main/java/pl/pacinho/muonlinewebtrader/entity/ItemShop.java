package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemShop {

    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;

    private String item;

    @ManyToOne
    @JoinColumn(name = "SELLER_ACC_ID")
    private Account sellerAccount;

    @ManyToOne
    @JoinColumn(name = "BUYER_ACC_ID")
    private Account buyerAccount;

    private LocalDateTime addDate;

    @Setter
    private int views;

    private long zenPrice;
    private long blessPrice;
    private long soulPrice;

    private int active;
}
