package Model;

public class ItemReward implements Rewardable {


    private Item puzzleItemReward;



    /* Constructors */
    public ItemReward(Item puzzleItemReward) {
        this.puzzleItemReward = puzzleItemReward;
    }
    @Override
    public void give(Player player) {
        //Nothing to give, just in case

        if (this.puzzleItemReward == null)
            return;


        // Add the item to the player's inventory

    }
}
