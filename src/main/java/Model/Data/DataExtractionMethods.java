package Model.Data;

import java.util.*;

import Model.Combat.CombatSkill;
import Model.Combat.Companion;
import Model.Exploration.Merchant;
import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;
import Model.Combat.Monster;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import Model.Factories.CombatSkillFactory;
import Model.Factories.MonsterFactory;
import Model.Items.Consumable;
import Model.Items.Weapon;

import Model.Items.Armor;
import Model.Items.UtilityItem;

import Model.Combat.StartingClass;
import Model.Util.ItemUtil;
import Model.Rewards.LoreReward;
import Model.Rewards.RoomReward;
import Model.Rewards.ItemReward;
public class DataExtractionMethods {

    /* Data Fields */
	public static final String DELIMITER_A = "_";
	public static final String DELIMITER_B = ",";
	public static final String DELIMITER_C = "*";

    //------------------------------------//

    /* Helper Methods */

    public static String[] trimStringArray(String[] arr) {
        /* Used to trim all strings in a string array */
        for (int i = 0; i < arr.length; i++)
            arr[i] = arr[i].trim();

        return arr;
    }

    //------------------------------------//

    /* Extract data from a file and parse it into Room objects */
    public static void extractMapData(Scanner fileScanner, Map worldMap) {
        HashMap<Integer, Room> rooms = worldMap.getRooms();

        // Read room data and create Room objects //
        while (fileScanner.hasNextLine()) {
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
            try {
                roomId = Integer.parseInt(attributes[0]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read roomName
            try {
                roomName = attributes[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read roomDesc
            try {
                roomDesc = attributes[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read exits //
            try {
                // Read exits as a single string and split it into an array of strings
                String[] roomExits = trimStringArray(attributes[attributes.length - 1].split(DELIMITER_B));

                // Store each exit as its own entry in the HashMap
                int exitRoomId;
                String direction;
                for (int i = 0; i < roomExits.length; i++) {
                    // Read roomId this exit leads to
                    try {
                        exitRoomId = Integer.parseInt(roomExits[i]);
                    } catch (NumberFormatException e) {
                        continue;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }

                    // Determine direction of this exit
                    switch (i) {
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
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            /* Create Room object */
            Room room = new Room(roomId, roomName, roomDesc, exits);

            // Place Room object into the Map's HashMap of rooms
            rooms.put(roomId, room);
        }
    }

    /* Extract data from a file and parse it into Item objects */
//    public static void extractItemData(Scanner fileScanner, Map worldMap) {
//        HashMap<Integer, Room> rooms = worldMap.getRooms();
//
//        // Read puzzle data and create Puzzle objects //
//        while (fileScanner.hasNextLine()) {
//            /* Read current line to get Puzzle data */
//            String currentLine = fileScanner.nextLine().trim();
//
//            // Use delimiter to split string into readable attributes
//            String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));
//
//            // Declare required attributes
//            int itemId;
//            String itemType = "...";
//            String itemName = "Item";
//            String itemDesc = "...";
//
//
//            // Read itemId
//            try {
//                itemId = Integer.parseInt(attributes[0]);
//            } catch (NumberFormatException e) {
//                continue;
//            } catch (ArrayIndexOutOfBoundsException e) {
//                continue;
//            }
//
//            // Read itemType
//            try {
//                itemType = attributes[1];
//            } catch (ArrayIndexOutOfBoundsException e) {
//                continue;
//            }
//
//            // Read itemName
//            try {
//                itemName = attributes[2];
//            } catch (ArrayIndexOutOfBoundsException e) {
//                // Leave blank
//            }
//
//            // Read itemDesc
//            try {
//                itemDesc = attributes[3];
//            } catch (ArrayIndexOutOfBoundsException e) {
//                // Leave blank
//            }
//
//            // There may be more than one of the same item in the game
//            // Depending on the number of room IDs listed in the file, a new Item object will be created for each room ID
//            try {
//                // Read room IDs as a single string and split it into an array of strings
//                String[] targetRoomIds = trimStringArray(attributes[attributes.length - 1].split(DELIMITER_B));
//
//                // Create a new Item object for each room ID listed
//                int roomId;
//                for (int i = 0; i < targetRoomIds.length; i++) {
//                    // Read roomId this item is located in
//                    try {
//                        roomId = Integer.parseInt(targetRoomIds[i]);
//                    } catch (NumberFormatException e) {
//                        continue;
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        continue;
//                    }
//
//                    /* Create Item object */
//
//                    // Check item type and create the appropriate subclass object
//                    Item item;
//                    switch (itemType.toLowerCase()) {
//                        case "weapon":
//                            //int baseAttack;
//                            double weight = Double.parseDouble(attributes[4]);
//                            int value = Integer.parseInt(attributes[5]);
//                            String slot = attributes[6];
//                            int physicalDamage = Integer.parseInt(attributes[7]);
//                            int magicDamage = Integer.parseInt(attributes[8]);
//                            int physicalDefense = Integer.parseInt(attributes[9]);
//                            int magicDefense = Integer.parseInt(attributes[10]);
//                            String strengthScaling = attributes[11];
//                            String dexterityScaling = attributes[12];
//                            String intelligenceScaling = attributes[13];
//                            String arcaneScaling = attributes[14];
//                            String specialEffect = attributes[15];
//
////                            try {
////                                baseAttack = Integer.parseInt(attributes[4]);
////                            } catch (NumberFormatException e) {
////                                // Leave default value
////                            } catch (ArrayIndexOutOfBoundsException e) {
////                                // Leave default value
////                            }
//
////                            item = new Weapon(itemId, itemName, itemDesc, baseAttack);
////                            break;
//                            item = new Weapon(itemId, itemName, itemDesc, weight, value, slot, physicalDamage,
//                            magicDamage, physicalDefense, magicDefense, strengthScaling, dexterityScaling,
//                                intelligenceScaling, arcaneScaling, specialEffect);
//                            break;
//
//                        case "consumable":
//                            //int healingAmount = 0;
////                            try {
////                                healingAmount = Integer.parseInt(attributes[4]);
////                            } catch (NumberFormatException e) {
////                                // Leave default value
////                            } catch (ArrayIndexOutOfBoundsException e) {
////                                // Leave default value
////                            }
////                            double weight = Double.parseDouble(attributes[4]);
////                            int value = Integer.parseInt(attributes[5]);
//                            weight = Double.parseDouble(attributes[4]);
//                            value = Integer.parseInt(attributes[5]);
//                            String effectType = attributes[6];
//                            int effectAmount = Integer.parseInt(attributes[7]);
//                            int effectDuration = Integer.parseInt(attributes[8]);
//
//                            item = new Consumable(itemId, itemName, itemDesc,
//                                    weight, value, effectType, effectAmount, effectDuration);
//                            break;
////                            item = new Consumable(itemId, itemName, itemDesc, healingAmount);
////                            break;
//
//                        default:
//                            continue; // Skip this item if the type is unrecognized
//                    }
//
//                    // Add item to the room with the matching room ID, if it exists
//                    if (rooms.containsKey(roomId))
//                        rooms.get(roomId).addItem(itemName.toLowerCase(), item);
//                }
//            } catch (ArrayIndexOutOfBoundsException e) {
//                continue;
//            }
//        }
//    }


// Loads weapon definitions.
public static void extractWeaponData(Scanner fileScanner, Map worldMap) {
    while (fileScanner.hasNextLine()) {
        String line = fileScanner.nextLine().trim();
        if (line.isEmpty()) continue;

        String[] a = trimStringArray(line.split(DELIMITER_A, -1));

        try {
            Weapon item = new Weapon(
                    Integer.parseInt(a[0]), a[1], a[2],
                    Double.parseDouble(a[3]), Integer.parseInt(a[4]),
                    a[5], Integer.parseInt(a[6]), Integer.parseInt(a[7]),
                    Integer.parseInt(a[8]), Integer.parseInt(a[9]),
                    a[10], a[11], a[12], a[13], a[14],a[16]
            );

            worldMap.addItemTemplate(item);
        } catch (NumberFormatException e) {
            continue;
        } catch (ArrayIndexOutOfBoundsException e) {
            continue;
        }
    }
}

    // Loads armor and shield definitions.
    public static void extractArmorData(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] a = trimStringArray(line.split(DELIMITER_A, -1));

            try {
                Armor item = new Armor(
                        Integer.parseInt(a[0]), a[1], a[2],
                        Double.parseDouble(a[3]), Integer.parseInt(a[4]),
                        a[5], Integer.parseInt(a[6]), Integer.parseInt(a[7]),
                        Integer.parseInt(a[8]), Integer.parseInt(a[9]),
                        a[10], a[11], a[12], a[13], a[14]
                );

                worldMap.addItemTemplate(item);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
    }

    // Loads consumable definitions.
    public static void extractConsumableData(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] a = trimStringArray(line.split(DELIMITER_A, -1));

            try {
                Consumable item = new Consumable(
                        Integer.parseInt(a[0]), a[1], a[2],
                        Double.parseDouble(a[3]), Integer.parseInt(a[4]),
                        a[5], Integer.parseInt(a[6]), Integer.parseInt(a[7])
                );

                worldMap.addItemTemplate(item);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
    }

    // Loads utility item definitions.
    public static void extractUtilityData(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] a = trimStringArray(line.split(DELIMITER_A, -1));

            try {
                UtilityItem item = new UtilityItem(
                        Integer.parseInt(a[0]), a[1], a[2],
                        Double.parseDouble(a[3]),
                        Integer.parseInt(a[4]), a[5], a[6],
                        a[7].equalsIgnoreCase("YES"));
                worldMap.addItemTemplate(item);

            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
    }

    /* Loads permanent and spawnable item IDs for each room. */
    public static void extractRoomItems(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String currentLine = fileScanner.nextLine().trim();

            if (currentLine.isEmpty()) {
                continue;
            }

            String[] attributes = trimStringArray(currentLine.split(DELIMITER_A, -1));
            if (attributes.length < 3){
                continue;
            }

            int roomId;
            try {
                roomId = Integer.parseInt(attributes[0]);
            }
            catch (NumberFormatException e) {
                continue;
            }

            Room room = worldMap.getRoomById(roomId);

            if (room == null) {
                continue;
            }

            // Load guaranteed items into the room.
            if (!attributes[1].equalsIgnoreCase("NONE")) {
                String[] permanentItemIds = trimStringArray(attributes[1].split(DELIMITER_B));

                for (int i = 0; i < permanentItemIds.length; i++) {
                    try {
                        int itemId = Integer.parseInt(permanentItemIds[i]);
                        Item template = worldMap.getItemTemplate(itemId);

                        if (template != null) {
                            Item roomItem = ItemUtil.copyItem(template);

                            if (roomItem != null) {
                                room.addItem(roomItem.getItemName(), roomItem);
                            }
                        }
                    }

                    catch (NumberFormatException e) { // Skip invalid item IDs.
                    }
                }
            }

            // Save IDs that can be used by the random item spawning system.
            if (!attributes[2].equalsIgnoreCase("NONE")) {
                String[] spawnableItemIds = trimStringArray(attributes[2].split(DELIMITER_B));

                for (int i = 0; i < spawnableItemIds.length; i++) {
                    try {
                        int itemId = Integer.parseInt(spawnableItemIds[i]);
                        room.addSpawnableItemId(itemId);
                    }
                    catch (NumberFormatException e) { // Skip invalid item IDs.
                    }
                }
            }
        }
    }

    /* Extract data from a file and parse it into Puzzle objects */
    public static void extractPuzzleData(Scanner fileScanner, Map worldMap) {
        HashMap<Integer, Room> rooms = worldMap.getRooms();

        // Read puzzle data and create Puzzle objects //
        while (fileScanner.hasNextLine()) {
            /* Read current line to get Puzzle data */
            String currentLine = fileScanner.nextLine().trim();

            // Use delimiter to split the line into puzzle attributes.
            String[] attributes = trimStringArray(currentLine.split(DELIMITER_A));

            // Declare required attributes and set defaults for optional values.
            int puzzleId;
            int targetRoomId;
            String puzzleName = "Puzzle";
            String puzzleDesc = "...";
            String puzzleSolution = "Answer";
            int attempts = 3;
            String hint1 = "";
            String hint2 = "";

            // Read puzzleId; skip this line if it is missing or invalid.
            try {
                puzzleId = Integer.parseInt(attributes[0]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read targetRoomId; skip this line if it is missing or invalid.
            try {
                targetRoomId = Integer.parseInt(attributes[1]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read puzzleName; keep the default if it is missing.
            try {
                puzzleName = attributes[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            // Read puzzleDesc; keep the default if it is missing.
            try {
                puzzleDesc = attributes[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            // Read puzzleSolution; keep the default if it is missing.
            try {
                puzzleSolution = attributes[4];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            // Read the configured attempt count; keep the default if missing or invalid.
            try {
                attempts = Integer.parseInt(attributes[5]);
            } catch (NumberFormatException e) {
                // Leave default value
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            // Read hint1; keep it blank if it is missing.
            try {
                hint1 = attributes[6];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            // Read hint2; keep it blank if it is missing.
            try {
                hint2 = attributes[7];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave default value
            }

            /* Create Puzzle object */
            Puzzle puzzle = new Puzzle(
                    puzzleId, puzzleName, puzzleDesc, puzzleSolution, attempts, hint1, hint2
            );

            // Attach the puzzle to its target room, if that room exists.
            if (rooms.containsKey(targetRoomId))
                rooms.get(targetRoomId).setPuzzle(puzzle);
        }
    }

    /* Extract data from a file and parse it into monster data */
	public static void extractMonsterData(Scanner fileScanner, Map worldMap)
	{
		// Have MonsterFactory variable set up just in case it's needed for creating Monster objects here
		MonsterFactory monsterFactory = new MonsterFactory();
		
		// Read monster data //
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
				targetRoomIds = trimStringArray(attributes[11].split(DELIMITER_B));

				for (int i = 0; i < targetRoomIds.length; i++)
				{
					int roomId;
					try 
					{ 
						roomId = Integer.parseInt(targetRoomIds[i]);
						Room targetRoom = worldMap.getRoomById(roomId);

						if (targetRoom != null)
						{
							// If the monster is a boss, create the Monster object and add it to the room with the matching room ID
							// If not, add the monsterId to the list of spawnable monster IDs for the room with the matching room ID
							
							if (!isBoss)
								// Find the Room object with the matching room ID and add the monster to it
								targetRoom.addSpawnableMonsterId(monsterId);

							// Create boss Monster object and add it to the room with the matching room ID
							else
							{
								Monster bossMonster = monsterFactory.create(monsterId);
								targetRoom.addMonster(bossMonster.getName(), bossMonster);
							}
						}
						
					}
					catch (NumberFormatException e) { System.out.println("NumberFormatException"); continue; }
					catch (ArrayIndexOutOfBoundsException e) { System.out.println("ArrayIndexOutOfBoundsException"); continue; }
				}
			}
			catch (ArrayIndexOutOfBoundsException e) { continue; }
		}
	}

    /* Extract reward data and attach rewards to Puzzle objects */
    public static void extractPuzzleRewardsData(Scanner fileScanner, Map worldMap) {
        // Track the line number so invalid reward entries can be identified.
        int lineNumber = 0;

        // Read each reward entry from the file.
        while (fileScanner.hasNextLine()) {
            lineNumber++;
            String currentLine = fileScanner.nextLine().trim();

            // Ignore blank lines and comment lines.
            if (currentLine.isEmpty())
                continue;

            // Split the entry into fields, preserving empty fields for validation.
            String[] attributes = trimStringArray(currentLine.split("\\|", -1));

            try {
                // Read the puzzle ID and reward type from the entry.
                int puzzleId = Integer.parseInt(attributes[0]);
                String rewardType = attributes[1].toUpperCase();

                // Find the puzzle with the matching ID.
                Puzzle puzzle = null;
                for (Room room : worldMap.getRooms().values()) {
                    if (room.getPuzzle() != null
                            && room.getPuzzle().getPuzzleId() == puzzleId) {
                        puzzle = room.getPuzzle();
                        break;
                    }
                }

                // Reject entries that refer to a puzzle that was not loaded.
                if (puzzle == null)
                    throw new IllegalArgumentException("Unknown puzzle ID: " + puzzleId);

                // Create the appropriate reward based on the entry type.
                switch (rewardType) {
                    case "LORE":
                        // LORE entries contain a puzzle ID, type, and lore text.
                        if (attributes.length != 3 || attributes[2].isBlank())
                            throw new IllegalArgumentException(
                                    "LORE format: puzzleId|LORE|lore text");

                        puzzle.addReward(new LoreReward(List.of(attributes[2])));
                        break;

                    case "ROOM":
                        // ROOM entries contain the source room, exit direction, and destination room.
                        if (attributes.length != 5)
                            throw new IllegalArgumentException(
                                    "ROOM format: puzzleId|ROOM|fromRoomId|direction|toRoomId");

                        int fromRoomId = Integer.parseInt(attributes[2]);
                        String direction = attributes[3].toLowerCase();
                        int toRoomId = Integer.parseInt(attributes[4]);

                        // Only accept the four supported exit directions.
                        if (!List.of("north", "east", "south", "west").contains(direction))
                            throw new IllegalArgumentException(
                                    "Direction must be north, east, south, or west");

                        // Look up both rooms that the reward connects.
                        Room fromRoom = worldMap.getRoomById(fromRoomId);
                        Room toRoom = worldMap.getRoomById(toRoomId);

                        // Reject room rewards that refer to rooms missing from the map.
                        if (fromRoom == null || toRoom == null)
                            throw new IllegalArgumentException(
                                    "Room reward refers to an unknown room");

                        // Attach this exit reward to the puzzle.
                        puzzle.addReward(new RoomReward(fromRoom, direction, toRoom));
                        break;
                    case "ITEM": {
                        // ITEM entries must contain a puzzle ID, the reward type, and an item ID.
                        if (attributes.length != 3)
                            throw new IllegalArgumentException(
                                    "ITEM format: puzzleId|ITEM|itemId");

                        // Parse the item ID and look up its shared template in the world map.
                        int itemId = Integer.parseInt(attributes[2]);
                        Item item = worldMap.getItemTemplate(itemId);

                        // Reject IDs that do not refer to a loaded item.
                        if (item == null)
                            throw new IllegalArgumentException("Unknown item ID: " + itemId);

                        // Attach the item reward to this puzzle.
                        puzzle.addReward(new ItemReward(item));
                        break;
                    }

                    default:
                        // Reject reward types that this loader does not support.
                        throw new IllegalArgumentException(
                                "Unsupported reward type: " + rewardType);
                }
            } catch (RuntimeException e) {
                // Include the line and entry in the error to help locate bad file data.
                throw new IllegalArgumentException(
                        "Invalid Rewards entry on line " + lineNumber + ": " + currentLine,
                        e);
            }
        }
    }

    // This reads each Merchant record and adds the Merchant to the room listed in the file.
    public static void extractMerchantData(Scanner fileScanner, Map worldMap) {
        // Each non-empty line in the file represents one Merchant.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            // A Merchant record contains an ID, a name, dialogue, stock item IDs, and a room ID.
            String[] fields = trimStringArray(line.split(DELIMITER_A, -1));

            // Skip records that do not have all five fields or are missing the Merchant's ID or name.
            if (fields.length != 5 || fields[0].isEmpty() || fields[1].isEmpty())
                continue;

            try {
                // Find the room where this Merchant belongs, and skip the record if that room is missing.
                Room room = worldMap.getRoomById(Integer.parseInt(fields[4]));
                if (room == null)
                    continue;

                // Store each dialogue line separately, while allowing a Merchant to have no dialogue.
                // This checks the List of dialogue lines to see if it is empty,
                // and if so, creates an empty List.
                // Otherwise, it splits the dialogue string into a List of lines.

                // Ternary operators are really neat and condense code like this down by a lot, otherwise it would
                // have been really complicated.
                List<String> dialogue = fields[2].isEmpty()
                        ? new ArrayList<>()
                        : Arrays.asList(fields[2].split("\\|", -1));

                // Build the Merchant's stock by finding each listed item and making a separate copy of it for the
                // Merchant's stock. This allows the Merchant to have its own unique copies of items,
                // rather than sharing the same instances with other Merchants or Rooms.
                HashMap<String, ArrayList<Item>> stock = new HashMap<>();
                if (!fields[3].isEmpty()) {
                    for (String id : fields[3].split(DELIMITER_B)) {
                        Item template = ItemUtil.findItemById(worldMap, Integer.parseInt(id.trim()));
                        // Skip stock IDs that do not match an item loaded into the world.
                        if (template == null)
                            continue;

                        // Add supported item copies under their lowercase names so stock can be looked up by name.
                        Item item = ItemUtil.copyItem(template);
                        if (item != null) {
                            stock.computeIfAbsent(item.getItemName().toLowerCase(),
                                    name -> new ArrayList<>()).add(item);
                        }
                    }
                }
                // Create the Merchant from the parsed fields and add it to the room.
                room.addMerchant(new Merchant(fields[0], fields[1], dialogue, stock));
            } catch (NumberFormatException e) {
                // Skip the record if its room ID or one of its stock item IDs is not a valid number.
                continue;
            }
        }
    }

    /* Extract data from a file and parse it into a finite state machine for a Combatant object */
    public static void extractFiniteStatesData(Scanner fileScanner, Combatant combatant) {
        // Read states data //
        if (fileScanner.hasNextLine()) {
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
        } else
            // No states data found, exit method
            return;
    }


    // Splits a comma-separated field into trimmed, non-empty values.
    private static ArrayList<String> parseCompanionValues(String value) {
        ArrayList<String> values = new ArrayList<>();
        for (String part : value.split(DELIMITER_B)) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty())
                values.add(trimmed);
        }
        return values;
    }

    /*
    Converts an array of equipment names into arraylist of items
     */
    private static ArrayList<Item> parseItemValues(String[] equipmentNames, Map worldMap){
        ArrayList<Item> defaultEquipment = new ArrayList<>();

        //loop through each equipment name
        for (String equipmentName : equipmentNames){

            //defaults quantity to 1
            int quantity = 1;
            String itemName = equipmentName.trim();

            //checks if itemName has "*"
            if(itemName.contains(DELIMITER_C)){

                String[] parts = itemName.split("\\*",2); //separates quantity and item name

                quantity = Integer.parseInt(parts[0].trim()); //converts quantity of item into int

                itemName = parts[1].trim();
            }

            Item temp = null;

            //search world map for item matching the given name.
            for (Item item : worldMap.getItemTemplates().values()){
                if(item.getItemName().equalsIgnoreCase(itemName)){
                    temp = item;
                    break;
                }
            }


            //creates items based on quantity
            for (int i = 0; i < quantity; i++){
                //create a separate item object from template
                Item copy = ItemUtil.copyItem(temp);

                //prevents non found items from being added to starting class equipment.
                if (copy != null){
                    defaultEquipment.add(copy);
                }
            }
        }

        return defaultEquipment;
    }
    // Reads each Companion record and adds it to the room listed in the file.
    public static void extractCompanionData(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            // Field Legend
            // Fields: 0 ID, 1 name, 2 backstory, 3 base max HP, 4 STR, 5 DEX,
            // 6 INT, 7 ARC, 8 END, 9 perk, 10 default abilities,
            // 11 default equipment, 12 room ID.
            String[] attributes = trimStringArray(line.split(DELIMITER_A, -1));
            if (attributes.length != 13)
                continue;

            try {
                int companionId = Integer.parseInt(attributes[0]);
                int baseMaxHP = Integer.parseInt(attributes[3]);
                int strength = Integer.parseInt(attributes[4]);
                int dexterity = Integer.parseInt(attributes[5]);
                int intelligence = Integer.parseInt(attributes[6]);
                int arcane = Integer.parseInt(attributes[7]);
                int endurance = Integer.parseInt(attributes[8]);
                int roomId = Integer.parseInt(attributes[12]);
                ArrayList<CombatSkill> combatSkills = new ArrayList<>();
                String[] defaultEquipmentNames = trimStringArray(attributes[11].split(DELIMITER_B));
                ArrayList<Item> defaultEquipment = parseItemValues(defaultEquipmentNames, worldMap);

                //split combat abilities using " , " as the delimiter
                ArrayList<String> combatAbilitiesList = parseCompanionValues(attributes[10]);

                //adds combat ability to list
                for(String abilities : combatAbilitiesList){
                    //creates CombatSkill factory
                    CombatSkillFactory factory = new CombatSkillFactory();
                    //creates skill by name received from text file
                    CombatSkill skill = factory.createByName(abilities);
                    //add to arrayList
                    combatSkills.add(skill);
                }
                Room room = worldMap.getRoomById(roomId);
                if (room == null)
                    continue;


                Companion companion = new Companion(
                        attributes[1],
                        attributes[2],
                        baseMaxHP,
                        strength,
                        dexterity,
                        intelligence,
                        arcane,
                        endurance,
                        attributes[9],
                        combatSkills,
                        defaultEquipment,
                        companionId,
                        room
                );

                room.addCompanion(companion);
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }
        public static void extractStartingClassData(Scanner fileScanner, ArrayList<StartingClass> classes, Map worldMap){


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
			ArrayList<CombatSkill> combatSkills = new ArrayList<>();
			ArrayList<Item> defaultEquipment = new ArrayList<>();
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
                    //creates CombatSkill factory
                    CombatSkillFactory factory = new CombatSkillFactory();
                    //creates skill by name received from text file
                    CombatSkill skill = factory.createByName(abilities);
                    //add to arrayList
                    combatSkills.add(skill);
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Leave blank
			}

			//assigns starting class equipment
			//starting class will have more than one equipment
			try{
                //split equipment items using "," as delimiter
                String[] equipmentNames = trimStringArray(parsedLine[10].split(DELIMITER_B));

                //retrieves default equipment for starting player
                defaultEquipment = parseItemValues(equipmentNames, worldMap);

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
							combatSkills,
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