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
	private String skillType;
	private String targetType;
	private String requiredWeaponType;
	private String requiredWeaponSlot;
	private int requiredWeaponID;
	private ArrayList<String> requiredCharacters;
	private int physicalDamage;
	private int magicDamage;
	private int manaCost;
	private int hitCount;
	private double damageMultiplier;
	private String effectType;
	private double effectValue;
	private int effectDuration;
	private double effectChance;
	private boolean ignoresDefense;
	private int ammunitionCost;
	private int flatHolyDamage;



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

	// Setters //

	// Custom //
	
	// (WIP 10.29.2026)
	public boolean canUse(Character user)
	{
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
