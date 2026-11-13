package Model.Factories;

import Model.AbstractClasses.Factory;
import Model.Combat.Monster;
import Model.Data.DataExtractionMethods;
import Model.Data.DataFileExtraction;
import Model.Exploration.Map;
import Model.Exploration.Room;

public class MonsterFactory extends Factory<Monster>
{
	/* Constructors */
	public MonsterFactory(String filePath, Map worldMap) { super(filePath, worldMap); }

	/* Methods */
	
	// Private //

	// Custom //
	public Monster create(int id)
	{
		String objectData = DataFileExtraction.extractObjectData(DataExtractionMethods::extractObjectData, this.filePath, id);
		
		// Guard Clauses //
		// Object data shouldn't be null nor empty
		if (objectData == null || objectData.isEmpty())
			return null;
		
		/* The fun part: Create a new Monster object using the extracted data */
		objectData = objectData.trim();

		// Use delimiter to split string into readable attributes
		String[] attributes = DataExtractionMethods.trimStringArray(objectData.split(DataExtractionMethods.DELIMITER_A));
		
		// Declare required attributes
		int monsterId;
		String monsterName = "Monster";
		String monsterDesc = "...";
		int goldReward = 1;
		int xpReward = 1;
		int maxHP = 10;
		int physicalDEF = 0;
		int magicDEF = 0;
		int attack = 1;
		String damageType = "physical";
		boolean isBoss = false;
		String possibleDropsString = "";
		
		// Read monsterId
		try { monsterId = Integer.parseInt(attributes[0]); }
		catch (NumberFormatException e) { return null; }
		catch (ArrayIndexOutOfBoundsException e) { return null; }
		
		// Read monsterName
		try { monsterName = attributes[1]; }
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read monsterDesc
		try { monsterDesc = attributes[2]; }
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read goldReward
		try { goldReward = Integer.parseInt(attributes[3]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read xpReward
		try { xpReward = Integer.parseInt(attributes[4]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank

		// Read maxHP
		try { maxHP = Integer.parseInt(attributes[5]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read physicalDEF
		try { physicalDEF = Integer.parseInt(attributes[6]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read magicDEF
		try { magicDEF = Integer.parseInt(attributes[7]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read attack
		try { attack = Integer.parseInt(attributes[8]); }
		catch (NumberFormatException e) {} // Leave blank
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read damageType
		try { damageType = attributes[9]; }
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank

		// Read isBoss
		try { isBoss = Boolean.parseBoolean(attributes[10]); }
		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		// Read possibleDropsString
//		try { isBoss = Boolean.parseBoolean(attributes[10]); }
//		catch (ArrayIndexOutOfBoundsException e) {} // Leave blank
		
		Monster newMonster = new Monster(
			monsterName, 
			maxHP, 
			physicalDEF, 
			magicDEF,
			attack,
			
			monsterId,
			monsterDesc,
			goldReward,
			xpReward,
			damageType,
			isBoss
		);
		
		return newMonster;
	}
}

