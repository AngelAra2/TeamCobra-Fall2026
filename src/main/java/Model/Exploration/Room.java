package Model.Exploration;
import java.util.*;

import Model.AbstractClasses.Item;
import Model.Combat.Monster;
import Model.Factories.MonsterFactory;

public class Room 
{
	/* Data Fields */
	private static final int MAX_MONSTERS_PER_ROOM = 3;
	
	private int roomId;
	private String roomName;
	private String roomDesc;
	private boolean wasVisited;
	private HashMap<String, Integer> exits;
	private HashMap<String, ArrayList<Item>> items;
	private HashMap<String, ArrayList<Monster>> monsters;
	private List<Integer> spawnableMonsterIds;
	private Puzzle puzzle;
	private MonsterFactory monsterFactory;
	
	/* Constructors */
	public Room(
		int roomId,
		String roomName,
		String roomDesc,
		HashMap<String, Integer> exits
	)
	{
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomDesc = roomDesc;
		this.wasVisited = false;
		this.exits = exits;
		this.items = new HashMap<>();
		this.monsters = new HashMap<>();
		this.spawnableMonsterIds = new ArrayList<>();
		this.puzzle = null;
		
		this.monsterFactory = new MonsterFactory();
	}
	
	/* Methods */
	
	// Getters //
	public int getRoomId() { return this.roomId; }
	public String getRoomName() { return this.roomName; }	
	public String getRoomDescription() { return this.roomDesc; }
	public boolean getWasVisited() { return this.wasVisited; }
	public HashMap<String, Integer> getExits() { return this.exits; }
	public HashMap<String, ArrayList<Item>> getItems() { return this.items; }
	public HashMap<String, ArrayList<Monster>> getMonsters() { return this.monsters; }
	public List<Integer> getSpawnedMonsterIds() { return this.spawnableMonsterIds; }
	public Puzzle getPuzzle() { return this.puzzle; }
	
	public Item getItemByName(String itemName)
	{
		/* Returns item of corresponding name */
		
		// Checks if the room contains at least one item of the corresponding
		// name. If it exists, returns it
		if (this.items.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.items.get(itemName);
			if (itemList.size() > 0)
				return itemList.get(0);
		}
		
		return null;
	}
	
	public Monster getMonsterByName(String monsterName)
	{
		/* Returns monster of corresponding name */
		
		// Checks if the room contains at least one monster of the corresponding
		// name. If it exists, returns it
		if (this.monsters.containsKey(monsterName))
		{
			ArrayList<Monster> monsterList = this.monsters.get(monsterName);
			if (monsterList.size() > 0)
				return monsterList.get(0);
		}
		
		return null;
	}
	
	// Setters //
	public void setWasVisited()
	{
		if (this.wasVisited)
			return;

		this.wasVisited = true;
	}

	public void setPuzzle(Puzzle puzzle)
	{
		// Don't set puzzle if it already exists
		if (this.puzzle != null)
			return;
		
		this.puzzle = puzzle;
	}
	
	// Custom //
	public int getRoomIdByDirection(String dir)
	{
		// Check if the room in the specified direction is recorded
		if (this.exits.containsKey(dir))
			return this.exits.get(dir);
		
		return 0;
	}
	
	public void addItem(String itemName, Item item)
	{
		itemName = itemName.toLowerCase();

		// Add item to the room if its ArrayList exists in the HashMap
		if (this.items.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.items.get(itemName);
			itemList.add(item);
			
			// Set the item's current room to this room
			item.setCurrentRoom(this);
			
			return;
		}
		
		// If the ArrayList for the item name doesn't exist, create it and add the item to it
		else
		{
			ArrayList<Item> itemList = new ArrayList<>();
			itemList.add(item);
			this.items.put(itemName, itemList);
			
			// Set the item's current room to this room
			item.setCurrentRoom(this);
		}
	}
	
	public void removeItem(String itemName, Item item)
	{
		itemName = itemName.toLowerCase();
		
		// Check if the item's name is recorded
		if (this.items.containsKey(itemName))
		{
			ArrayList<Item> itemList = this.items.get(itemName);
			itemList.remove(item);
			
			// Set the item's current room to null
			item.removeFromCurrentRoom();
			
			// Remove the item's name from the HashMap if there are no more items of that name
			if (itemList.size() <= 0)
				this.items.remove(itemName);
		}
	}
	
	public void addMonster(String monsterName, Monster monster)
	{
		monsterName = monsterName.toLowerCase();

		// Add monster to the room if its ArrayList exists in the HashMap
		if (this.monsters.containsKey(monsterName))
		{
			ArrayList<Monster> monsterList = this.monsters.get(monsterName);
			monsterList.add(monster);
			
			// Set the monster's current room to this room
			monster.setCurrentRoom(this);
			
			return;
		}
		
		// If the ArrayList for the monster name doesn't exist, create it and add the monster to it
		else
		{
			ArrayList<Monster> monsterList = new ArrayList<>();
			monsterList.add(monster);
			this.monsters.put(monsterName, monsterList);
			
			// Set the monster's current room to this room
			monster.setCurrentRoom(this);
		}
	}
	
	public void removeMonster(String monsterName, Monster monster)
	{
		monsterName = monsterName.toLowerCase();
		
		// Check if the monster's name is recorded
		if (this.monsters.containsKey(monsterName))
		{
			ArrayList<Monster> monsterList = this.monsters.get(monsterName);
			monsterList.remove(monster);
			
			// Set the monsters's current room to null
			monster.removeFromCurrentRoom();
			
			// Remove the monster's name from the HashMap if there are no more monsters of that name
			if (monsterList.size() <= 0)
				this.monsters.remove(monsterName);
		}
	}
	
	public void addSpawnableMonsterId(int monsterId)
	{
		/* Adds the monsterId to the list of spawned monster IDs if it doesn't already exist in the list */
		if (!this.spawnableMonsterIds.contains(monsterId))
			this.spawnableMonsterIds.add(monsterId);
	}
	
	public void generateMonsters()
	{
		// Guard Clauses // 
		// If there are no spawnable monster IDs, return
		if (this.spawnableMonsterIds.size() <= 0)
			return;
		
		// Generate a random number of monsters to spawn (between 1 and the number of spawnable monster IDs)
		Random rng = new Random();
		int numMonstersToSpawn = rng.nextInt(1, this.spawnableMonsterIds.size() + 1);
		
		// Spawn the monsters
		for (int i = 0; i < numMonstersToSpawn; i++)
		{
			// Don't generate any more monsters if the room already has enough monsters
			if (this.monsters.size() >= MAX_MONSTERS_PER_ROOM)
				break;

			// Randomly select a monster ID from the list of spawnable monster IDs
			int randomIndex = rng.nextInt(this.spawnableMonsterIds.size());
			int monsterId = this.spawnableMonsterIds.get(randomIndex);
			
			// Create a new Monster object using the MonsterFactory
			Monster newMonster = this.monsterFactory.create(monsterId);
			
			// Add the new monster to the room
			if (newMonster != null)
				this.addMonster(newMonster.getName(), newMonster);
		}
	}
	
	// Overrides //
	
	/*
	@Override
	public String toString()
	{
		String a = "Room ID: " + this.roomId + "\n";
		String b = "Room Name: " + this.roomName + "\n";
		String c = "Room Desc: " + this.roomDesc + "\n";
		String d = "Has been visited: " + this.wasVisited + "\n\n";
		String e = "Room Exits: {";
		
		// Print exits
		try
		{
			for (HashMap.Entry<String, Integer> exit: this.exits.entrySet())
			{
				String direction = exit.getKey();
				String roomId = String.valueOf(exit.getValue());
				
				e += direction + " = " + roomId + ", ";
			}
		}
		catch (NullPointerException ex)
		{
			// Leave blank
		}
		catch (ConcurrentModificationException ex)
		{
			// Leave blank
		}
		finally
		{
			e += "}\n";
		}

		return a + b + c + d + e;
	}
	*/
}

