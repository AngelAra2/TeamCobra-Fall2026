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
//import Model.Items.Weapon;
import Model.Items.Inventory;
import Model.Items.EquipmentItem;
import View.*;

public class Player extends Character
{
	/* Data Fields */
	private static final String STATES_FILE_PATH = "/finiteStates/PlayerStates";
	private static final String CONSTANTS_FILE_PATH = "/constants/PlayerConstants";
	
	// Private constants that cannot be modified
	private float BASE_ENCOUNTER_CHANCE; // Base encounter chance for monsters
	private float ENCOUNTER_CHANCE_INCREMENT; // Incremental increase in encounter chance after each move
	private int ENCOUNTER_GRACE_MOVES; // Number of moves before monster encounters can occur again after a previous encounter
	
	private Room currentRoom;
	//private HashMap<String, ArrayList<Item>> inventory;
    private Inventory inventory;
	private Monster examinedMonster;
	
	private float currentEncounterChance; // Current encounter chance for monsters, which increases after each move
	private int movesSinceLastEncounter; // Keeps track of the number of moves since the last monster encounter

	/* Constructors */
	/*
	11.10.26
	Angel
	Adding new constructor to accept player name and starting class.
	 */

	public Player(String name, StartingClass startingClass, Room startingRoom){
		super(
				name, //name
				startingClass.getBaseMaxHP(), //max HP
				0, //physicalDEF
				0, //magicDEF
				42, //attack
				1, //currentLevel

				0, //currentXP
				100, //currentMana
				startingClass.getBaseMaxHP(), //baseMaxHP
				startingClass.getBaseSTR(), //strength
				startingClass.getBaseINT(), //intelligence
				startingClass.getBaseDEX(), //dexterity
				startingClass.getBaseARC(),  //arcane
				startingClass.getBaseEND(), //endurance
				new ArrayList<>() //combatSkills
		);
		this.currentRoom = startingRoom;
		//this.inventory = new HashMap<>();
        this.inventory = new Inventory();
		this.examinedMonster = null;

		//Create Finite State Machine for Player
		DataFileExtraction.extractStatesDataToCombatant(DataExtractionMethods::extractFiniteStatesData, STATES_FILE_PATH, this);
		
		// Populate private constants from file
		String[] constantsData = DataFileExtraction.extractConstantsData(DataExtractionMethods::extractPlayerConstants, CONSTANTS_FILE_PATH);
		
		try
		{
			this.BASE_ENCOUNTER_CHANCE = Float.parseFloat(constantsData[0]);
			this.ENCOUNTER_CHANCE_INCREMENT = Float.parseFloat(constantsData[1]);
			this.ENCOUNTER_GRACE_MOVES = Integer.parseInt(constantsData[2]);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			this.BASE_ENCOUNTER_CHANCE = 0.33f;
			this.ENCOUNTER_CHANCE_INCREMENT = 0.05f;
			this.ENCOUNTER_GRACE_MOVES = 3;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			this.BASE_ENCOUNTER_CHANCE = 0.33f;
			this.ENCOUNTER_CHANCE_INCREMENT = 0.05f;
			this.ENCOUNTER_GRACE_MOVES = 3;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			this.BASE_ENCOUNTER_CHANCE = 0.33f;
			this.ENCOUNTER_CHANCE_INCREMENT = 0.05f;
			this.ENCOUNTER_GRACE_MOVES = 3;
		}
		
		this.currentEncounterChance = this.BASE_ENCOUNTER_CHANCE;
		this.movesSinceLastEncounter = 0;
	}

	/* Methods */
	
	// Private //
	private boolean forceMonsterEncounter()
	{
		// If the current room already has monsters when the player enters, trigger an encounter immediately
		if (this.currentRoom.getMonsters().size() > 0)
			return true;
		
		// Guard Clauses //
		// If the player has not made enough moves since the last encounter, return false
		if (this.movesSinceLastEncounter < this.ENCOUNTER_GRACE_MOVES)
			return false;
		
		// Calculate the encounter chance based on the number of moves since the last encounter
		float rng = (float) Math.random();

		// If chance triggers, reset the encounter chance and moves since last encounter, then return true
		if (rng < this.currentEncounterChance)
		{
			this.currentEncounterChance = this.BASE_ENCOUNTER_CHANCE;
			this.movesSinceLastEncounter = 0;
			return true;
		}
		
		// Otherwise, increment the encounter chance, then return false
		this.currentEncounterChance += this.ENCOUNTER_CHANCE_INCREMENT;
		return false;
	}
	
	// Adds an item to the player's inventory if there is enough capacity.
    private boolean _addItemToInventory(Item item) {
        return this.inventory.addItem(item, this.currentLevel);
    }

    // Removes an item from the player's inventory.
    private boolean _removeItemFromInventory(Item item) {
        return this.inventory.removeItem(item);
    }

    // Returns how many copies of an item the player owns.
    private int _getQuantityOfItemsInInventory(String itemName) {
        return this.inventory.getQuantity(itemName);
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

//	public Item getItemFromInventoryByName(String itemName)
//	{
//		// Check if the inventory contains this item name
//		if (this.inventory.containsKey(itemName))
//		{
//			ArrayList<Item> itemList = this.inventory.get(itemName);
//			if (itemList.size() > 0)
//				return itemList.get(0);
//		}
//
//		return null;
//	}
    public Item getItemFromInventoryByName(String itemName) {
        return this.inventory.findItem(itemName);
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
			View.println(roomDesc);
			
			// Print out message if this room has been visited before
			if (wasVisited)
				View.println("You've been here before...");
			
			/* Move player to this room */
			this._setCurrentRoom(targetRoom);
			this.movesSinceLastEncounter++; // Increase the number of moves since the last combat encounter
			
			// Reset monster being examined when moving to a new room
			this.resetExaminedMonster();
			
			/* Check for monster encounter */
			boolean encounterTriggered = this.forceMonsterEncounter();

			// If an encounter with monsters is triggered, have the current room generate random monsters
			if (encounterTriggered)
				this.currentRoom.generateMonsters();
			
			/* Check if the current room has monsters */
			// If there are monsters, the player and co. will enter combat
			if (this.currentRoom.getMonsters().size() > 0)
			{
				// Populate battlefield with player and monsters
				ArrayList<Monster> monstersInRoom = new ArrayList<>();
				for (ArrayList<Monster> monsterList: this.currentRoom.getMonsters().values()) // HashMap
					for (Monster monster: monsterList) // ArrayList
						monstersInRoom.add(monster);
				
				Battlefield.populateBattlefield(this, monstersInRoom);
				
				// Set the player's state to "inCombat"
				this.setCurrentState("inCombat");
			}
			
			/* Check if the player has entered a room with a puzzle that is unsolved */
			// Make sure the player is *not* in combat before checking for puzzles
			Puzzle getPuzzleInRoom = this.getCurrentPuzzle();
			if ( 
				!this.getCurrentState().equalsIgnoreCase("inCombat") && 
				(getPuzzleInRoom != null && !getPuzzleInRoom.getIsSolved())
			)
			{
				// Reset the puzzle (reset its remaining attempts)
				getPuzzleInRoom.resetPuzzle();
				
				// Set player's state to "solvingPuzzle"
				this.setCurrentState("solvingPuzzle");
			}
			
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
        HashMap<String, ArrayList<Item>> allItems = this.inventory.getItems();
		// Get all items in the player's inventory
		//if (this.inventory.size() > 0)
        if (allItems.size() > 0) {
			View.println("Inventory:");

			//for (ArrayList<Item> itemList: this.inventory.values())
            for (ArrayList<Item> itemList: allItems.values()) {
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
//            if (action.equals("pickup"))
//            {
//                // The player picks up the item from the current room and adds it to their inventory
//                // The item is removed from the current room's items
//                targetItem = this.currentRoom.getItemByName(itemName);
//                if (targetItem != null)
//                {
//                    // Add item to inventory
//                    this._addItemToInventory(targetItem);
//
//                    // Remove item from current room
//                    this.currentRoom.removeItem(itemName, targetItem);
//
//                    View.println("Picked up " + targetItem.getItemName() + ". Added to Inventory");
//                }
//                else
//                    View.println("No such item to pick up in the current room.");
//            }
            // Pickup item
            if (action.equals("pickup")) {

                targetItem = this.currentRoom.getItemByName(itemName);

                if (targetItem != null) {

                    // Check carrying capacity before removing the item from the room.
                    if (this._addItemToInventory(targetItem)) {

                        this.currentRoom.removeItem(itemName, targetItem);

                        View.println("Picked up " + targetItem.getItemName()
                                + ". Added to Inventory");
                    }
                    else {
                        View.println("You cannot carry this item. "
                                + "Your inventory is too heavy.");
                    }
                }
                else {
                    View.println("No such item to pick up in the current room.");
                }
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
//            else if (action.equals("drop"))
//            {
//                // The player drops the item from their inventory and adds it to the current room's items HashMap
//                // The item is removed from the inventory
//                targetItem = this.getItemFromInventoryByName(itemName);
//                if (targetItem != null)
//                {
//                    // Remove item from inventory
//                    this._removeItemFromInventory(targetItem);
//
//                    // Add item to current room
//                    this.currentRoom.addItem(itemName, targetItem);
//
//                    View.println("Dropped " + targetItem.getItemName() + ". Added to " + this.currentRoom.getRoomName());
//
//                    // If the dropped item was the currently equipped weapon, unequip it
//                    if (targetItem instanceof Weapon && this.equippedWeapon == targetItem)
//                    {
//                    	this.handleItemInteraction("unequip " + itemName);
//                    }
//                }
//                else
//                    View.println("No such item to drop in your inventory");
//            }

            // Drop item
            else if (action.equals("drop")) {

                targetItem = this.getItemFromInventoryByName(itemName);

                if (targetItem != null) {

                    // If the item is equipped, unequip it first.
                    if (targetItem instanceof EquipmentItem) {

                        EquipmentItem equipment = (EquipmentItem) targetItem;

                        if (equipment.isEquipped()) {
                            this.equipmentLoadout.unequipItem(equipment.getSlot());
                        }
                    }

                    // Remove the item from inventory.
                    if (this._removeItemFromInventory(targetItem)) {

                        // Add the item to the current room.
                        this.currentRoom.addItem(itemName, targetItem);

                        View.println("Dropped " + targetItem.getItemName()
                                + ". Added to " + this.currentRoom.getRoomName());
                    }
                }
                else {
                    View.println("No such item to drop in your inventory");
                }
            }
            
            // Equip item (weapon)
//            else if (action.equals("equip"))
//			{
//				// The player equips the item from their inventory
//				targetItem = this.getItemFromInventoryByName(itemName);
//				if (targetItem != null)
//				{
//					if (targetItem instanceof Weapon)
//					{
//						// Unequip the currently equipped weapon
//						if (this.equippedWeapon != null)
//						{
//							this.handleItemInteraction("unequip " + this.equippedWeapon.getItemName());
//						}
//
//						// Equip the weapon
//						this.equipWeapon((Weapon) targetItem);
//						View.println("Equipped " + targetItem.getItemName());
//					}
//					else
//						View.println("Cannot equip " + targetItem.getItemName() + " since it isn't a weapon");
//				}
//				else
//					View.println("No such weapon to equip in your inventory");
//			}
//
//            // Unequip item (weapon)
//			else if (action.equals("unequip"))
//			{
//				// Check if the player has a weapon equipped
//				if (this.equippedWeapon != null)
//				{
//					// Unequip the weapon
//					Weapon unequippedWeapon = this.equippedWeapon;
//
//					this.unequipWeapon();
//					View.println("Unequipped " + unequippedWeapon.getItemName());
//				}
//				else
//					View.println("No weapon is currently equipped");
//			}
            // Equip an item.
            else if (action.equals("equip")) {
                targetItem = this.getItemFromInventoryByName(itemName);

                if (targetItem != null) {

                    // Only EquipmentItem objects can be equipped.
                    if (targetItem instanceof EquipmentItem) {
                        EquipmentItem equipment = (EquipmentItem) targetItem;

                        // Equip the item using the character's equipment loadout.
                        boolean equipped = this.equipmentLoadout.equipItem(equipment, this.calculateEquipLoad());

                        if (equipped) {
                            View.println("Equipped " + equipment.getItemName());
                        } else {
                            View.println("Unable to equip " + equipment.getItemName()
                                    + ". Check its equipment slot and your equipment load.");
                        }
                    } else {
                        View.println("Cannot equip " + targetItem.getItemName()
                                + " because it is not equipment.");
                    }
                } else {
                    View.println("No such item to equip in your inventory.");
                }
            }

// Unequip an item.
            else if (action.equals("unequip")) {
                targetItem = this.getItemFromInventoryByName(itemName);

                if (targetItem != null && targetItem instanceof EquipmentItem) {
                    EquipmentItem equipment = (EquipmentItem) targetItem;

                    if (equipment.isEquipped()) {
                        this.equipmentLoadout.unequipItem(equipment.getSlot());
                        View.println("Unequipped " + equipment.getItemName());
                    }

                    else {
                        View.println(equipment.getItemName() + " is not equipped.");
                    }
                }
                else {
                    View.println("No equipped item with that name was found in your inventory.");
                }
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
						//this._removeItemFromInventory(consumable);

                        // Marks the consumable as consumed after it is successfully removed from the inventory
                        if (this._removeItemFromInventory(consumable)) {
                            consumable.setConsumed(true);
                        }
						
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
    			View.println("Invalid command: no monster name provided"); 
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
//    public void addItemToInventory(Item item) {
//        this._addItemToInventory(item);
//    }
    // Allows other classes to add an item while checking carrying capacity.
    public boolean addItemToInventory(Item item) {
        return this._addItemToInventory(item);
    }

    // Allows other classes to access the player's inventory.
    public Inventory getInventory() {
        return this.inventory;
    }
}

