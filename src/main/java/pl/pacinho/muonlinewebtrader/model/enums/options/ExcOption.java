package pl.pacinho.muonlinewebtrader.model.enums.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExcOption {

    //WEAPONS, PENDANTS
    XDMG(0, "Excellent Damage Rate + 10%"),
    LVL_20(1, "Increase Damage + level/20"),
    DMG_2_PROC(2, "Increase Damage + 2%"),
    SPEED(3, "Increase Speed + 7"),
    HP_MONSTER(4, "Increase life after monster + life/8"),
    MANA_MONSTER(5, "Increase mana after monster + mana/8"),

    //SETS, SHIELDS, RINGS
    INC_HP(0, "Increase HP 4%"),
    INC_MANA(1, "Increase MP 4%"),
    DD(2, "Damage Decrease 4%"),
    REF(3, "Reflect Damage 5%"),
    DSR(4, "Defense Success Rate 10%"),
    ZEN(5, "Increase Zen After Hunt 40%"),

    //WINGS 2nd
    HP(0, "HP +125 Increased"),
    MANA(1, "MP +125 Increased "),
    IGNORE_DEF_POWER_3(2, "Ignore opponent's defensive power by 3%"),
    AG(3, "Max AG +50 Increased"),
    SPEED_WIZZ(4, "Increase Attacking(Wizardry) speed +5"),

    //WINGS 3rd
    IGNORE_DEF_POWER_5(0, "Ignore opponent's defensive power by 5%"),
    REF_POWER_5(1, "Renturn's the enemy's attack power in 5%"),
    REC_HP(2, "Complete recovery of Life in 5% rate"),
    REC_MANA(3, "Complete recover of Mana in 5% rate");

    private final int idx;
    private final String name;
}