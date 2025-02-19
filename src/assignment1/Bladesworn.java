package assignment1;

public class Bladesworn extends Warrior {
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 3;
    public static int BASE_ATTACK_DAMAGE;

    public Bladesworn (Tile position) {
        super(position,BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE, BASE_COST);

    }

    public int takeAction() {
        int skillpoints = 0;
        double damageDealt = 0;

        if (getPosition().getMonsters() != null) {
            damageDealt = getPosition().getMonster().takeDamage(BASE_ATTACK_DAMAGE,WEAPON_TYPE);
        }
        else if (!getPosition().towardTheCamp().isCamp() && getPosition().towardTheCamp().getMonsters() == null) {
            getPosition().towardTheCamp().addFighter(this);
            getPosition().removeFighter(this);
            this.setPosition(getPosition().towardTheCamp());
        }
        if (damageDealt != 0){
            skillpoints = (int) ((BASE_ATTACK_DAMAGE/damageDealt) + 1);
        }
        return skillpoints;
    }
}
