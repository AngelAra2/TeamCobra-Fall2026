package Model;

import java.util.*;
import View.View;

public class LoreReward implements Rewardable
{
    /* Data Fields */
    private List<String> puzzleLoreReward;

    /* Constructors */
    public LoreReward(List<String> puzzleLoreReward)
    {
        this.puzzleLoreReward = puzzleLoreReward;
    }

    /* Methods */
    @Override
    public void give(Player player)
    {
        // Nothing to show, just in case!
        if (this.puzzleLoreReward == null || this.puzzleLoreReward.isEmpty())
            return;

        View.println("");

        //most likely will need to change this
        for (String entry : this.puzzleLoreReward)
            View.println("- " + entry);
    }
}