package Model.AbstractClasses;

import java.util.List;

import Model.Combat.CombatSkill;
import Model.Items.*;

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
	//protected Weapon equippedWeapon;
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
        this.equipmentLoadout = new EquipmentLoadout();
	}
	
	/* Methods */
	
	// Private //
	
	// Keep currentMana within 0 and maxMana
	private void setMana(int mana) { this.currentMana = Math.clamp(mana, 0, this.calculateMaxMana()); }

	// Getters //
    public EquipmentLoadout getEquipmentLoadout() {
        return this.equipmentLoadout;
    }

    public int getEndurance() {
        return this.endurance;
    }

	public int getCurrentLevel() { return this.currentLevel;}

	public int getCurrentMana() {return this.currentMana;}

	public int getStrength() {return strength;}

	public int getIntelligence() {return intelligence;}

	public int getDexterity() {return dexterity;}

	public int getArcane() {return arcane;}

	public List<CombatSkill> getSkills() {return skills;}

	public double getPhysicalDamageBonus() {return physicalDamageBonus;}

	public int getPhysicalDamageBonusTurns() {return physicalDamageBonusTurns;}

	// Calculate maximum equipment load
    public double calculateEquipLoad() {
        return 15 + this.endurance;
    }

// Calculates the character's total physical attack.
    public int getTotalAttack() {

        int totalAttack = this.attack;

    // Add physical damage from equipped weapons.
        totalAttack = totalAttack
            + this.equipmentLoadout.getTotalPhysicalDamage();

        return totalAttack;
    }

    public int getXPToNextLevel() {
		/* Calculate the XP required for the next level using a simple formula */
    	// Base XP for level 1 is 100, and it increases by 20 for each subsequent level
		return 100 + (20 * this.currentLevel - 1);
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
//	public void equipWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
//	public void unequipWeapon() { this.equippedWeapon = null; }
	
	public void levelUp()
	{
		// Max Mana is recalculated every time Mana is spent or restored
		
		// Update Max HP on new level
		this.setMaxHP(this.calculateMaxHP());
		
		// Restore HP and Mana to full on level up
		this.setCurrentHP(this.getMaxHP());
		this.setMana(this.calculateMaxMana());
	}
	
	public void addXP(int amount)
	{
		this.currentXP += amount;
		
		// Check if the character has enough XP to level up
		while (this.currentXP >= this.getXPToNextLevel())
		{
			this.currentXP -= this.getXPToNextLevel();
			this.currentLevel++;
			this.levelUp();
		}
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
}
