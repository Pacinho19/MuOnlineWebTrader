package pl.pacinho.muonlinewebtrader.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;

    private String name;

    @Setter
    @Column(name = "PASSWORD")
    private String password;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
}