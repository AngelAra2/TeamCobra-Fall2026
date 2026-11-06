package Model.Data;

import java.util.*;

import Model.Item;
import Model.Map;
import Model.Puzzle;
import Model.Room;
import Model.Combat.Monster;

public class DataExtractionMethods {

	/* Data Fields */
	private static String delimiterA = "_";
	private static String delimiterB = ",";
	
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
			String[] attributes = currentLine.split(delimiterA);
			
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
				String[] roomExits = attributes[3].split(delimiterB);
				
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
			String[] attributes = currentLine.split(delimiterA);
			
			// Declare required attributes
			int itemId;
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
			
			// Read itemName
			try
			{
				itemName = attributes[1];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read itemDesc
			try
			{
				itemDesc = attributes[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// There may be more than one of the same item in the game
			// Depending on the number of room IDs listed in the file, a new Item object will be created for each room ID
			try
			{
				String[] targetRoomIds = attributes[attributes.length - 1].split(delimiterB);
				
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
					Item item = new Item(itemId, itemName, itemDesc);
					
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
			String[] attributes = currentLine.split(delimiterA);
			
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
	
	/* Extract data from a file and parse it into Monster objects */
	public static void extractMonsterData(Scanner fileScanner, Map worldMap)
	{
		HashMap<Integer, Room> rooms = worldMap.getRooms();
		
		// Read monster data and create Monster objects //
		while (fileScanner.hasNextLine())
		{
			/* Read current line to get Monster data */
			String currentLine = fileScanner.nextLine().trim();

			// Use delimiter to split string into readable attributes
			String[] attributes = currentLine.split(delimiterA);
			
			// Trim all string attributes to remove whitespace
			for (int i = 0; i < attributes.length; i++)
				attributes[i] = attributes[i].trim();
			
			// Declare required attributes
			int monsterId;
			String monsterName = "Monster";
			String monsterDesc = "...";
			int maxHP = 10;
			int physicalDEF = 0;
			int magicDEF = 0;
			int xpReward = 1;
			int goldReward = 1;
			boolean isBoss = false;
			
			// Read monsterId
			try
			{
				monsterId = Integer.parseInt(attributes[0]);
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
			
			// Read monsterName
			try
			{
				monsterName = attributes[1];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read monsterDesc
			try
			{
				monsterDesc = attributes[2];
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read maxHP
			try
			{
				maxHP = Integer.parseInt(attributes[3]);
			}
			catch (NumberFormatException e)
			{
				// Leave blank
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read physicalDEF
			try
			{
				physicalDEF = Integer.parseInt(attributes[4]);
			}
			catch (NumberFormatException e)
			{
				// Leave blank
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read magicDEF
			try
			{
				magicDEF = Integer.parseInt(attributes[5]);
			}
			catch (NumberFormatException e)
			{
				// Leave blank
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read xpReward
			try
			{
				xpReward = Integer.parseInt(attributes[6]);
			}
			catch (NumberFormatException e)
			{
				// Leave blank
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read goldReward
			try
			{
				goldReward = Integer.parseInt(attributes[7]);
			}
			catch (NumberFormatException e)
			{
				// Leave blank
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// Read isBoss
			try
			{
				isBoss = Boolean.parseBoolean(attributes[8]);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}
			
			// There may be more than one of the same monster in the game
			// Depending on the number of room IDs listed in the file, a new Monster object will be created for each room ID
			try
			{
				String[] targetRoomIds = attributes[attributes.length - 1].split(delimiterB);
				
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
					
					// Guard clause
					// If the room ID does not exist in the world map, skip creating this object
					if (!rooms.containsKey(roomId))
						continue;
						
					/* Create Monster object */
					Monster monster = new Monster(
							monsterName, 
							maxHP, 
							physicalDEF, 
							magicDEF,
							
							monsterId,
							monsterDesc,
							xpReward,
							goldReward,
							isBoss
						);
					
					// Add monster to the room with the matching room ID, if it exists
					rooms.get(roomId).addMonster(monsterName.toLowerCase(), monster);
				}
			}			
			catch (ArrayIndexOutOfBoundsException e)
			{
				continue;
			}
		}
	}
	
	public static void extractFiniteStatesData(Scanner fileScanner)
	{
		
	}
}

