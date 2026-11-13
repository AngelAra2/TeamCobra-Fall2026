package Model.Combat;

import java.util.List;

import Model.AbstractClasses.Combatant;
import Model.AbstractClasses.Item;

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
	
	// Getters //
	public boolean isBossMonster() { return this.isBoss; }
	public String getDescription() { return this.description; }

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
	
	public Character chooseTarget(Party party)
	{
		return null;
	}
	*/
	
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
