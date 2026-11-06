package Model.Combat;

import Model.*;
import Model.Util.*;

public abstract class Combatant 
{
	/* Data Fields */
	protected String name;
	protected int currentHP;
	protected int maxHP;
	protected int physicalDEF;
	protected int magicDEF;
	protected Room currentRoom;
	protected FiniteStateMachine stateMachine;

	/* Constructors */
	public Combatant(
			String name, 
			int maxHP, 
			int physicalDEF, 
			int magicDEF
		) 
	{
		this.name = name;
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		this.physicalDEF = physicalDEF;
		this.magicDEF = magicDEF;
		this.currentRoom = null;
	}

	/* Methods */
	
	// Private //
	
	// Ensure currentHP is within 0 and maxHP
	protected void setCurrentHP(int currentHP) { this.currentHP = Math.clamp(currentHP, 0, this.maxHP); }
	
	// Ensure maxHP is at least 1
	protected void setMaxHP(int maxHP) { this.maxHP = Math.max(maxHP, 1); }
	
	// Getters //
	public String getName() { return name; }
	public int getCurrentHP() { return currentHP; }
	public int getMaxHP() { return maxHP; }
	public boolean isAlive() { return (currentHP > 0); }

	// Setters //
	public void setCurrentRoom(Room room)
	{
		this.currentRoom = room;
	}

	// Custom //
	public void removeFromCurrentRoom()
	{
		this.currentRoom = null;
	}

	public void takeDamage(int damage, String dmgType)
	{
		// Calculate effective damage based on damage type and defense
		int effectiveDamage = damage;
		
		switch(dmgType.toLowerCase()) {
			case "physical":
				effectiveDamage -= physicalDEF;
				break;
			case "magic":
				effectiveDamage -= magicDEF;
				break;
			default:
				// If damage type is unknown, no defense is applied
				break;
		}
		
		// Ensure damage is not negative
		effectiveDamage = Math.max(effectiveDamage, 0);
		
		// Reduce current HP by effective damage
		setCurrentHP(this.getCurrentHP() - effectiveDamage);
	};
	
	// Heal the combatant by a certain amount
	public void heal(int healing) 
	{ 
		setCurrentHP(this.getCurrentHP() + healing);
	}
	
	// Overrides //
}

