package Model.Combat;

public class CombatSkill {
	/* Data Fields */
	private int id;
	private String name;
	private String description;
	private String skilLType;
	private String targetType;
	private String requiredWeaponType;
	private int physicalDamage;
	private int magicDamage;
	private int manaCost;
	private int hitCount;
	// private List<Weapon> usuableWeapons; // Need to implement Weapon class; uncomment as needed
	
	
	// Added list of usable weapons

	/* Constructors */
	public CombatSkill(
			int id, 
			String name, 
			String description, 
			String skillType, 
			String targetType,
			String requiredWeaponType, 
			int physicalDamage, 
			int magicDamage, 
			int manaCost, 
			int hitCount
		)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.skilLType = skillType;
		this.targetType = targetType;
		this.requiredWeaponType = requiredWeaponType;
		this.physicalDamage = physicalDamage;
		this.magicDamage = magicDamage;
		this.manaCost = manaCost;
		this.hitCount = hitCount;
	}
	
	/* Methods */
	
	// Private //
	
	// Getters //

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
		
	}
	// Overrides //
}
