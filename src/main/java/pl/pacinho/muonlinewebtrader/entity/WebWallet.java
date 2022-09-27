package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class WebWallet {

    @Setter(AccessLevel.NONE)
    @Id
    @GenericGenerator(name = "webWalletIdGen", strategy = "increment")
    @GeneratedValue(generator = "webWalletIdGen")
    private Long id;

    private Long blessCount;
    private Long soulCount;
    private Long zenAmount;

    @OneToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;

    public static WebWallet empty(Account acc) {
        return WebWallet.builder()
                .blessCount(0L)
                .soulCount(0L)
                .zenAmount(0L)
                .account(acc)
                .build();
    }
}