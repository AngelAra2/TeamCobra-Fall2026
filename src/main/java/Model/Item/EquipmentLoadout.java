package Model.Item;
import java.util.HashMap;

public class EquipmentLoadout {
    private HashMap<String, EquipmentItem> equippedItems;

    public EquipmentLoadout() {
        equippedItems = new HashMap<>();
    }
    // Equip an item
    public boolean equipItem(EquipmentItem item, double maxLoad) {
        if (item == null) {
            return false;
        }

        double totalWeight = 0;

        for (EquipmentItem equipped : equippedItems.values()) {
            totalWeight += equipped.getWeight();
        }

        // Account for an item already occupying this slot
        EquipmentItem oldItem = equippedItems.get(item.getSlot());

        if (oldItem != null) {
            totalWeight -= oldItem.getWeight();
        }

        if (totalWeight + item.getWeight() > maxLoad) {
            return false;
        }

        equippedItems.put(item.getSlot(), item);
        return true;
    }

    // Unequip an item
    public EquipmentItem unequipItem(String slot) {
        return equippedItems.remove(slot);
    }

    // Get equipped item
    public EquipmentItem getEquippedItem(String slot) {
        return equippedItems.get(slot);
    }
}
