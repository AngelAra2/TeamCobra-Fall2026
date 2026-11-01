package Model;
public abstract class Item {
	/* Data Fields */
    private Room currentRoom;
    private int itemId;
    private String itemName;
    private String itemDesc;
    private double weight;
    private int value;


    private boolean collected;
    private boolean consumed;
    private boolean equipped;

	/* Constructors */
    public Item(int itemId, String itemName, String itemDesc,
                double weight, int value) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.weight = weight;
        this.value = value;

        this.collected = false;
        this.consumed = false;
        this.equipped = false;
    }
	
	/* Methods */
	
	// Private //
	
	// Getters //
    public double getWeight() {
        return this.weight;
    }

	public int getItemId()
	{
		return this.itemId;
	}
	
	public String getItemName()
	{
		return this.itemName;
	}
	
	public String getItemDescription()
	{
		return this.itemDesc;
	}

	// Setters //
	public void setCurrentRoom(Room room)
	{
		this.currentRoom = room;
	}

	// Custom //
	public void removeFromCurrentRoom()
	{
		this.currentRoom = null;
	}
	
	// Overrides //
	@Override
	public String toString()
	{
		return "[" + this.itemName + "]\n" 
				+ this.itemDesc;
	}
}

