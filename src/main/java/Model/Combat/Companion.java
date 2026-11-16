package Model.Combat;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import Model.AbstractClasses.Character;
import Model.Exploration.Room;
import Model.Interfaces.NPCable;
import Model.AbstractClasses.Item;
import Model.Items.Inventory;
import Model.Items.EquipmentItem;
import View.View;
public class Companion extends Character implements NPCable
{
    /* Data Fields */
    private int id;
    private String partyStatus;
    private String perkDescription;
    private boolean isRecruited;
    private Room homeRoom;
    private String backstory;
    private List<Item> defaultEquipment; // PLACEHOLDER UNTIL THE EQUIPMENT CLASS IS IMPLEMENTED
    private Inventory inventory;
    //private List<Dialogue> dialogues;

    /* Constructors */
    public Companion(
            String name,
            String backstory,
            int baseMaxHP,
            int strength,
            int dexterity,
            int intelligence,
            int arcane,
            int endurance,
            String perkDescription,
            List<CombatSkill> combatSkills,
            List<Item> defaultEquipment,
            int id,
            Room homeRoom
    )
    {
        super(
                name,
                baseMaxHP,
                0,
                0,
                strength,
                1,
                0,
                30 + (2 * intelligence),
                baseMaxHP,
                strength,
                intelligence,
                dexterity,
                arcane,
                endurance,
                combatSkills
        );

        this.id = id;
        this.partyStatus = null;
        this.perkDescription = perkDescription;
        this.isRecruited = false;
        this.homeRoom = homeRoom;
        this.backstory = backstory;
        this.defaultEquipment = defaultEquipment;
        this.inventory = new Inventory();
        for (Item item : defaultEquipment){
            this.inventory.addItem(item, this.currentLevel);
        }

    }

    /* Methods */

    // Private //

    // Getters //
    public int getId() { return this.id; }
    public String getPartyStatus() { return this.partyStatus; }
    public String getPerkDescription() { return this.perkDescription; }
    public boolean isRecruited() { return this.isRecruited; }
    public Room getHomeRoom() { return this.homeRoom; }
    public String getBackstory() { return this.backstory; }
    public List<Item> getDefaultEquipment() { return this.defaultEquipment;}

    // Setters //
    public void setPartyStatus(String partyStatus) { this.partyStatus = partyStatus; }

    // Custom //
    public void recruit() { this.isRecruited = true; }

    // Overrides //


    public Inventory getInventory() {
        return inventory;
    }

    public boolean addItem(Item item) {
        return inventory.addItem(item, this.currentLevel);
    }

    /*
        Displays all items in companion's inventory
     */
    public void viewInventory() {
        View.println("\n" + this.getName() + "'s Inventory:");

        //checks if inventory is empty
        if (inventory.isEmpty()) {
            View.println("Inventory empty");
            return;
        }

        //loop through each group of items in inventory
        for (ArrayList<Item> itemList : inventory.getItems().values()) {
            //skips empty item lists
            if (itemList.isEmpty()) {
                continue;
            }

            //get first item to get name
            Item item = itemList.get(0);

            //Format item name and quantity
            String info = "- " + item.getItemName() + " (x" + itemList.size() + ")";

            //checks if item is equippable
            if (item instanceof EquipmentItem) {
                EquipmentItem equipment = (EquipmentItem) item;

                //displays equipment slot
                info += " [" + equipment.getSlot() + "]";

                //checks if item is equipped.
                for (Item currentItem : itemList) {
                    if (currentItem.isEquipped()) {
                        info += " (Equipped)";
                        break;
                    }
                }
            }

            View.println(info);
        }
    }

    /*
    Handles equipping and unequipping for companion
     */
    public void handleEquipmentCommand(String action, String itemName) {

        //checks if item name was provided
        if (itemName.isEmpty()) {
            View.println("Specify an item name.");
            return;
        }

        Item item = inventory.findItem(itemName);

        //checks if item is an equipment item.
        if (!(item instanceof EquipmentItem)) {
            View.println("Equipment not found in companion inventory.");
            return;
        }

        EquipmentItem equipment = (EquipmentItem) item;

        if (action.equalsIgnoreCase("equip")) {
            //tries to equip item while checking equipment load
            boolean success = equipmentLoadout.equipItem(equipment, calculateEquipLoad());

            if (success) {

                View.println(getName() + " equipped " + equipment.getItemName());

            } else {

                View.println("Unable to equip " + equipment.getItemName());

            }
        }


        else if (action.equalsIgnoreCase("unequip")) {
            //checks if item is equipped
            if (!equipment.isEquipped()) {
                View.println(equipment.getItemName()
                        + " is not equipped.");
                return;
            }

            equipmentLoadout.unequipItem(equipment.getSlot());
            //confirm to user
            View.println(getName() + " unequipped " + equipment.getItemName());
        }
    }
    @Override
    public String talk() {
        return "";
    }
}
