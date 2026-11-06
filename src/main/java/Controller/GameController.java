package Controller;

import Model.CommandProcessor;
import Model.Map;
import Model.Player;
import Model.Puzzle;
import Model.Room;
import View.View;

public class GameController 
{
	
	/* Core game objects */
	private static CommandProcessor cmdProcessor;
	private static Map worldMap;
	private static Player player;

	//------------------------------------//
	
	public static void printStartingIterationMessages()
	{
		/* Used to print messages at the start of each iteration of the main gameplay loop */
		
		View.separate(3);
		
		// Check to see if the current room has a puzzle that is unsolved
		// AND if that puzzle has any remaining attempts left
		boolean isSolvingPuzzle = isSolvingPuzzle();
		if (isSolvingPuzzle)
		{
			Puzzle currentPuzzle = player.getCurrentPuzzle();
			System.out.print(currentPuzzle);
		}
		else
		{
			View.print("Enter a command: ");
		}
	}
	
	//------------------------------------//
	
	/* Helper methods */
	public static boolean isSolvingPuzzle()
	{
		/* Used to check if the player is currently solving a puzzle in their current room */
		
		Puzzle currentPuzzle = player.getCurrentPuzzle();
		return (currentPuzzle != null 
				&& !currentPuzzle.getIsSolved() 
				&& currentPuzzle.getRemainingAttempts() > 0);
	}
	
	//------------------------------------//
	
	public static void start() 
	{
		/* Create game variables */
		boolean isGameRunning = true;
		
		/* Create core game objects */
		cmdProcessor = new CommandProcessor();
		worldMap = new Map();
		
		// Start the player in the first room of the map
		player = new Player(worldMap.getRoomById(1));
		
		/* Pre-game setup */
		
		// Print instructions
		View.println("");
		View.printWelcomeMessage();
		
		/* Main gameplay loop */
		while (isGameRunning)
		{
			/* Get and process user input */
			// Prompt user to type a command
			printStartingIterationMessages();
			
			String userCmd = cmdProcessor.getUserInput();
			
			/* Player is solving puzzle */
			if (isSolvingPuzzle())
			{
				// Have the player's input be used as an answer to the puzzle
				boolean isCorrect = player.attemptToSolvePuzzle(userCmd);
				
				View.separate(3);
				
				// Print out status messages letting the player know
				// if they solved the puzzle or how many attempts they
				// have left on an incorrect answer
				if (isCorrect)
				{
					View.println("You solved the puzzle correctly!");
				}
				else
				{
					Puzzle currentPuzzle = player.getCurrentPuzzle();
					int remainingAttempts = currentPuzzle.getRemainingAttempts();
					
					if (remainingAttempts > 0)
						View.println("The answer you provided is incorrect. "
								+ "You have " + remainingAttempts + " attempt(s) remaining. "
								+ "Try again.");
					else
						View.println("You failed to solve the puzzle.");
				}
				
				// Don't do anything else
				continue;
			}
			
			// Process user input
			cmdProcessor.setCurrentCommand(userCmd);
			
			// Get results of processed command
			String cmdType = cmdProcessor.getCommandType();
			String processedCmd = cmdProcessor.getProcessedCommand();
			
			View.separate(10);
			
			/* Update game state based on command */
			// Movement commands take first priority
			if (cmdType.equals("move"))
			{
				View.println("Attempting to move " + processedCmd + "...");
				View.println("");
				
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
						View.println("");
						View.println("!!! PUZZLE ENCOUNTERED !!!");
						
						// Reset the puzzle (reset its remaining attempts)
						getPuzzleInRoom.resetPuzzle();
						continue;
					}
					
					// Print error message if the player cannot move in that direction
					if (!movementSuccessful)
						View.println("You can't move in this direction.");
				}
			}
			
			// Item interaction commands have second priority
			else if (cmdType.equals("item"))
			{
				// The user would like to explore the current room
				if (processedCmd.equals("explore"))
				{
					View.println("Exploring the current room...");
					View.println("");
					
					player.exploreCurrentRoom();
				}
				
				// The user would like to view their inventory
				else if (processedCmd.equals("inventory"))
				{
					player.viewInventory();
				}
				
				// The user would like to pickup/inspect/drop an item, 
				// either in the current room or in their inventory
				else if (processedCmd.equals("targetItem"))
				{
					// Let the player handle the logic for this command
					// Pass in userCmd so that the player can parse it to get the item name
					player.handleItemInteraction(userCmd);
				}
			}
			
			// Miscellaneous commands have the lowest priority
			else if (cmdType.equals("misc"))
			{
				// The user would like to view the current room's details
				if (processedCmd.equals("location"))
					player.printCurrentRoomDetails();
				
				// The user would like to view the game's commands
				else if (processedCmd.equals("help"))
					View.printCommands();
				
				// The user would like to quit the game
				else if (processedCmd.equals("quit"))
					isGameRunning = false;
			}
			
			// Invalid command detected; don't do anything
			else
			{
				View.println("Invalid command. Type \"help\" to view the game's commands.");
			}
		}
		
		/* Clean up class objects */
		cmdProcessor.destroy();
		
		// Print quit message
		View.println("Goodbye! Thanks for playing.");
	}

}

