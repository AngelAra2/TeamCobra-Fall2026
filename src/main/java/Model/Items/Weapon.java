package Model.Items;

import Model.AbstractClasses.Item;

public class Weapon extends Item
{
	/* Data Fields */
	private int baseAttack;

	/* Constructors */
	public Weapon(
		int itemId,
		String itemName,
		String itemDesc,
		int baseAttack
	)
	{
		super(
			itemId,
			itemName,
			itemDesc
		);
		
		this.baseAttack = baseAttack;
	}

	/* Methods */
	
	// Private //
	
	// Getters //
	public int getBaseAttack() { return this.baseAttack; }

	// Setters //

	// Custom //
	
	// Overrides //
}

