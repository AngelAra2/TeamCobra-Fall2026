package Model.Exploration;
import java.util.*;

import Model.Data.DataFileExtraction;
import Model.Data.DataExtractionMethods;
import Model.AbstractClasses.Item;
import Model.Items.Consumable;
import Model.Items.Weapon;

public class Map
{
	/* Data Fields */
	private static String roomsFilePath = "/gameObjects/Rooms";
	private static String itemsFilePath = "/gameObjects/Items";
	private static String puzzlesFilePath = "/gameObjects/Puzzles";
	private static String monstersFilePath = "/gameObjects/Monsters";
    private static String companionsFilePath = "/gameObjects/Companions";
	private static String merchantsFilePath = "/gameObjects/Merchants";
	private HashMap<Integer, Room> rooms;

	/* Constructors */
	public Map()
	{
		this.rooms = new HashMap<>();

		// The below method calls use "method referencing" to pass the data extraction methods 
		// as a parameter to the extractDataToMap method
		
		/* Populate world map with Rooms */
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMapData, roomsFilePath, this);

		/* Populate Rooms with game objects */
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractItemData, itemsFilePath, this); // Items
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractPuzzleData, puzzlesFilePath, this); // Puzzles	
		DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMonsterData, monstersFilePath, this); // Monsters
        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractCompanionData, companionsFilePath, this); // Companions
        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMerchantData, merchantsFilePath, this); // Merchants
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

