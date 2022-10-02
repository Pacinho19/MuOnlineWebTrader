package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    @GenericGenerator(name = "wareIdGen", strategy = "increment")
    @GeneratedValue(generator = "wareIdGen")
    private Long id;

    @Setter
    @Column(length = 8_000)
    private String content;

    @OneToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;

    @Setter
    private Long zen;
}