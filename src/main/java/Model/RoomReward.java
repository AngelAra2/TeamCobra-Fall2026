package Model;

import View.View;

public class RoomReward implements Rewardable
{
    /* Data Fields */
    private Room fromRoom;
    private String direction;
    private Room toRoom;

    /* Constructors */
    public RoomReward(Room fromRoom, String direction, Room toRoom)
    {
        this.fromRoom = fromRoom;
        this.direction = direction;
        this.toRoom = toRoom;
    }

    /* Methods */
    @Override
    public void give(Player player)
    {
        // Nothing to open
        if (this.fromRoom == null || this.toRoom == null || this.direction == null)
            return;

        // Open the exit by adding it to fromRoom's exits
        String dir = this.direction.toLowerCase();
        this.fromRoom.getExits().put(dir, this.toRoom.getRoomId());

        View.println("A new exit has opened to the " + dir + " of "
                + this.fromRoom.getRoomName() + ", leading to "
                + this.toRoom.getRoomName() + "!");
    }
}
