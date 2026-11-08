package Model.Combat;

import java.util.*;

import Model.AbstractClasses.Combatant;
import View.*;

public class Battlefield 
{
	/* Data Fields */
	private static Player playerCombatant;
	private static ArrayList<Monster> monsters = new ArrayList<>();

	/* Methods */
	
	// Getters //
	public static ArrayList<Monster> getMonsters() { return Battlefield.monsters; }
	
	public static boolean canBattleContinue() 
	{
		return ((Battlefield.playerCombatant != null && 
				Battlefield.playerCombatant.isAlive()) &&
				
				(Battlefield.monsters != null && 
				!Battlefield.monsters.isEmpty())
		);
	}
	
	public static Monster getFirstAliveMonster() 
	{
		for (Monster monster : Battlefield.monsters)
			if (monster.isAlive())
				return monster;

		return null;
	}
	
	// Custom //
	public static void removeMonster(Monster monster)
	{
		if (Battlefield.monsters.contains(monster))
			Battlefield.monsters.remove(monster);
	}
	
	public static void performAttack(Combatant attacker, Combatant receiver) 
	{
		// If receiver is dead, they cannot be attacked
		if (!receiver.isAlive())
			return;

		// Get Attack stat from attacker
		int attackerTotalAttack = attacker.getAttack();
		
		// Check if the attacker is of the Character class
		if (attacker instanceof Player)
		{
			Player player = (Player) attacker;
			attackerTotalAttack = player.getTotalAttack();
		}
		
		// Store receiver's current HP before taking damage
		int receiver_HPBefore = receiver.getCurrentHP();
		
		// Receiver takes damage
		receiver.takeDamage(attackerTotalAttack, "physical");
		
		// Store receiver's current HP after taking damage
		int receiver_HPAfter = receiver.getCurrentHP();
		
		// Print how much damage was dealt to the receiver
		int damageDealt = receiver_HPBefore - receiver_HPAfter;
		View.println(attacker.getName() + " dealt " + damageDealt + " damage to " + receiver.getName() + "");
	}
	
	public static void displayBattleStatuses()
	{
		// Don't display battle statuses if the battlefield is empty
		if (!Battlefield.canBattleContinue())
			return;
		
		View.println("=== BATTLE STATUS ===");
		
		/* Print player's party status */
		View.println("");
		BattleView.printCombatantStatus(Battlefield.playerCombatant);

		View.println("\n-------------------- VS. --------------------\n");
		
		/* Print monsters' statuses */
		for (Monster monster: Battlefield.monsters)
			BattleView.printCombatantStatus(monster);
	}
	
	public static void populateBattlefield(Player player, ArrayList<Monster> monsters) 
	{
		Battlefield.playerCombatant = player;
		
		for (Monster monster : monsters)
			Battlefield.monsters.add(monster);
	}
	
	public static void clearBattlefield() 
	{
		Battlefield.playerCombatant = null;	
		Battlefield.monsters.clear();
	}
}

