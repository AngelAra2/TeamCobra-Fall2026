package Model;
import java.io.*;
import java.util.*;

public class Map 
{
	/* Data Fields */
	private static String delimiterA = "_";
	private static String delimiterB = ",";
	
	private HashMap<Integer, Room> rooms;

	/* Constructors */
	public Map(
			String roomsFileName, 
			String itemsFileName,
			String puzzlesFileName
		)
	{
		this.rooms = new HashMap<>();
		
		/* Construct base directory path for data files */
		File projectDir = new File(System.getProperty("user.dir"));
			File srcDir = new File(projectDir, "src");
				File resourcesDir = new File(srcDir, "resources");
		
		/* Get map data from Rooms file */
		File roomsFile = new File(resourcesDir, roomsFileName);
		
		// Try opening this file
		try
		{
			Scanner fileScanner = new Scanner(roomsFile);
			
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
				rooms.put(roomId, room);
			}
			
			// Clean up
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		/* Populate rooms with items and puzzles */
		
		// Items //
		File itemsFile = new File(resourcesDir, itemsFileName);
		
		// Try opening this file
		try
		{
			Scanner fileScanner = new Scanner(itemsFile);
			
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
				
				// There more be more than one of the same item in the game
				// Depending on the number of room IDs listed in the file, a new Item object will be created for each room ID
				try
				{
					String[] targetRoomIds = attributes[3].split(delimiterB);
					
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
						if (this.rooms.containsKey(roomId))
							this.rooms.get(roomId).addItem(itemName.toLowerCase(), item);
					}
				}			
				catch (ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
			}
			
			// Clean up
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		// Puzzles //
		File puzzlesFile = new File(resourcesDir, puzzlesFileName);
		
		// Try opening this file
		try
		{
			Scanner fileScanner = new Scanner(puzzlesFile);
			
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
				if (this.rooms.containsKey(targetRoomId))
					this.rooms.get(targetRoomId).setPuzzle(puzzle);
			}
			
			// Clean up
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/* Methods */
	
	// Getters //
	public HashMap<Integer, Room> getRooms()
	{
		return this.rooms;
	}
	
	public Room getRoomById(int roomId)
	{
		// Check if the room ID is recorded
		if (this.rooms.containsKey(roomId))
			return this.rooms.get(roomId);
		
		return null;
	}
}

