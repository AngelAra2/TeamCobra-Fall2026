
package Model.Items;

// Represents armor and shields that characters can equip.
public class Armor extends EquipmentItem {

    // Creates armor using its equipment and defensive information.
    public Armor(int itemId, String itemName, String itemDesc, double weight, int value, String slot, int physicalDamage,
                 int magicDamage, int physicalDefense, int magicDefense, String strengthScaling, String dexterityScaling,
                 String intelligenceScaling, String arcaneScaling, String specialEffect) {

        super(itemId, itemName, itemDesc, weight, value, slot, physicalDamage, magicDamage, physicalDefense, magicDefense,
                strengthScaling, dexterityScaling, intelligenceScaling, arcaneScaling, specialEffect);
    }
}

