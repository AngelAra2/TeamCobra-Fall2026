//package Model.AbstractClasses;
//
//import Model.Exploration.Room;
//
//public abstract class Item
//{
//	/* Data Fields */
//    private Room currentRoom;
//    private int itemId;
//    private String itemName;
//    private String itemDesc;
//    private double weight;
//    private int value;
//
//
//    private boolean collected;
//    private boolean consumed;
//    private boolean equipped;
//
//	/* Constructors */
//    public Item(int itemId, String itemName, String itemDesc,
//                double weight, int value) {
//        this.itemId = itemId;
//        this.itemName = itemName;
//        this.itemDesc = itemDesc;
//        this.weight = weight;
//        this.value = value;
//
//        this.collected = false;
//        this.consumed = false;
//        this.equipped = false;
//    }
//
//	/* Methods */
//
//	// Getters //
//    public double getWeight() {
//        return this.weight;
//    }
//
//	public int getItemId()
//	{
//		return this.itemId;
//	}
//
//	public String getItemName()
//	{
//		return this.itemName;
//	}
//
//	public String getItemDescription()
//	{
//		return this.itemDesc;
//	}
//
//
//	// Setters //
//	public void setCurrentRoom(Room room) { this.currentRoom = room; }
//
//	// Custom //
//	public void removeFromCurrentRoom() { this.currentRoom = null; }
//
//	// Overrides //
//	@Override
//	public String toString()
//	{
//		return "[" + this.itemName + "]\n"
//				+ this.itemDesc;
//	}
//}



//package Model.AbstractClasses;
//
//import Model.Exploration.Room;
//
//public abstract class Item {
//
//    /* Data Fields */
//
//    // Stores the room where the item is currently located.
//    private Room currentRoom;
//
//    // Stores the item's basic information.
//    private int itemId;
//    private String itemName;
//    private String itemDesc;
//    private double weight;
//    private int value;
//
//    // Tracks the item's current status in the game.
//    private boolean collected;
//    private boolean consumed;
//    private boolean equipped;
//
//    /* Constructor */
//
//    // Creates an item using information loaded from its resource file.
//    public Item(int itemId, String itemName, String itemDesc,
//                double weight, int value) {
//
//        this.itemId = itemId;
//        this.itemName = itemName;
//        this.itemDesc = itemDesc;
//        this.weight = weight;
//        this.value = value;
//
//        this.collected = false;
//        this.consumed = false;
//        this.equipped = false;
//    }
//
//    /* Getters */
//
//    // Returns the item's basic information.
//    public int getItemId() {
//        return this.itemId;
//    }
//
//    public String getItemName() {
//        return this.itemName;
//    }
//
//    public String getItemDescription() {
//        return this.itemDesc;
//    }
//
//    public double getWeight() {
//        return this.weight;
//    }
//
//    public int getValue() {
//        return this.value;
//    }
//
//    // Returns the item's current status.
//    public boolean isCollected() {
//        return this.collected;
//    }
//
//    public boolean isConsumed() {
//        return this.consumed;
//    }
//
//    public boolean isEquipped() {
//        return this.equipped;
//    }
//
//    public Room getCurrentRoom() {
//        return this.currentRoom;
//    }
//
//    /* Setters */
//
//    // Updates the room containing the item.
//    public void setCurrentRoom(Room room) {
//        this.currentRoom = room;
//    }
//
//    // Updates the item's collected status.
//    public void setCollected(boolean collected) {
//        this.collected = collected;
//    }
//
//    // Updates the item's consumed status.
//    public void setConsumed(boolean consumed) {
//        this.consumed = consumed;
//    }
//
//    // Updates the item's equipped status.
//    public void setEquipped(boolean equipped) {
//        this.equipped = equipped;
//    }
//
//    /* Custom Methods */
//
//    // Removes the item's association with its current room.
//    public void removeFromCurrentRoom() {
//        this.currentRoom = null;
//    }
//
//    /* Overrides */
//
//    // Displays the item's name and description.
//    @Override
//    public String toString() {
//        return "[" + this.itemName + "]\n" + this.itemDesc;
//    }
//}


package Model.AbstractClasses;

import Model.Exploration.Room;

// Parent class for all items in the game.
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



