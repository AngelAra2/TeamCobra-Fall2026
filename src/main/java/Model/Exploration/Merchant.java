package Model.Exploration;

import java.util.ArrayList;
import java.util.List;
import Model.Interfaces.NPCable;
public class Merchant implements NPCable {


    //WIP 11/11/2026
    @Override
    public String talk() {
        return "";
    }

    // NOTE TO DYLAN: PROPERLY IMPLEMENT WHEN INVENTORY GETS ADDED
/*
    private final String id;
    private final String name;
    private final List<String> dialogues;

    // The merchant creates and owns
    // its own stock. The stock has no reason to exist without the merchant.
    private final Inventory inventory;

    // Tracks which dialogue line comes next. Without it, talking to the
    // merchant twice would always give back the exact same line.
    private int dialogueIndex;

    // After a failed deal, store the reason here. The Controller or View
    // reads it and prints it, so the Model never prints anything itself.
    private String lastMessage;

    public Merchant(String id, String name, List<String> dialogues) {
        this.id = id;
        this.name = name;
        this.dialogues = new ArrayList<>(dialogues);
        this.inventory = new Inventory();
        this.dialogueIndex = 0;
        this.lastMessage = "";
    }

    // The game data loader calls this to fill the fixed stock from the
    // merchant table (for example Lockpick x3, Health Potion x5, just to say something)
    public void addStock(Item item, int quantity) {
        inventory.addItem(item, quantity);
    }

    @Override
    public String talk() {
        // Just in case a merchant is out of dialogue, either by exhausting it or it just isn't added.
        if (dialogues.isEmpty()) {
            return name + " has nothing to say.";
        }

        String line = dialogues.get(dialogueIndex);

        // Wrap back to the start once it runs out of lines.
        dialogueIndex = (dialogueIndex + 1) % dialogues.size();

        return line;
    }

    // Checks whether this merchant can sell the item to this player right now.
    public boolean offer(Item item, Player player) {

        // Not stocked, or the last copy was already bought.
        if (item == null || !inventory.contains(item) || inventory.getQuantity(item) < 1) {
            lastMessage = "Invalid transaction. The item is not available.";
            return false;
        }

        if (player.getGold() < item.getValue()) {
            lastMessage = "You don't have enough money, sell some items to make it back.";
            return false;
        }

        // The carry weight limit depends on the player's level, which is why
        // canAdd needs the level passed in.
        if (!player.getInventory().canAdd(item, player.getLevel())) {
            lastMessage = "You don't have enough space in your inventory, drop or sell items to make room.";
            return false;
        }

        return true;
    }

    // The merchant sells one copy of the item to the player.
    // offer() already set the failure message if it said no.
    public void sellItem(Item item, Player player) {
        if (!offer(item, player)) {
            return;
        }

        player.spendGold(item.getValue());
        inventory.removeItem(item, 1);
        player.getInventory().addItem(item, 1);

        lastMessage = "";
    }

    // The player sells one copy to the merchant. Merchants apparently have
    // infinite gold, so there is no funds check on this side. The item goes
    // into the merchant's stock so the player can buy it back later.
    public boolean buyFromPlayer(Item item, Player player) {
        if (item == null || !player.getInventory().contains(item)) {
            lastMessage = "Invalid transaction. The item is not available.";
            return false;
        }

        player.getInventory().removeItem(item, 1);
        player.gainGold(item.getValue());
        inventory.addItem(item, 1);

        lastMessage = "";
        return true;
    }

    // What the merchant says when the player types Cancel.
    public String cancelTrade() {
        return "That's a pity.";
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

 */
}
