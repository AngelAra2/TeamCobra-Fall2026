package View;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class View
{
	/* Data Fields */
	
	// Colors
	private static final String RESET = "\u001B[0m";
	private static final String BLACK = "\u001B[90m";
	private static final String RED = "\u001B[91m";
	private static final String GREEN = "\u001B[92m";
	private static final String YELLOW = "\u001B[93m";
	private static final String BLUE = "\u001B[94m";
	private static final String PURPLE = "\u001B[95m";
	private static final String CYAN = "\u001B[96m";
	
	/* Helper methods: printing to console */
	public static void print(String line) { System.out.print(line); }	
	public static void println(String line) { System.out.println(line); }
	public static void printObject(Object obj) { System.out.print(obj); }
	public static void printlnObject(Object obj) { System.out.println(obj); }

	//11.10.26
	//Angel
	//Used to wrap text in terminal for long strings without splitting words.
	public static String wrapText(String line, int maxWidth){
		//used to rebuild line with added '\n'
		StringBuilder result = new StringBuilder();
		int length = 0; //tracks total characters on line being built

		//splits word after each space
		for(String word : line.split("\\s+")){
			//checks whether adding the next word would make the line too long
			if(length > 0 && length + word.length() + 1 > maxWidth){
				result.append("\n");
				length = 0;
			}

			//checks if length is greater than one, if so, add a space
			if (length > 0){
				result.append(" ");
				length++;
			}


			result.append(word);
			//update length
			length += word.length();

		}

		return result.toString();
	}
	public static void printColored(String line, String color, boolean printOnNewLine) 
	{
		/* Prints a line to the console in a specified color */
		
		String colorCode = "";	
		switch (color.toLowerCase()) 
		{
			case "black": colorCode = BLACK; break;
			case "red": colorCode = RED; break;
			case "green": colorCode = GREEN; break;
			case "yellow": colorCode = YELLOW; break;
			case "blue": colorCode = BLUE; break;
			case "purple": colorCode = PURPLE; break;
			case "cyan": colorCode = CYAN; break;
			default: colorCode = RESET; break;
		}
		
		if (printOnNewLine)
			println(colorCode + line + RESET);
		else
			print(colorCode + line + RESET);
	}
	
	public static void printProgressBar(float progress, int barLength, String color, boolean printOnNewLine) 
	{
		/* Prints a progress bar to the console */
		// Clamp progress value between 0 and 1
		progress = Math.clamp(progress, 0f, 1f);
		
		int filledLength = (int) Math.floor(progress * barLength);
		StringBuilder bar = new StringBuilder();
		
		// Progress
		for (int i = 0; i < filledLength; i++)
			bar.append("=");
		
		// Missing progress
		for (int i = filledLength; i < barLength; i++)
			bar.append(" ");

		String barString = "<" + bar.toString() + ">";		
		printColored(barString, color, printOnNewLine);
	}

	public static void separate(int iterations)
	{
		/* Used for separating sections of the console output for easier reading */
		for (int i = 0; i < iterations; i++)
			println(".");
	}

	public static void printWelcomeMessage()
	{
		/* Prints out the welcome message to the console */
		
		printColored("-----<< Welcome to Inheritor >>-----", "yellow", true);
		println("");
		println("Explore the world, solve puzzles, and collect items to progress through the game");
		println("Type \"help\" to view all available commands");
	}
	
	public static void printCommands()
	{
		/* Prints out all available game commands to the console */
		
		println("------------[EXPLORATION COMMANDS]------------");
		
		// Movement
		println("");
		println("MOVEMENT");
		println("\"n\" or \"north\" --> move north");
		println("\"s\" or \"south\" --> move south");
		println("\"e\" or \"east\" --> move east");
		println("\"w\" or \"west\" --> move west");

		// Item Interaction
		println("");
		println("ITEM INTERACTION");
		println("\"explore\" --> view all items in the current room");
		println("\"pickup [item name]\" --> pick up an item from the current room");
		println("\"inspect [item name]\" --> inspect an item in your inventory");
		println("\"drop [item name]\" --> drop an item from your inventory into the current room. Also unequips weapon if the dropped item is the equipped weapon");
		println("\"equip [item name]\" --> equip a weapon from your inventory, increasing your Attack stat");
		println("\"unequip [item name]\" --> unequip your currently equipped weapon");
		println("\"heal [item name]\" --> use a consumable item from your inventory to heal yourself");
		println("\"inventory\" --> view all items in your inventory");
		
		// Monster Interaction
		println("");
		println("MONSTER INTERACTION");
		println("\"examine [monster name]\" --> examines a monster in the current room, giving details about the monster");
		println("\"attack\" --> after examining a monster, initiate combat with it");
		println("\"ignore\" --> after examining a monster, ignore it and continue exploring. The monster will disappear from the room when ignored");
		
		// Miscellaneous
		println("");
		println("MISCELLANEOUS");
		println("\"room\" or \"location\" --> view the current room's name and description");
		println("\"help\" --> view all the game's commands");
		println("\"q\" or \"quit\" --> quit the game");
		
		separate(3);
		println("------------[COMBAT COMMANDS]------------");
		
		println("");
		println("\"attack\" --> attack the monster");
		println("\"heal [item name]\" --> use a consumable item from your inventory to heal yourself");
	}

	/*
	11.10.26
	Angel
	 */
	public static void printStartingClassStats(String background,
											   int hp, int baseSTR, int baseDEX,
											   int baseINT, int baseARC, int baseEND, ArrayList<String> combatAbilities,
											   ArrayList<String> defaultEquipment){

		View.println("Background: " + wrapText(background, 65));
		View.println("Base Max HP: " + hp);
		View.println("Base STR: " + baseSTR);
		View.println("Base DEX: " + baseDEX);
		View.println("Base INT: " + baseINT);
		View.println("Base ARC: " + baseARC);
		View.println("Base END: " + baseEND);

		separate(1);
		View.println("Combat Abilities: ");
		for (String ability : combatAbilities){
			View.println("- " + ability);
		}

		separate(1);
		View.println("Equipment:");
		for (String equipment : defaultEquipment){
			View.println("- " + equipment);
		}

	}
}

