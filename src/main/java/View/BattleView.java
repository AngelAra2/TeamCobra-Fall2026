package View;

import java.util.*;

import Model.AbstractClasses.Combatant;
import Model.Combat.CombatSkill;
import Model.Combat.Monster;
import Model.AbstractClasses.Character;

public class BattleView 
{
	/* Data Fields */
	private static final int PROGRESS_BAR_LENGTH = 20;
	private static final float CRIT_HP_THRESHOLD = 0.4f; // Threshold for low HP warning
	
	public static void printCombatantStatus(Combatant combatant) 
	{
		/* Prints out the status of a combatant to the console */
		
		// Print out name and current HP of the combatant
		String combatantName = combatant.getName();
		float currentHP = (float) combatant.getCurrentHP();
		float maxHP = (float) combatant.getMaxHP();
		float hpPercentage = currentHP/maxHP;
		boolean criticalHP = hpPercentage <= CRIT_HP_THRESHOLD;
		//used to print physical defense and magical defense for combatant
		float physDef = (float) combatant.getPhysicalDEF();
		float magDef = (float) combatant.getMagicDEF();

		View.print(">>> [" + combatantName + "]");
		View.print(" | ");
		
		String hpText = "HP " + (int) currentHP + "/" + (int) maxHP;
		String hpColor = criticalHP ? "red" : "green";

		String pDefText = "Phys Def: " + (int) physDef;
		String mDefText = "Mag Def: " + (int) magDef;
		// If critical HP, print HP text and progress bar in red; print in green otherwise
		View.printColored(hpText, hpColor, false);
		View.print(" ");
		View.printProgressBar(hpPercentage, PROGRESS_BAR_LENGTH, hpColor, false);

		View.printColored(" | " + pDefText + " | " + mDefText, "cyan", true);
		View.println("");
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

	public static void printFightMenu(Character character){
		View.printColored("---< FIGHT MENU >--- ", "yellow", true);
		View.printColored("\n- Basic Attack","blue", true);

		View.println(" ");

		View.println("- Combat Skills:\n");
		//displays available skills for player
		for (CombatSkill skills : character.getSkills()){
			View.printColored("- " + skills.getName(),"blue",true);
		}

		View.println("\nChoose basic attack or skill, or cancel to return.");
	}
}
