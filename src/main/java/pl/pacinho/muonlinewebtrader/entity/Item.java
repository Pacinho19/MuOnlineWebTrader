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
    private int width;
    private int height;
    private int number;
    @Enumerated(EnumType.STRING)
    private ItemType category;
    private String name;
    @Setter
    private String iconPath;

    private int dk;
    private int dw;
    private int dl;
    private int mg;
    private int fe;
    private int su;
    private int rf;

    public Item(int idx, int section, int width, int height, String name, int dk, int dw, int dl, int mg, int fe, int su, int rf) {
        this.idx = idx;
        this.section = section;
        this.width = width;
        this.height = height;
        this.name = name;
        this.dk = dk;
        this.dw = dw;
        this.dl = dl;
        this.mg = mg;
        this.fe = fe;
        this.su = su;
        this.rf = rf;
        this.category = ItemType.getBySection(section);
        this.number = (section * 512) + idx;
    }
}