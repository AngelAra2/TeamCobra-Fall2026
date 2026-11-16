package Model.Combat;

import java.util.*;

import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Character;
import Model.Items.EquipmentItem;
import Model.Items.Inventory;
import Model.Items.Weapon;
import View.*;

//Danh, Angel
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
		if (attacker instanceof Character)
		{
			Character character  = (Character) attacker;
			attackerTotalAttack = character.getTotalAttack();
		}
		
		// Store receiver's current HP before taking damage
		int receiver_HPBefore = receiver.getCurrentHP();

		//applies any ongoing buffs
		if (attacker instanceof Character) {
			Character character = (Character) attacker;

			attackerTotalAttack = (int) Math.round(
					attackerTotalAttack *
							(1.0 + character.getPhysicalDamageBonus())
			);
		}
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
	//WIP
	public static void performSkillAttack(Character attacker, CombatSkill skill, Combatant receiver, Inventory inventory){

		//checks that attacker and collected skill are valid.
		if (skill == null || attacker == null){
			View.println("Invalid combat skill.");
			return;
		}

		//checks whether character meets skill requirements.
		if(!skill.canUse(attacker)){
			View.println(attacker.getName() + " cannot use " + skill.getName() + ".");
			return;
		}

		//checks if inventory contains enough arrows for the skill.
		if(!skill.hasEnoughAmmunition(inventory)){
			View.println("Not enough Wooden Arrows to use " + skill.getName() + ".");
			return;
		}

		//checks whether attacker has enough mana to cast the skill.
		if(attacker.getCurrentMana() < skill.getManaCost()){
			View.println("You do not have enough mana to use " + skill.getName());
			return;
		}

		//deduct required mana from attacker.
		attacker.spendMana(skill.getManaCost());

		// Remove ammunition required by the combat skill.
		for (int i = 0; i < skill.getAmmunitionCost(); i++) {
			inventory.removeItem(inventory.findItem("Wooden Arrow"));
		}
		//apply damage to selected target for single target skills.
		if (skill.getTargetType().equalsIgnoreCase("Single")) {
			applySkillDamage(attacker, skill, receiver);
		}

		//apply damage to every living monster in battlefield for AOE skills.
		else if (skill.getTargetType().equalsIgnoreCase("All")) {
			performAllEnemySkill(attacker, skill);
		}

		else if (skill.getTargetType().equalsIgnoreCase("Party")){
			if (skill.getId() == 3){
				performBarbaricRoar(attacker,skill);
			} else if (skill.getId() == 9){
				performHeal(attacker);
			}
		}
	}

	/*
	Applies a combat skill's damage to all monsters in battle
	Used for skills such as Flame of Ruin and Wave of Darkness
	 */
	private static void performAllEnemySkill(Character attacker, CombatSkill skill){
		for(Monster monster : monsters){
			if (!monster.isAlive()) {
				continue;
			}
			//store HP before attack to calculate damage dealt.
			int hpBefore = monster.getCurrentHP();

			//calculate physical damage, including active Barbaric Roar buff.
			int physicalDamage = (int) Math.round(
					skill.getPhysicalDamage()
							* (1.0 + attacker.getPhysicalDamageBonus())
			);

			//Apply physical damage if the skill has any.
			if (physicalDamage > 0){
				monster.takeDamage(physicalDamage, "physical");
			}

			//apply magic damage if skill has any
			if(skill.getMagicDamage() > 0){
				monster.takeDamage(skill.getMagicDamage(),"magic");
			}
			//calculate damage dealt after monster's defenses.
			int damageDealt = hpBefore - monster.getCurrentHP();

			BattleView.printDamageDealt(attacker,monster, damageDealt, false);
		}
	}


	/*
	Calculates and applies damage from single target combat skills.
	 */
	private static void applySkillDamage(Character attacker, CombatSkill skill, Combatant receiver){
		//get skills base physical and magic damage
		int physicalDamage = skill.getPhysicalDamage();
		int magicDamage = skill.getMagicDamage();

		//if both damage values are zero, use equipped weapon's damage.
		if (physicalDamage == 0 && magicDamage == 0){
			//get weapon that's equipped on main hand
			EquipmentItem mainHand = attacker.getEquipmentLoadout().getEquippedItem(EquipmentItem.MAIN_HAND);

			//verifies equipped item is a weapon before accessing damage
			if(mainHand instanceof Weapon){
				Weapon weapon = (Weapon) mainHand;

				//applies the skill's damage multiplier to the weapon's damage.
				physicalDamage = (int) Math.round(weapon.getPhysicalDamage() * skill.getDamageMultiplier());
				magicDamage = (int) Math.round(weapon.getMagicDamage() * skill.getDamageMultiplier());
			}
		}

		// Apply Barbaric Roar's physical damage bonus
		// if barbaric roar is not active, returns 0.0 for damage bonus.
		physicalDamage = (int) Math.round(
				physicalDamage * (1.0 + attacker.getPhysicalDamageBonus())
		);

		//gets number of hits
		int hitCount = Math.max(1, skill.getHitCount());
		int totalDamage = 0;

		//repeat the attack for the number of hits specified in hitCount.
		for (int i = 0; i < hitCount; i++){

			if (!receiver.isAlive()){
				break;
			}

			//store target HP before damage
			int hpBefore = receiver.getCurrentHP();

			//apply damage without bypassing target defense
			if(skill.ignoresDefense()){
				receiver.takeDamage(physicalDamage, "true");
				receiver.takeDamage(magicDamage, "true");
			}

			else {
				//apply physical damage using target's physical defense
				if (physicalDamage > 0){
					receiver.takeDamage(physicalDamage, "physical");
				}
				//apply magic damage using target's magic defense.
				if(magicDamage > 0){
					receiver.takeDamage(magicDamage,"magic");
				}
			}

			//calculate damage dealt by individual hit
			int hitDamage = hpBefore - receiver.getCurrentHP();

			//add hit damage to overall total.
			totalDamage += hitDamage;


			//display damage dealt
			BattleView.printDamageDealt(attacker, receiver, hitDamage, false);

		}
		//Only used to activate Great Serpent Hunt's holy damage.
		if (receiver.isAlive() && skill.getFlatHolyDamage() > 0){
			//store hp of target before applying holy damage
			int hpBefore = receiver.getCurrentHP();
			//apply holy damage
			receiver.takeDamage(skill.getFlatHolyDamage(), "true");

			totalDamage += hpBefore - receiver.getCurrentHP();

			//display holy damage
			BattleView.printDamageDealt(attacker, receiver, totalDamage, false);
		}

	}

	/*
	Retrieves all active party members, including the player and active companions.
	 */
	private static ArrayList<Character> getAllPartyTargets() {
		ArrayList<Character> targets = new ArrayList<>();

		if (playerParty == null) {
			return targets;
		}

		// Include the player, even if unconscious,
		// because Heal can revive them.
		targets.add(playerParty.getPlayer());

		// Include active companions only.
		for (Companion companion : playerParty.getActiveCompanions()) {
			targets.add(companion);
		}

		return targets;
	}

	/*
	Executes the heal combat skill on all active party members.
	Each member recovers HP equal to 50% to their maximum HP.

	 */
	private static void performHeal(Character attacker){
		//get player and activeCompanions
		ArrayList<Character> targets = getAllPartyTargets();

		//apply healing to each party member individually
		for (Character member : targets){
			//store current HP
			int hpBefore = member.getCurrentHP();

			//Calculate 50% of members maximum hp

			int healing = (int) Math.ceil(member.getMaxHP() * 0.50);

			member.heal(healing);

			//Used to determine how much HP was restored
			int actualHealing = member.getCurrentHP() - hpBefore;

			View.print(">>> ");
			View.printColored(member.getName() + " recovered " + actualHealing + " HP.", "cyan", false);
			View.println("");
		}
	}

	/*
	Executes Barbaric roar, party only damage buff.
	 */
	private static void performBarbaricRoar(
			Character attacker, CombatSkill skill) {

		//retrieve player and activeCompanions
		ArrayList<Character> targets = getAllPartyTargets();

		for (Character member : targets) {

			//stops from applying buff to unconscious party member.
			if (!member.isAlive()) {
				continue;
			}

			//apply physical damage buff and duration
			member.applyPhysicalDamageBuff(
					skill.getEffectValue(),
					skill.getEffectDuration());

			View.print(">>> ");
			View.printColored(member.getName() + " received a physical damage buff. ", "cyan", false);
			View.println("");
		}
	}
}

