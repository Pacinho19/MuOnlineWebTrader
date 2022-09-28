package pl.pacinho.muonlinewebtrader.model.dto.filters;

import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;

@Getter
public class ClassFilterDto {
    private CharacterClass characterClass;
    private boolean selected;

    public ClassFilterDto(CharacterClass characterClass) {
        this.characterClass = characterClass;
        this.selected = true;
    }
}