package Model.Util;

import java.util.ArrayList;

import Model.AbstractClasses.Item;
import Model.Exploration.Map;
import Model.Exploration.Room;
import Model.Items.Consumable;
import Model.Items.Weapon;

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
        if (item instanceof Weapon weapon) {
            return new Weapon(item.getItemId(), item.getItemName(),
                    item.getItemDescription(), weapon.getBaseAttack());
        }

        if (item instanceof Consumable consumable) {
            return new Consumable(item.getItemId(), item.getItemName(),
                    item.getItemDescription(), consumable.getHealingAmount());
        }

        return null;
    }
}