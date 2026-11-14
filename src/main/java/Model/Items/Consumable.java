//package Model.Items;
//import Model.AbstractClasses.Item;
//
//public class Consumable extends Item
//{
//	/* Data Fields */
//	private int healingAmount;
//
//	/* Constructors */
//	public Consumable(int itemId, String itemName, String itemDesc, int healingAmount) {
//		super(itemId, itemName, itemDesc, 0, 0);
//
//		this.healingAmount = healingAmount;
//	}
//
//	/* Methods */
//
//	// Getters //
//	public int getHealingAmount() { return this.healingAmount; }
//}


package Model.Items;

import Model.AbstractClasses.Item;

public class Consumable extends Item {

    /* Data Fields */

    // Identifies the effect produced when the item is consumed.
    private String effectType;

    // Stores the amount of the effect.
    private int effectAmount;

    // Stores how long the effect lasts.
    private int effectDuration;

    /* Constructor */

    // Creates a consumable using information from ConsumableItems.txt.
    public Consumable(int itemId, String itemName, String itemDesc,
                      double weight, int value, String effectType,
                      int effectAmount, int effectDuration) {

        super(itemId, itemName, itemDesc, weight, value);

        this.effectType = effectType;
        this.effectAmount = effectAmount;
        this.effectDuration = effectDuration;
    }

    /* Getters */

    public String getEffectType() {
        return this.effectType;
    }

    public int getEffectAmount() {
        return this.effectAmount;
    }

    public int getEffectDuration() {
        return this.effectDuration;
    }

    // Maintains compatibility with the existing Player healing code.
    public int getHealingAmount() {
        return this.effectAmount;
    }
}


