package Model.Items;

public class EquipmentItem {

    private String slot;
    private int physicalDamage;
    private int magicDamage;
    private int physicalDefense;
    private int magicDefense;


    public EquipmentItem(String slot, int physicalDamage, int magicDamage, int physicalDefense, int magicDefense) {
        this.slot = slot;
        this.physicalDamage = physicalDamage;
        this.magicDamage = magicDamage;
        this.physicalDefense = physicalDefense;
        this.magicDefense = magicDefense;
    }

    /* Getters */

    public String getSlot() {
        return slot;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }

    public int getMagicDamage() {
        return magicDamage;
    }

    public int getPhysicalDefense() {
        return physicalDefense;
    }

    public int getMagicDefense() {
        return magicDefense;
    }

    public int getWeight() {
        // Placeholder for weight calculation logic
        return 0;
    }
}
