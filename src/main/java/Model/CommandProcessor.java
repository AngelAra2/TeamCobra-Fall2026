package Model;
import java.util.Scanner;

public class CommandProcessor 
{
	/* Data Fields */
	private String currentCmd;
	private String cmdType;
	private String processedCmd;
	private Scanner inputSC;

	/* Constructors */
	public CommandProcessor()
	{
		this.currentCmd = "";
		this.inputSC = new Scanner(System.in);
	}
	
	/* Methods */
	
	// Private //
	private void reset()
	{
		this.cmdType = "";
		this.processedCmd = "";
	}
	
	private void processCmd()
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
		 * e. "inventory" -> prints all item names in the player's inventory to console
		 * 
		 * -- Misc. --
		 * a. "room" or "location" -> print current room name and description to console
		 * b. "help" -> print all commands to console
		 * c. "q" or "quit" -> stop running the game and clean up objects
		 * 
		 * -- Order of Priority --
		 * Movement > Item Interaction > Misc.
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
		
		// Return
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
			
			switch (cmd)
			{
				// For these commands, the player will take care of the logic for them, 
				// but the CommandProcessor will process them as "targetItem" commands
				case "pickup", "inspect", "drop":
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
		
		// Return
		if (this.cmdType.equals("item"))
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
	
	// Getters //
	public String getCommandType()
	{
		return this.cmdType;
	}
	
	public String getProcessedCommand()
	{
		return this.processedCmd;
	}
	
	// Setters //
	public void setCurrentCommand(String newCmd)
	{
		this.currentCmd = newCmd;
		this.processCmd();
	}
	
	// Custom //
	public String getUserInput()
	{
		// Yields current thread until the user types something in and presses [Enter]/[Return]
		return inputSC.nextLine().trim();
	}
	
	public void destroy()
	{
		this.inputSC.close();
	}
}

