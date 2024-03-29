package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebWarehouse {

    @Id
    @GenericGenerator(name = "webWareIdGen", strategy = "increment")
    @GeneratedValue(generator = "webWareIdGen")
    private Long id;

    @Setter
    private Long zen;

    @OneToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;

}