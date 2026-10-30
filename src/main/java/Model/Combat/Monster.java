package Model.Combat;

import java.util.List;

import Model.Item;

public class Monster extends Combatant {
	/* Data Fields */
	private int id;
	private String description;
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
			
			int id,
			String description,
			int xpReward,
			int goldReward,
			boolean isBoss
		) 
	{
		super(name, maxHP, physicalDEF, magicDEF);
		
		this.id = id;
		this.description = description;
		this.xpReward = xpReward;
		this.goldReward = goldReward;
		this.isBoss = isBoss;
	}

	/* Methods */
	
	// Private //
	
	// Getters //
	public boolean isBossMonster() { return this.isBoss; }

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
}
