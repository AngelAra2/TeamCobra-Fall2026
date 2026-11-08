package Model.Combat;
import java.util.*;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Item;
import Model.Data.DataExtractionMethods;
import Model.Data.DataFileExtraction;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import Model.Items.Consumable;
import Model.Items.Weapon;
import View.*;

public class Player extends Character
{
	/* Data Fields */
	private static String statesFilePath = "/finiteStates/PlayerStates";
	
	private Room currentRoom;
	private HashMap<String, ArrayList<Item>> inventory;
	private Monster examinedMonster;

	/* Constructors */
	public Player(Room startingRoom)
	{
		super(
			"Player", // name
			100, // maxHP
			0, // physicalDEF
			0, // magicDEF
			5, // attack
			
			1, // currentLevel
			0, // currentXP
			100, // currentMana
			100, // baseMaxHP
			10, // strength
			10, // intelligence
			10, // dexterity
			10, // arcane
			10, // endurance
			new ArrayList<>() // CombatSkills
		);
		
		this.currentRoom = startingRoom;
		this.inventory = new HashMap<>();
		this.examinedMonster = null;
		
		// Create Finite State Machine for Player
		DataFileExtraction.extractStatesDataToCombatant(DataExtractionMethods::extractFiniteStatesData, statesFilePath, this);
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
	public String getCurrentState() { return this.stateMachine.getCurrentState(); }
	public Room getCurrentRoom() { return this.currentRoom; }
	public Puzzle getCurrentPuzzle() { return this.currentRoom.getPuzzle(); }
	public Monster getExaminedMonster() { return this.examinedMonster; }

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
	
	// Setters //
	public void setCurrentState(String newState) { this.stateMachine.setCurrentState(newState); }
	public void setExaminedMonster(Monster monster) { this.examinedMonster = monster; }
	public void resetExaminedMonster() { this.examinedMonster = null; }
	
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
				View.println("You've been here before...");
			
			/* Move player to this room */
			this._setCurrentRoom(targetRoom);
			
			// Reset monster being examined when moving to a new room
			this.resetExaminedMonster();
			
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
			View.println("You've been here before...");
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
				
				View.println("- " + itemName + " (x" + itemQuantity + ")");
			}
		}
		
		// If no items were found, print message
		else
			View.println("No items found in this room");
		
		View.println("");
		
		// Get all monsters in the current room //
		HashMap<String, ArrayList<Monster>> allMonsters = this.currentRoom.getMonsters();
		if (allMonsters.size() > 0)
		{
			View.println("Monsters found in this room:");
			for (ArrayList<Monster> monsterList: allMonsters.values())
			{
				// Print out item name and quantity of each item in the room
				Monster getFirstMonster = monsterList.get(0);
				String monsterName = getFirstMonster.getName();
				int monsterQuantity = monsterList.size();
				
				View.println("- " + monsterName + " (x" + monsterQuantity + ")");
			}
		}
		
		// If no monsters were found, print message
		else
			View.println("No monsters found in this room");
	}
	
	public void viewInventory()
	{
		/* View the player's inventory */
		View.println("");

		// Get all items in the player's inventory
		if (this.inventory.size() > 0)
		{
			View.println("Inventory:");
			for (ArrayList<Item> itemList: this.inventory.values())
			{
				// Print out item name and quantity of each item in the room
				Item getFirstItem = itemList.get(0);
				String itemName = getFirstItem.getItemName();
				int itemQuantity = this._getQuantityOfItemsInInventory(itemName);
				
				View.println("- " + itemName + " (x" + itemQuantity + ")");
			}
		}
		
		// If no items were found, print message
		else
			View.println("Inventory empty");
	}

    public void handleItemInteraction(String cmd)
    {
        /* Handles item interaction commands */
        // Normalize the command (item names are stored in lowercase), then split it into
        // the action and the item name at the first run of whitespace. This keeps item
        // names that contain spaces (e.g. "collector's coin") intact
        String[] cmdParts = cmd.trim().toLowerCase().split("\\s+", 2);

        // Declare attributes
        String action = cmdParts[0];
        String itemName = (cmdParts.length > 1) ? cmdParts[1].trim() : "";

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
                    
                    // If the dropped item was the currently equipped weapon, unequip it
                    if (targetItem instanceof Weapon && this.equippedWeapon == targetItem)
                    {
                    	this.handleItemInteraction("unequip " + itemName);
                    }
                }
                else
                    View.println("No such item to drop in your inventory");
            }
            
            // Equip item (weapon)
            else if (action.equals("equip"))
			{
				// The player equips the item from their inventory
				targetItem = this.getItemFromInventoryByName(itemName);
				if (targetItem != null)
				{
					if (targetItem instanceof Weapon)
					{
						// Unequip the currently equipped weapon
						if (this.equippedWeapon != null)
						{
							this.handleItemInteraction("unequip " + this.equippedWeapon.getItemName());
						}
						
						// Equip the weapon
						this.equipWeapon((Weapon) targetItem);
						View.println("Equipped " + targetItem.getItemName());
					}
					else
						View.println("Cannot equip " + targetItem.getItemName() + " since it isn't a weapon");
				}
				else
					View.println("No such weapon to equip in your inventory");
			}
            
            // Unequip item (weapon)
			else if (action.equals("unequip"))
			{
				// Check if the player has a weapon equipped
				if (this.equippedWeapon != null)
				{
					// Unequip the weapon
					Weapon unequippedWeapon = this.equippedWeapon;
					
					this.unequipWeapon();
					View.println("Unequipped " + unequippedWeapon.getItemName());
				}				
				else
					View.println("No weapon is currently equipped");
			}
            
            // Heal item (consumable)
			else if (action.equals("heal"))
			{
				// The player uses the item from their inventory to heal themselves
				targetItem = this.getItemFromInventoryByName(itemName);
				if (targetItem != null)
				{
					if (targetItem instanceof Consumable)
					{
						// Check player's current health
						if (this.getCurrentHP() >= this.getMaxHP())
						{
							View.println("You're at full health. There's no need to heal");
							return;
						}
						
						Consumable consumable = (Consumable) targetItem;
						int healingAmount = consumable.getHealingAmount();
						
						// Heal the player
						this.heal(healingAmount);
						
						// Remove the consumable from the player's inventory after use
						this._removeItemFromInventory(consumable);
						
						View.println("Used " + consumable.getItemName() + " to heal " + healingAmount + " HP");
					}
					else
						View.println("Cannot use " + targetItem.getItemName() + " to heal since it isn't a consumable");
				}
				else
					View.println("No such consumable to use in your inventory");
				
				BattleView.printCombatantStatus(this);
			}
        }

        // If item name is empty, print error message and return
        else
            View.println("Invalid command: no item name provided.");
    }
    
    public void handleMonsterInteraction(String cmd)
    {
    	/* Handles monster interaction commands */
        // Normalize the command (monster names are stored in lowercase), then split it into
        // the action and the monster name at the first run of whitespace. This keeps monster
        // names that contain spaces (e.g. "thief") intact
        String[] cmdParts = cmd.trim().toLowerCase().split("\\s+", 2);

        // Declare attributes
        String action = cmdParts[0];
        String monsterName = (cmdParts.length > 1) ? cmdParts[1].trim() : "";
        
        /* Handle the proper interaction with the given monster name */
        // Check if player is examining a monster
        Monster targetMonster;
        if (this.examinedMonster != null)
		{
        	// Attack/ignore monster
        	if (action.equals("attack") || action.equals("ignore"))
			{       		
        		targetMonster = this.getExaminedMonster();
        		
        		// Attack monster
        		if (action.equals("attack"))
        		{
        			// The player attacks the monster in the current room and initiates combat
        			if (targetMonster != null)
    				{
    					// Populate battlefield with player and monsters
    					Battlefield.populateBattlefield(this, new ArrayList<>(Arrays.asList(targetMonster)));
    					
    					// Change player's state to "inCombat"
    					this.setCurrentState("inCombat");

    					View.println("You've attacked " + targetMonster.getName() + "!");
    				}
    				else
    					View.println("No such monster to attack in the current room");
        		}
        		
        		// Ignore monster
				else
				{
					// Remove the monster from the current room and reset the examined monster
					monsterName = targetMonster.getName();
					this.getCurrentRoom().removeMonster(monsterName, targetMonster);

					View.println("You've chosen to ignore " + monsterName + ". You won't see them again...for now");
				}
        		
        		// Reset the examined monster after taking action
        		this.resetExaminedMonster();
			}
        }
        
        // Currently not examining a monster
        else
        {
        	if (!monsterName.isEmpty())
            {
            	// Examine monster
            	if (action.equals("examine"))
            	{
            		// The player examines the monster in the current room and prints its details to console
    				targetMonster = this.currentRoom.getMonsterByName(monsterName);
    				if (targetMonster != null)
    				{
    					// Set the examined monster for the player
    					this.setExaminedMonster(targetMonster);

    					View.println("");
    					View.printlnObject(targetMonster);
    				}
    				else
    					View.println("No such monster to examine in the current room");
            	}
            }
            
            // If monster name is empty, print error message and return
    		else
    			View.println("Invalid: no monster name provided");
        }  
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

	// Reset to default state (exploring) when the player is not in combat or solving a puzzle
    public void resetToDefaultState() { this.setCurrentState("exploring"); }

    // Add item to inventory; public method that calls private method
    public void addItemToInventory(Item item) { this._addItemToInventory(item); }
}

