package pl.pacinho.muonlinewebtrader.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.muonlinewebtrader.model.enums.ItemType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Item {
    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;
    private int idx;
    private int section;
    private int level;
    private int number;
    @Enumerated(EnumType.STRING)
    private ItemType category;
    private String name;

    @Setter
    private String iconPath;

    public Item(int idx, int section, int level, ItemType category, String name) {
        this.idx = idx;
        this.section = section;
        this.level = level;
        this.category = category;
        this.name = name;
        this.number = (section * 512) + idx;
    }
}