package Model.Combat;

import java.util.List;

import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;

//Danh
public class Monster extends Combatant 
{
	/* Data Fields */
	private int id;
	private String description;
	private String damageType;
	private int xpReward;
	private int goldReward;
	private boolean isBoss;
	private boolean isDefeated;
	private List<CombatSkill> attacks;
	private List<Item> possibleDrops;

	/* Constructors */
	public Monster(
			String name,
			int maxHP,
			int physicalDEF,
			int magicDEF,
			int attack,
			
			int id,
			String description,
			int xpReward,
			int goldReward,
			String damageType,
			boolean isBoss
		) 
	{
		super(
			name, 
			maxHP, 
			physicalDEF,
			magicDEF, 
			attack
		);
		
		this.id = id;
		this.description = description;
		this.xpReward = xpReward;
		this.goldReward = goldReward;
		this.damageType = damageType;
		this.isBoss = isBoss;
	}

	/* Methods */
	
	// Private //
	private Combatant chooseRandomCompanionTarget(List<Companion> companions)
	{
		int randomIndex = (int) (Math.random() * companions.size());
		return companions.get(randomIndex);
	}
	
	// Getters //
	public boolean isBossMonster() { return this.isBoss; }
	public String getDescription() { return this.description; }
	public int getGoldReward() { return this.goldReward; }
	public int getXPReward() { return this.xpReward; }

	// Setters //
	public void markDefeated() { this.isDefeated = true; }

	// Custom //
	/* (WIP 10.29.2026)
	public List<Item> generateLoot()
	{
		return null;
	}
	
	public CombatSkill chooseAttack()
	{
		return null;
	}
	*/
	
	public Combatant chooseTarget(Party party)
	{
		/* The monster chooses a random target from the player's party */
		// Guard Clause //
		// If the party is defeated, the monster cannot choose a target
		if (party.isPartyDefeated())
			return null;
		
		float randomFloat = (float) Math.random();
		List<Companion> activeCompanions = party.getActiveCompanions();
		
		// If the random float is less than 0.5, target the player
		// If they are defeated, target a random companion instead
		if (randomFloat < 0.5)
		{
			if (!party.getPlayer().isAlive())
				return this.chooseRandomCompanionTarget(activeCompanions);
			
			return party.getPlayer();
		}
		
		// Otherwise, target a random companion in the party
		// If the party has no active companions or the chosen companion is defeated, target the player instead
		else
			if (party.getActiveCompanions().isEmpty() || !this.chooseRandomCompanionTarget(party.getActiveCompanions()).isAlive())
				return party.getPlayer();
			else
				return this.chooseRandomCompanionTarget(activeCompanions);
	}
	
	// Overrides //
	@Override
	public String toString()
	{
		return "[" + this.name + "]\n"
				+ this.description + "\n\n"
				+ "=== STATS ===\n"
				+ "- HP: " + this.currentHP + "/" + this.maxHP + "\n"
				+ "- Attack: " + this.attack;
	}
}
