package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;

@Getter
@Setter
public class ClassFilterDto {
    private CharacterClass characterClass;
    private boolean selected;

    public ClassFilterDto(CharacterClass characterClass) {
        this.characterClass = characterClass;
        this.selected = true;
    }
}