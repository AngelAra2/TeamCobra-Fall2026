package Model.AbstractClasses;

import java.util.*;

import Model.Combat.CombatSkill;
import Model.Items.*;

public abstract class Character extends Combatant 
{
	/* Data Fields */
	private static final int baseMana = 30;
	private static final int addedManaPerIntelligence = 2;
	private static final int addedMaxHPPerLevel = 5;
	private static final int baseXP = 100;
	private static final int addedXPPerLevel = 20;
	private static final int baseEquipLoad = 15;
	private static final int addedOtherStatsPerLevel = 2;

	protected int previousLevel;
	
	// Base stats for the character
	protected int _baseMaxHP;
	protected int _baseStrength;
	protected int _baseIntelligence;
	protected int _baseDexterity;
	protected int _baseArcane;
	protected int _baseEndurance;
	
	// Current stats for the character
	protected int currentLevel;
	protected long currentXP;
	protected int currentMana;
	protected int strength;
	protected int intelligence;
	protected int dexterity;
	protected int arcane;
	protected int endurance;
	
	protected List<CombatSkill> skills;
    protected EquipmentLoadout equipmentLoadout;
    
	private double physicalDamageBonus = 0.0;
	private int physicalDamageBonusTurns = 0;

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
		
		this.previousLevel = currentLevel;
		
		// Base stats for the character
		this._baseMaxHP = baseMaxHP;
		this._baseStrength = strength;
		this._baseIntelligence = intelligence;
		this._baseDexterity = dexterity;
		this._baseArcane = arcane;
		this._baseEndurance = endurance;

		// Current stats for the character
		this.currentLevel = currentLevel;
		this.currentXP = currentXP;
		this.currentMana = currentMana;
		this.strength = strength;
		this.intelligence = intelligence;
		this.dexterity = dexterity;
		this.arcane = arcane;
		this.endurance = endurance;
		this.skills = skills;
        this.equipmentLoadout = new EquipmentLoadout();
	}
	
	/* Methods */
	
	// Private //

	private void checkLevelUp()
	{
		/* Check if the character has enough XP to level up */
		while (this.currentXP >= this.getTotalXPRequiredForLevel(this.currentLevel + 1) )
		{
			this.currentLevel++;
			this.levelUp();
		}
	}
	
	// Keep currentMana within 0 and maxMana
	private void setMana(int mana) { this.currentMana = Math.clamp(mana, 0, this.calculateMaxMana()); }

	// Getters //
    public EquipmentLoadout getEquipmentLoadout() {
        return this.equipmentLoadout;
    }

    public long getCurrentXP() { return this.currentXP; }
    
	public int getCurrentLevel() { return this.currentLevel; }

	public int getCurrentMana() {return this.currentMana; }

	public int getStrength() {return strength; }

	public int getIntelligence() {return intelligence; }

	public int getDexterity() {return dexterity; }

	public int getArcane() {return arcane; }
	
	public int getEndurance() { return this.endurance; }

	public List<CombatSkill> getSkills() {return skills; }

	public double getPhysicalDamageBonus() {return physicalDamageBonus; }

	public int getPhysicalDamageBonusTurns() {return physicalDamageBonusTurns; }

	// Calculate maximum equipment load
    public double calculateEquipLoad() {
        return baseEquipLoad + this.endurance;
    }

// Calculates the character's total physical attack.
    public int getTotalAttack() {

        int totalAttack = this.attack;

    // Add physical damage from equipped weapons.
        totalAttack = totalAttack
            + this.equipmentLoadout.getTotalPhysicalDamage();

        return totalAttack;
    }

    /* Returns the total XP required for a given level */
    public long getTotalXPRequiredForLevel(int level) { return (level - 1) * (2 * baseXP + (level - 2) * addedXPPerLevel) / 2; }
    
	// Setters //

	// Custom //
	
	// Gain 5 Max HP per level beyond the first
	public int calculateMaxHP() { return this._baseMaxHP + (addedMaxHPPerLevel * (this.currentLevel - 1)); }
	
	// Base Mana is 30
	// Gain 2 Max Mana per point of Intelligence
	public int calculateMaxMana() { return baseMana + (addedManaPerIntelligence * this.intelligence); }

	public void spendMana(int amount) { this.setMana(this.currentMana - amount); }
	public void restoreMana(int amount) { this.setMana(this.currentMana + amount); }

	public void levelUp()
	{
		/* Update stats and attributes when the character levels up */
		
		// Update Max HP and Max Mana on new level
		this.setMaxHP(this.calculateMaxHP());
		this.setMana(this.calculateMaxMana()); // Max Mana is recalculated every time Mana is spent or restored
		
		// Restore Current HP to Max HP on level up
		this.setCurrentHP(this.getMaxHP());

		// Calculate all other stats based on base stats and current level
		// Update Strength, Intelligence, Dexterity, Arcane, and Endurance: each stat increases by 2 per level	
		this.strength = this._baseStrength + (addedOtherStatsPerLevel * (this.currentLevel - 1));
		this.intelligence = this._baseIntelligence + (addedOtherStatsPerLevel * (this.currentLevel - 1));
		this.dexterity = this._baseDexterity + (addedOtherStatsPerLevel * (this.currentLevel - 1));
		this.arcane = this._baseArcane + (addedOtherStatsPerLevel * (this.currentLevel - 1));
		this.endurance = this._baseEndurance + (addedOtherStatsPerLevel * (this.currentLevel - 1));
	}
	
	public boolean addXP(long amount)
	{
		/* Adds XP to the character and checks if they level up */
		this.currentXP += (amount * 10);
		this.checkLevelUp();
		
		// Return true if the character leveled up at least 1 level, false otherwise
		if (this.currentLevel > this.previousLevel)
		{
			this.previousLevel = this.currentLevel;
			return true;
		}
		
		return false;
	}

	// Overrides //
    // Returns the character's total physical defense, including equipment.
    @Override
    public int getPhysicalDEF() {

        return this.physicalDEF
                + this.equipmentLoadout.getTotalPhysicalDefense();
    }

    // Returns the character's total magic defense, including equipment.
    @Override
    public int getMagicDEF() {

        return this.magicDEF
                + this.equipmentLoadout.getTotalMagicDefense();
    }

    // Returns the total magic damage provided by equipped items.
    public int getTotalMagicDamage() {
        return this.equipmentLoadout.getTotalMagicDamage();
    }

	//receives combat skill name, and returns combat skill from Players ArrayList of combat skills
	public CombatSkill getSkillByName(String name){
		for (CombatSkill skill : skills){
			if(skill.getName().equals(name)){
				return skill;
			}
		}

		return null;
	}

	//applies temporary physical damage bonus to character
	public void applyPhysicalDamageBuff(double bonus, int duration) {

		//stores physical damage bonus applied to character.
		physicalDamageBonus = bonus;

		//set number of turns before buff expires.
		physicalDamageBonusTurns = duration;
	}

	//Decreases remaining turns of an active damage buff.
	public void decreaseBuffDuration() {
		//reduce the remaining duration by one if buff is active
		if (physicalDamageBonusTurns > 0) {
			physicalDamageBonusTurns--;
		}
		//remove physical damage bonus.
		if (physicalDamageBonusTurns == 0) {
			physicalDamageBonus = 0.0;
		}
	}
	
	// Overrides //
}
