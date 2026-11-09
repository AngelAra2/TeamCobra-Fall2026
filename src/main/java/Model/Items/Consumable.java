package Model.Items;
import Model.AbstractClasses.Item;

public class Consumable extends Item
{
	/* Data Fields */
	private int healingAmount;

	/* Constructors */
	public Consumable(
		int itemId,
		String itemName,
		String itemDesc,
		int healingAmount
	) {
		super(itemId, itemName, itemDesc, 0, 0);
		
		this.healingAmount = healingAmount;
	}

	/* Methods */
	
	// Private //
	
	// Getters //
	public int getHealingAmount() { return this.healingAmount; }

	// Setters //

	// Custom //
	
	// Overrides //
}

