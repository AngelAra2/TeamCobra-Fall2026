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
	//private static String itemsFilePath = "/gameObjects/Items";
	private static String puzzlesFilePath = "/gameObjects/Puzzles";
	private static String monstersFilePath = "/gameObjects/Monsters";
    private static String companionsFilePath = "/gameObjects/Companions";
	private static String merchantsFilePath = "/gameObjects/Merchants";
    private static String weaponsFilePath = "/gameObjects/WeaponItems";
    private static String armorFilePath = "/gameObjects/ArmorItems";
    private static String consumablesFilePath = "/gameObjects/ConsumeableItems";
    private static String utilityFilePath = "/gameObjects/UtilityItems";
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
		//DataFileExtraction.extractDataToMap(DataExtractionMethods::extractItemData, itemsFilePath, this); // Items
        DataFileExtraction.extractDataToMap(
                DataExtractionMethods::extractWeaponData, weaponsFilePath, this);

        DataFileExtraction.extractDataToMap(
                DataExtractionMethods::extractArmorData, armorFilePath, this);

        DataFileExtraction.extractDataToMap(
                DataExtractionMethods::extractConsumableData, consumablesFilePath, this);

        DataFileExtraction.extractDataToMap(
                DataExtractionMethods::extractUtilityData, utilityFilePath, this);
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




