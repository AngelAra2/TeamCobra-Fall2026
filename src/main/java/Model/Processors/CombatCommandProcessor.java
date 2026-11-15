package Model.Processors;

import Model.AbstractClasses.CommandProcessor;
import Model.Combat.CombatSkill;

import java.util.List;

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
			case "fight":
				this.cmdType = "fight";
				this.processedCmd = "fight";
				
				break;
			case "help":
				this.cmdType = "help";
				this.processedCmd = "help";
				
				break;
			default:
				// targetItem
				if (cmd.startsWith("use item"))
				{
					this.cmdType = "use item";
					this.processedCmd = "targetItem";
				}
				
				// Switching monster target
				else if (cmd.startsWith("target"))
				{
					this.cmdType = "target";
					this.processedCmd = "switchMonsterTarget";
				}
				
				else
					this.reset();
				
				break;
		}
		
		// If this command cannot be processed, it's an invalid command
		if (this.cmdType == "")
			this.cmdType = "invalid";
	}

	public void processFightCmd(String input, List<CombatSkill> availableSkills){
		/*
		 * [Process Current Command within fight menu]
		 *
		 * -- Attacks --
		 * a. "basic attack" -> perform a basic Attack on the enemy target
		 *
		 * -- Combat Skill --
		 * a. "[combat skill name]" -> performs a combat skill that character has
		 *
		 * -- Cancel --
		 * c. Return to main combat menu
		 *
		 *
		 */
		this.reset();

		String cmd = input.trim();

		if(cmd.equals("basic attack")){
			this.cmdType = "basicAttack";
			this.processedCmd = "basicAttack";
			return;
		}

		if(cmd.equals("cancel")){
			this.cmdType = "cancel";
			this.processedCmd = "cancel";
			return;
		}

		for(CombatSkill skill : availableSkills){
			if(skill.getName().equalsIgnoreCase(cmd)){
				this.cmdType = "combatSkill";
				this.processedCmd = skill.getName();
				return;
			}
		}

		this.cmdType = "invalid";
	}
}

