package pl.pacinho.muonlinewebtrader.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebWarehouseItem {
    @Id
    @GenericGenerator(name = "webWareItemIdGen", strategy = "increment")
    @GeneratedValue(generator = "webWareItemIdGen")
    private Long id;

    private String item;

    @Setter
    @Column(columnDefinition = "smallint")
    private int active;

    @ManyToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;

}