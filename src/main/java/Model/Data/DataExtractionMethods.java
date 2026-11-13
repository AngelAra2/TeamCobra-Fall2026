package Model.Data;

import java.util.*;

import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;
//import Model.Combat.Monster;
import Model.Combat.StartingClass;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import Model.Items.Consumable;
import Model.Items.Weapon;

public class DataExtractionMethods 
{

	/* Data Fields */
	public static final String DELIMITER_A = "_";
	public static final String DELIMITER_B = ",";
	public static final String DELIMITER_C = "*";
	
	//------------------------------------//
	
	/* Helper Methods */
	
	public static String[] trimStringArray(String[] arr)
	{
		/* Used to trim all strings in a string array */
		for (int i = 0; i < arr.length; i++)
			arr[i] = arr[i].trim();
		
		return arr;
	}
	
	//------------------------------------//
	
	/* Extract data from a file and parse it into Room objects */
	public static void extractMapData(Scanner fileScanner, Map worldMap)
	{
		HashMap<Integer, Room> rooms = worldMap.getRooms();
		
		// Read room data and create Room objects //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get Room data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));
			
			// Declare required attributes
			int roomId;
			String roomName = "Room";
			String roomDesc = "...";
			HashMap<String, Integer> exits = new HashMap<>(4);
			
			// Read roomId
			try
			{
				roomId = Integer.parseInt(attributes[0]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read roomName
			try
			{
				roomName = attributes[1];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read roomDesc
			try
			{
				roomDesc = attributes[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read exits //
			try
			{
				// Read exits as a single string and split it into an array of strings
				String[] roomExits = trimStringArray(attributes[attributes.length - 1].split(DELIMITER_B));
				
				// Store each exit as its own entry in the HashMap
				int exitRoomId;
				String direction;
				for (int i = 0; i < roomExits.length; i++)
				{
					// Read roomId this exit leads to		
					try
					{
						exitRoomId = Integer.parseInt(roomExits[i]);
					}
					catch (NumberFormatException e)
					{
						continue;
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						continue;
					}
					
					// Determine direction of this exit
					switch (i)
					{
						case 1:
							direction = "east";
							break;
						case 2:
							direction = "south";
							break;
						case 3:
							direction = "west";
							break;
						default:
							direction = "north";
							break;
					}
					
					// Enter data
					exits.put(direction, exitRoomId);
				}
			}			
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			/* Create Room object */
			Room room = new Room(roomId, roomName, roomDesc, exits);
			
			// Place Room object into the Map's HashMap of rooms
			rooms.put(roomId, room);
		}
	}
	
	/* Extract data from a file and parse it into Item objects */
	public static void extractItemData(Scanner fileScanner, Map worldMap)
	{
		HashMap<Integer, Room> rooms = worldMap.getRooms();
		
		// Read puzzle data and create Puzzle objects //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get Puzzle data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));

			// Declare required attributes
			int itemId;
			String itemType = "...";
			String itemName = "Item";
			String itemDesc = "...";

			// Read itemId
			try
			{
				itemId = Integer.parseInt(attributes[0]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read itemType
			try
			{
				itemType = attributes[1];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read itemName
			try
			{
				itemName = attributes[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read itemDesc
			try
			{
				itemDesc = attributes[3];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// There may be more than one of the same item in the game
			// Depending on the number of room IDs listed in the file, a new Item object will be created for each room ID
			try
			{
				// Read room IDs as a single string and split it into an array of strings
				String[] targetRoomIds = trimStringArray(attributes[attributes.length - 1].split(DELIMITER_B));
				
				// Create a new Item object for each room ID listed
				int roomId;
				for (int i = 0; i < targetRoomIds.length; i++)
				{
					// Read roomId this item is located in		
					try
					{
						roomId = Integer.parseInt(targetRoomIds[i]);
					}
					catch (NumberFormatException e)
					{
						continue;
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						continue;
					}
					
					/* Create Item object */
					
					// Check item type and create the appropriate subclass object
					Item item;
					switch (itemType.toLowerCase())
					{
						case "weapon":
							int baseAttack = 0;
							try
							{
								baseAttack = Integer.parseInt(attributes[4]);
							}
							catch (NumberFormatException e)
							{
								// Leave default value
							}
							catch (ArrayIndexOutOfBoundsException e)
							{
								// Leave default value
							}
							
							item = new Weapon(itemId, itemName, itemDesc, baseAttack);
							break;
							
						case "consumable":
							int healingAmount = 0;
							try
							{
								healingAmount = Integer.parseInt(attributes[4]);
							}
							catch (NumberFormatException e)
							{
								// Leave default value
							}
							catch (ArrayIndexOutOfBoundsException e)
							{
								// Leave default value
							}
							
							item = new Consumable(itemId, itemName, itemDesc, healingAmount);
							break;
							
						default:
							continue; // Skip this item if the type is unrecognized
					}
					
					// Add item to the room with the matching room ID, if it exists
					if (rooms.containsKey(roomId))
						rooms.get(roomId).addItem(itemName.toLowerCase(), item);
				}
			}			
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
		}
	}
	
	/* Extract data from a file and parse it into Puzzle objects */
	public static void extractPuzzleData(Scanner fileScanner, Map worldMap)
	{
		HashMap<Integer, Room> rooms = worldMap.getRooms();
		
		// Read puzzle data and create Puzzle objects //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get Puzzle data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));
			
			// Declare required attributes
			int puzzleId;
			int targetRoomId;
			String puzzleName = "Puzzle";
			String puzzleDesc = "...";
			String puzzleSolution = "Answer";
			int attempts = 3;
			
			// Read puzzleId
			try
			{
				puzzleId = Integer.parseInt(attributes[0]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read targetRoomId
			try
			{
				targetRoomId = Integer.parseInt(attributes[1]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read puzzleName
			try
			{
				puzzleName = attributes[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read puzzleDesc
			try
			{
				puzzleDesc = attributes[3];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read puzzleSolution
			try
			{
				puzzleSolution = attributes[4];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read attempts
			try
			{
				attempts = Integer.parseInt(attributes[5]);
			}
			catch (NumberFormatException e)
			{
				// Leave default value
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave default value
			}
			
			/* Create Puzzle object */
			Puzzle puzzle = new Puzzle(puzzleId, puzzleName, puzzleDesc, puzzleSolution, attempts);
			
			// Attach the puzzle to the room with the matching room ID, if it exists
			if (rooms.containsKey(targetRoomId))
				rooms.get(targetRoomId).setPuzzle(puzzle);
		}
	}
	
	/* Extract data from a file and parse it into monster data */
	public static void extractMonsterData(Scanner fileScanner, Map worldMap)
	{
		// Read monster data and create Monster objects //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get Monster data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));
			
			// Declare required attributes
			int monsterId;
			boolean isBoss = false;
			String[] targetRoomIds;

			// Read monsterId
			try { monsterId = Integer.parseInt(attributes[0]); }
			catch (NumberFormatException e) { continue; }
			catch (ArrayIndexOutOfBoundsException e) { continue; }
			
			// Read isBoss
			try { isBoss = Boolean.parseBoolean(attributes[10]); }
			catch (NumberFormatException e) { continue; }
			catch (ArrayIndexOutOfBoundsException e) { continue; }
			
			// Read targetRoomIds
			try 
			{
				// Read room IDs as a single string and split it into an array of strings
				targetRoomIds = trimStringArray(attributes[10].split(DELIMITER_B));

				for (int i = 0; i < targetRoomIds.length; i++)
				{
					int roomId;
					try 
					{ 
						roomId = Integer.parseInt(targetRoomIds[i]);
	
						// Find the Room object with the matching room ID and add the monster to it
						Room targetRoom = worldMap.getRoomById(roomId);
						if (targetRoom != null)
							targetRoom.addSpawnableMonsterId(monsterId);
					}
					catch (NumberFormatException e) { System.out.println("NumberFormatException"); continue; }
					catch (ArrayIndexOutOfBoundsException e) { System.out.println("ArrayIndexOutOfBoundsException"); continue; }
				}
			}
			catch (ArrayIndexOutOfBoundsException e) { continue; }
		}
	}

	/* Extract data from a file and parse it into a finite state machine for a Combatant object */
	public static void extractFiniteStatesData(Scanner fileScanner, Combatant combatant)
	{
		// Read states data //
		if (fileScanner.hasNextLine())
		{
			// TEST CODE
			//System.out.println("Extracting states data for " + combatant.getName());
			
			/* Read current line to get states data */
			String currentLine = fileScanner.nextLine().trim();
	
			// Use delimiter to split string into readable states
			String[] states = trimStringArray(currentLine.split(DELIMITER_A));
			
			// Trim all string attributes to remove whitespace
			for (int i = 0; i < states.length; i++)
				states[i] = states[i].trim();
			
			// Initialize the combatant's state machine with the extracted states
			combatant.initStateMachine(states);
		}
		
		else
			// No states data found, exit method
			return;
	}

	/*
	Angel 11.9.26
	Extracting starting classes from text file
	 */
	public static void extractStartingClassData(Scanner fileScanner, ArrayList<StartingClass> classes){


		while(fileScanner.hasNextLine()){

			String line = fileScanner.nextLine();
			//splits line string using " _ " as the delimiter.
			String parsedLine[] = line.split(DELIMITER_A);

			// Declare required attributes
			int id = 0;
			String className = "";
			String background = "";
			int baseMaxHP = 0;
			int baseSTR = 0;
			int baseDEX = 0;
			int baseINT = 0;
			int baseARC = 0;
			int baseEND = 0;
			ArrayList<String> combatAbilities = new ArrayList<>();
			ArrayList<String> defaultEquipment = new ArrayList<>();

			//assigns starting class ID
			try{
				id = Integer.parseInt(parsedLine[0]);
			}
			catch(NumberFormatException e){
				continue;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			//assigns starting class name
			try {
				className = parsedLine[1];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
					// Leave blank
			}

			//assigns starting class background description
			try{
				background = parsedLine[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class base max HP
			try{
				baseMaxHP = Integer.parseInt(parsedLine[3]);
			} catch (NumberFormatException e) {
                continue;
            }
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class base STR
			try{
				baseSTR = Integer.parseInt(parsedLine[4]);
			}
			catch (NumberFormatException e)
			{
                continue;
            }
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			//assigns starting class base DEX
			try{
				baseDEX = Integer.parseInt(parsedLine[5]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class base INT
			try{
				baseINT = Integer.parseInt(parsedLine[6]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class base ARC
			try{
				baseARC = Integer.parseInt(parsedLine[7]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			try{
				baseEND = Integer.parseInt(parsedLine[8]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class combat abilities
			//starting class could have more than one ability
			try{
				//split combat abilities using " , " as the delimiter
				String[] combatAbilitiesList = trimStringArray(parsedLine[9].split(DELIMITER_B));

				//adds combat ability to list
				for(String abilities : combatAbilitiesList){
					combatAbilities.add(abilities);
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class equipment
			//starting class will have more than one equipment
			try{
				//split combat abilities using " , " as the delimiter
				String[] targetItemIds = trimStringArray(parsedLine[10].split(DELIMITER_B));

				//adds equipment name to list
				for(int i = 0; i <targetItemIds.length; i++){
					defaultEquipment.add(targetItemIds[i]);
				}
				//create a StartingClass object, and add it into list of StartingClasses
				StartingClass tempClass = new StartingClass(
							id,
							className,
							background,
							baseMaxHP,
							baseSTR,
							baseDEX,
							baseINT,
							baseARC,
							baseEND,
							combatAbilities,
						    defaultEquipment
				);

				classes.add(tempClass);

			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
		}

	}
	
	public static String[] extractPlayerConstants(Scanner fileScanner)
	{
		String[] constantsData = null;
		
		// Read player constants data //
		if (fileScanner.hasNextLine())
		{
			/* Read current line to get player constants data */
			String currentLine = fileScanner.nextLine().trim();
			constantsData = trimStringArray(currentLine.split(DELIMITER_A));
		}
		
		return constantsData;
	}
	
	/* Extract data from a file and return the string of data that matches the given object ID */
	public static String extractObjectData(Scanner fileScanner, int objectId)
	{
		// Read data and find the line that matches the given object ID //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get object data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));
			
			// Read objectId
			int currentObjectId;
			try { currentObjectId = Integer.parseInt(attributes[0]); }
			catch (NumberFormatException e) { continue; }
			catch (ArrayIndexOutOfBoundsException e) { continue; }
			
			// Return the matching line of data
			if (currentObjectId == objectId)
				return currentLine;
		}
		
		return ""; // No matching line found
	}
	
}

