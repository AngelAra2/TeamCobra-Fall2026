package Controller;

import java.util.*;

import Model.AbstractClasses.Character;
import Model.Combat.*;
import Model.Data.DataExtractionMethods;
import Model.Data.DataFileExtraction;
import Model.Processors.*;
import Model.Util.Wait;
import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;
import Model.Combat.*;
import Model.Combat.Battlefield;
import Model.Combat.Monster;
import Model.Combat.Player;
import Model.Exploration.Map;
import Model.Exploration.Puzzle;
import Model.Exploration.Room;
import Model.Exploration.Merchant;
import View.BattleView;
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
    private static Merchant activeMerchant;

	// Command Processors //
	private static BasicCommandProcessor basicCmdProcessor;
	private static ExplorationCommandProcessor explorationCmdProcessor;
	private static CombatCommandProcessor combatCmdProcessor;

	private static String startingClassFilePath = "/gameObjects/StartingClasses";
	//------------------------------------//
	
	/* Helper Methods */
	//Dylan Morrison
    private static void puzzleSolvingLoop()
    {
        // Get the puzzle in the player's current room.
        Puzzle puzzle = player.getCurrentPuzzle();
        if (puzzle == null)
        {
            View.printNoPuzzleInRoom();
            player.resetToDefaultState();
            return;
        }

        // Each Solve command allows two incorrect guesses and up to two hints.
        int incorrectGuesses = 0;
        int hintsGiven = 0;
        View.printPuzzlePrompt(puzzle);

        // Keep accepting input until the puzzle is solved or both guesses are used.
        while (incorrectGuesses < 2)
        {
            String answer = basicCmdProcessor.getUserInput();
            String normalizedInput = answer.trim().toLowerCase().replaceAll("\\s+", " ");

            // Hint requests do not count as guesses.
            if (normalizedInput.equals("hint")
                    || normalizedInput.equals("request hint")
                    || normalizedInput.equals("ask for hint"))
            {
                if (hintsGiven < 2)
                    View.printPuzzleHint(puzzle.getHint(hintsGiven++));
                else
                    View.printNoMorePuzzleHints();

                // Prompt again without ending the current solve attempt.
                View.printPuzzleInputPrompt();
                continue;
            }

            // A correct answer marks the puzzle solved and ends this loop.
            if (player.attemptToSolvePuzzle(answer))
            {
                View.printPuzzleSolved();
                puzzle.giveRewards(player);
                break;
            }

            // An incorrect answer uses one of this attempt's two guesses.
            incorrectGuesses++;
            if (incorrectGuesses == 1)
            {
                View.printIncorrectFirstGuess();
                View.printPuzzleInputPrompt();
            }
            else
                View.printIncorrectSecondGuess();
        }

        // Return the player to normal exploration after the attempt ends.
        player.resetToDefaultState();
    }

	//Danh and Angel
	private static void combatLoop()
	{
		/* In combat state, the player can:
		 * - attack
		 * - heal [item name]
		 */
		
		Wait.yield(1f);

		/* Init variables */
		String userCmd = "";
		String cmdType = "";
		
		@SuppressWarnings("unused")
		String processedCmd = "";
		
		/* Print our pre-battle messages */

		// Print out messages about the encounter
		BattleView.printMonsterNames(Battlefield.getMonsters());

		Wait.yield(1.25f);

		View.println("");
		View.printColored("!!! BATTLE START !!!", "red", true);

		// Begin combat loop
		outerLoop:
		while (isGameRunning && Battlefield.canBattleContinue())
		{
			Wait.yield(1f);

			/* Player's party's turn */
			List<Companion> activeCompanions = player.getPlayerParty().getActiveCompanions();
			int totalPartyMembers = 1 + activeCompanions.size(); // 1 for the player + number of active companions
			
			boolean hasPartyLeveledUp = false; // Keeps track of whether any party member has leveled up during this combat round
			
			// Player's turn first //
			// Do a for loop to iterate through the player's party members, starting with the player and then each active companion
			for (int i = 0; i < totalPartyMembers; i++)
			{
				// If battle cannot continue, break out of the combat loop
				if (!Battlefield.canBattleContinue())
					continue outerLoop;
				
				hasPartyLeveledUp = false;
				View.separate(3);

				Character combatant;
				// If i == 0, it's the player's turn
				if (i == 0)
					combatant = player;
				
				// Otherwise, it's an active companion's turn
				else
					combatant = activeCompanions.get(i - 1);
				
				// If the combatant is alive, let them take their turn
				if (combatant.isAlive())
				{
					// Print battle statuses at the beginning of each iteration of the combat loop
					Battlefield.displayBattleStatuses();
					
					// Print out the combatant's turn info
					BattleView.printCombatantTurnInfo(combatant);
				}
				
				// Skip turn if not alive
				else
					continue;
				
				// Get and process user input
				userCmd = combatCmdProcessor.getUserInput();
				combatCmdProcessor.setCurrentCommand(userCmd);
				
				// Get results
				cmdType = combatCmdProcessor.getCommandType();
				processedCmd = combatCmdProcessor.getProcessedCommand();
				
				/* Update game state based on combat command */
				switch (cmdType)
				{
					case "fight":

						View.separate(3);
						//prints available attacks / combat skills to player.
						BattleView.printFightMenu(combatant);

						//store user's fight command and its commandType
						String fightCmd = "";
						String fightCmdType = "";

						//validates users command
						do{
							//processes users input
							fightCmd = combatCmdProcessor.getUserInput();
							combatCmdProcessor.processFightCmd(fightCmd, combatant.getSkills());

							//retrieves command type from processor
							//EX: basicAttack, combatSkill, cancel, or invalid
							fightCmdType = combatCmdProcessor.getCommandType();

							//if cmd does not match any valid cmd, disp error.
							if(combatCmdProcessor.getCommandType().equals("invalid")){
								View.println("Invalid fight command.");
							}
						}
						while(combatCmdProcessor.getCommandType().equals("invalid"));

						//perform basic attack
						if(fightCmdType.equals("basicAttack"))
						{
							View.separate(3);
							View.println("Launching attack...");
							Wait.yield(1f);
							Battlefield.performAttack(combatant, Battlefield.getTargetMonster());
							break;
						}

						//validate and perform combatSkill
						else if(fightCmdType.equals("combatSkill"))
						{
							//retrieve skill name
							processedCmd = combatCmdProcessor.getProcessedCommand(); //receives combat skill name

							//find object for skill
							CombatSkill skill = combatant.getSkillByName(processedCmd); //receive skill

							View.separate(3);
							View.println("Launching skill...");
							Wait.yield(1f);
							//attempts to execute skill against target.
							//battlefield handles skill validation, mana requirements, targeting, damage, and healing
							Battlefield.performSkillAttack(combatant, skill, Battlefield.getTargetMonster(), player.getInventory());
						}

						//return to main combat menu
						else if (fightCmdType.equals("cancel"))
						{
							cmdType = "cancel";
							break;
						}

						Wait.yield(1f);

						break;

//					// The user wants to attack their currently targeted monster
//					case "attack":
//						View.separate(3);
//						View.println("Launching attack...");
//						Wait.yield(1f);
//
//						Battlefield.performAttack(combatant, Battlefield.getTargetMonster());
//
//						break;
//
					// The user wants to switch their target to a different monster
					case "target":
						View.separate(3);
						View.println("Switching targets...");
						Wait.yield(1f);
						
						// Pass in userCmd so that the player can parse it to get the target index
						player.switchMonsterTarget(userCmd);
						
						break;

					// The user wants to use a healing item from their inventory
					case "use item":
						View.separate(3);
						View.println("Attempting to heal...");
						Wait.yield(1f);

						// Pass in userCmd so that the player can parse it to get the item name
						player.handleItemInteraction(userCmd);

						break;
					// The user wants to use a healing item from their inventory
//					case "heal":
//						View.separate(3);
//						View.println("Attempting to heal...");
//						Wait.yield(1f);
//						
//						// Pass in userCmd so that the player can parse it to get the item name
//						player.handleItemInteraction(userCmd);
//					
//						break;

					// The user wants to view the game's commands
					case "help":
						View.separate(3);
						View.printCommands();
						
						break;
					
					// Invalid combat command
					default:
						View.separate(3);
						View.println("Command not recognized. Type \"help\" to view the game's commands");
						
						// Skip the monsters' turn and continue to the next iteration of the combat loop
						break;
				}
				
				// Skip the monsters' turns if specific commands were used
				switch (cmdType)
				{
					case "help", "target", "", "invalid", "cancel":
						i--; // Decrement i to keep because it's still the same combatant's turn
						continue;
					default:
						break;
				}
				
				Wait.yield(1f);

				// After each combatant's turn, check if any monster has been defeated. If so, remove it from the battlefield and current room
				// and give rewards
				ArrayList<Monster> monstersList = Battlefield.getMonsters();
				for (int j = 0; j < monstersList.size(); j++)
				{
					Monster monster = monstersList.get(j);
					
					// Monster defeated; remove it from the battlefield and current room
					if (!monster.isAlive())
					{
						View.separate(3);
						
						Battlefield.removeMonster(monster);
						
						Room currentRoom = player.getCurrentRoom();
						if (currentRoom != null)
							currentRoom.removeMonster(monster.getName(), monster);

						BattleView.printCombatantDeath(monster);
						
						// Reset monster target to the first monster in the list if there are still monsters remaining
						if (Battlefield.getMonsters().size() > 0)
							Battlefield.switchMonsterTarget(0);
						
						// Reward player with gold and experience for defeating the monster
						int goldReward = monster.getGoldReward();
						int xpReward = monster.getXPReward();
						player.addGold(goldReward);
						hasPartyLeveledUp = player.gainXP(xpReward);
							
						// Reduce i by 1 to keep loop in sync
						j--;
						
						Wait.yield(1f);
						// If player and co. have leveled up, print out a message			
						if (hasPartyLeveledUp)
							BattleView.printPartyLevelUp(player.getPlayerParty());
					}
				}

			}

			/* Monsters' turn */
			ArrayList<Monster> monstersList = Battlefield.getMonsters();
			List<Character> deadPartyMembers = new ArrayList<>(); 
			for (int i = 0; i < monstersList.size(); i++)
			{
				Monster monster = monstersList.get(i);

				View.separate(3);
				
				// If the monster is alive, let it attack the player
				if (monster.isAlive())
				{
					View.println(monster.getName() + " attacks...");
					
					Wait.yield(1f);
					
					// Monster attacks //
					// Randomly choose a target from the player's party for the monster to attack
					Combatant target = Battlefield.getRandomTargetFromPlayerParty(monster);
					
					if (target != null)
						Battlefield.performAttack(monster, target);
				}
				
				// After each monster's turn, check if anyone (including the player) is defeated
				// Do a for loop to iterate through the player's party members, starting with the player and then each active companion
				for (int j = 0; j < totalPartyMembers; j++)
				{
					Character combatant;
					
					// Get the player if j == 0
					if (j == 0)
						combatant = player;
					
					// Otherwise, it's an active companion's turn
					else
						combatant = activeCompanions.get(j - 1);
					
					// If the character is defeated, print out a message
					if (!combatant.isAlive() && !deadPartyMembers.contains(combatant))
					{
						// Prevents them from being printed more than once
						deadPartyMembers.add(combatant);
						
						View.separate(3);
						BattleView.printCombatantDeath(combatant);
						
						Wait.yield(1f);
					}
				}
			}
			
			// Clear out dead party members list
			deadPartyMembers.clear();

			//update party buffs after the combat round.
			player.decreaseBuffDuration();

			for (Companion companion : player.getPlayerParty().getActiveCompanions()){
				companion.decreaseBuffDuration();
			}
		}
		
		Wait.yield(1f);
		View.separate(10);
		
		// Combat loop exited; clear battlefield
		Battlefield.clearBattlefield();
		
		View.printColored("!!! BATTLE OVER !!!", "yellow", true);
		
		// Make the player exit combat state
		player.resetToDefaultState();
		
		// Check whether the player's party has survived the battle
		if (!player.getPlayerParty().isPartyDefeated())
			View.printColored("Victory! All monsters downed", "green", true);
	}
	
	//------------------------------------//
	
	/* Gameplay Loop Helper Methods */
	//Danh
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

	//Danh
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

	//Angel
	private static boolean handleCompanionCommand(String userCmd){
		String input = userCmd.trim();

		for (Companion c : player.getPlayerParty().getActiveCompanions()){
			String name = c.getName();

			if(!input.toLowerCase().startsWith(name.toLowerCase() + " ")){
				continue;
			}

			//remove companion name from command
			String remaining = input.substring(name.length()).trim();

			//splits command and possible item name
			String[] parts = remaining.split("\\s+", 2);
			String action = parts[0].toLowerCase();

			//checks if parts contains at least 2 elements, if so, trim, otherwise, set item name to blank: ""
			String itemName = parts.length > 1 ? parts[1].trim() : "";

			switch (action){
				case "inventory":
					//view companion inventory
					c.viewInventory();
					return true;

				case "equip":
				case "unequip":
					//equips or unequips item
					c.handleEquipmentCommand(action, itemName);
					return true;

				case "status":
					View.printCompanionStatus(c);
					return true;
				default:
					View.println("Invalid companion command.");
					return true;
			}
		}
		return false;

	}
	//Danh,Angel,Dylan
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

		if(handleCompanionCommand(userCmd)){
			return true;
		}
		explorationCmdProcessor.setCurrentCommand(userCmd);
		
		// Get results
		cmdType = explorationCmdProcessor.getCommandType();
		processedCmd = explorationCmdProcessor.getProcessedCommand();
		
		/* Update game state based on exploration command */
		View.separate(10);
		
		// Movement command
		if (cmdType.equalsIgnoreCase("move"))
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

				// Print error message if the player cannot move in that direction
				if (!movementSuccessful)
					View.printColored("Can't go this way...", "red", true);
			}
		}
		
		// NPC interaction command
		else if (cmdType.equalsIgnoreCase("npc"))
		{
			// The user would like to recruit or talk to a companion in the current room
			if (processedCmd.equalsIgnoreCase("targetNPC"))
			{
				View.println("...");
				Wait.yield(1.25f);
				
				View.println("");
				
				// Let the player handle the logic for this command
				// Pass in userCmd so that the player can parse it to get the companion name
				player.handleNPCInteraction(userCmd);
			}
		}
        // Merchant interaction command
        else if (cmdType.equalsIgnoreCase("merchant"))
        {
            handleMerchantCommand();
        }
		// Monster interaction command
		else if (cmdType.equalsIgnoreCase("monster"))
		{
			// The user would like to examine/attack/ignore a monster in the current room
			if (processedCmd.equalsIgnoreCase("targetMonster"))
			{
				View.println("...");
				Wait.yield(1.25f);
				
				View.println("");
				
				// Let the player handle the logic for this command
				// Pass in userCmd so that the player can parse it to get the monster name
				player.handleMonsterInteraction(userCmd);
			}
		}
		
		// Item interaction command
		else if (cmdType.equalsIgnoreCase("item"))
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
		else if (cmdType.equalsIgnoreCase("misc"))
		{
			View.println("...");
			View.println("");
			Wait.yield(1f);
			
			switch (processedCmd)
			{
				// The user would like to view the current room's details
				case "location":
					player.viewRoomInfo();
					break;

				//displays player's status
				case "status":
					View.printPlayerStatus(player);
					break;

				// The user would like to view the party's status
				case "party status":
					View.printPlayerStatus(player);

					for (Companion companion : player.getPlayerParty().getActiveCompanions()){
						View.printCompanionStatus(companion);
					}
					break;

				// The user would like to view the game's commands
                case "solve":
                    Puzzle puzzle = player.getCurrentPuzzle();

                    if (puzzle == null) {
                        View.printNoPuzzleInRoom();
                    } else if (puzzle.getIsSolved()) { // Check if the puzzle has already been solved and outputs respectively.
                        View.println("You have already solved this puzzle.");
                    } else {
                        player.setCurrentState("solvingPuzzle");
                    }
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

	//Danh
	private static boolean checkIfPlayerIsAlive()
	{
		/* Check player's status */
		if (!player.isAlive())
		{
			View.separate(10);
			View.printColored("!!! GAME OVER !!!", "red", true);
			View.println("Your party has fallen. Your journey ends prematurely...");

			// Break out of the main gameplay loop
			return true;
		}
		
		return false; // Continue with the current iteration of the main gameplay loop
	}

	//Danh
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

	//Danh
	private static void resetVariables()
	{
		userCmd = "";
		cmdType = "";
		processedCmd = "";
	}


	//------------------------------------//


	//makes sure user enters a starting class name, and returns starting class object
	//Angel
	private static StartingClass getValidStartingClass(ArrayList<StartingClass> classes){

		//gets user input, if class exists, return the starting class, if it doesn't exist
		//keep prompting user to enter a valid starting class.
		while(true){
			String input = basicCmdProcessor.getUserInput();

			//looks for a starting class that matches user's input, if found, return starting class
			for(StartingClass startingClass : classes){
				if (startingClass.getName().equalsIgnoreCase(input)){
					return startingClass;
				}
			}

			View.println("Invalid class. Please enter a valid class name.");
		}

	}
	//method gets user's input, and validates it to ensure user enter's either yes or no.
	//Angel
	private static boolean getYesNoInput(){

		//validates user's input, user cannot exit loop unless they enter yes or no.
		while(true){
			String input = basicCmdProcessor.getUserInput();

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

	//Angel
	private static StartingClass pickStartingClass(){
		ArrayList<StartingClass> classes = new ArrayList<>();

		//loads Starting Class data from text file to classes ArrayList
		//DataFileExtraction.extractClassDataToController(DataExtractionMethods::extractStartingClassData, startingClassFilePath, classes, worldMap);

		DataFileExtraction.extractClassDataToController((scanner, classList) -> DataExtractionMethods.extractStartingClassData
						(scanner, classList, worldMap), startingClassFilePath, classes);
		//for testing
		if (classes.isEmpty()){
			throw new IllegalStateException("No starting class loaded.");
		}

		//displays available classes, and asks user to choose a class, after choosing a class, we display the
		//character stats, and ask user if they want to move forward with this class, entering yes returns the starting class
		//entering no restarts the loop so the user can select another starting class.
		while (true){

			View.printColored("Choose your starting class:","blue", true);

			//display class names
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
					startingClass.getCombatSkill(),
					startingClass.getDefaultEquipment());


			View.println("\nDo you want to select this class? Yes or No");

			//validates user's input, if user entered yes, return startingClass, otherwise, restart loop
			if(getYesNoInput()){
				return startingClass;
			}
		}

	}

    // Routes a parsed merchant command to the selected merchant.
    // A merchant must be selected with "shop" before buy or sell can be used.
	//Dylan
    private static void handleMerchantCommand()
    {
        // Split the command into its action and optional target.
        // For example, "shop Trader Joe" becomes ["shop", "Trader Joe"].
        String[] parts = userCmd.trim().split("\\s+", 2);
        Room currentRoom = player.getCurrentRoom();

        switch (processedCmd)
        {
            case "shop":
            {
                Merchant merchant = null;

                // If a merchant name was provided, look for that NPC in this room
                // and select it only if it is actually a merchant.
                if (parts.length > 1)
                {
                    Object npc = currentRoom.getNPCByName(parts[1]);
                    if (npc instanceof Merchant)
                        merchant = (Merchant) npc;
                }
                // If no name was provided, select the merchant automatically
                // only when there is exactly one merchant in the room.
                else if (currentRoom.getMerchants().size() == 1)
                    merchant = currentRoom.getMerchants().get(0);

                // Do not open a trading session if the name was invalid
                // or the room has multiple merchants and none was specified.
                if (merchant == null)
                {
                    View.println("There is no matching merchant here.");
                    return;
                }

                // Remember this merchant for later buy, sell, and cancel commands.
                activeMerchant = merchant;
                View.println("Wares offered by " + activeMerchant.getName() + ":");
                printMerchantStock(activeMerchant);
                break;
            }

            // Buy and sell share session/item validation; dispatch to the matching transaction below.
            case "buy":
            case "sell":
            {
                if (activeMerchant == null
                        || !player.getCurrentRoom().getMerchants().contains(activeMerchant))
                {
                    activeMerchant = null;
                    View.println("Use \"shop\" with a merchant here before trading.");
                    break;
                }

                if (parts.length < 2 || parts[1].isBlank())
                {
                    View.println("Invalid transaction. The item is not available.");
                    break;
                }

                if (processedCmd.equals("buy"))
                    buyMerchantItem(parts[1].trim());
                else
                    sellItemToMerchant(parts[1].trim());

                break;
            }
            case "cancel":
            {
                if (activeMerchant != null)
                {
                    activeMerchant = null;
                    View.println("That's a pity.");
                }
                else
                {
                    View.println("You are not currently shopping.");
                }
                break;
            }
        }
    }

    // Displays each stocked item with its description, quantity, and per-item price.
	//Dylan
    private static void printMerchantStock(Merchant merchant)
    {
        HashMap<String, ArrayList<Item>> stock = merchant.getStock();

        if (stock.isEmpty())
        {
            View.println("This merchant has no wares right now.");
            return;
        }

        for (ArrayList<Item> items : stock.values())
        {
            // Each stock list contains copies of the same item, so use its first copy
            // for the shared name, description, and price.
            Item item = items.get(0);

            View.println(item.getItemName() + " | Stock: " + items.size()
                    + " | Price: " + item.getValue() + " Gold");
            View.println(item.getItemDescription());
            View.println("");
        }
    }
    // Finds one stocked item by its full name, ignoring letter case.
	//Dylan
	private static Item findMerchantItem(String itemName)

    {
        ArrayList<Item> items = activeMerchant.getStock()
                .get(itemName.toLowerCase());

        return items == null || items.isEmpty() ? null : items.get(0);
    }

    // Buys one item, changing stock, inventory, and gold only after the checks pass.
	//Dylan
    private static void buyMerchantItem(String itemName)
    {
        Item item = findMerchantItem(itemName);

        if (item == null)
        {
            View.println("Invalid transaction. The item is not available.");
            return;
        }

        int price = item.getValue();
        if (!player.hasSufficientGold(price))
        {
            View.println("You don’t have enough money, sell some items to make it back.");
            return;
        }

        if (!player.getInventory().canCarry(item, player.getCurrentLevel()))
        {
            View.println("You don’t have enough space in your inventory, drop or sell items to make room.");
            return;
        }

        // Merchant.sellItem adds the item to the player's inventory and removes it from stock.
        if (!activeMerchant.sellItem(item, player))
        {
            View.println("Invalid transaction. The item is not available.");
            return;
        }

        player.spendGold(price);
        View.println("Bought " + item.getItemName() + " for " + price + " Gold.");
    }

    // Sells one un-equipped inventory item after the player confirms the sale.
	//Dylan
    private static void sellItemToMerchant(String itemName)
    {
        Item item = player.getItemFromInventoryByName(itemName);

        if (item == null)
        {
            View.println("Invalid transaction. The item is not available.");
            return;
        }

        int value = item.getValue();
        View.println("Are you sure you want to sell " + item.getItemName()
                + " for " + value + " Gold? (Yes/No)");

        if (!getYesNoInput())
        {
            View.println("Sale cancelled.");
            return;
        }

        // Inventory refuses to remove equipped items; do not credit gold unless removal succeeds.
        if (!player.getInventory().removeItem(item))
        {
            View.println("Unequip the item before selling it.");
            return;
        }

        activeMerchant.addStock(item);
        player.addGold(value);
        View.println("Sold " + item.getItemName() + " for " + value + " Gold.");
    }


	//------------------------------------//


	//Danh, Angel, Dylan
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

		//Populate Starting classes from text file, and prompt player to choose class
		StartingClass startingClass = pickStartingClass();


		// Player //
		// Start the player in the first room of the map and assign player name, and starting class
		player = new Player(playerName,startingClass,worldMap.getRoomById(1));
        player.getCurrentRoom().generateItems(worldMap);
		// Print welcome message
		View.separate(25);
		View.printWelcomeMessage();
	}
	
	//------------------------------------//

	//Danh
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

	//Danh
	public static void start() 
	{
		mainGameplayLoop();
		quit();
	}

}
