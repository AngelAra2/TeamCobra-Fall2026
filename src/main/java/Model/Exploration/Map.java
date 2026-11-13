package Model.Exploration;
import java.util.*;

import Model.Data.DataFileExtraction;
import Model.Data.DataExtractionMethods;

public class Map
{
	/* Data Fields */
	private static final String ROOMS_FILE_PATH = "/gameObjects/Rooms";
	private static final String ITEMS_FILE_PATH = "/gameObjects/Items";
	private static final String PUZZLES_FILE_PATH = "/gameObjects/Puzzles";
	private static final String MONSTERS_FILE_PATH = "/gameObjects/Monsters";
	
	private HashMap<Integer, Room> rooms;

	/* Constructors */
	public Map()
	{
		this.rooms = new HashMap<>();

		// The below method calls use "method referencing" to pass the data extraction methods 
		// as a parameter to the extractDataToMap method
		
		/* Populate world map with Rooms */
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMapData, ROOMS_FILE_PATH, this);

		/* Populate Rooms with game objects */
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractItemData, ITEMS_FILE_PATH, this); // Items
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractPuzzleData, PUZZLES_FILE_PATH, this); // Puzzles	
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMonsterData, MONSTERS_FILE_PATH, this); // Monsters
	}
	
	/* Methods */
	
	// Getters //
	public HashMap<Integer, Room> getRooms() { return this.rooms; }
	
	public Room getRoomById(int roomId)
	{
		// Check if the room ID is recorded
		if (this.rooms.containsKey(roomId))
			return this.rooms.get(roomId);
		
		return null;
	}
}

