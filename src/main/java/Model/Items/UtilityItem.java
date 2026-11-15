//package Model.Items;
//
//import Model.AbstractClasses.Item;
//
//public class UtilityItem extends Item {
//
//    public UtilityItem(int itemId, String itemName,
//                       String itemDesc, double weight, int value) {
//        super(itemId, itemName, itemDesc, weight, value);
//    }
//}
//HAVEN'T GOTTEN TO


//package Model.Items;
//
//import Model.AbstractClasses.Item;
//
//public class UtilityItem extends Item {
//
//    /* Constructor */
//
//    // Creates a utility item using information from its resource file.
//    public UtilityItem(int itemId, String itemName, String itemDesc,
//                       double weight, int value) {
//
//        super(itemId, itemName, itemDesc, weight, value);
//    }
//}

package Model.Items;

import Model.AbstractClasses.Item;

// Represents items used for exploration and other utility actions.
public class UtilityItem extends Item {

    private String utilityType;
    private String utilityAction;
    private boolean consumedWhenUsed;

    public UtilityItem(int itemId, String itemName, String itemDesc,
                       double weight, int value, String utilityType,
                       String utilityAction, boolean consumedWhenUsed) {

        super(itemId, itemName, itemDesc, weight, value);

        this.utilityType = utilityType;
        this.utilityAction = utilityAction;
        this.consumedWhenUsed = consumedWhenUsed;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public String getUtilityAction() {
        return utilityAction;
    }

    public boolean isConsumedWhenUsed() {
        return consumedWhenUsed;
    }
}

