package Model.Combat;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

/*
Party class
Refer to 3.2.2 User Class 2: NPC Companions
ID: FR32, FR33, FR34, FR35
 */
public class Party {
    private Player player;

    private List<Companion> activeCompanions;
    private List<Companion> inactiveCompanions;
    private int maxActiveCompanions = 2;

    public Party(Player player){
        this.player = player;
        this.activeCompanions = new ArrayList<>();
        this.inactiveCompanions = new ArrayList<>();
    }




    //per inheritor documentation, player MUST have talked to the companion before they can be recruited.
    //ID: FR32
    public boolean recruit(Companion companion){

        //if companion already exists in player's party, disp message, and return false.
        if (activeCompanions.contains(companion) || inactiveCompanions.contains(companion)){
            System.out.println(companion.getName() + " already exists in party.");
            return false;
        }

        //updates companion's party status to recruited
        companion.setPartyStatus("Recruited");

        //if statement checks if party has max active companions, if so, add companion to inactiveCompanions list
        if(activeCompanions.size() == maxActiveCompanions){
            System.out.println("New companion recruited!" + companion.getName() + " added to inactive party.");
            inactiveCompanions.add(companion);
            return true;
        }


        System.out.println("New companion recruited! " + companion.getName() + " added to the active party!");
        activeCompanions.add(companion);
        return true;

    }

    /*
    ID: FR34
    This method handles the player's request on adding a companion to their active party.
     */
    public boolean addCompanionToActiveParty(Companion companion){
        //if companion already exists in active party, disp msg and return true.
        if (activeCompanions.contains(companion)){
            System.out.println(companion.getName() + " is already in the active party!");
            return true;
        }

        //if the size of the activeCompanion is not 2, then add companion to active party.
        if(activeCompanions.size() != maxActiveCompanions){
            System.out.println(companion.getName() + " moved to active party!");
            activeCompanions.add(companion);
            inactiveCompanions.remove(companion);
            return true;
        } else{
            //display error message and return false.
            System.out.println("There is no room in the active party!" + "Remove someone from the current party using 'Remove (Companion Name)'" );
            return false;
        }



    }

    /*
    ID: FR33
    This method handles the player's request on adding a companion to their active party.
     */
    public boolean removeCompanionFromActiveParty(Companion companion){
        //checks if companion is in active party.
        if (!activeCompanions.contains(companion)){
            System.out.println(companion.getName() + " is not currently in the active party.");
            return true;
        }

        //removes companion from active party
        activeCompanions.remove(companion);

        //adds companion to inactive party
        inactiveCompanions.add(companion);

        System.out.println(companion.getName() + " moved to inactive party.");

        return true;
    }


    //temp
    public void getActiveCompanions(){
        for (Companion companion: activeCompanions){
            System.out.println(companion.getName());
        }
    }

    //temp
    public void getInactiveCompanions(){
        for (Companion companion : inactiveCompanions){
            System.out.println(companion.getName());
        }
    }

    //temp
    public void getAllCompanions(){
        getActiveCompanions();
        getInactiveCompanions();
    }
    //returns player name, and active companions
    public void getCombatMembers(){

    }


    //method checks if party is defeated
    //if method returns true, that indicates that player and active party have 0 hp.
    //returns false if at least one member does not have 0 hp.
    public boolean isPartyDefeated(){

        //if player is still alive, party isn't defeated.
        if (player.getCurrentHP() > 0){
            return false;
        }

        //checks if any companion is still alive.
        for (Companion companion : activeCompanions){
            if (companion.getCurrentHP() > 0){
                return false;
            }
        }

        return true;
    }



    /*
    GETTERS and SETTERS
     */

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setActiveCompanions(List<Companion> activeCompanions) {
        this.activeCompanions = activeCompanions;
    }

    public void setInactiveCompanions(List<Companion> inactiveCompanions) {
        this.inactiveCompanions = inactiveCompanions;
    }

    public int getMaxActiveCompanions() {
        return maxActiveCompanions;
    }

    public void setMaxActiveCompanions(int maxActiveCompanions) {
        this.maxActiveCompanions = maxActiveCompanions;
    }







}
