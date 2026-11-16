package Model.Rewards;

import Model.AbstractClasses.Item;
import Model.Combat.Player;
import Model.Interfaces.Rewardable;
import Model.Util.ItemUtil;
import View.View;

//Dylan
public class ItemReward implements Rewardable {


    private Item puzzleItemReward;



    /* Constructors */
    public ItemReward(Item puzzleItemReward) {
        this.puzzleItemReward = puzzleItemReward;
    }
    @Override
    public void give(Player player) {
        if (puzzleItemReward == null || player == null)
            return;

        Item reward = ItemUtil.copyItem(puzzleItemReward);
        if (reward == null) {
            throw new IllegalStateException("Unsupported item reward type"); // Shouldn't see this, just a failsafe.
        }

        if (player.addItemToInventory(reward)) {
            View.printColored("You received " + reward.getItemName() + "!", "green", true);
        } else {
            View.printColored("Your inventory is too heavy to carry "
                    + reward.getItemName() + ".", "red", true);
        }
    }
}
