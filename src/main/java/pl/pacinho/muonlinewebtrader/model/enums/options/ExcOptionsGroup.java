package pl.pacinho.muonlinewebtrader.model.enums.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pacinho.muonlinewebtrader.tools.ExcOptionsUtils;

import java.util.List;

@RequiredArgsConstructor
public enum ExcOptionsGroup {

    WEAPONS(ExcOptionsUtils.WEAPONS_PENDANTS),
    SETS_SHIELDS_RINGS(ExcOptionsUtils.SETS_SHIELDS_RINGS),
    WINGS_2ND(ExcOptionsUtils.WINGS_2ND),
    WINGS_3ND(ExcOptionsUtils.WINGS_3ND);

    @Getter
    private final List<ExcOption> excOptions;

    public ExcOption findByIdx(int idx) {
        return this.excOptions.stream()
                .filter(eo -> eo.getIdx() == idx)
                .findFirst()
                .orElse(null);
    }

}