//package Model.Items;
//import java.util.HashMap;
//
//public class EquipmentLoadout {
//    private HashMap<String, EquipmentItem> equippedItems;
//
//    public EquipmentLoadout() {
//        equippedItems = new HashMap<>();
//    }
//    // Equip an item
//    public boolean equipItem(EquipmentItem item, double maxLoad) {
//        if (item == null) {
//            return false;
//        }
//
//        double totalWeight = 0;
//
//        for (int i = 0; i < equippedItems.size(); i++) {
//            EquipmentItem equipped = equippedItems.get(i);
//            totalWeight = totalWeight + equipped.getWeight();
//        }
//
//        // Account for an item already occupying this slot
//        EquipmentItem oldItem = equippedItems.get(item.getSlot());
//
//        if (oldItem != null) {
//            totalWeight -= oldItem.getWeight();
//        }
//
//        if (totalWeight + item.getWeight() > maxLoad) {
//            return false;
//        }
//
//        equippedItems.put(item.getSlot(), item);
//        return true;
//    }
//
//    // Unequip an item
//    public EquipmentItem unequipItem(String slot) {
//        return equippedItems.remove(slot);
//    }
//
//    // Get equipped item
//    public EquipmentItem getEquippedItem(String slot) {
//        return equippedItems.get(slot);
//    }
//}


package Model.Items;

import java.util.HashMap;

public class EquipmentLoadout {

    /* Data Fields */

    // Stores equipped items using their equipment slot as the key.
    private HashMap<String, EquipmentItem> equippedItems;

    // Tracks the total weight of equipped items.
    private double totalWeight;

    /* Constructor */

    // Initializes the player's equipment slots.
    public EquipmentLoadout() {
        equippedItems = new HashMap<String, EquipmentItem>();
        totalWeight = 0;
    }

    /* Custom Methods */

    // Equips an item if it fits the player's equipment load limit.
    public boolean equipItem(EquipmentItem item, double maxLoad) {

        if (item == null) {
            return false;
        }

        String slot = item.getSlot();

        // Make sure the equipment has a valid slot.
        if (!slot.equals(EquipmentItem.MAIN_HAND)
                && !slot.equals(EquipmentItem.OFF_HAND)
                && !slot.equals(EquipmentItem.ARMOR)) {
            return false;
        }

        // Check whether another item already occupies this slot.
        EquipmentItem oldItem = equippedItems.get(slot);

        double newTotalWeight = totalWeight;

        // Account for the weight of the item being replaced.
        if (oldItem != null) {
            newTotalWeight = newTotalWeight - oldItem.getWeight();
        }

        // Add the new item's weight.
        newTotalWeight = newTotalWeight + item.getWeight();

        // Prevent the player from exceeding their equipment load.
        if (newTotalWeight > maxLoad) {
            return false;
        }

        // Mark the previous item as unequipped.
        if (oldItem != null) {
            oldItem.setEquipped(false);
        }

        // Store the new item in its equipment slot.
        equippedItems.put(slot, item);

        // Update the item's status and total equipment weight.
        item.setEquipped(true);
        totalWeight = newTotalWeight;

        return true;
    }

    // Removes the item occupying the requested equipment slot.
    public EquipmentItem unequipItem(String slot) {

        EquipmentItem item = equippedItems.remove(slot);

        if (item != null) {

            // Update the item's status and equipment weight.
            item.setEquipped(false);
            totalWeight = totalWeight - item.getWeight();
        }

        return item;
    }

    // Retrieves the item occupying a particular slot.
    public EquipmentItem getEquippedItem(String slot) {
        return equippedItems.get(slot);
    }

    // Returns the combined weight of equipped items.
    public double getTotalWeight() {
        return totalWeight;
    }

    // Calculates the combined physical damage of equipped items.
    public int getTotalPhysicalDamage() {

        int totalDamage = 0;

        EquipmentItem mainHand =
                equippedItems.get(EquipmentItem.MAIN_HAND);

        EquipmentItem offHand =
                equippedItems.get(EquipmentItem.OFF_HAND);

        if (mainHand != null) {
            totalDamage = totalDamage + mainHand.getPhysicalDamage();
        }

        if (offHand != null) {
            totalDamage = totalDamage + offHand.getPhysicalDamage();
        }

        return totalDamage;
    }

    // Calculates the combined magic damage of equipped items.
    public int getTotalMagicDamage() {

        int totalDamage = 0;

        EquipmentItem mainHand =
                equippedItems.get(EquipmentItem.MAIN_HAND);

        EquipmentItem offHand =
                equippedItems.get(EquipmentItem.OFF_HAND);

        if (mainHand != null) {
            totalDamage = totalDamage + mainHand.getMagicDamage();
        }

        if (offHand != null) {
            totalDamage = totalDamage + offHand.getMagicDamage();
        }

        return totalDamage;
    }

    // Calculates the combined physical defense of equipped items.
    public int getTotalPhysicalDefense() {

        int totalDefense = 0;

        EquipmentItem offHand =
                equippedItems.get(EquipmentItem.OFF_HAND);

        EquipmentItem armor =
                equippedItems.get(EquipmentItem.ARMOR);

        if (offHand != null) {
            totalDefense = totalDefense + offHand.getPhysicalDefense();
        }

        if (armor != null) {
            totalDefense = totalDefense + armor.getPhysicalDefense();
        }

        return totalDefense;
    }

    // Calculates the combined magic defense of equipped items.
    public int getTotalMagicDefense() {

        int totalDefense = 0;

        EquipmentItem offHand =
                equippedItems.get(EquipmentItem.OFF_HAND);

        EquipmentItem armor =
                equippedItems.get(EquipmentItem.ARMOR);

        if (offHand != null) {
            totalDefense = totalDefense + offHand.getMagicDefense();
        }

        if (armor != null) {
            totalDefense = totalDefense + armor.getMagicDefense();
        }

        return totalDefense;
    }

    // Returns all currently equipped items.
    public HashMap<String, EquipmentItem> getEquippedItems() {
        return equippedItems;
    }
}
