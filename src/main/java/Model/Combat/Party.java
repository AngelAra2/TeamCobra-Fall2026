package Model.Combat;

import java.util.ArrayList;
import java.util.List;

import View.View;

/*
Party class
Refer to 3.2.2 User Class 2: NPC Companions
ID: FR32, FR33, FR34, FR35
 */
//Priscilla, Danh, Dylan
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
            return false;
        }

        //updates companion's party status to recruited
        companion.recruit();
        companion.setPartyStatus("Recruited");
        
        // Set companion's XP to match the player's XP
        companion.addXP(this.player.getCurrentXP());

        //if statement checks if party has max active companions, if so, add companion to inactiveCompanions list
        if(activeCompanions.size() == maxActiveCompanions){
            inactiveCompanions.add(companion);
            return true;
        }

        activeCompanions.add(companion);
        return true;

    }

    /*
    ID: FR34
    This method handles the player's request on adding a companion to their active party.
     */

    /*TODO: Can add a companion who was never recruited, should be restricted to already-recruited companions.
     TODO: Check if that companion belongs to the inactive party!*/
    public boolean addCompanionToActiveParty(Companion companion){
        //if companion already exists in active party, disp msg and return true.
        if (activeCompanions.contains(companion)){
            return true;
        }

        //if the size of the activeCompanion is not 2, then add companion to active party.
        if(activeCompanions.size() != maxActiveCompanions){
            activeCompanions.add(companion);
            inactiveCompanions.remove(companion);
            return true;
        } else{
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
            return true;
        }

        //removes companion from active party
        activeCompanions.remove(companion);

        //adds companion to inactive party
        inactiveCompanions.add(companion);

        return true;
    }


    //temp
    public List<Companion> getActiveCompanions(){
        return activeCompanions;
    }

    //temp
    public List<Companion> getInactiveCompanions(){
        return inactiveCompanions;
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
