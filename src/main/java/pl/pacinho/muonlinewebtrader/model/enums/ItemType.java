package pl.pacinho.muonlinewebtrader.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ItemType {

    SWORD("Sword")
    ,AXE("Axe")
    ,MACE_AND_SCEPTER ("Mace/Scepter")
    ,SPEAR("Spear")
    ,BOW_AND_CROSSBOW("Bow/Crossbow")
    ,STAFF("Staff")
    ,SHIELD("Shield")
    ,HELM("Helm")
    ,ARMOR("Armor")
    ,PANTS("Pants")
    ,GLOVES("Gloves")
    ,BOOTS("Boots")
    ,WING_AND_ORB("Wing/Orb")
    ,MISC("MISC")
    ,DW_SM_MG_SCROLL ("DW/SM/MG Scroll");

    private String name;

    public static ItemType getItemType(String name){
        return Arrays.stream(ItemType.values())
                .filter(i -> i.getName().equals(name))
                .findFirst().orElse(ItemType.MISC);
    }
}