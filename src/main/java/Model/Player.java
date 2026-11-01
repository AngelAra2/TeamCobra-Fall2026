package Model;
import java.util.ArrayList;
import java.util.HashMap;

import Model.Combat.Character;
import View.View;
import java.util.List;

public class Player extends Character
{
	/* Data Fields */
	private Room currentRoom;
    private Inventory inventory;
	private int health;

	/* Constructors */
	public Player(Room startingRoom)
	{
		super("Name", 100, 10, 10, 10,
				10, 10, 100, 10, 10, 10,
				10, 10,new ArrayList<>());
		this.currentRoom = startingRoom;
		this.inventory = new Inventory();
	}
	
	/* Methods */
	
	// Private //
	private void _addItemToInventory(Item item)
	{
		String itemName = item.getItemName();
		itemName = itemName.toLowerCase();
		
		// If the inventory doesn't already contain this item ID, create a new ArrayList for it
		if (!this.inventory.containsKey(itemName))
			this.inventory.put(itemName, new ArrayList<Item>());
		
		// Add the item to the inventory
		this.inventory.get(itemName).add(item);
	}
	
	private void _removeItemFromInventory(Item item)
	{
		String itemName = item.getItemName();
		itemName = itemName.toLowerCase();
		
		// Check if the item ID is recorded
		if (this.inventory.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.inventory.get(itemName);
			itemList.remove(item);
			
			// Remove the item ID from the HashMap if there are no more items of that ID
			if (itemList.size() <= 0)
				this.inventory.remove(itemName);
		}
	}
	
	private int _getQuantityOfItemsInInventory(String itemName)
	{
		itemName = itemName.toLowerCase();
		
		// Check if the inventory contains this item name
		if (this.inventory.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.inventory.get(itemName);
			return itemList.size();
		}

		return 0;
	}
	
	private void _setCurrentRoom(Room newRoom)
	{
		this.currentRoom = newRoom;
		
		// Mark this room as visited if it wasn't already
		this.currentRoom.setWasVisited();
	}

	// Getters //
	public Room getCurrentRoom()
	{
		return this.currentRoom;
	}
	
	public Puzzle getCurrentPuzzle()
	{
		return this.currentRoom.getPuzzle();
	}

	public Item getItemFromInventoryByName(String itemName)
	{
		// Check if the inventory contains this item name
		if (this.inventory.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.inventory.get(itemName);
			if (itemList.size() > 0)
				return itemList.get(0);
		}
		
		return null;
	}
    // Get inventory
    public Inventory getInventory() {
        return this.inventory;
    }

    // Equip an item from inventory
    public boolean equipItem(String itemName) {
        Item item = this.inventory.findItem(itemName);

        if (item instanceof EquipmentItem) {
            EquipmentItem equipment = (EquipmentItem) item;

            if (this.equipmentLoadout.equipItem(
                    equipment, this.calculateEquipLoad())) {

                this.inventory.removeItem(item);
                return true;
            }
        }

        return false;
    }

	// Custom //
	public boolean moveToRoom(Map worldMap, String direction)
	{
		/* Update player to move in the direction they want */
		// Get room ID based on direction
		int targetRoomId = currentRoom.getRoomIdByDirection(direction);
		Room targetRoom = worldMap.getRoomById(targetRoomId);
		
		// If room was found, attempt to move the player in that direction
		if (targetRoom != null)
		{
			/* Print out details about the room BEFORE entering */
			String roomName = targetRoom.getRoomName();
			String roomDesc = targetRoom.getRoomDescription();
			boolean wasVisited = targetRoom.getWasVisited();
			
			// Print out room details
			View.println("Now entering: " + roomName);
			View.println("Description: " + roomDesc);
			
			// Print out message if this room has been visited before
			if (wasVisited)
				View.println("You've visited this room before...");
			
			/* Move player to this room */
			this._setCurrentRoom(targetRoom);
			
			// Movement successful
			return true;
		}
		
		// Movement failed
		return false;
	}
	
	public void printCurrentRoomDetails()
	{
		/* Print out details about the current room */
		String roomName = this.currentRoom.getRoomName();
		String roomDesc = this.currentRoom.getRoomDescription();
		boolean wasVisited = this.currentRoom.getWasVisited();
		
		View.println("Current Room: " + roomName);
		View.println("Description: " + roomDesc);
		
		if (wasVisited)
			View.println("You've visited this room before...");
	}
	
	public void exploreCurrentRoom()
	{
		/* Return the HashMap of items in the current room */
		
		// Get all items in the current room
		HashMap<String, ArrayList<Item>> allItems = this.currentRoom.getItems();
		if (allItems.size() > 0)
		{
			View.println("Items found in this room:");
			for (ArrayList<Item> itemList: allItems.values())
			{
				// Print out item name and quantity of each item in the room
				Item getFirstItem = itemList.get(0);
				String itemName = getFirstItem.getItemName();
				int itemQuantity = itemList.size();
				
				View.println("- " + itemName + " (�" + itemQuantity + ")");
			}
		}
		
		// If no items were found, print message
		else
			View.println("No items found in this room.");
	}
	
	public void viewInventory()
	{
		/* View the player's inventory */
		View.println("Viewing your inventory...");
		View.println("");

		// Get all items in the player's inventory
		if (this.inventory.size() > 0)
		{
			View.println("Items in your inventory:");
			for (ArrayList<Item> itemList: this.inventory.values())
			{
				// Print out item name and quantity of each item in the room
				Item getFirstItem = itemList.get(0);
				String itemName = getFirstItem.getItemName();
				int itemQuantity = this._getQuantityOfItemsInInventory(itemName);
				
				View.println("- " + itemName + " (�" + itemQuantity + ")");
			}
		}
		
		// If no items were found, print message
		else
			View.println("Your inventory is empty. Pick up some items!");
	}
	
	public void handleItemInteraction(String cmd)	
	{
		/* Handles item interaction commands */
		// Replace first space in userCmd with an underscore to prevent issues with item names that have spaces
		cmd = cmd.replaceFirst(" ", "_");

		// Split the command into parts
		String[] cmdParts = cmd.split("_");
		
		// Declare attributes
		String action = cmdParts[0];
		String itemName = (cmdParts.length > 1) ? cmdParts[1] : "";
		
		/* Handle the proper interaction with the given item name */
		if (!itemName.isEmpty())
		{
			Item targetItem;
			
			// Pickup item
			if (action.equals("pickup"))
			{
				// The player picks up the item from the current room and adds it to their inventory
				// The item is removed from the current room's items
				targetItem = this.currentRoom.getItemByName(itemName);
				if (targetItem != null)
				{
					// Add item to inventory
					this._addItemToInventory(targetItem);

					// Remove item from current room
					this.currentRoom.removeItem(itemName, targetItem);
					
					View.println("Picked up " + targetItem.getItemName() + ". Added to Inventory");
				}
				else
					View.println("No such item to pick up in the current room.");
			}
			
			// Inspect item
			else if (action.equals("inspect"))
			{
				// The player inspects the item in their inventory and prints its details to console
				// The item is not removed from the inventory
				targetItem = this.getItemFromInventoryByName(itemName);
				if (targetItem != null)
				{
					View.println("Inspecting item in inventory...");
					View.println("");
					View.println(targetItem.toString());
					View.println("");
					View.println("Number owned in inventory: " + this._getQuantityOfItemsInInventory(itemName));
				}
				else
					View.println("No such item to inspect in your inventory.");
			}
			
			// Drop item
			else if (action.equals("drop"))
			{
				// The player drops the item from their inventory and adds it to the current room's items HashMap
				// The item is removed from the inventory
				targetItem = this.getItemFromInventoryByName(itemName);
				if (targetItem != null)
				{
					// Remove item from inventory
					this._removeItemFromInventory(targetItem);

					// Add item to current room
					this.currentRoom.addItem(itemName, targetItem);
					
					View.println("Dropped " + targetItem.getItemName() + ". Added to " + this.currentRoom.getRoomName());
				}
				else
					View.println("No such item to drop in your inventory.");
			}
		}
		
		// If item name is empty, print error message and return
		else
			View.println("Invalid command: no item name provided.");		
	}
	
	public boolean attemptToSolvePuzzle(String userInput)
	{
		/* Guard Clauses */
		// Do not attempt to solve a puzzle if there is no puzzle in the current room
		if (this.currentRoom.getPuzzle() == null)
			return false;
		
		// Do not attempt to solve a puzzle if it has no remaining attempts
		if (this.currentRoom.getPuzzle().getRemainingAttempts() <= 0)
			return false;
		
		// Do not attempt to solve a puzzle if the player has already solved it
		if (this.currentRoom.getPuzzle().getIsSolved())
			return true;
		
		// Attempt to solve the puzzle
		return this.currentRoom.getPuzzle().checkSolution(userInput);
	}
}


