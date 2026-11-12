package Model.Data;

import java.util.*;

import Model.Combat.Companion;
import Model.Exploration.Merchant;
import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;
import Model.Combat.Monster;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import Model.Items.Consumable;
import Model.Items.Weapon;
import Model.Combat.StartingClass;

public class DataExtractionMethods {

    /* Data Fields */
    private static String delimiterA = "_";
    private static String delimiterB = ",";

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
            String[] attributes = trimStringArray(currentLine.split(delimiterA));

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
                String[] roomExits = trimStringArray(attributes[attributes.length - 1].split(delimiterB));

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
    public static void extractItemData(Scanner fileScanner, Map worldMap) {
        HashMap<Integer, Room> rooms = worldMap.getRooms();

        // Read puzzle data and create Puzzle objects //
        while (fileScanner.hasNextLine()) {
            /* Read current line to get Puzzle data */
            String currentLine = fileScanner.nextLine().trim();

            // Use delimiter to split string into readable attributes
            String[] attributes = trimStringArray(currentLine.split(delimiterA));

            // Declare required attributes
            int itemId;
            String itemType = "...";
            String itemName = "Item";
            String itemDesc = "...";

            // Read itemId
            try {
                itemId = Integer.parseInt(attributes[0]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read itemType
            try {
                itemType = attributes[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read itemName
            try {
                itemName = attributes[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read itemDesc
            try {
                itemDesc = attributes[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // There may be more than one of the same item in the game
            // Depending on the number of room IDs listed in the file, a new Item object will be created for each room ID
            try {
                // Read room IDs as a single string and split it into an array of strings
                String[] targetRoomIds = trimStringArray(attributes[attributes.length - 1].split(delimiterB));

                // Create a new Item object for each room ID listed
                int roomId;
                for (int i = 0; i < targetRoomIds.length; i++) {
                    // Read roomId this item is located in
                    try {
                        roomId = Integer.parseInt(targetRoomIds[i]);
                    } catch (NumberFormatException e) {
                        continue;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }

                    /* Create Item object */

                    // Check item type and create the appropriate subclass object
                    Item item;
                    switch (itemType.toLowerCase()) {
                        case "weapon":
                            int baseAttack = 0;
                            try {
                                baseAttack = Integer.parseInt(attributes[4]);
                            } catch (NumberFormatException e) {
                                // Leave default value
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // Leave default value
                            }

                            item = new Weapon(itemId, itemName, itemDesc, baseAttack);
                            break;

                        case "consumable":
                            int healingAmount = 0;
                            try {
                                healingAmount = Integer.parseInt(attributes[4]);
                            } catch (NumberFormatException e) {
                                // Leave default value
                            } catch (ArrayIndexOutOfBoundsException e) {
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
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
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

            // Use delimiter to split string into readable attributes
            String[] attributes = trimStringArray(currentLine.split(delimiterA));

            // Declare required attributes
            int puzzleId;
            int targetRoomId;
            String puzzleName = "Puzzle";
            String puzzleDesc = "...";
            String puzzleSolution = "Answer";
            int attempts = 3;

            // Read puzzleId
            try {
                puzzleId = Integer.parseInt(attributes[0]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read targetRoomId
            try {
                targetRoomId = Integer.parseInt(attributes[1]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read puzzleName
            try {
                puzzleName = attributes[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read puzzleDesc
            try {
                puzzleDesc = attributes[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read puzzleSolution
            try {
                puzzleSolution = attributes[4];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read attempts
            try {
                attempts = Integer.parseInt(attributes[5]);
            } catch (NumberFormatException e) {
                // Leave default value
            } catch (ArrayIndexOutOfBoundsException e) {
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
    public static void extractMonsterData(Scanner fileScanner, Map worldMap) {
        HashMap<Integer, Room> rooms = worldMap.getRooms();

        // Read monster data and create Monster objects //
        while (fileScanner.hasNextLine()) {
            /* Read current line to get Monster data */
            String currentLine = fileScanner.nextLine().trim();

            // Use delimiter to split string into readable attributes
            String[] attributes = trimStringArray(currentLine.split(delimiterA));

            // Declare required attributes
            int monsterId;
            String monsterName = "Monster";
            String monsterDesc = "...";
            int maxHP = 10;
            int physicalDEF = 0;
            int magicDEF = 0;
            int attack = 1;
            int xpReward = 1;
            int goldReward = 1;
            boolean isBoss = false;

            // Read monsterId
            try {
                monsterId = Integer.parseInt(attributes[0]);
            } catch (NumberFormatException e) {
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            // Read monsterName
            try {
                monsterName = attributes[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read monsterDesc
            try {
                monsterDesc = attributes[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read maxHP
            try {
                maxHP = Integer.parseInt(attributes[3]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read physicalDEF
            try {
                physicalDEF = Integer.parseInt(attributes[4]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read magicDEF
            try {
                magicDEF = Integer.parseInt(attributes[5]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read attack
            try {
                attack = Integer.parseInt(attributes[6]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read xpReward
            try {
                xpReward = Integer.parseInt(attributes[7]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read goldReward
            try {
                goldReward = Integer.parseInt(attributes[8]);
            } catch (NumberFormatException e) {
                // Leave blank
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // Read isBoss
            try {
                isBoss = Boolean.parseBoolean(attributes[9]);
            } catch (ArrayIndexOutOfBoundsException e) {
                // Leave blank
            }

            // There may be more than one of the same monster in the game
            // Depending on the number of room IDs listed in the file, a new Monster object will be created for each room ID
            try {
                // Read room IDs as a single string and split it into an array of strings
                String[] targetRoomIds = trimStringArray(attributes[attributes.length - 1].split(delimiterB));

                // Create a new Item object for each room ID listed
                int roomId;
                for (int i = 0; i < targetRoomIds.length; i++) {
                    // Read roomId this item is located in
                    try {
                        roomId = Integer.parseInt(targetRoomIds[i]);
                    } catch (NumberFormatException e) {
                        continue;
                    } catch (ArrayIndexOutOfBoundsException e) {
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
                            attack,

                            monsterId,
                            monsterDesc,
                            xpReward,
                            goldReward,
                            isBoss
                    );

                    // Add monster to the room with the matching room ID, if it exists
                    rooms.get(roomId).addMonster(monsterName.toLowerCase(), monster);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
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
            String[] fields = trimStringArray(line.split(delimiterA, -1));

            // Skip records that do not have all five fields or are missing the Merchant's ID or name.
            if (fields.length != 5 || fields[0].isEmpty() || fields[1].isEmpty())
                continue;

            try {
                // Find the room where this Merchant belongs, and skip the record if that room is
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
                    for (String id : fields[3].split(delimiterB)) {
                        Item template = findItemById(worldMap, Integer.parseInt(id.trim()));
                        // Skip stock IDs that do not match an item loaded into the world.
                        if (template == null)
                            continue;

                        // Add supported item copies under their lowercase names so stock can be looked up by name.
                        Item item = copyItem(template);
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

    // This searches the rooms for an item with the requested ID.
    private static Item findItemById(Map worldMap, int itemId) {
        for (Room room : worldMap.getRooms().values()) {
            for (ArrayList<Item> items : room.getItems().values()) {
                for (Item item : items) {
                    if (item.getItemId() == itemId)
                        return item;
                }
            }
        }

        return null;
    }

    // This makes a separate copy of each item type supported in Merchant stock.
    private static Item copyItem(Item item) {
        if (item instanceof Weapon weapon) {
            return new Weapon(item.getItemId(), item.getItemName(),
                    item.getItemDescription(), weapon.getBaseAttack());
        }

        if (item instanceof Consumable consumable) {
            return new Consumable(item.getItemId(), item.getItemName(),
                    item.getItemDescription(), consumable.getHealingAmount());
        }

        return null;
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
            String[] states = trimStringArray(currentLine.split(delimiterA));

            // Trim all string attributes to remove whitespace
            for (int i = 0; i < states.length; i++)
                states[i] = states[i].trim();

            // Initialize the combatant's state machine with the extracted states
            combatant.initStateMachine(states);
        } else
            // No states data found, exit method
            return;
    }


    // This reads each Companion record and adds the Companion to the room listed in the file.
    public static void extractCompanionData(Scanner fileScanner, Map worldMap) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            String[] attributes = trimStringArray(line.split(delimiterA, -1));
            if (attributes.length != 19)
                continue;

            try {
                int companionId = Integer.parseInt(attributes[0]);
                int maxHP = Integer.parseInt(attributes[2]);
                int physicalDEF = Integer.parseInt(attributes[3]);
                int magicDEF = Integer.parseInt(attributes[4]);
                int attack = Integer.parseInt(attributes[5]);
                int level = Integer.parseInt(attributes[6]);
                int xp = Integer.parseInt(attributes[7]);
                int mana = Integer.parseInt(attributes[8]);
                int baseMaxHP = Integer.parseInt(attributes[9]);
                int strength = Integer.parseInt(attributes[10]);
                int intelligence = Integer.parseInt(attributes[11]);
                int dexterity = Integer.parseInt(attributes[12]);
                int arcane = Integer.parseInt(attributes[13]);
                int endurance = Integer.parseInt(attributes[14]);
                int roomId = Integer.parseInt(attributes[18]);

                if (!attributes[17].equalsIgnoreCase("true")
                        && !attributes[17].equalsIgnoreCase("false"))
                    continue;

                Room room = worldMap.getRoomById(roomId);
                if (room == null)
                    continue;

                Companion companion = new Companion(
                        attributes[1],
                        maxHP, physicalDEF, magicDEF, attack,
                        level, xp, mana, baseMaxHP,
                        strength, intelligence, dexterity, arcane, endurance,
                        new ArrayList<>(),
                        companionId,
                        attributes[15],
                        attributes[16],
                        Boolean.parseBoolean(attributes[17]),
                        room
                );

                room.addCompanion(companion);
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }

        public static void extractStartingClassData(Scanner fileScanner, ArrayList<StartingClass> classes){


            while(fileScanner.hasNextLine()){

                String line = fileScanner.nextLine();
                //splits line string using " _ " as the delimiter.
                String parsedLine[] = line.split(delimiterA);

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
                    String[] combatAbilitiesList = trimStringArray(parsedLine[9].split(delimiterB));

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
                    String[] targetItemIds = trimStringArray(parsedLine[10].split(delimiterB));

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
}

