package Model.Processors;

import Model.AbstractClasses.CommandProcessor;

public class ExplorationCommandProcessor extends CommandProcessor
{
	/* Constructors */
	public ExplorationCommandProcessor() { super(); }
	
	/* Methods */

	// Custom //	
	public void processCmd()
	{
		/*
		 * [Process Current Command]
		 * 
		 * -- Movement --
		 * a. "n" or "north" -> move north
		 * b. "s" or "south" -> move south
		 * c. "e" or "east" -> move east
		 * d. "w" or "west" -> move west
		 * 
		 * -- Item Interaction --
		 * a. "explore" -> print all item names in the current room to console
		 * b. "pickup" -> picks up an item from the current room and adds it to the player's inventory
		 * c. "inspect" -> inspects an item in the player's inventory and prints its details to console
		 * d. "drop" -> drops an item in the player's inventory and places it in the current room
		 * e. "equip" -> equips a weapon in the player's inventory, increasing the player's Attack stat
		 * f. "unequip" -> unequips the player's currently equipped weapon
		 * g. "heal" -> uses a consumable item in the player's inventory to heal the player
		 * h. "inventory" -> prints all item names in the player's inventory to console
		 * 
		 * -- Monster Interaction --
		 * a. "examine [monster name]" -> prints the monster's details to the console
		 * b. "attack [monster name]" -> attacks a monster in the current room and initiates combat
		 * c. "ignore [monster name]" -> ignores a monster in the current room and continues exploration. The monster will be removed from the current room
		 * 
		 * -- Misc. --
		 * a. "room" or "location" -> print current room name and description to console
		 * b. "help" -> print all commands to console
		 * c. "q" or "quit" -> stop running the game and clean up objects
		 * 
		 * -- Order of Priority --
		 * Movement > Item Interaction > Monster Interaction > Misc.
		 */
		
		String cmd = this.currentCmd.toLowerCase();

		// Detect and process movement commands //
		this.cmdType = "move";
		switch (cmd)
		{
			case "n", "north":
				this.processedCmd = "north";
				break;
			//-----------------------------//
			case "s", "south":
				this.processedCmd = "south";
				break;
			//-----------------------------//
			case "e", "east":
				this.processedCmd = "east";
				break;
			//-----------------------------//
			case "w", "west":
				this.processedCmd = "west";
				break;
			//-----------------------------//
			default:
				this.reset();
				break;
		}
		
		// Return because a valid movement command was detected
		if (this.cmdType.equals("move"))
			return;
		
		// Detect and process item interaction commands //
		if (!this.cmdType.equals("item"))
		{
			this.cmdType = "item";
			
			// Check if the command is one of the item interaction commands
			if (cmd.startsWith("pickup"))
			{
				cmd = "pickup";
			}
			else if (cmd.startsWith("inspect"))
			{
				cmd = "inspect";
			}
			else if (cmd.startsWith("drop"))
			{
				cmd = "drop";
			}
			else if (cmd.startsWith("equip"))
			{
				cmd = "equip";
			}
			else if (cmd.startsWith("unequip"))
			{
				cmd = "unequip";
			}
			else if (cmd.startsWith("heal"))
			{
				cmd = "heal";
			}
			
			switch (cmd)
			{
				// For these commands, the player will take care of the logic for them, 
				// but the CommandProcessor will process them as "targetItem" commands
				case "pickup", "inspect", "drop", "equip", "unequip", "heal":
					this.processedCmd = "targetItem";
					break;
				//-----------------------------//
				case "explore":
					this.processedCmd = "explore";
					break;
				//-----------------------------//
				case "inventory":
					this.processedCmd = "inventory";
					break;
				//-----------------------------//
				default:
					this.reset();
					break;
			}
		}
		
		// Return because a valid item interaction command was detected
		if (this.cmdType.equals("item"))
			return;
		
		// Detect and process monster interaction commands //
		if (!this.cmdType.equals("monster"))
		{
			this.cmdType = "monster";
			
			// Check if the command is one of the monster interaction commands
			if (cmd.startsWith("examine"))
			{
				cmd = "examine";
			}
			else if (cmd.startsWith("attack"))
			{
				cmd = "attack";
			}
			else if (cmd.startsWith("ignore"))
			{
				cmd = "ignore";
			}
			
			switch (cmd)
			{
				// For these commands, the player will take care of the logic for them, 
				// but the CommandProcessor will process them as "targetMonster" commands
				case "examine", "attack", "ignore":
					this.processedCmd = "targetMonster";
					break;
				//-----------------------------//
				default:
					this.reset();
					break;
			}
		}
		
		// Return because a valid monster interaction command was detected
		if (this.cmdType.equals("monster"))
			return;
		
		// Detect and process miscellaneous commands //
		if (!this.cmdType.equals("item"))
		{
			this.cmdType = "misc";
			switch (cmd)
			{
				case "room", "location":
					this.processedCmd = "location";
					break;
				//-----------------------------//
				case "help":
					this.processedCmd = "help";
					break;
				//-----------------------------//
				case "q", "quit":
					this.processedCmd = "quit";
					break;
				//-----------------------------//
				default:
					this.reset();
					break;
			}
		}
		
		// If this command cannot be processed, it's an invalid command
		if (this.cmdType == "")
			this.cmdType = "invalid";
	}
}

