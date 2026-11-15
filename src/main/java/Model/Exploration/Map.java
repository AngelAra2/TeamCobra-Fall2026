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
	private static final String roomsFilePath = "/gameObjects/Rooms";
	private static final String puzzlesFilePath = "/gameObjects/Puzzles";
	private static final String monstersFilePath = "/gameObjects/Monsters";
    private static final String companionsFilePath = "/gameObjects/Companions";
	private static final String merchantsFilePath = "/gameObjects/Merchants";
    private static final String weaponsFilePath = "/gameObjects/WeaponItems";
    private static final String armorFilePath = "/gameObjects/ArmorItems";
    private static final String consumablesFilePath = "/gameObjects/ConsumeableItems";
    private static final String utilityFilePath = "/gameObjects/UtilityItems";
    private static final String rewardsFilePath = "/gameObjects/Rewards";

	private HashMap<Integer, Room> rooms;
    private HashMap<Integer, Item> itemTemplates;

	/* Constructors */
    public Map()
    {
        this.rooms = new HashMap<>();
        this.itemTemplates = new HashMap<>();


        // The below method calls use "method referencing" to pass the data extraction methods
        // as a parameter to the extractDataToMap method

        /* Populate world map with Rooms */
        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMapData, roomsFilePath, this);

	    /* Populate Rooms with game objects */
	        DataFileExtraction.extractDataToMap(
	    DataExtractionMethods::extractWeaponData, weaponsFilePath, this);
	
	        DataFileExtraction.extractDataToMap(
	    DataExtractionMethods::extractArmorData, armorFilePath, this);
	
	        DataFileExtraction.extractDataToMap(
	    DataExtractionMethods::extractConsumableData, consumablesFilePath, this);
	
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractUtilityData, utilityFilePath, this);
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractPuzzleData, puzzlesFilePath, this); // Puzzles
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMonsterData, monstersFilePath, this); // Monsters
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractCompanionData, companionsFilePath, this); // Companions
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractMerchantData, merchantsFilePath, this); // Merchants
	        DataFileExtraction.extractDataToMap(DataExtractionMethods::extractPuzzleRewardsData, rewardsFilePath, this); // Rewards

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
    public void addItemTemplate(Item item) {
        this.itemTemplates.put(item.getItemId(), item);
    }

    public Item getItemTemplate(int itemId) {
        return this.itemTemplates.get(itemId);
    }

    public HashMap<Integer, Item> getItemTemplates() {
        return this.itemTemplates;
    }
}

