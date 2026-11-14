//package Model.Items;
//
//import Model.AbstractClasses.Item;
//
//// Represents weapons, shields, and armor.
//public class EquipmentItem extends Item {
//
//    // Equipment slots
//    public static final String MAIN_HAND = "MAIN-HAND";
//    public static final String OFF_HAND = "OFF-HAND";
//    public static final String ARMOR = "ARMOR";
//
//    // Equipment information
//    private String slot;
//
//    // Damage and defense
//    private int physicalDamage;
//    private int magicDamage;
//    private int physicalDefense;
//    private int magicDefense;
//
//    // Character stat scaling
//    private String strengthScaling;
//    private String dexterityScaling;
//    private String intelligenceScaling;
//    private String arcaneScaling;
//
//    // Special item effect
//    private String specialEffect;
//
//    // Full constructor
//    public EquipmentItem(int itemId, String itemName, String itemDesc,
//                         double weight, int value, String slot,
//                         int physicalDamage, int magicDamage,
//                         int physicalDefense, int magicDefense,
//                         String strengthScaling, String dexterityScaling,
//                         String intelligenceScaling, String arcaneScaling,
//                         String specialEffect) {
//
//        super(itemId, itemName, itemDesc, weight, value);
//
//        this.slot = slot;
//
//        this.physicalDamage = physicalDamage;
//        this.magicDamage = magicDamage;
//        this.physicalDefense = physicalDefense;
//        this.magicDefense = magicDefense;
//
//        this.strengthScaling = strengthScaling;
//        this.dexterityScaling = dexterityScaling;
//        this.intelligenceScaling = intelligenceScaling;
//        this.arcaneScaling = arcaneScaling;
//
//        this.specialEffect = specialEffect;
//    }
//
//    // Constructor for equipment without scaling or special effects.
//    public EquipmentItem(int itemId, String itemName, String itemDesc,
//                         double weight, int value, String slot,
//                         int physicalDamage, int magicDamage,
//                         int physicalDefense, int magicDefense) {
//
//        this(itemId, itemName, itemDesc, weight, value, slot,
//                physicalDamage, magicDamage, physicalDefense, magicDefense,
//                "NONE", "NONE", "NONE", "NONE", "NONE");
//    }
//
//    // Keeps the original constructor available for existing code.
//    public EquipmentItem(String slot, int physicalDamage, int magicDamage,
//                         int physicalDefense, int magicDefense) {
//
//        this(0, "Equipment", "", 0, 0, slot,
//                physicalDamage, magicDamage, physicalDefense, magicDefense);
//    }
//
//    // Return the equipment slot.
//    public String getSlot() {
//        return this.slot;
//    }
//
//    // Return physical damage.
//    public int getPhysicalDamage() {
//        return this.physicalDamage;
//    }
//
//    // Return magic damage.
//    public int getMagicDamage() {
//        return this.magicDamage;
//    }
//
//    // Return physical defense.
//    public int getPhysicalDefense() {
//        return this.physicalDefense;
//    }
//
//    // Return magic defense.
//    public int getMagicDefense() {
//        return this.magicDefense;
//    }
//
//    // Return Strength scaling.
//    public String getStrengthScaling() {
//        return this.strengthScaling;
//    }
//
//    // Return Dexterity scaling.
//    public String getDexterityScaling() {
//        return this.dexterityScaling;
//    }
//
//    // Return Intelligence scaling.
//    public String getIntelligenceScaling() {
//        return this.intelligenceScaling;
//    }
//
//    // Return Arcane scaling.
//    public String getArcaneScaling() {
//        return this.arcaneScaling;
//    }
//
//    // Return the equipment's special effect.
//    public String getSpecialEffect() {
//        return this.specialEffect;
//    }
//}


package Model.Items;

import Model.AbstractClasses.Item;

// Parent class for all equippable items.
public abstract class EquipmentItem extends Item {

    // Equipment slot names.
    public static final String MAIN_HAND = "MAIN-HAND";
    public static final String OFF_HAND = "OFF-HAND";
    public static final String ARMOR = "ARMOR";

    // Identifies where the equipment belongs.
    private String slot;

    // Offensive and defensive statistics.
    private int physicalDamage;
    private int magicDamage;
    private int physicalDefense;
    private int magicDefense;

    // Character stat scaling.
    private String strengthScaling;
    private String dexterityScaling;
    private String intelligenceScaling;
    private String arcaneScaling;

    // Special equipment effect.
    private String specialEffect;

    // Creates an equipment item with all its information.
    public EquipmentItem(
            int itemId,
            String itemName,
            String itemDesc,
            double weight,
            int value,
            String slot,
            int physicalDamage,
            int magicDamage,
            int physicalDefense,
            int magicDefense,
            String strengthScaling,
            String dexterityScaling,
            String intelligenceScaling,
            String arcaneScaling,
            String specialEffect) {

        super(itemId, itemName, itemDesc, weight, value);

        this.slot = slot;

        this.physicalDamage = physicalDamage;
        this.magicDamage = magicDamage;
        this.physicalDefense = physicalDefense;
        this.magicDefense = magicDefense;

        this.strengthScaling = strengthScaling;
        this.dexterityScaling = dexterityScaling;
        this.intelligenceScaling = intelligenceScaling;
        this.arcaneScaling = arcaneScaling;

        this.specialEffect = specialEffect;
    }

    // Returns the equipment slot.
    public String getSlot() {
        return this.slot;
    }

    // Returns physical damage.
    public int getPhysicalDamage() {
        return this.physicalDamage;
    }

    // Returns magic damage.
    public int getMagicDamage() {
        return this.magicDamage;
    }

    // Returns physical defense.
    public int getPhysicalDefense() {
        return this.physicalDefense;
    }

    // Returns magic defense.
    public int getMagicDefense() {
        return this.magicDefense;
    }

    // Returns Strength scaling.
    public String getStrengthScaling() {
        return this.strengthScaling;
    }

    // Returns Dexterity scaling.
    public String getDexterityScaling() {
        return this.dexterityScaling;
    }

    // Returns Intelligence scaling.
    public String getIntelligenceScaling() {
        return this.intelligenceScaling;
    }

    // Returns Arcane scaling.
    public String getArcaneScaling() {
        return this.arcaneScaling;
    }

    // Returns the special equipment effect.
    public String getSpecialEffect() {
        return this.specialEffect;
    }
}


