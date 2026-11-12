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


package Model.Items;

import Model.AbstractClasses.Item;

public class UtilityItem extends Item {

    /* Constructor */

    // Creates a utility item using information from its resource file.
    public UtilityItem(int itemId, String itemName, String itemDesc,
                       double weight, int value) {

        super(itemId, itemName, itemDesc, weight, value);
    }
}

