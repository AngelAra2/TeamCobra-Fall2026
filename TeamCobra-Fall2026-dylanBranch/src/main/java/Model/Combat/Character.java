package Model.Combat;

import java.util.List;

public abstract class Character extends Combatant {
	/* Data Fields */
	protected int currentLevel;
	protected int currentXP;
	protected int currentMana;
	protected int baseMaxHP;
	protected int strength;
	protected int intelligence;
	protected int dexterity;
	protected int arcane;
	protected int endurance;
	// protected Equipment equipment; // Need to implement Equipment class; uncomment as needed
	protected List<CombatSkill> skills;

	/* Constructors */
	public Character(
			String name,
			int maxHP,
			int physicalDEF,
			int magicDEF,
			
			int currentLevel,
			int currentXP,
			int currentMana,
			int baseMaxHP,
			int strength,
			int intelligence,
			int dexterity,
			int arcane,
			int endurance,
			List<CombatSkill> skills
		) 
	{
		super(name, maxHP, physicalDEF, magicDEF);
		
		this.currentLevel = currentLevel;
		this.currentXP = currentXP;
		this.currentMana = currentMana;
		this.baseMaxHP = baseMaxHP;
		this.strength = strength;
		this.intelligence = intelligence;
		this.dexterity = dexterity;
		this.arcane = arcane;
		this.endurance = endurance;
		this.skills = skills;
	}
	
	/* Methods */
	
	// Private //
	private void setMana(int mana)
	{
		this.currentMana = Math.clamp(mana, 0, this.calculateMaxMana());
	}

	// Getters //

	// Setters //

	// Custom //
	public int calculateMaxHP()
	{
		// Gain 5 Max HP per level beyond the first
		return this.baseMaxHP + (5 * (this.currentLevel - 1));
	}
	
	public int calculateMaxMana()
	{
		// Base Mana is 30
		// Gain 2 Max Mana per point of Intelligence
		return 30 + (2 * this.intelligence);
	}
	
	// (WIP 10.29.2026)
	/*
	public double calculateCritChance()
	{
		return 0.0;
	}
	*/
	
	// (WIP 10.29.2026)
	/*
	public double calculateEquipLoad()
	{
		return 0.0;
	}
	*/
	
	public void spendMana(int amount)
	{
		this.setMana(this.currentMana - amount);
	}
	
	public void restoreMana(int amount)
	{
		this.setMana(this.currentMana + amount);
	}
	
	public void levelUp()
	{
		// Max Mana is recalculated every time Mana is spent or restored
		
		// Update Max HP on new level
		this.setMaxHP(this.calculateMaxHP());
		
		// Restore HP and Mana to full on level up
		this.setCurrentHP(this.getMaxHP());
		this.setMana(this.calculateMaxMana());
	}
	
	// (WIP 10.29.2026)
	/*
	public boolean equip(EquipmentItem item)
	{
		return false;
	}
	*/
	
	// (WIP 10.29.2026)
	/*
	public boolean unequip(EquipmentSlot slo)
	{
		return false;
	}
	*/
	
	public String getStatus()
	{
		return "";
	}
	
	// Overrides //
}
