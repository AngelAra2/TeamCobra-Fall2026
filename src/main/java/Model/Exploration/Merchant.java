package Model.Exploration;

import java.util.*;

import Model.AbstractClasses.Item;
import Model.Combat.Player;
import Model.Interfaces.NPCable;
public class Merchant implements NPCable {




    private String id;
    private String name;
    private List<String> dialogue;
    private HashMap<String, ArrayList<Item>> stock;
    private int dialogueIndex;

    public Merchant(String id, String name, List<String> dialogue, HashMap<String, ArrayList<Item>> stock) {
        this.id = id;
        this.name = name;
        this.dialogue = new ArrayList<>(dialogue);
        this.stock = new HashMap<>();

        //To explain what this does, it copies each stock list into the merchant's map, so the merchant gets its own unique list
        // without sharing the OG ones.
        for (String itemName : stock.keySet()) {
            this.stock.put(itemName, new ArrayList<>(stock.get(itemName)));
        }
        this.dialogueIndex = 0;
    }

    //WIP 11/11/2026
    @Override
    public String talk() {
        if (dialogue.isEmpty()) {
            return name + " has nothing to say.";
        }

        String line = dialogue.get(dialogueIndex);
        dialogueIndex = (dialogueIndex + 1) % dialogue.size(); // Tracks which dialogue line to return next, cycling back to the first line at the end.
        return line;
    }

    //Adds an item to the merchant's stock, grouped under the item's lowercased name. It actively rejects null.
    public void addStock(Item item) {
        Objects.requireNonNull(item);
        String itemName = item.getItemName().toLowerCase();

        ArrayList<Item> items = stock.get(itemName);
        if (items == null) {
            items = new ArrayList<>();
            stock.put(itemName, items);
        }

        items.add(item);
    }

    // Returns true if that exact item instance is in the merchant's stock, otherwise returns false.
    public boolean offer(Item item) {
        if (item == null) {
            return false;
        }

        ArrayList<Item> items = stock.get(item.getItemName().toLowerCase());
        return items != null && items.contains(item);
    }

    // Checks that the player exists and the item is available, removes the item from the merchant's stock, and adds it to the player's inventory. It returns whether or not the sale succeeded.
    public boolean sellItem(Item item, Player player) {
        if (player == null || !offer(item)) {
            return false;
        }

        String itemName = item.getItemName().toLowerCase();
        ArrayList<Item> items = stock.get(itemName);
        items.remove(item);

        if (items.isEmpty()) {
            stock.remove(itemName);
        }

        player.addItemToInventory(item);
        return true;
    }

    // Returns a copy of the stock map and its lists, so callers can inspect the stock without directly changing the merchant's collections.
    public HashMap<String, ArrayList<Item>> getStock() {
        HashMap<String, ArrayList<Item>> copy = new HashMap<>();
        stock.forEach((name, items) -> copy.put(name, new ArrayList<>(items)));
        return copy;
    }
}
