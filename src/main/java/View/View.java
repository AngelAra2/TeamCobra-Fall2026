package View;

import Model.AbstractClasses.Item;
import Model.Combat.CombatSkill;
import Model.Combat.Companion;
import Model.Combat.Player;
import java.util.ArrayList;
import java.util.*;
import Model.Exploration.Puzzle;

//Angel, Dylan, Danh
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

		println(wrapText("Sol Invictus was a great warrior, who came from a faraway land in search of a Distant Light, after his home was surrounded by Darkness and fell to ruin soon after."
				,50));

		printColored("You must become the INHERITOR", "yellow",true);

		separate(3);
		printColored("-----<< Welcome to Inheritor >>-----", "yellow", true);
		println("");
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

		// Item Interaction
		println("");
		println("COMPANION ITEM INTERACTION");
		println("\"[companion name] equip [item name]\" --> equip a weapon from your inventory, increasing your Attack stat");
		println("\"[companion name] unequip [item name]\" --> unequip your currently equipped weapon");
		println("\"[companion name] heal [item name]\" --> use a consumable item from your inventory to heal yourself");
		println("\"[companion name] inventory\" --> view all items in your inventory");
		println("\"[companion name] status\" --> view companion status");


		// Monster Interaction
		println("");
		println("PARTY");
		println("\"add [companion name]\" --> adds companion to active party");
		println("\"remove [companion name]\" --> removes companion to active party");

        // Merchant Interaction
        println("");
        println("MERCHANT INTERACTION");
        println("\"shop [merchant name]\" --> view a merchant's wares");
        println("\"buy [item name]\" --> buy one item from the active merchant");
        println("\"sell [item name]\" --> sell one item to the active merchant");
        println("\"cancel\" --> end the current merchant interaction");
		
		// Miscellaneous
		println("");
		println("MISCELLANEOUS");
		println("\"room\" or \"location\" --> view the current room's name and description");
		println("\"status\" --> view the player's status");
        println("\"solve\" --> attempt to solve a puzzle in a room."); // Dylan 11.15.26
		println("\"help\" --> view all the game's commands");
		println("\"q\" or \"quit\" --> quit the game");
		
		separate(3);
		println("------------[COMBAT COMMANDS]------------");
		
		println("");
		println("\"Fight\" --> attack the monster");
		println("\"Use Item\" --> use a consumable item from your inventory to heal yourself");
		//println("\"attack\" --> attack the monster");
		println("\"target [target number]\" --> switch to a different monster target in battle");
		//println("\"heal [item name]\" --> use a consumable item from your inventory to heal yourself");
	}

	/*
	11.10.26
	Angel
	 */
	public static void printStartingClassStats(String background,
											   int hp, int baseSTR, int baseDEX,
											   int baseINT, int baseARC, int baseEND, ArrayList<CombatSkill> skills,
											   ArrayList<Item> defaultEquipment){

		View.println("Background: " + wrapText(background, 65));
		View.println("\nBase Max HP: " + hp);
		View.println("Base STR: " + baseSTR);
		View.println("Base DEX: " + baseDEX);
		View.println("Base INT: " + baseINT);
		View.println("Base ARC: " + baseARC);
		View.println("Base END: " + baseEND);


		View.println("\nCombat Abilities: ");
		for (CombatSkill skill : skills){
			View.println("- " + skill.getName());
		}

		View.println("\nEquipment:");
		HashMap<String, Integer> itemCounts = new HashMap<>();

		// Count each item
		for (Item equipment : defaultEquipment) {
			String itemName = equipment.getItemName();

			itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + 1
			);
		}

		// Display each item with its quantity
		for (String itemName : itemCounts.keySet()) {
			int quantity = itemCounts.get(itemName);

			if (quantity > 1) {
				View.println("- " + itemName + " x" + quantity);
			} else {
				View.println("- " + itemName);
			}
		}

	}

	//Angel
	//Method is used to display player status
	public static void printCompanionStatus(Companion character){
		printColored("============ COMPANION STATUS ============","blue", true );

		println("Name: " + character.getName());
		print("Level: " + character.getCurrentLevel());

		println("\nHP: " + character.getCurrentHP() + "/" + character.getMaxHP());

		println("Mana: " + character.getCurrentMana() + "/" + character.calculateMaxMana());

		println("\nPhysical Defense: " + character.getPhysicalDEF());
		println("Magic Defense: " + character.getMagicDEF());

		println("\nSTR: " + character.getStrength());
		println("DEX: " + character.getDexterity());
		println("INT: " + character.getIntelligence());
		println("ARC: " + character.getArcane());
		println("END: " + character.getEndurance());

		println("\nCombat Abilities:");

		if(character.getSkills().isEmpty()){
			println("- None Available");
		} else{
			for (CombatSkill skill : character.getSkills()){
				print("- ");
				printColored(skill.getName(),"cyan", true);
				print("- ");

				printColored("Description: ","cyan", true);
				println("");
				println(wrapText(skill.getDescription(), 50));
				separate(2);
			}
		}
		printColored("\n=======================================","blue",true);
	}

	public static void printPlayerStatus(Player player){
		printColored("============ PLAYER STATUS ============","blue", true );

		println("Name: " + player.getName());
		print("Level: " + player.getCurrentLevel());

		println("\nHP: " + player.getCurrentHP() + "/" + player.getMaxHP());

		println("Mana: " + player.getCurrentMana() + "/" + player.calculateMaxMana());

		println("\nPhysical Defense: " + player.getPhysicalDEF());
		println("Magic Defense: " + player.getMagicDEF());

		println("\nSTR: " + player.getStrength());
		println("DEX: " + player.getDexterity());
		println("INT: " + player.getIntelligence());
		println("ARC: " + player.getArcane());
		println("END: " + player.getEndurance());

		println("\nCombat Abilities:");

		if(player.getSkills().isEmpty()){
			println("- None Available");
		} else{
			for (CombatSkill skill : player.getSkills()){
				print("- ");
				printColored(skill.getName(),"cyan", true);
				print("- ");

				printColored("Description: ","cyan", true);
				println("");
				println(wrapText(skill.getDescription(), 50));
				separate(2);
			}
		}
		printColored("\n=======================================","blue",true);
	}

	public static void printPuzzlePrompt(Puzzle puzzle) {
		separate(3);
		println("[" + puzzle.getPuzzleName() + "]");
		println("Puzzle prompt: " + wrapText(puzzle.getPuzzleDescription(), 100));
		println("");
		printColored("Enter a solution: ", "cyan", false);
	}

	public static void printIncorrectFirstGuess() {
		println("Incorrect solution, please enter a different guess.");
	}

	public static void printIncorrectSecondGuess() {
		println("Incorrect solution, try again later.");
	}

	public static void printPuzzleSolved() {
		printColored("Correct solution! You have solved the puzzle!", "green", true);
	}

	public static void printNoPuzzleInRoom() {
		println("There is no puzzle in the current room to solve.");
	}

	public static void printPuzzleInputPrompt() {
		printColored("Enter a solution: ", "cyan", false);
	}

	public static void printPuzzleHint(String hint) {
		println("Hint: " + wrapText(hint, 100));
	}

	public static void printNoMorePuzzleHints() {
		println("No more hints available for the current puzzle. Attempt to solve the puzzle");
	}
}




