package Model.Factories;

import Model.Combat.CombatSkill;
import Model.AbstractClasses.Factory;
import Model.Data.DataExtractionMethods;
import Model.Data.DataFileExtraction;

//Angel Aragon
public class CombatSkillFactory extends Factory<CombatSkill>
{
    private static final String COMBATSKILLS_FILE_PATH = "/gameObjects/CombatSkills";

    public CombatSkillFactory() { super(COMBATSKILLS_FILE_PATH);}


    @Override
    public CombatSkill create(int id){
        String objectData = DataFileExtraction.extractObjectData(DataExtractionMethods::extractObjectData, this.filePath,id);

        //if string is null or empty, return null
        if (objectData == null || objectData.isEmpty())
            return null;

        objectData = objectData.trim();

        String[] attributes = DataExtractionMethods.trimStringArray(objectData.split(DataExtractionMethods.DELIMITER_A));

        //ensures that all 21 fields are filled out in file.
        if (attributes.length != 21){
            throw new IllegalArgumentException(
                    "Invalid combat skill data for ID: " + id
            );
        }

        //Declares required attributes
        int combatSkillId = Integer.parseInt(attributes[0]); //Read ID
        String name = attributes[1]; //Read combat skill name
        String skillType = attributes[2]; //Read Combat skill type
        String targetType = attributes[3]; //Read target type
        String requiredWeaponType = attributes[4]; //Read weapon type required
        String requiredWeaponSlot = attributes[5]; //Read required weapon slot

        //assigns weapon ID, returns -1 if weapon is not required to use skill
        int requiredWeaponID = attributes[6].equalsIgnoreCase("NONE")
                                ? -1
                                : Integer.parseInt(attributes[6]);


        String requiredCharacter = attributes[7]; //Read character required to use skill
        int physicalDamage = Integer.parseInt(attributes[8]); //Read physical dmg
        int magicDamage = Integer.parseInt(attributes[9]); //Read magic damage
        int manaCost = Integer.parseInt(attributes[10]); //Read mana cost
        int hitCount = Integer.parseInt(attributes[11]); //Read hit count
        double damageMultiplier = Double.parseDouble(attributes[12]); //Read damage multiplier
        String effectType = attributes[13]; //Read effect type
        double effectValue = Double.parseDouble(attributes[14]); //Read effect value
        int effectDuration = Integer.parseInt(attributes[15]); //Read effect duration
        double effectChance = Double.parseDouble(attributes[16]); //Read effect chance
        boolean ignoresDefense = Boolean.parseBoolean(attributes[17]); //Read ignoresDefense
        int ammunitionCost = Integer.parseInt(attributes[18]); //Read ammunition cost
        int flatHolyDamage = Integer.parseInt(attributes[19]); //Read flat holy damage
        String description = attributes[20]; //Read description

        return new CombatSkill(combatSkillId, name, skillType,
                targetType,requiredWeaponType, requiredWeaponSlot,
                requiredWeaponID, requiredCharacter, physicalDamage,
                magicDamage, manaCost, hitCount, damageMultiplier, effectType,
                effectValue, effectDuration, effectChance, ignoresDefense, ammunitionCost, flatHolyDamage,
                description);
    }


    public CombatSkill createByName(String name){
        if (name == null || name.isBlank()){
            return null;
        }

        //creates combat skill object based off of ID, if the name of the skill matches the name, return skill
        for(int id = 1; id <= 13; id++){
            CombatSkill skill = create(id);

            //if skill is not null, and the skill name matches the name received, return the skill.
            if (skill != null && skill.getName().equalsIgnoreCase(name.trim())){
                return skill;
            }
        }

        return null;
    }

}
