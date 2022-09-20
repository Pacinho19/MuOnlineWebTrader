package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOptionsGroup;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ItemType {

    SWORD("Sword", ExcOptionsGroup.WEAPONS),
    AXE("Axe", ExcOptionsGroup.WEAPONS),
    MACE_AND_SCEPTER("Mace/Scepter", ExcOptionsGroup.WEAPONS),
    SPEAR("Spear", ExcOptionsGroup.WEAPONS),
    BOW_AND_CROSSBOW("Bow/Crossbow", ExcOptionsGroup.WEAPONS),
    STAFF("Staff", ExcOptionsGroup.WEAPONS),
    SHIELD("Shield", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    HELM("Helm", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    ARMOR("Armor", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    PANTS("Pants", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    GLOVES("Gloves", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    BOOTS("Boots", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    WING_AND_ORB("Wing/Orb", ExcOptionsGroup.WINGS_2ND),
    MISC("MISC", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    DW_SM_MG_SCROLL("DW/SM/MG Scroll", null),
    UNKNOWN("Unknown", null);

    private String name;
    private ExcOptionsGroup excOptionsGroup;

    public static ItemType getItemType(String name) {
        return Arrays.stream(ItemType.values())
                .filter(i -> i.getName().equals(name))
                .findFirst().orElse(ItemType.MISC);
    }
}