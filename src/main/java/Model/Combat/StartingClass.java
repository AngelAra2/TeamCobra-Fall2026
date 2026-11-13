package Model.Combat;

import java.util.ArrayList;

public class StartingClass {
    private int id;
    private String name;
    private String background;
    private int baseMaxHP;
    private int baseSTR;
    private int baseDEX;
    private int baseINT;
    private int baseARC;
    private int baseEND;
    private ArrayList<String> combatAbility;
    private ArrayList<String> defaultEquipment;

    public StartingClass(int id, String name, String background, int baseMaxHP, int baseSTR,
                         int baseDEX, int baseINT, int baseARC, int baseEND,
                         ArrayList<String> combatAbility, ArrayList<String> defaultEquipment){
        this.id = id;
        this.name = name;
        this.background = background;
        this.baseMaxHP = baseMaxHP;
        this.baseSTR = baseSTR;
        this.baseDEX = baseDEX;
        this.baseINT = baseINT;
        this.baseARC = baseARC;
        this.baseEND = baseEND;
        this.combatAbility = combatAbility;
        this.defaultEquipment = defaultEquipment;



    }

    public StartingClass(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public int getBaseMaxHP() {
        return baseMaxHP;
    }

    public int getBaseSTR() {
        return baseSTR;
    }

    public int getBaseDEX() {
        return baseDEX;
    }

    public int getBaseINT() {
        return baseINT;
    }

    public int getBaseARC() {
        return baseARC;
    }

    public int getBaseEND() {
        return baseEND;
    }

    public ArrayList<String> getCombatAbility() {
        return combatAbility;
    }

    public ArrayList<String> getDefaultEquipment() {
        return defaultEquipment;
    }
}

//bleh
