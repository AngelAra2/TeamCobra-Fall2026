package View;

import java.util.*;

import Model.Combat.*;
import Model.Util.Wait;
import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Character;
//Danh, Angel
public class BattleView 
{
	/* Data Fields */
	private static final int PROGRESS_BAR_LENGTH = 20;
	private static final float CRIT_HP_THRESHOLD = 0.4f; // Threshold for low HP warning
	
	public static void printCombatantStatus(Combatant combatant, boolean isTargeted) 
	{
		/* Prints out the status of a combatant to the console */
		
		// Print out name and current HP of the combatant
		String combatantName = "[" + combatant.getName() + "]";
		float currentHP = (float) combatant.getCurrentHP();
		float maxHP = (float) combatant.getMaxHP();
		float hpPercentage = currentHP/maxHP;
		boolean isBoss = false;
		boolean criticalHP = hpPercentage <= CRIT_HP_THRESHOLD;
		//used to print physical defense and magical defense for combatant
		float physDef = (float) combatant.getPhysicalDEF();
		float magDef = (float) combatant.getMagicDEF();

		
		// Check if the combatant is a Monster and if it is a boss
		if (combatant instanceof Monster)
		{
			Monster monster = (Monster) combatant;
			isBoss = monster.isBossMonster();
		}
		
		View.print(">>> ");
		
		// Tag this combatant as a boss if it is one
		if (isBoss)
			View.printColored("[BOSS] ", "red", false);
		
		// If the combatant is currently being targeted by the player's party, tag them as targetted
		if (isTargeted)
			View.printColored("--> " + combatantName + " <--", "cyan", false);
		else
			View.print(combatantName);

		View.print(" | ");
		
		String hpText = "HP " + (int) currentHP + "/" + (int) maxHP;
		String hpColor = criticalHP ? "red" : "green";

		String pDefText = "Phys Def: " + (int) physDef;
		String mDefText = "Mag Def: " + (int) magDef;
		// If critical HP, print HP text and progress bar in red; print in green otherwise
		View.printColored(hpText, hpColor, false);
		View.print(" ");

		if(combatant instanceof Character){
			Character character = (Character) combatant;
			float totalMana =  character.calculateMaxMana();
			float manaUsed = character.getCurrentMana();

			String tManaTxt = "/" + (int) totalMana;
			String uManaTxt = "Mana: " + (int) manaUsed;
			View.printProgressBar(hpPercentage, PROGRESS_BAR_LENGTH, hpColor, false);

			View.printColored(" | " + pDefText + " | " + mDefText + " | " + uManaTxt + tManaTxt, "cyan", true);
			View.println("");

			return;
		}
		View.printProgressBar(hpPercentage, PROGRESS_BAR_LENGTH, hpColor, false);

		View.printColored(" | " + pDefText + " | " + mDefText, "cyan", true);
		View.println("");
	}
	
	public static void printDamageDealt(Combatant attacker, Combatant receiver, int damageDealt, boolean isCritical) 
	{
		/* Prints out the amount of damage dealt by an attacker to a receiver */
		String textColor = "cyan";
		String attackerName = "[" + attacker.getName() + "]";
		String receiverName = "[" + receiver.getName() + "]";
		
		// If the attack is a monster, textColor is red
		if (attacker instanceof Monster)
			textColor = "red";
		
		// If the attack is critical, add text to indiciate such
		if (isCritical)
			View.printColored("Critical Hit! ", "yellow", false);
		
		View.print(">>> ");
		View.printColored(attackerName + " | " + damageDealt + " damage --> " + receiverName, textColor, false);
		View.println("");
	}
	
	public static void printSwitchedMonsterTarget(Monster monster) 
	{
		/* Prints out a message indicating that the player has switched their target to a new monster */
		
		// If monster is null, print message indicating that switching targets failed
		if (monster == null)
		{
			View.println("");
			View.printColored("Couldn't switch targets\n", "red", false);
		}
		
		else
		{
			String monsterName = monster.getName();
			
			View.println("");
			View.print("Monster target switched to ");
			View.printColored(monsterName, "cyan", false);
			View.println("");
		}
	}
	
	public static void printMonsterNames(List<Monster> monsters) 
	{
		/* Prints out the names of all monsters in the list */
		View.separate(3);
		View.printColored("You've entered combat!", "red", true);
		View.println("");
		
		View.println("Monsters in this encounter:");
		for (Monster monster: monsters) 
		{
			String monsterName = monster.getName();
			View.println("- " + monsterName);
		}
	}

	public static void printFightMenu(Character character) {
		View.printColored("---< FIGHT MENU >--- ", "yellow", true);
		View.printColored("\n- Basic Attack", "blue", true);

		View.println(" ");

		View.println("- Combat Skills:\n");
		//displays available skills for player
		for (CombatSkill skills : character.getSkills()) {
			View.printColored("- " + skills.getName(), "blue", true);
		}

		View.println("\nChoose basic attack or skill, or cancel to return.");
	}
	public static void printCombatantDeath(Combatant combatant) 
	{
		/* Prints out a message indicating that a combatant has died */
		String combatantName = "[" + combatant.getName() + "]";
		String textColor = "red";
		boolean isMonster = combatant instanceof Monster;
		
		// Adjust textColor based on whether the combatant is a Monster or not
		if (isMonster)
			textColor = "cyan";

		View.printColored(combatantName + " has fallen!", textColor, true);

		// Print gold and XP rewards if the combatant is a Monster
		if (isMonster)
		{
			Monster monster = (Monster) combatant;
			int goldReward = monster.getGoldReward();
			int xpReward = monster.getXPReward();
			
			View.println("");
			View.printColored(goldReward + " Gold earned", "yellow", true);
			View.printColored(xpReward + " XP earned", "yellow", true);
		}
	}
	
	public static void printCombatantTurnInfo(Combatant combatant)
	{
		/* Prints out information about the combatant's turn */
		String characterName = "[" + combatant.getName() + "]";
		
		View.separate(3);

		View.println("Type \"help\" to view all of the game's commands");
		View.printColored("It's ", "cyan", false);
		View.print(characterName + "'s ");
		View.printColored("turn to act. Fight or Use Item ",  "cyan", false);
		View.printColored("\nTo target a specific enemy, use \"target [slot #]\".","cyan", true);
	}
	
	public static void printPartyLevelUp(Party party) 
	{
		/* Prints out a message indicating that the player's party has leveled up */
		Player player = party.getPlayer();
		List<Companion> activeCompanions = party.getActiveCompanions();
		List<Companion> inactiveCompanions = party.getInactiveCompanions();
		
		View.println("");
		View.printColored("Your party has leveled up!!! Stat points have been distributed to all members", "yellow", true);
		View.printColored("----------<<< Lv. " + player.getCurrentLevel() + " >>>----------", "yellow", true);
		
		// Print stats of the player
		View.separate(3);
		Wait.yield(1f);
		
		View.printPlayerStatus(player);
		
		// Print stats of each party member
		// Active Party
		for (Companion companion : activeCompanions)
		{
			View.separate(3);
			Wait.yield(1f);
			
			View.printCompanionStatus(companion);
		}
		
		// Inactive Party
		for (Companion companion : inactiveCompanions)
		{
			View.separate(3);
			Wait.yield(1f);
			
			View.printCompanionStatus(companion);
		}
		
		// Print out the amount of XP needed for the next level
		int nextLevel = player.getCurrentLevel() + 1;
		long currentXP = player.getCurrentXP();
		long xpForNextLevel = player.getTotalXPRequiredForLevel(nextLevel);
		
		View.println("");
		View.println("XP to reach Lv. " + nextLevel + ": " + currentXP + "/" + xpForNextLevel);
	}
}

