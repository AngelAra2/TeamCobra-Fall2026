//package Model.Items;
//
//import Model.AbstractClasses.Item;
//
//public class Weapon extends Item
//{
//	/* Data Fields */
//	private int baseAttack;
//
//	/* Constructors */
//	public Weapon(int itemId, String itemName, String itemDesc, int baseAttack) {
//		super(itemId, itemName, itemDesc, 0, 0);
//		this.baseAttack = baseAttack;
//	}
//
//	// Getters //
//	public int getBaseAttack() { return this.baseAttack; }
//
//}


package Model.Items;

// Represents weapons that characters can equip.
public class Weapon extends EquipmentItem {
    private String weaponType;
    // Full constructor for weapons loaded from item data.
    public Weapon(int itemId, String itemName, String itemDesc, double weight, int value, String slot, int physicalDamage,
                  int magicDamage, int physicalDefense, int magicDefense, String strengthScaling, String dexterityScaling,
                  String intelligenceScaling, String arcaneScaling, String specialEffect,String weaponType) {

        super(itemId, itemName, itemDesc, weight, value, slot, physicalDamage, magicDamage, physicalDefense,
                magicDefense, strengthScaling, dexterityScaling, intelligenceScaling, arcaneScaling, specialEffect);
        this.weaponType = weaponType;
    }

    // Maintains compatibility with older code that requests baseAttack.
    public int getBaseAttack() {
        return this.getPhysicalDamage();
    }
    public String getWeaponType() {return this.weaponType;}
}


