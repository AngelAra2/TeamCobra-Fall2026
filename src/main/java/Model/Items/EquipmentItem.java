//package Model.Items;
//import Model.AbstractClasses.Item;
//
//public class EquipmentItem extends Item {
//
//    private String slot;
//    private int physicalDamage;
//    private int magicDamage;
//    private int physicalDefense;
//    private int magicDefense;
//
//
//    public EquipmentItem(int itemId, String itemName, String itemDesc, double weight, int value,String slot, int physicalDamage, int magicDamage, int physicalDefense, int magicDefense) {
//        super(itemId, itemName, itemDesc, weight, value);
//
//        this.slot = slot;
//        this.physicalDamage = physicalDamage;
//        this.magicDamage = magicDamage;
//        this.physicalDefense = physicalDefense;
//        this.magicDefense = magicDefense;
//    }
//
//    /* Getters */
//
//    public String getSlot() {
//        return slot;
//    }
//
//    public int getPhysicalDamage() {
//        return physicalDamage;
//    }
//
//    public int getMagicDamage() {
//        return magicDamage;
//    }
//
//    public int getPhysicalDefense() {
//        return physicalDefense;
//    }
//
//    public int getMagicDefense() {
//        return magicDefense;
//    }
//
////    public int getWeight() {
////        // Placeholder for weight calculation logic
////        return 0;
////    }
//}


//package Model.Items;
//
//import Model.AbstractClasses.Item;
//
//public class EquipmentItem extends Item {
//
//    /* Equipment Slot Constants */
//
//    // Identifies where the equipment can be equipped.
//    public static final String MAIN_HAND = "MAIN_HAND";
//    public static final String OFF_HAND = "OFF_HAND";
//    public static final String ARMOR = "ARMOR";
//
//    /* Data Fields */
//
//    // Determines which equipment slot the item occupies.
//    private String slot;
//
//    // Stores the item's offensive and defensive statistics.
//    private int physicalDamage;
//    private int magicDamage;
//    private int physicalDefense;
//    private int magicDefense;
//
//    /* Constructor */
//
//    // Creates equipment using information loaded from its resource file.
//    public EquipmentItem(int itemId, String itemName, String itemDesc,
//                         double weight, int value, String slot,
//                         int physicalDamage, int magicDamage,
//                         int physicalDefense, int magicDefense) {
//
//        super(itemId, itemName, itemDesc, weight, value);
//
//        this.slot = slot;
//        this.physicalDamage = physicalDamage;
//        this.magicDamage = magicDamage;
//        this.physicalDefense = physicalDefense;
//        this.magicDefense = magicDefense;
//    }
//
//    /* Getters */
//
//    // Returns the equipment slot.
//    public String getSlot() {
//        return this.slot;
//    }
//
//    // Returns the item's attack and defense statistics.
//    public int getPhysicalDamage() {
//        return this.physicalDamage;
//    }
//
//    public int getMagicDamage() {
//        return this.magicDamage;
//    }
//
//    public int getPhysicalDefense() {
//        return this.physicalDefense;
//    }
//
//    public int getMagicDefense() {
//        return this.magicDefense;
//    }
//}


package Model.Items;

import Model.AbstractClasses.Item;

// Represents weapons, shields, and armor.
public class EquipmentItem extends Item {

    // Equipment slots
    public static final String MAIN_HAND = "MAIN-HAND";
    public static final String OFF_HAND = "OFF-HAND";
    public static final String ARMOR = "ARMOR";

    // Equipment information
    private String slot;

    // Damage and defense
    private int physicalDamage;
    private int magicDamage;
    private int physicalDefense;
    private int magicDefense;

    // Character stat scaling
    private String strengthScaling;
    private String dexterityScaling;
    private String intelligenceScaling;
    private String arcaneScaling;

    // Special item effect
    private String specialEffect;

    // Full constructor
    public EquipmentItem(int itemId, String itemName, String itemDesc,
                         double weight, int value, String slot,
                         int physicalDamage, int magicDamage,
                         int physicalDefense, int magicDefense,
                         String strengthScaling, String dexterityScaling,
                         String intelligenceScaling, String arcaneScaling,
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

    // Constructor for equipment without scaling or special effects.
    public EquipmentItem(int itemId, String itemName, String itemDesc,
                         double weight, int value, String slot,
                         int physicalDamage, int magicDamage,
                         int physicalDefense, int magicDefense) {

        this(itemId, itemName, itemDesc, weight, value, slot,
                physicalDamage, magicDamage, physicalDefense, magicDefense,
                "NONE", "NONE", "NONE", "NONE", "NONE");
    }

    // Keeps the original constructor available for existing code.
    public EquipmentItem(String slot, int physicalDamage, int magicDamage,
                         int physicalDefense, int magicDefense) {

        this(0, "Equipment", "", 0, 0, slot,
                physicalDamage, magicDamage, physicalDefense, magicDefense);
    }

    // Return the equipment slot.
    public String getSlot() {
        return this.slot;
    }

    // Return physical damage.
    public int getPhysicalDamage() {
        return this.physicalDamage;
    }

    // Return magic damage.
    public int getMagicDamage() {
        return this.magicDamage;
    }

    // Return physical defense.
    public int getPhysicalDefense() {
        return this.physicalDefense;
    }

    // Return magic defense.
    public int getMagicDefense() {
        return this.magicDefense;
    }

    // Return Strength scaling.
    public String getStrengthScaling() {
        return this.strengthScaling;
    }

    // Return Dexterity scaling.
    public String getDexterityScaling() {
        return this.dexterityScaling;
    }

    // Return Intelligence scaling.
    public String getIntelligenceScaling() {
        return this.intelligenceScaling;
    }

    // Return Arcane scaling.
    public String getArcaneScaling() {
        return this.arcaneScaling;
    }

    // Return the equipment's special effect.
    public String getSpecialEffect() {
        return this.specialEffect;
    }
}

