package pl.pacinho.muonlinewebtrader.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
public class Warehouse {

    @Id
    @GenericGenerator(name = "wareIdGen", strategy = "increment")
    @GeneratedValue(generator = "wareIdGen")
    private Long id;

    @Column(length = 8_000)
    private String content;

    @OneToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;
}