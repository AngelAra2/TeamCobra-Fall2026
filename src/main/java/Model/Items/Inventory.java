//package Model.Items;
//import Model.AbstractClasses.Item;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Inventory {
//
//    private List<Item> items;
//
//    public Inventory() {
//        items = new ArrayList<>();
//    }
//
//    //if player has enough carrying capacity, add item to inventory
//    public boolean addItem(Item item, int playerLevel) {
//        if (item == null || !canCarry(item, playerLevel)) {
//            return false;
//        }
//
//        items.add(item);
//        return true;
//    }
//
//    // Remove an item from the inventory
//    public boolean removeItem(Item item) {
//        return items.remove(item);
//    }
//
//    // Find the first item with the given name
//    public Item findItem(String itemName) {
//        for (Item item : items) {
//            if (item.getItemName().equalsIgnoreCase(itemName)) {
//                return item;
//            }
//        }
//
//        return null;
//    }
//
//    // Check whether the inventory contains an item
//    public boolean containsItem(String itemName) {
//        return findItem(itemName) != null;
//    }
//
//    // Count how many copies of an item are in the inventory
//    public int getQuantity(String itemName) {
//        int quantity = 0;
//
//        for (Item item : items) {
//            if (item.getItemName().equalsIgnoreCase(itemName)) {
//                quantity++;
//            }
//        }
//
//        return quantity;
//    }
//
//    // Calculate the total weight of all inventory items
//    public double getTotalWeight() {
//        double totalWeight = 0;
//
//        for (Item item : items) {
//            totalWeight += item.getWeight();
//        }
//
//        return totalWeight;
//    }
//
//    // Calculate carrying capacity based on player level
//    public double getCarryCapacity(int playerLevel) {
//        return 40 + (5 * playerLevel);
//    }
//
//    // Check whether an item can be added without exceeding capacity
//    public boolean canCarry(Item item, int playerLevel) {
//        if (item == null) {
//            return false;
//        }
//
//        return getTotalWeight() + item.getWeight()
//                <= getCarryCapacity(playerLevel);
//    }
//
//    // Return a copy of the inventory list
//    public List<Item> getItems() {
//        return new ArrayList<>(items);
//    }
//
//    // Check whether the inventory is empty
//    public boolean isEmpty() {
//        return items.isEmpty();
//    }
//}


package Model.Items;

import java.util.ArrayList;
import java.util.HashMap;

import Model.AbstractClasses.Item;

public class Inventory {

    /* Data Fields */

    // Stores items by name and allows multiple copies of each item.
    private HashMap<String, ArrayList<Item>> items;

    // Tracks the combined weight of everything in the inventory.
    private double totalWeight;

    /* Constructor */

    // Initializes the player's inventory.
    public Inventory() {
        items = new HashMap<String, ArrayList<Item>>();
        totalWeight = 0;
    }

    /* Custom Methods */

    // Adds an item if the player has enough carrying capacity.
    public boolean addItem(Item item, int playerLevel) {

        if (item == null) {
            return false;
        }

        if (!canCarry(item, playerLevel)) {
            return false;
        }

        // Convert the item name to lowercase for consistent searching.
        String itemName = item.getItemName().toLowerCase();

        // Create a new list if this item name is not already recorded.
        if (!items.containsKey(itemName)) {
            items.put(itemName, new ArrayList<Item>());
        }

        // Add the item to the inventory.
        items.get(itemName).add(item);

        // Update the inventory weight.
        totalWeight = totalWeight + item.getWeight();

        // Update the item's collected status.
        item.setCollected(true);

        return true;
    }

    // Removes an item from the inventory.
    public boolean removeItem(Item item) {

        if (item == null) {
            return false;
        }

        String itemName = item.getItemName().toLowerCase();

        if (items.containsKey(itemName)) {

            ArrayList<Item> itemList = items.get(itemName);

            if (itemList.remove(item)) {

                // Subtract the removed item's weight.
                totalWeight = totalWeight - item.getWeight();

                // Remove the name if no copies remain.
                if (itemList.isEmpty()) {
                    items.remove(itemName);
                }

                return true;
            }
        }

        return false;
    }

    // Finds the first item matching the requested name.
    public Item findItem(String itemName) {

        itemName = itemName.toLowerCase();

        if (items.containsKey(itemName)) {

            ArrayList<Item> itemList = items.get(itemName);

            if (itemList.size() > 0) {
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

        itemName = itemName.toLowerCase();

        if (items.containsKey(itemName)) {
            return items.get(itemName).size();
        }

        return 0;
    }

    // Returns the current combined weight of the inventory.
    public double getTotalWeight() {
        return totalWeight;
    }

    // Calculates carrying capacity using the player's level.
    public double getCarryCapacity(int playerLevel) {
        return 40 + (5 * playerLevel);
    }

    // Checks whether the player can carry another item.
    public boolean canCarry(Item item, int playerLevel) {

        if (item == null) {
            return false;
        }

        return totalWeight + item.getWeight()
                <= getCarryCapacity(playerLevel);
    }

    // Returns the inventory's HashMap.
    public HashMap<String, ArrayList<Item>> getItems() {
        return items;
    }

    // Checks whether the inventory is empty.
    public boolean isEmpty() {
        return items.isEmpty();
    }
}

