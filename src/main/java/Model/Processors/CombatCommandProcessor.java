package Model.Processors;

import Model.AbstractClasses.CommandProcessor;

public class CombatCommandProcessor extends CommandProcessor
{
	/* Constructors */
	public CombatCommandProcessor() { super(); }
	
	/* Methods */
	// Custom //
	public void processCmd()
	{
		/*
		 * [Process Current Command]
		 * 
		 * -- Attacks --
		 * a. "attack" -> perform a Regular Attack on the enemy target
		 * 
		 * -- Heal --
		 * a. "heal [item name]" -> use a consumable item from the player's inventory to heal the player
		 * 
		 * -- Order of Priority --
		 * Attacks > Heal
		 */
		
		String cmd = this.currentCmd.toLowerCase();

		// Detect the type of command the user has entered
		switch(cmd)
		{
			case "attack":
				this.cmdType = "attack";
				this.processedCmd = "attack";
				
				break;
			case "help":
				this.cmdType = "help";
				this.processedCmd = "help";
				
				break;
			default:
				// targetItem
				if (cmd.startsWith("heal"))
				{
					this.cmdType = "heal";
					this.processedCmd = "targetItem";
				}
				else
					this.reset();
				
				break;
		}
		
		// If this command cannot be processed, it's an invalid command
		if (this.cmdType == "")
			this.cmdType = "invalid";
	}
}

