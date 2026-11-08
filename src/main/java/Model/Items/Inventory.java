package Model.Items;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    //if player has enough carrying capacity, add item to inventory
    public boolean addItem(Item item, int playerLevel) {
        if (item == null || !canCarry(item, playerLevel)) {
            return false;
        }

        items.add(item);
        return true;
    }

    // Remove an item from the inventory
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    // Find the first item with the given name
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }

        return null;
    }

    // Check whether the inventory contains an item
    public boolean containsItem(String itemName) {
        return findItem(itemName) != null;
    }

    // Count how many copies of an item are in the inventory
    public int getQuantity(String itemName) {
        int quantity = 0;

        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                quantity++;
            }
        }

        return quantity;
    }

    // Calculate the total weight of all inventory items
    public double getTotalWeight() {
        double totalWeight = 0;

        for (Item item : items) {
            totalWeight += item.getWeight();
        }

        return totalWeight;
    }

    // Calculate carrying capacity based on player level
    public double getCarryCapacity(int playerLevel) {
        return 40 + (5 * playerLevel);
    }

    // Check whether an item can be added without exceeding capacity
    public boolean canCarry(Item item, int playerLevel) {
        if (item == null) {
            return false;
        }

        return getTotalWeight() + item.getWeight()
                <= getCarryCapacity(playerLevel);
    }

    // Return a copy of the inventory list
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    // Check whether the inventory is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
