package pl.pacinho.muonlinewebtrader.tools;

import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOption;

import java.util.List;

public class ExcOptionsUtils {

    public static final List<ExcOption> WEAPONS_PENDANTS = List.of(
            ExcOption.XDMG,
            ExcOption.LVL_20,
            ExcOption.DMG_2_PROC,
            ExcOption.SPEED,
            ExcOption.HP_MONSTER,
            ExcOption.MANA_MONSTER
    );

    public static final List<ExcOption> SETS_SHIELDS_RINGS = List.of(
            ExcOption.INC_HP,
            ExcOption.INC_MANA,
            ExcOption.DD,
            ExcOption.REF,
            ExcOption.ZEN,
            ExcOption.DSR
    );

    public static final List<ExcOption> WINGS_2ND = List.of(
            ExcOption.HP,
            ExcOption.MANA,
            ExcOption.IGNORE_DEF_POWER_3,
            ExcOption.AG,
            ExcOption.SPEED_WIZZ
    );

    public static final List<ExcOption> WINGS_3ND = List.of(
            ExcOption.IGNORE_DEF_POWER_5,
            ExcOption.REF_POWER_5,
            ExcOption.REC_HP,
            ExcOption.REC_MANA
    );
}