package Model.Util;

import java.util.ArrayList;

import Model.AbstractClasses.Item;
import Model.Exploration.Map;
import Model.Exploration.Room;
import Model.Items.Consumable;
import Model.Items.Weapon;
import Model.Items.Armor;
import Model.Items.EquipmentItem;

// Provides shared item lookup and copying methods.
public class ItemUtil {
    // There isn't supposed to be any instances of this class because this class only provides static methods.
    private ItemUtil() {}

    // Finds an item by ID, or returns null if it is not in the map.
    public static Item findItemById(Map worldMap, int itemId) {
        for (Room room : worldMap.getRooms().values()) {
            for (ArrayList<Item> items : room.getItems().values()) {
                for (Item item : items) {
                    if (item.getItemId() == itemId)
                        return item;
                }
            }
        }

        return null;
    }

    // Copies supported item types; returns null for unsupported types.
    public static Item copyItem(Item item) {
        // instanceof checks whether the item is a Weapon; if so, weapon refers to it as that type.
//        if (item instanceof Weapon weapon) {
//            return new Weapon(item.getItemId(), item.getItemName(),
//                    item.getItemDescription(), weapon.getBaseAttack());
//        }
        // Copy a weapon and preserve its equipment information.
        if (item instanceof Weapon weapon) {
            return new Weapon(item.getItemId(), item.getItemName(), item.getItemDescription(), item.getWeight(), item.getValue(),
                    weapon.getSlot(), weapon.getPhysicalDamage(), weapon.getMagicDamage(), weapon.getPhysicalDefense(),
                    weapon.getMagicDefense(), weapon.getStrengthScaling(), weapon.getDexterityScaling(),
                    weapon.getIntelligenceScaling(), weapon.getArcaneScaling(), weapon.getSpecialEffect());
        }

// Copy armor or a shield.
        if (item instanceof Armor armor) {
            return new Armor(item.getItemId(), item.getItemName(), item.getItemDescription(), item.getWeight(), item.getValue(),
                    armor.getSlot(), armor.getPhysicalDamage(), armor.getMagicDamage(), armor.getPhysicalDefense(),
                    armor.getMagicDefense(), armor.getStrengthScaling(), armor.getDexterityScaling(), armor.getIntelligenceScaling(),
                    armor.getArcaneScaling(), armor.getSpecialEffect());
        }

//        if (item instanceof Consumable consumable) {
//            return new Consumable(item.getItemId(), item.getItemName(),
//                    item.getItemDescription(), consumable.getHealingAmount());
//        }
        if (item instanceof Consumable consumable) {
            return new Consumable(item.getItemId(), item.getItemName(), item.getItemDescription(), item.getWeight(), item.getValue(),
                    consumable.getEffectType(), consumable.getEffectAmount(), consumable.getEffectDuration());
        }

        return null;
    }
}