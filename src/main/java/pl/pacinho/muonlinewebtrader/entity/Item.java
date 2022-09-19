package pl.pacinho.muonlinewebtrader.entity;

import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;
    private int idx;
    private int section;
    private int number;
    @Enumerated(EnumType.STRING)
    private ItemType category;
    private String name;

    public Item(int idx, int section, ItemType category, String name) {
        this.idx = idx;
        this.section = section;
        this.category = category;
        this.name = name;
        this.number = (section * 512) + idx;
    }
}