package Model.Combat;

import java.util.List;

import Model.Room;

public class Companion extends Character {
	/* Data Fields */
	private int id;
	private String partyStatus;
	private String perkDescription;
	private boolean isRecruited;
	private Room homeRoom;
	//private List<Dialogue> dialogues;

	/* Constructors */
	public Companion(
			String name,
			int maxHP,
			int physicalDEF,
			int magicDEF,
			
			int currentLevel,
			int currentXP,
			int currentMana,
			int baseMaxHP,
			int strength,
			int intelligence,
			int dexterity,
			int arcane,
			int endurance,
			List<CombatSkill> skills,

			int id,
			String partyStatus,
			String perkDescription,
			boolean isRecruited,
			Room homeRoom
		) 
	{
		super(
				name, 
				maxHP, 
				physicalDEF, 
				magicDEF, 
				currentLevel, 
				currentXP, 
				currentMana, 
				baseMaxHP, 
				strength, 
				intelligence, 
				dexterity, 
				arcane, 
				endurance, 
				skills
			);
		
		this.id = id;
		this.partyStatus = partyStatus;
		this.perkDescription = perkDescription;
		this.isRecruited = isRecruited;
		this.homeRoom = homeRoom;
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

	// Setters //
	public void setPartyStatus(String partyStatus) { this.partyStatus = partyStatus; }

	// Custom //
	public void recruit() { this.isRecruited = true; }
	
	// (WIP 10.29.2026)
	/*
	public String talk()
	{
		return "";
	}
	*/
	
	// (WIP 10.29.2026)
	/*
	public void applyPerk()
	{
		
	}
	*/
	
	// Overrides //

}
