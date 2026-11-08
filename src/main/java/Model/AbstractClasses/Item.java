package Model.AbstractClasses;

import Model.Exploration.Room;

public abstract class Item 
{
	/* Data Fields */
	private int itemId;
	private String itemName;
	private String itemDesc;
	private Room currentRoom;

	/* Constructors */
	public Item(
		int itemId,
		String itemName,
		String itemDesc
	)
	{
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDesc = itemDesc;
		this.currentRoom = null;
	}
	
	/* Methods */

	// Getters //
	public int getItemId() { return this.itemId; }
	public String getItemName() { return this.itemName; }
	public String getItemDescription() { return this.itemDesc; }

	// Setters //
	public void setCurrentRoom(Room room) { this.currentRoom = room; }

	// Custom //
	public void removeFromCurrentRoom() { this.currentRoom = null; }
	
	// Overrides //
	@Override
	public String toString()
	{
		return "[" + this.itemName + "]\n" 
				+ this.itemDesc;
	}
}

