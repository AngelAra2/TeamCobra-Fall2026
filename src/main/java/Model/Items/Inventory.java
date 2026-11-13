//package Model.Items;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import Model.AbstractClasses.Item;
//
//public class Inventory {
//
//    /* Data Fields */
//
//    // Stores items by name and allows multiple copies of each item.
//    private HashMap<String, ArrayList<Item>> items;
//
//    // Tracks the combined weight of everything in the inventory.
//    private double totalWeight;
//
//    /* Constructor */
//
//    // Initializes the player's inventory.
//    public Inventory() {
//        items = new HashMap<String, ArrayList<Item>>();
//        totalWeight = 0;
//    }
//
//    /* Custom Methods */
//
//    // Adds an item if the player has enough carrying capacity.
//    public boolean addItem(Item item, int playerLevel) {
//
//        if (item == null) {
//            return false;
//        }
//
//        if (!canCarry(item, playerLevel)) {
//            return false;
//        }
//
//        // Convert the item name to lowercase for consistent searching.
//        String itemName = item.getItemName().toLowerCase();
//
//        // Create a new list if this item name is not already recorded.
//        if (!items.containsKey(itemName)) {
//            items.put(itemName, new ArrayList<Item>());
//        }
//
//        // Add the item to the inventory.
//        items.get(itemName).add(item);
//
//        // Update the inventory weight.
//        totalWeight = totalWeight + item.getWeight();
//
//        // Update the item's collected status.
//        item.setCollected(true);
//
//        return true;
//    }
//
//    // Removes an item from the inventory.
//    public boolean removeItem(Item item) {
//
//        if (item == null) {
//            return false;
//        }
//
//        String itemName = item.getItemName().toLowerCase();
//
//        if (items.containsKey(itemName)) {
//
//            ArrayList<Item> itemList = items.get(itemName);
//
//            if (itemList.remove(item)) {
//
//                // Subtract the removed item's weight.
//                totalWeight = totalWeight - item.getWeight();
//
//                // Remove the name if no copies remain.
//                if (itemList.isEmpty()) {
//                    items.remove(itemName);
//                }
//
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    // Finds the first item matching the requested name.
//    public Item findItem(String itemName) {
//
//        itemName = itemName.toLowerCase();
//
//        if (items.containsKey(itemName)) {
//
//            ArrayList<Item> itemList = items.get(itemName);
//
//            if (itemList.size() > 0) {
//                return itemList.get(0);
//            }
//        }
//
//        return null;
//    }
//
//    // Checks whether the inventory contains an item.
//    public boolean containsItem(String itemName) {
//        return findItem(itemName) != null;
//    }
//
//    // Returns the number of copies of an item.
//    public int getQuantity(String itemName) {
//
//        itemName = itemName.toLowerCase();
//
//        if (items.containsKey(itemName)) {
//            return items.get(itemName).size();
//        }
//
//        return 0;
//    }
//
//    // Returns the current combined weight of the inventory.
//    public double getTotalWeight() {
//        return totalWeight;
//    }
//
//    // Calculates carrying capacity using the player's level.
//    public double getCarryCapacity(int playerLevel) {
//        return 40 + (5 * playerLevel);
//    }
//
//    // Checks whether the player can carry another item.
//    public boolean canCarry(Item item, int playerLevel) {
//
//        if (item == null) {
//            return false;
//        }
//
//        return totalWeight + item.getWeight()
//                <= getCarryCapacity(playerLevel);
//    }
//
//    // Returns the inventory's HashMap.
//    public HashMap<String, ArrayList<Item>> getItems() {
//        return items;
//    }
//
//    // Checks whether the inventory is empty.
//    public boolean isEmpty() {
//        return items.isEmpty();
//    }
//}


package Model.Items;

import java.util.ArrayList;
import java.util.HashMap;

import Model.AbstractClasses.Item;

// Stores the items a character is carrying and tracks their total weight.
public class Inventory {

    // Stores items by lowercase name. Multiple copies can share a name.
    private HashMap<String, ArrayList<Item>> items;

    // Tracks the combined weight of all inventory items.
    private double totalWeight;

    // Creates an empty inventory.
    public Inventory() {
        this.items = new HashMap<String, ArrayList<Item>>();
        this.totalWeight = 0;
    }

    // Adds an item if the character has enough carrying capacity.
    public boolean addItem(Item item, int playerLevel) {

        if (item == null || !canCarry(item, playerLevel)) {
            return false;
        }

        String itemName = item.getItemName().toLowerCase();

        if (!items.containsKey(itemName)) {
            items.put(itemName, new ArrayList<Item>());
        }

        items.get(itemName).add(item);
        totalWeight = totalWeight + item.getWeight();

        // Update the item's status.
        item.setCollected(true);

        return true;
    }

    // Removes an item and updates the inventory's total weight.
    public boolean removeItem(Item item) {

        if (item == null) {
            return false;
        }

        // An equipped item must be unequipped before it can be removed.
        if (item.isEquipped()) {
            return false;
        }

        String itemName = item.getItemName().toLowerCase();

        if (items.containsKey(itemName)) {

            ArrayList<Item> itemList = items.get(itemName);

            if (itemList.remove(item)) {

                totalWeight = totalWeight - item.getWeight();

                // Remove the name if no copies remain.
                if (itemList.isEmpty()) {
                    items.remove(itemName);
                }

                // The item is no longer being carried.
                item.setCollected(false);

                return true;
            }
        }

        return false;
    }

    // Finds the first item matching the requested name.
    public Item findItem(String itemName) {

        if (itemName == null) {
            return null;
        }

        itemName = itemName.toLowerCase();

        if (items.containsKey(itemName)) {

            ArrayList<Item> itemList = items.get(itemName);

            if (!itemList.isEmpty()) {
                return itemList.get(0);
            }
        }

        return null;
    }

    // Checks whether the inventory contains an item.
    public boolean containsItem(String itemName) {
        return findItem(itemName) != null;
    }

    // Returns the number of copies of an item.
    public int getQuantity(String itemName) {

        if (itemName == null) {
            return 0;
        }

        itemName = itemName.toLowerCase();

        if (items.containsKey(itemName)) {
            return items.get(itemName).size();
        }

        return 0;
    }

    // Returns the combined weight of all carried items.
    public double getTotalWeight() {
        return totalWeight;
    }

    // Calculates carrying capacity based on character level.
    public double getCarryCapacity(int playerLevel) {
        return 40 + (5 * playerLevel);
    }

    // Checks whether another item can be carried.
    public boolean canCarry(Item item, int playerLevel) {

        if (item == null) {
            return false;
        }

        return totalWeight + item.getWeight()
                <= getCarryCapacity(playerLevel);
    }

    // Returns the stored inventory items.
    public HashMap<String, ArrayList<Item>> getItems() {
        return items;
    }

    // Checks whether the inventory is empty.
    public boolean isEmpty() {
        return items.isEmpty();
    }
}


