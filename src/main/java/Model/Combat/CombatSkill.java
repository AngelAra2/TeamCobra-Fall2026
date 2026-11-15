package Model.Combat;

import Model.AbstractClasses.Character;
import Model.AbstractClasses.Combatant;

import java.util.ArrayList;

//11.14.26
//Angel Aragon
//Finished CombatSkills class
public class CombatSkill 
{
	/* Data Fields */
	private int id;
	private String name;
	private String description;
	private String skillType; //Spell or Skill
	private String targetType; //Single, Party or All
	private String requiredWeaponType;
	private String requiredWeaponSlot; //Main Hand or Off Hand
	private int requiredWeaponID;
	private ArrayList<String> requiredCharacters;
	private int physicalDamage;
	private int magicDamage;
	private int manaCost;
	private int hitCount;
	private double damageMultiplier; //used for skills that involve a damage multiplier
	private String effectType; //string used to determine how effect will be applied to combatant
	private double effectValue;
	private int effectDuration; //turns effect is active
	private double effectChance; //chance of effect occurring
	private boolean ignoresDefense; //some skills ignore phys and magic defense, if true, ignore defense stats
	private int ammunitionCost; //used for skills that require arrows
	private int flatHolyDamage; //only used for 1 skill - "Great Serpent Hunt"



	/* Constructors */
	public CombatSkill(int id, String name, String skillType, String targetType,
                       String requiredWeaponType, String requiredWeaponSlot, int requiredWeaponID,
                       ArrayList<String> requiredCharacters, int physicalDamage, int magicDamage, int manaCost,
                       int hitCount, double damageMultiplier, String effectType,
                       double effectValue, int effectDuration, double effectChance,
                       boolean ignoresDefense, int ammunitionCost, int flatHolyDamage,
                       String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.skillType = skillType;
		this.targetType = targetType;
		this.requiredWeaponType = requiredWeaponType;
		this.requiredWeaponSlot = requiredWeaponSlot;
		this.requiredWeaponID = requiredWeaponID;
		this.requiredCharacters = requiredCharacters;
		this.physicalDamage = physicalDamage;
		this.magicDamage = magicDamage;
		this.manaCost = manaCost;
		this.hitCount = hitCount;
		this.damageMultiplier = damageMultiplier;
		this.effectType = effectType;
		this.effectValue = effectValue;
		this.effectDuration = effectDuration;
		this.effectChance = effectChance;
		this.ignoresDefense = ignoresDefense;
		this.ammunitionCost = ammunitionCost;
		this.flatHolyDamage = flatHolyDamage;
	}

	/* Methods */
	
	// Private //
	
	// Getters //

	public String getName() { return name; }

	public int getId() {return id;}

	public int getManaCost() {return manaCost;}

	public String getTargetType() { return targetType; }

	public String getEffectType() { return effectType; }

	public int getPhysicalDamage() { return physicalDamage; }

	public int getMagicDamage() { return magicDamage; }

	public int getHitCount() { return hitCount; }

	public int getEffectDuration() { return effectDuration; }

	public int getAmmunitionCost() { return ammunitionCost; }

	public int getFlatHolyDamage() { return flatHolyDamage; }

	public double getDamageMultiplier() { return damageMultiplier; }

	public double getEffectValue() { return effectValue; }

	public double getEffectChance() { return effectChance; }

	public boolean ignoresDefense() { return ignoresDefense; }

	public String getRequiredWeaponType() {
		return requiredWeaponType;
	}

	public String getRequiredWeaponSlot() {
		return requiredWeaponSlot;
	}

	public int getRequiredWeaponID() {
		return requiredWeaponID;
	}

	// Setters //

	// Custom //

	// (Modified by Angel)
	public boolean canUse(Character user)
	{
		String characterName;

//		if(user instanceof Player){
//			Player player = (Player) user;
//			characterName = player.getStartingClass();
//		}
//		else if(user instanceof Companion){
//			Companion companion = (Companion) user;
//			character
//		}

		return false;
	}
	
	// (WIP 10.29.2026)
	public int calculateDamage(Character user)
	{
		return 0;
	}
	
	// (WIP 10.29.2026)
	public void castSkill(Combatant user, Combatant target)
	{
		return;
	}

	// Overrides //
}
