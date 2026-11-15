package Model.Combat;

import java.util.*;

import Model.AbstractClasses.Combatant;
import View.*;

public class Battlefield 
{
	/* Data Fields */
	//private static Player playerCombatant;
	private static Party playerParty;
	private static ArrayList<Monster> monsters = new ArrayList<>();
	
	// Keeps track of which monster in the monsters list is currently being targeted by the player's party
	private static int playerPartyTargetIndex = 0;

	/* Methods */
	
	// Getters //
	public static ArrayList<Monster> getMonsters() { return Battlefield.monsters; }

	public static boolean canBattleContinue() 
	{
		return (!Battlefield.playerParty.isPartyDefeated() &&				
				(Battlefield.monsters != null && 
				!Battlefield.monsters.isEmpty())
		);
	}
	
	// Return the monster that is currently being targeted by the player's party. If there are no monsters, return null
	public static Monster getTargetMonster() { return (Battlefield.monsters.isEmpty()) ? null : Battlefield.monsters.get(Battlefield.playerPartyTargetIndex); }
	
	// Return the random target the monster will attack from the player's party. If the player's party is defeated, return null
	public static Combatant getRandomTargetFromPlayerParty(Monster monster) { return monster.chooseTarget(playerParty); }
	
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
		
		// Print information about the attack to the console
		int damageDealt = receiver_HPBefore - receiver_HPAfter;
		BattleView.printDamageDealt(attacker, receiver, damageDealt, false);
	}
	
	public static void switchMonsterTarget(int targetIndex) 
	{
		// Target index must be within the bounds of the monsters list
		if (targetIndex < 0 || targetIndex >= Battlefield.monsters.size())
		{
			BattleView.printSwitchedMonsterTarget(null);
			return;
		}
		
		// Switch the target index to the new target index; print information about the new target to the console
		Battlefield.playerPartyTargetIndex = targetIndex;
		BattleView.printSwitchedMonsterTarget(Battlefield.monsters.get(targetIndex));
	}
	
	public static void displayBattleStatuses()
	{
		// Don't display battle statuses if the battlefield is empty
		if (!Battlefield.canBattleContinue())
			return;
		
		View.printColored("---< BATTLE STATUS >---", "yellow", true);
		
		/* Print player's party status */
		View.println("");
		
		// Player
		BattleView.printCombatantStatus(Battlefield.playerParty.getPlayer(), false);
		
		// Companions
		for (Companion companion: Battlefield.playerParty.getActiveCompanions())
			BattleView.printCombatantStatus(companion, false);

		View.println("\n------------------------------ VS. ------------------------------\n");
		
		/* Print monsters' statuses */
		for (Monster monster: Battlefield.monsters)
			BattleView.printCombatantStatus(monster, (Battlefield.monsters.indexOf(monster) == Battlefield.playerPartyTargetIndex));
	}
	
	public static void populateBattlefield(Party playerParty, ArrayList<Monster> monsters) 
	{
		Battlefield.playerParty = playerParty;
		
		for (Monster monster : monsters)
			Battlefield.monsters.add(monster);
	}
	
	public static void clearBattlefield() 
	{
		Battlefield.playerParty = null;
		Battlefield.monsters.clear();
	}
}

