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
import Model.Items.Weapon;
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
    private Inventory inventory;
	private Monster examinedMonster;
	private Party playerParty;
	
	private float currentEncounterChance; // Current encounter chance for monsters, which increases after each move
	private int movesSinceLastEncounter; // Keeps track of the number of moves since the last monster encounter
	private String startingClass;
	
	// Currency
	private int goldBalance;
	
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
				70, //attack
				1, //currentLevel

				0, //currentXP
				30 + (2 * startingClass.getBaseINT()), //currentMana
				startingClass.getBaseMaxHP(), //baseMaxHP
				startingClass.getBaseSTR(), //strength
				startingClass.getBaseINT(), //intelligence
				startingClass.getBaseDEX(), //dexterity
				startingClass.getBaseARC(),  //arcane
				startingClass.getBaseEND(), //endurance
				startingClass.getCombatSkill() //combatSkills
		);
		this.currentRoom = startingRoom;
        this.inventory = new Inventory();

		//add starting class equipment to player inventory
		for (Item item : startingClass.getDefaultEquipment()){
			this.inventory.addItem(item, this.currentLevel);

		}
		this.examinedMonster = null;
		this.startingClass = startingClass.getName();
		this.playerParty = new Party(this);

		// Currency
		this.goldBalance = 0;
		
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
			this.ENCOUNTER_CHANCE_INCREMENT = 0.03f;
			this.ENCOUNTER_GRACE_MOVES = 3;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			this.BASE_ENCOUNTER_CHANCE = 0.33f;
			this.ENCOUNTER_CHANCE_INCREMENT = 0.03f;
			this.ENCOUNTER_GRACE_MOVES = 3;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			this.BASE_ENCOUNTER_CHANCE = 0.33f;
			this.ENCOUNTER_CHANCE_INCREMENT = 0.03f;
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
	
	private void _adjustGoldBalance()
	{
		// Ensure that the player's gold balance is never negative
		if (this.goldBalance < 0)
			this.goldBalance = 0;
	}

	// Getters //
	public String getCurrentState() { return this.stateMachine.getCurrentState(); }
	public Room getCurrentRoom() { return this.currentRoom; }
	public Puzzle getCurrentPuzzle() { return this.currentRoom.getPuzzle(); }
	public Monster getExaminedMonster() { return this.examinedMonster; }
	public String getStartingClass(){return this.startingClass; }
	public Party getPlayerParty() { return this.playerParty; }
	public int getGoldBalance() { return this.goldBalance; }
	
	public boolean hasSufficientGold(int amount) { return this.goldBalance >= amount; }

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
   
    // Allows other classes to access the player's inventory.
    public Inventory getInventory() {
        return this.inventory;
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
			ExplorationView.printRoomInfo(targetRoom);
			
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
				
				Battlefield.populateBattlefield(this.playerParty, monstersInRoom);
				
				// Set the player's state to "inCombat"
				this.setCurrentState("inCombat");
			}
			
			// NOTE TO MERGERS. MERGE USING THIS PLEASE FOR THE LOVE OF GOD.
			// Movement successful
			return true;
		}
		
		// Movement failed
		return false;
	}
	
	public void viewRoomInfo()
	{
		/* Print out details about the current room */
		ExplorationView.printRoomInfo(this.getCurrentRoom());
	}
	
	public void exploreCurrentRoom()
	{
		/* Explore the current room */
		
		// Print out all items in the current room
		ExplorationView.printItemsInRoom(currentRoom.getItems());
		View.println("");
		
		// Print out all available exits in the current room
		ExplorationView.printRoomExits(currentRoom.getExits());
		View.println("");
		
		// Print out NPCs/NPC companions in the current room //
		ExplorationView.printCompanionsInRoom(currentRoom.getCompanions());
        View.println("");

        ExplorationView.printMerchantsInRoom(currentRoom);
        View.println("");

        ExplorationView.printPuzzleInRoom(currentRoom);
        View.println("");
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
				
				String itemInfo = "- " + itemName + " (x" + itemQuantity + ")";

				// Check if item is equipment
				if (getFirstItem instanceof EquipmentItem) {

					EquipmentItem equipment = (EquipmentItem) getFirstItem;

					String itemSlot = equipment.getSlot();
					//adds item slot name.
					itemInfo += " [" + itemSlot + "]";

					// Check if any copy is equipped
					for (Item inventoryItem : itemList) {
						if (inventoryItem instanceof EquipmentItem) {
							EquipmentItem equippedItem = (EquipmentItem) inventoryItem;

							//if item is equipped, add (Equipped)
							if (equippedItem.isEquipped()) {
								itemInfo += " (Equipped)";
								break;
							}
						}
					}
				}
				//display item info
				View.println(itemInfo);
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
//            if (action.equalsIgnoreCase("pickup"))
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
            if (action.equalsIgnoreCase("pickup")) {

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
            else if (action.equalsIgnoreCase("inspect"))
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
//            else if (action.equalsIgnoreCase("drop"))
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
            else if (action.equalsIgnoreCase("drop")) {

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
//            else if (action.equalsIgnoreCase("equip"))
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
//			else if (action.equalsIgnoreCase("unequip"))
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
            else if (action.equalsIgnoreCase("equip")) {
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
            else if (action.equalsIgnoreCase("unequip")) {
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
			else if (action.equalsIgnoreCase("heal"))
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
				
				BattleView.printCombatantStatus(this, false);
			}
        }

        // If item name is empty, print error message and return
        else
            View.println("Invalid command: no item name provided.");
    }
    
    public void handleNPCInteraction(String cmd)
    {
    	/* Handles interaction commands with NPCs and NPC companions */
    	
    	// Normalize the command (NPC names are case-insensitive), then split it into
        // the action and the monster name at the first run of whitespace
        String[] cmdParts = cmd.trim().toLowerCase().split("\\s+", 2);

        // Declare attributes
        String action = cmdParts[0];
        String npcName = (cmdParts.length > 1) ? cmdParts[1].trim() : "";
        
        // Handle the proper interaction with the given NPC name //
        if (!npcName.isEmpty())
		{
			// Recruit NPC companion
			if (action.equalsIgnoreCase("recruit"))
			{
				// The player recruits the NPC companion in the current room and adds them to their party
				// The NPC companion is removed from the current room's companions list
				Object targetNPC = this.currentRoom.getNPCByName(npcName);
				if (targetNPC != null)
				{
					if (targetNPC instanceof Companion)
					{
						// Have the Party class handle the recruitment of the companion
						Companion companion = (Companion) targetNPC;

						boolean recruitmentSuccessful = this.playerParty.recruit(companion);

						// Remove companion from current room if recruitment was successful
						if (recruitmentSuccessful)
							this.currentRoom.removeCompanion(companion);

						// Print information about the companion being recruited
						ExplorationView.printCompanionRecruitment(recruitmentSuccessful, this.playerParty, companion);
					}
					
					// Not an NPC companion, so cannot be recruited
					else
						View.println("Cannot recruit " + npcName + " since they're not a Companion");
				}
				
				// No NPC companion with that name was found in the current room
				else
					View.println("No such Companion to recruit in the current room");
			}

			// Add/remove NPC companion to/from party and mark them as active/inactive
			else if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("remove"))
			{
				Object targetNPC;
				List<Companion> companions;

				// Adding party member
				if (action.equalsIgnoreCase("add"))
				{
					companions = this.playerParty.getInactiveCompanions();

					// Attempts to find the companion in the inactive companions list
					targetNPC = companions.stream()
							.filter(companion -> companion.getName().equalsIgnoreCase(npcName))
							.findFirst()
							.orElse(null);

					// Print information about the companion being added to the active party
					if (targetNPC != null && targetNPC instanceof Companion)
					{
						Companion companion = (Companion) targetNPC;
						ExplorationView.printAddedCompanionToParty(this.playerParty.addCompanionToActiveParty(companion), companion);
					}

					else
						View.println("No such Companion in the Inactive Party to add to the Active Party");
				}

				// Removing party member
				else if (action.equalsIgnoreCase("remove"))
				{
					companions = this.playerParty.getActiveCompanions();

					// Attempts to find the companion in the active companions list
					targetNPC = companions.stream()
							.filter(companion -> companion.getName().equalsIgnoreCase(npcName))
							.findFirst()
							.orElse(null);

					// Print information about the companion being removed from the active party
					if (targetNPC != null && targetNPC instanceof Companion)
					{
						Companion companion = (Companion) targetNPC;
						ExplorationView.printRemovedCompanionFromParty(this.playerParty.removeCompanionFromActiveParty(companion), companion);
					}

					else
						View.println("No such Companion in the Active Party to remove to the Inactive Party");
				}
			}
		}
        
        // No NPC name provided
		else
			View.println("Invalid command: no NPC name provided");
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
            	if (action.equalsIgnoreCase("examine"))
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
    
    public void switchMonsterTarget(String cmd)
    {
    		/* While in combat, the player can switch their party's current target to a different monster on the battlefield */
    	
    		// Guard Clauses //
    		// If the player is not in combat, they cannot switch targets
    		if (!this.getCurrentState().equalsIgnoreCase("inCombat"))
    			return;
    		
    		String[] cmdParts = cmd.trim().toLowerCase().split("\\s+", 2);
    		
    		// Get the target monster index from the command (subtract 1 to convert to 0-based index)
    		int targetMonsterIndex = cmdParts.length > 1 ? Integer.parseInt(cmdParts[1].trim()) - 1 : -1;
    		
    		// Attempt to switch the monster target on the battlefield
    		Battlefield.switchMonsterTarget(targetMonsterIndex);
    }
    
	public boolean attemptToSolvePuzzle(String userInput)
	{
		/* Guard Clauses */
		// Do not attempt to solve a puzzle if there is no puzzle in the current room
		if (this.currentRoom.getPuzzle() == null)
			return false;
		
		// Do not attempt to solve a puzzle if the player has already solved it
		if (this.currentRoom.getPuzzle().getIsSolved())
			return true;
		
		// Attempt to solve the puzzle
		return this.currentRoom.getPuzzle().checkSolution(userInput);
	}

	public void viewPartyStatus()
	{
		/* View the player's party's status */
		//this.playerParty.printPartyStatus();
	}
	
	// Reset to default state (exploring) when the player is not in combat or solving a puzzle
    public void resetToDefaultState() { this.setCurrentState("exploring"); }

    
    public void addGold(int amount) 
    {
    		/* Adds gold to the player's balance and adjusts it to ensure it is not negative */
    	
		this.goldBalance += amount;
		this._adjustGoldBalance();
	}

    public void spendGold(int amount) 
	{
    		/* Subtracts gold to the player's balance and adjusts it to ensure it is not negative. */
    		this.goldBalance -= amount;
		this._adjustGoldBalance();
	}

    public boolean gainXP(int amount) 
	{
    	/* Adds XP to the player and their active companions */
		boolean hasLeveledUp = this.addXP(amount); // XP for player
		
		// Add XP to all active Companions in the player's party
		for (Companion companion: this.playerParty.getActiveCompanions())
			companion.addXP(amount);
		
		// Add XP to all inactive Companions in the player's party
		for (Companion companion: this.playerParty.getInactiveCompanions())
			companion.addXP(amount);
		
		// Return whether the player has leveled up
		return hasLeveledUp;
	}
    
    // Allows other classes to add an item while checking carrying capacity.
    public boolean addItemToInventory(Item item) {
        return this._addItemToInventory(item);
    }

}

