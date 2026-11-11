package Controller;

import java.util.*;

import Model.Combat.StartingClass;
import Model.Data.DataExtractionMethods;
import Model.Data.DataFileExtraction;
import Model.Processors.*;
import Model.Util.Wait;

import Model.Combat.Battlefield;
import Model.Combat.Monster;
import Model.Combat.Player;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import View.View;


public class GameController 
{
	/* Data Fields */
	
	// Variables //
	private static boolean isGameRunning;
	private static String userCmd = "";
	private static String cmdType = "";
	private static String processedCmd = "";
	
	// Core Game Objects //
	private static Map worldMap;
	private static Player player;

	// Command Processors //
	private static BasicCommandProcessor basicCmdProcessor;
	private static ExplorationCommandProcessor explorationCmdProcessor;
	private static CombatCommandProcessor combatCmdProcessor;

	private static String startingClassFilePath = "/gameObjects/StartingClasses";
	//------------------------------------//
	
	/* Helper Methods */
	private static boolean isSolvingPuzzle()
	{
		/* Check if the player is currently solving a puzzle in their current room */
		Puzzle currentPuzzle = player.getCurrentPuzzle();
		return (currentPuzzle != null && !currentPuzzle.getIsSolved() && currentPuzzle.getRemainingAttempts() > 0);
	}
	
	private static void puzzleSolvingLoop()
	{
		View.println("");
		View.printColored("!!! PUZZLE ENCOUNTERED !!!", "yellow", true);
		
		Puzzle currentPuzzle;
		while (isSolvingPuzzle())
		{
			Wait.yield(1f);
			
			/* Check if the player is currently solving a puzzle in their current room */
			currentPuzzle = player.getCurrentPuzzle();

			// Print out the puzzle prompt and the number of remaining attempts
			View.separate(3);

			View.printObject(currentPuzzle);
			View.println("");
			View.printColored("Enter an answer to the puzzle: ", "cyan", false);
			
			// Get user input for the puzzle
			userCmd = basicCmdProcessor.getUserInput();
			
			// Have the player's input be used as an answer to the puzzle
			boolean isCorrect = player.attemptToSolvePuzzle(userCmd);
			
			View.separate(3);
			View.println("...");
			Wait.yield(1.5f);
			
			View.println("");
			
			// Print out status messages letting the player know
			// if they solved the puzzle or how many attempts they
			// have left on an incorrect answer
			if (isCorrect)
				View.printColored("Correct! Puzzled solved", "green", true);
			else
			{
				int remainingAttempts = currentPuzzle.getRemainingAttempts();
				if (remainingAttempts > 0)
					View.printColored("Incorrect. " + remainingAttempts + " attempt(s) remaining", "red", true);
				else
					View.printColored("Incorrect. No more attempts remaining", "red", true);
			}
		}
		
		// Reset player to default state (exploring)
		player.resetToDefaultState();
	}
	
	private static void combatLoop()
	{
		/* In combat state, the player can:
		 * - attack
		 * - heal [item name]
		 */
		
		/* Init variables */
		String userCmd = "";
		String cmdType = "";
		
		@SuppressWarnings("unused")
		String processedCmd = "";
		
		View.println("");
		View.printColored("!!! BATTLE START !!!", "red", true);

		// Begin combat loop
		while (isGameRunning && Battlefield.canBattleContinue())
		{
			Wait.yield(1f);
			View.separate(3);
			
			// Print battle statuses at the beginning of each iteration of the combat loop
			Battlefield.displayBattleStatuses();
			
			/* Player's turn */
			View.separate(3);
			View.println("Type \"help\" to view all of the game's commands");
			View.printColored("Your turn. Enter a command: ", "cyan", false);
			
			// Get and process user input
			userCmd = combatCmdProcessor.getUserInput();
			combatCmdProcessor.setCurrentCommand(userCmd);
			
			// Get results
			cmdType = combatCmdProcessor.getCommandType();
			processedCmd = combatCmdProcessor.getProcessedCommand();
			
			/* Update game state based on combat command */
			switch (cmdType)
			{
				// The user wants to attack
				case "attack":
					View.separate(3);
					View.println("Attacking monster...");
					Wait.yield(1f);

					Battlefield.performAttack(player, Battlefield.getFirstAliveMonster());
				
					break;
					
				// The user wants to use a healing item from their inventory
				case "heal":
					View.separate(3);
					View.println("Attempting to heal...");
					Wait.yield(1f);
					
					// Pass in userCmd so that the player can parse it to get the item name
					player.handleItemInteraction(userCmd);
				
					break;
				
				// The user wants to view the game's commands
				case "help":
					View.separate(3);
					View.printCommands();
					
					// Skip the monsters' turn and continue to the next iteration of the combat loop
					continue;
				
				// Invalid combat command
				default:
					View.println("Command not recognized. Type \"help\" to view the game's commands");
					
					// Skip the monsters' turn and continue to the next iteration of the combat loop
					continue;
			}
			
			/* Monsters' turn */
			ArrayList<Monster> monstersList = Battlefield.getMonsters();
			for (int i = 0; i < monstersList.size(); i++)
			{
				Monster monster = monstersList.get(i);

				View.separate(3);
				
				// If the monster is alive, let it attack the player
				if (monster.isAlive())
				{
					View.println(monster.getName() + " attacks...");
					
					Wait.yield(1f);
					
					// Let the monster attack the player
					Battlefield.performAttack(monster, player);
				}
				
				// Monster defeated; remove it from the battlefield and current room
				else
				{
					Battlefield.removeMonster(monster);
					
					Room currentRoom = player.getCurrentRoom();
					if (currentRoom != null)
						currentRoom.removeMonster(monster.getName(), monster);

					View.printColored(monster.getName() + " defeated!", "green", true);
					
					// Reduce i by 1 to keep loop in sync
					i--;
				}
				
			}
		}
		
		Wait.yield(1f);
		View.separate(10);
		
		// Combat loop exited; clear battlefield
		Battlefield.clearBattlefield();
		
		View.printColored("!!! BATTLE OVER !!!", "yellow", true);
		
		// Make the player exit combat state
		player.resetToDefaultState();
		
		// Check whether the player has won or lost the battle
		if (player.isAlive())
			View.printColored("Victory! All monsters downed", "green", true);
	}
	
	//------------------------------------//
	
	/* Gameplay Loop Helper Methods */
	private static void quit()
	{
		/* Clean up class objects */
		basicCmdProcessor.destroy();
		explorationCmdProcessor.destroy();
		combatCmdProcessor.destroy();
		
		// Print quit message
		View.separate(10);
		View.printColored("Thanks for playing. Goodbye~", "blue", true);
	}
	
	private static boolean gameOverScreen()
	{
		/* Give the player two options: restart the game or quit */
		while (true)
		{
			View.separate(3);
			View.println("You can either restart the game or quit");
			View.println("\"restart\" or \"quit\": what do you choose? ");
			View.printColored("Enter a command: ", "cyan", false);
			
			String userCmd = basicCmdProcessor.getUserInput();
			
			switch (userCmd)
			{
				// Restart the game
				case "restart":
					return true;
					
				case "quit":
					isGameRunning = false;
					return false;
					
				default:
					View.println("Command not recognized. Type \"restart\" to restart the game or \"quit\" to exit ");
					break;
			}
		}
	}
	
	private static boolean exploration()
	{
		/* In exploration state, the player can:
		 * - move
		 * - interact with items
		 * - interact with monsters
		 * - perform miscellaneous actions
		 */
		
		View.separate(3);
		View.printColored("Exploring. Enter a command: ", "cyan", false);
		
		// Get and process user input
		userCmd = explorationCmdProcessor.getUserInput();
		explorationCmdProcessor.setCurrentCommand(userCmd);
		
		// Get results
		cmdType = explorationCmdProcessor.getCommandType();
		processedCmd = explorationCmdProcessor.getProcessedCommand();
		
		/* Update game state based on exploration command */
		View.separate(10);
		
		// Movement command
		if (cmdType.equals("move"))
		{
			View.println("Attempting to move " + processedCmd + "...");
			View.println("");
			
			Wait.yield(1.5f);
			
			/* Update player to move in the direction they want */
			// Get room ID based on direction
			Room currentRoom = player.getCurrentRoom();
			if (currentRoom != null)
			{
				boolean movementSuccessful = player.moveToRoom(worldMap, processedCmd);
				
				/* Check if the player has entered a room with a puzzle that is unsolved */
				Puzzle getPuzzleInRoom = player.getCurrentPuzzle();
				if (movementSuccessful 
					&& (getPuzzleInRoom != null && !getPuzzleInRoom.getIsSolved())
				)
				{
					// Reset the puzzle (reset its remaining attempts)
					getPuzzleInRoom.resetPuzzle();
					
					// Set player's state to "solvingPuzzle"
					player.setCurrentState("solvingPuzzle");

					// Continue to the next iteration of the main gameplay loop
					return true;
				}
				
				// Print error message if the player cannot move in that direction
				if (!movementSuccessful)
					View.printColored("Can't go this way...", "red", true);
			}
		}
		
		// Monster interaction command
		else if (cmdType.equals("monster"))
		{
			// The user would like to examine/attack/ignore a monster in the current room
			if (processedCmd.equals("targetMonster"))
			{
				View.println("...");
				Wait.yield(1.25f);
				
				View.println("");
				
				// Let the player handle the logic for this command
				// Pass in userCmd so that the player can parse it to get the monster name
				player.handleMonsterInteraction(userCmd);

				// If the player has examined a monster, give them the option to attack or ignore it
				Monster examinedMonster = player.getExaminedMonster();
				
				Wait.yield(1f);
				
				if (examinedMonster != null)
				{
					View.separate(3);
					View.println("What now: \"attack\" or \"ignore\" " + examinedMonster.getName() + "?");						

					// Continuously prompt the player to attack or ignore the monster until they do so
					while (true)
					{
						View.println("");
						View.printColored("Enter a command: ", "cyan", false);

						String subUserCmd = basicCmdProcessor.getUserInput();
						
						View.separate(3);
						View.println("...");
						View.println("");
						Wait.yield(1f);

						if (subUserCmd.equals("attack") || subUserCmd.equals("ignore"))
						{
							// Pass in userCmd as arbitrary input, the handler will do the rest
							player.handleMonsterInteraction(subUserCmd);
							break;
						}
						else
							View.println("Command not recognized. Choose to either \"attack\" or \"ignore\"");
					}
				}
				
				// Not currently examining a monster in the current room
				else
					View.println("Examine a monster first before taking action on it");
			}
		}
		
		// Item interaction command
		else if (cmdType.equals("item"))
		{
			View.println("...");
			View.println("");
			Wait.yield(1.5f);
			
			switch (processedCmd)
			{
				// The user would like to pickup/inspect/drop an item, 
				// either in the current room or in their inventory
				case "targetItem":
					// Let the player handle the logic for this command
					// Pass in userCmd so that the player can parse it to get the item name
					player.handleItemInteraction(userCmd);
					break;
				
				// The user would like to explore the current room
				case "explore":
					player.exploreCurrentRoom();
					break;
				
				// The user would like to view their inventory
				case "inventory":
					player.viewInventory();
					break;
					
				// Not an item interaction command
				default:
					break;
			}
		}
		
		// Miscellaneous command
		else if (cmdType.equals("misc"))
		{
			View.println("...");
			View.println("");
			Wait.yield(1f);
			
			switch (processedCmd)
			{
				// The user would like to view the current room's details
				case "location":
					player.printCurrentRoomDetails();
					break;
				
				// The user would like to view the game's commands
				case "help":
					View.printCommands();
					break;
					
				// The user would like to quit the game
				case "quit":
					isGameRunning = false;
					break;
				
				// Not a miscellaneous command
				default:
					break;
			}
		}
		
		// Invalid command
		else
			View.println("Command not recognized. Type \"help\" to view the game's commands");
		
		// Continue with the current iteration of the main gameplay loop
		return false;
	}
	
	private static boolean checkIfPlayerIsAlive()
	{
		/* Check player's status */
		if (!player.isAlive())
		{
			View.separate(10);
			View.printColored("!!! GAME OVER !!!", "red", true);
			View.println("You've fallen and can't get up. Your journey ends prematurely...");

			// Break out of the main gameplay loop
			return true;
		}
		
		return false; // Continue with the current iteration of the main gameplay loop
	}
	
	private static boolean checkPlayerState()
	{
		/* Check the player's current state */
		switch (player.getCurrentState())
		{
			// "inCombat" state
			case "inCombat":
				combatLoop(); // Begin combat loop
				return true; // Continue to the next iteration of the main gameplay loop
				
			// "solvingPuzzle" state
			case "solvingPuzzle":
				puzzleSolvingLoop(); // Begin puzzle solving loop
				return true; // Continue to the next iteration of the main gameplay loop
				
			// "exploring" state, which is the default state
			default:
				return false; // Continue with current iteration of the main gameplay loop
		}
	}
	
	private static void resetVariables()
	{
		userCmd = "";
		cmdType = "";
		processedCmd = "";
	}


	//------------------------------------//


	//makes sure user enters a starting class name, and returns starting class object
	private static StartingClass getValidStartingClass(ArrayList<StartingClass> classes){
		while(true){
			String input = basicCmdProcessor.getUserInput().trim();

			for(StartingClass startingClass : classes){
				if (startingClass.getName().equalsIgnoreCase(input)){
					return startingClass;
				}
			}

			View.println("Invalid class. Please enter a valid class name.");
		}

	}
	//method gets user's input, and validates it to ensure user enter's either yes or no.
	private static boolean getYesNoInput(){
		while(true){
			String input = basicCmdProcessor.getUserInput().trim();

			if (input.equalsIgnoreCase("yes"))
			{
				return true;
			}
			if(input.equalsIgnoreCase("no"))
			{
				return false;
			}

			View.println("Invalid input. Please enter Yes or No");
		}
	}

	private static StartingClass pickStartingClass(){
		ArrayList<StartingClass> classes = new ArrayList<>();

		//loads Starting Class data from text file to classes ArrayList
		DataFileExtraction.extractClassDataToController(DataExtractionMethods::extractStartingClassData, startingClassFilePath, classes);

		//for testing
		if (classes.isEmpty()){
			throw new IllegalStateException("No starting class loaded.");
		}

		while (true){

			View.printColored("Choose your starting class:","blue", true);

			for(StartingClass tempClass : classes){
				View.println("- " + tempClass.getName());
			}
			//accepts user's input, returns startingClass they selected
			StartingClass startingClass = getValidStartingClass(classes);
			View.separate(5);

			View.printColored("Class: " + startingClass.getName(),"purple", true);

			//displays class stats
			View.printStartingClassStats(startingClass.getBackground(),
					startingClass.getBaseMaxHP(),
					startingClass.getBaseSTR(),
					startingClass.getBaseDEX(),
					startingClass.getBaseINT(),
					startingClass.getBaseARC(),
					startingClass.getBaseEND(),
					startingClass.getCombatAbility(),
					startingClass.getDefaultEquipment());


			View.println("\nDo you want to select this class? Yes or No");

			//validates user's input, if user entered yes, return startingClass, otherwise, restart loop
			if(getYesNoInput()){
				return startingClass;
			}
		}

	}


	//------------------------------------//


	private static void preGameSetup()
	{
		/* Pre-game setup */
		isGameRunning = true;
		
		// Create core game objects
		
		// Command Processors //
		basicCmdProcessor = new BasicCommandProcessor();
		explorationCmdProcessor = new ExplorationCommandProcessor();		
		combatCmdProcessor = new CombatCommandProcessor();

		// World Map //
		worldMap = new Map();


		View.println("What is your name?");
		String playerName = basicCmdProcessor.getUserInput();

		//Populate Starting classes, and prompt player to choose class
		StartingClass startingClass = pickStartingClass();


		// Player //
		// Start the player in the first room of the map and assign player name, and starting class
		player = new Player(playerName,startingClass,worldMap.getRoomById(1));

		// Print welcome message
		View.separate(25);
		View.printWelcomeMessage();
	}
	
	//------------------------------------//
	
	private static void mainGameplayLoop()
	{
		preGameSetup();
		
		/* Main Gameplay Loop */
		while (isGameRunning)
		{
			resetVariables();

			if (checkPlayerState())
				continue;
			
			// If the player has died, break out of the main gameplay loop
			if (checkIfPlayerIsAlive())
				break;
			
			if (exploration())
				continue;
		}
		
		if (isGameRunning)
			gameOverScreen();
	}
	
	//------------------------------------//
	
	public static void start() 
	{
		mainGameplayLoop();
		quit();
	}

}

