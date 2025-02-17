package assignment1;

public abstract class Warrior extends Fighter {
    private int requiredSkillPoints;
    public static double CASTLE_DMG_REDUCTION;

    public Warrior (Tile position, double health, int weaponType, int attackDamage, int requiredSkillPoints) {
        super(position, health, weaponType, attackDamage);
        this.requiredSkillPoints = requiredSkillPoints;
    }

    public int getTrainingCost(){
        return requiredSkillPoints;
    }

    @Override
    public double takeDamage (double rawDamage, int weapon_type) {
       if (this.getPosition().isCastle()) {
           rawDamage = rawDamage * (1-CASTLE_DMG_REDUCTION);
       }
       return super.takeDamage(rawDamage, weapon_type);
    }
}
