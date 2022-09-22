package pl.pacinho.muonlinewebtrader.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
public class ExtendedItemDto extends SimpleItemDto {

    private String serialNumber;
    @Setter
    private List<ExcOption> excOptions;
    private int level;
    private boolean luck;
    private boolean skill;
    private boolean exc;

    public String getFullName() {
        return
                (exc ? "Excellent " : "")
                + this.getName()
                + (level > 0 ? "+" + level : "")
                + (luck ? "+Luck " : "")
                + (skill ? "+Skill " : "")
                + (excOptions != null && !excOptions.isEmpty()
                        ? excOptions.stream().map(ExcOption::getName).collect(Collectors.joining(" +", " +", ""))
                        : "");
    }

}