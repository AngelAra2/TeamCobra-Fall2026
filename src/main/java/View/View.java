package View;

public class View {
	/* Helper methods: printing to console */
	public static void print(String line)
	{
		System.out.print(line);
	}
	
	public static void println(String line)
	{
		System.out.println(line);
	}
	
	public static void printObject(Object obj)
	{
		System.out.print(obj);
	}
	
	public static void printlnObject(Object obj)
	{
		System.out.println(obj);
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
		
		println("Welcome to the Text-Based Adventure Game! Explore the world, solve puzzles, and collect items to progress through the game.");
		println("Type \"help\" to view all available commands.");
	}
	
	public static void printCommands()
	{
		/* Prints out all available game commands to the console */
		
		println("------------[GAME COMMANDS]------------");
		
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
		println("\"drop [item name]\" --> drop an item from your inventory into the current room");
		println("\"inventory\" --> view all items in your inventory");
		
		// Miscellaneous
		println("");
		println("MISCELLANEOUS");
		println("\"room\" or \"location\" --> view the current room's name and description");
		println("\"help\" --> view all the game's commands");
		println("\"q\" or \"quit\" --> quit the game");
	}
}
