


package Model.AbstractClasses;

import Model.Exploration.Room;

// Parent class for all items in the game.
//Priscilla, Danh
public abstract class Item {

    // Item information
    private Room currentRoom;
    private int itemId;
    private String itemName;
    private String itemDesc;
    private double weight;
    private int value;

    // Item status
    private boolean collected;
    private boolean consumed;
    private boolean equipped;

    // Constructor
    public Item(int itemId, String itemName, String itemDesc,
                double weight, int value) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.weight = weight;
        this.value = value;

        this.collected = false;
        this.consumed = false;
        this.equipped = false;
    }

    // Return the item's ID.
    public int getItemId() {
        return this.itemId;
    }

    // Return the item's name.
    public String getItemName() {
        return this.itemName;
    }

    // Return the item's description.
    public String getItemDescription() {
        return this.itemDesc;
    }

    // Return the item's weight.
    public double getWeight() {
        return this.weight;
    }

    // Return the item's gold value.
    public int getValue() {
        return this.value;
    }

    // Return whether the item has been collected.
    public boolean isCollected() {
        return this.collected;
    }

    // Return whether the item has been consumed.
    public boolean isConsumed() {
        return this.consumed;
    }

    // Return whether the item is equipped.
    public boolean isEquipped() {
        return this.equipped;
    }

    // Return the room where the item is located.
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    // Update the item's current room.
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    // Update whether the item has been collected.
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    // Update whether the item has been consumed.
    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    // Update whether the item is equipped.
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    // Remove the item from its current room.
    public void removeFromCurrentRoom() {
        this.currentRoom = null;
    }

    // Display the item's name and description.
    @Override
    public String toString() {
        return "[" + this.itemName + "]\n" + this.itemDesc;
    }
}



