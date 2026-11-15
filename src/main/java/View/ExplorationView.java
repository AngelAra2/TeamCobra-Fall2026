package View;

import java.util.*;

import Model.AbstractClasses.Item;
import Model.Combat.Companion;
import Model.Exploration.Room;
import Model.Exploration.Puzzle;
import Model.Exploration.Merchant;

public class ExplorationView 
{
	/* Data Fields */
	
	/* Methods */
	public static void printRoomInfo(Room currentRoom)
	{
		/* Prints out the current room's name and description */
		/* Print out details about the current room */
		String roomName = "[" + currentRoom.getRoomName() + "]";
		String roomDesc = currentRoom.getRoomDescription();
		boolean wasVisited = currentRoom.getWasVisited();
		
		View.println("Current room: " + roomName);
		View.println(View.wrapText(roomDesc, 65));
		
		if (wasVisited)
			View.println("You've been here before...");
	}

    public static void printPuzzleInRoom(Room currentRoom) // Not using the toString here because I
                                                           // want it to print everything BUT the puzzle description.
    {
        if (currentRoom.getPuzzle() != null)
        {
            Puzzle puzzle = currentRoom.getPuzzle();
            View.println("Puzzle in this room:");
            View.println("[" + puzzle.getPuzzleName() + "] | "
                    + puzzle.getRemainingAttempts() + "/"
                    + puzzle.getMaxAttempts() + " attempt(s) remaining");
        }
        else
            View.println("No puzzle in this room");
    }

	public static void printItemsInRoom(HashMap<String, ArrayList<Item>> itemsInRoom)
	{
		/* Prints out all items in the current room */
		int numItems = 0;
		
		for (ArrayList<Item> itemList: itemsInRoom.values())
			numItems += itemList.size();
		
		if (numItems > 0)
		{
			View.println("Items in this room:");
			
			for (Map.Entry<String, ArrayList<Item>> entry: itemsInRoom.entrySet())
			{
				String itemType = entry.getKey();
				ArrayList<Item> itemList = entry.getValue();
				
				// Print out each item's quantity
				for (Item item: itemList)
					View.println("> " + item.getItemName() + " (x" + itemList.size() + ")");
			}
		}
		
		else
			View.println("No items in this room");
	}
	
	public static void printCompanionsInRoom(ArrayList<Companion> companionsInRoom)
	{
		/* Prints out the number of companions in the current room */
		int numCompanions = companionsInRoom.size();
		
		if (numCompanions > 0)
		{
			View.println("Recruitable Companions in this room:");
			for (Companion companion: companionsInRoom)
				View.println(">>> " + companion.getName());
		}
		
		else
			View.println("No recruitable Companions in this room");
	}
	
	public static void printRoomExits(HashMap<String, Integer> exits)
	{
		/* Prints out the available exits in the current room */
		
		if (exits.size() > 0)
		{
			View.println("Available exits in this room:");
			
			// Print out all available exits in the current room
			// If an exit doesn't lead to a room, skip printing it
			for (Map.Entry<String, Integer> exit: exits.entrySet())
			{
				String exitDirection = exit.getKey();
				int exitRoomId = exit.getValue();
				
				if (exitRoomId <= 0)
					continue;
				
				View.println("> " + exitDirection);
			}
		}
		
		else
			View.println("No available exits in this room");
	}

    // Lists merchants in the room, or reports when there are none.
    public static void printMerchantsInRoom(Room room)
    {
        if (room.getMerchants().isEmpty())
        {
            View.println("No merchant in this room.");
            return;
        }

        View.println("Merchants in this room:");
        for (Merchant merchant : room.getMerchants())
            View.println("> " + merchant.getName());
    }
}

