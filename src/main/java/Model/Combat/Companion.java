package Model.Combat;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import Model.AbstractClasses.Character;
import Model.Exploration.Room;
import Model.Interfaces.NPCable;

public class Companion extends Character implements NPCable
{
    /* Data Fields */
    private int id;
    private String partyStatus;
    private String perkDescription;
    private boolean isRecruited;
    private Room homeRoom;
    private String backstory;
    private List<String> defaultCombatAbilities; // PLACEHOLDER UNTIL THE COMBAT ABILITY CLASS IS IMPLEMENTED
    private List<String> defaultEquipment; // PLACEHOLDER UNTIL THE EQUIPMENT CLASS IS IMPLEMENTED
    //private List<Dialogue> dialogues;

    /* Constructors */
    public Companion(
            String name,
            String backstory,
            int baseMaxHP,
            int strength,
            int dexterity,
            int intelligence,
            int arcane,
            int endurance,
            String perkDescription,
            List<String> defaultCombatAbilities,
            List<String> defaultEquipment,
            int id,
            Room homeRoom
    )
    {
        super(
                name,
                baseMaxHP,
                0,
                0,
                strength,
                1,
                0,
                30 + (2 * intelligence),
                baseMaxHP,
                strength,
                intelligence,
                dexterity,
                arcane,
                endurance,
                new ArrayList<>()
        );

        this.id = id;
        this.partyStatus = null;
        this.perkDescription = perkDescription;
        this.isRecruited = false;
        this.homeRoom = homeRoom;
        this.backstory = backstory;
        this.defaultCombatAbilities = new ArrayList<>(defaultCombatAbilities);
        this.defaultEquipment = new ArrayList<>(defaultEquipment);
    }

    /* Methods */

    // Private //

    // Getters //
    // (WIP 10.29.2026)
	/*
	public boolean isRecruitable()
	{
		return false;
	}
	*/

    public int getId() { return this.id; }
    public String getPartyStatus() { return this.partyStatus; }
    public String getPerkDescription() { return this.perkDescription; }
    public boolean isRecruited() { return this.isRecruited; }
    public Room getHomeRoom() { return this.homeRoom; }
    public String getBackstory() { return this.backstory; }
    public List<String> getDefaultCombatAbilities() {
        return new ArrayList<>(this.defaultCombatAbilities);
    }
    public List<String> getDefaultEquipment() {
        return new ArrayList<>(this.defaultEquipment);
    }

    // Setters //
    public void setPartyStatus(String partyStatus) { this.partyStatus = partyStatus; }

    // Custom //
    public void recruit() { this.isRecruited = true; }


    // (WIP 10.29.2026)
	/*
	public void applyPerk()
	{
		
	}
	*/

    // Overrides //
    // WIP 11/11/2026
    @Override
    public String talk() {
        return "";
    }
}
