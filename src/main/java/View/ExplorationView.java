package View;

import java.util.*;

import Model.Combat.Companion;

public class ExplorationView 
{
	/* Data Fields */
	
	/* Methods */
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
				
				View.println("- " + exitDirection);
			}
		}
		
		else
			View.println("No available exits in this room");
	}
}

