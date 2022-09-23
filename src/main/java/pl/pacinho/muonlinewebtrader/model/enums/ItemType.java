package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pacinho.muonlinewebtrader.model.enums.options.ExcOptionsGroup;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ItemType {

    SWORD(0, "Sword", ExcOptionsGroup.WEAPONS),
    AXE(1, "Axe", ExcOptionsGroup.WEAPONS),
    MACE_AND_SCEPTER(2, "Mace/Scepter", ExcOptionsGroup.WEAPONS),
    SPEAR(3, "Spear", ExcOptionsGroup.WEAPONS),
    BOW_AND_CROSSBOW(4, "Bow/Crossbow", ExcOptionsGroup.WEAPONS),
    STAFF(5, "Staff", ExcOptionsGroup.WEAPONS),
    SHIELD(6, "Shield", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    HELM(7, "Helm", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    ARMOR(8, "Armor", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    PANTS(9, "Pants", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    GLOVES(10, "Gloves", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    BOOTS(11, "Boots", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    WING_AND_ORB(12, "Wing/Orb", ExcOptionsGroup.WINGS_2ND),
    MISC(13, "MISC", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    MISC_2(14, "MISC", ExcOptionsGroup.SETS_SHIELDS_RINGS),
    DW_SM_MG_SCROLL(15, "DW/SM/MG Scroll", null),
    UNKNOWN(-1, "Unknown", null);

    private int sectionNumber;
    private String name;
    private ExcOptionsGroup excOptionsGroup;

    public static ItemType getItemType(String name) {
        return Arrays.stream(ItemType.values())
                .filter(i -> i.getName().equals(name))
                .findFirst().orElse(ItemType.MISC);
    }

    public static ItemType getBySection(int section) {
        return Arrays.stream(ItemType.values())
                .filter(i -> i.getSectionNumber() == section)
                .findFirst().orElse(ItemType.MISC);
    }
}