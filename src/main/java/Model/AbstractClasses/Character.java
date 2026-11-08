package Model.AbstractClasses;

import java.util.List;

import Model.Combat.CombatSkill;
import Model.Items.Weapon;

public abstract class Character extends Combatant 
{
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
	protected Weapon equippedWeapon;
	protected List<CombatSkill> skills;

	/* Constructors */
	public Character(
			String name,
			int maxHP,
			int physicalDEF,
			int magicDEF,
			int attack,
			
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
		super(name, maxHP, physicalDEF, magicDEF, attack);
		
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
	
	// Keep currentMana within 0 and maxMana
	private void setMana(int mana) { this.currentMana = Math.clamp(mana, 0, this.calculateMaxMana()); }

	// Getters //
	public int getTotalAttack()
	{
		/* Return total Attack of character */
		int totalAttack = this.attack;
		
		// Add weapon's base attack if a weapon is equipped
		if (this.equippedWeapon != null)
			totalAttack += this.equippedWeapon.getBaseAttack();

		return totalAttack;
	}

	// Setters //

	// Custom //
	
	// Gain 5 Max HP per level beyond the first
	public int calculateMaxHP() { return this.baseMaxHP + (5 * (this.currentLevel - 1)); }
	
	// Base Mana is 30
	// Gain 2 Max Mana per point of Intelligence
	public int calculateMaxMana() { return 30 + (2 * this.intelligence); }

	public void spendMana(int amount) { this.setMana(this.currentMana - amount); }
	public void restoreMana(int amount) { this.setMana(this.currentMana + amount); }
	public void equipWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
	public void unequipWeapon() { this.equippedWeapon = null; }
	
	public void levelUp()
	{
		// Max Mana is recalculated every time Mana is spent or restored
		
		// Update Max HP on new level
		this.setMaxHP(this.calculateMaxHP());
		
		// Restore HP and Mana to full on level up
		this.setCurrentHP(this.getMaxHP());
		this.setMana(this.calculateMaxMana());
	}

	// Overrides //
}
