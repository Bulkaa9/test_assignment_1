package assignment1;

public class Axebringer extends Warrior {
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 2;
    public static int BASE_ATTACK_DAMAGE;

    private int count = 0;

    public Axebringer(Tile position) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE,BASE_COST);
    }

    public int takeAction() {
        int skillpoints = 0;
        double damageDealt = 0;

        if (count == 0) {
            if (getPosition().getMonsters() != null) {
                damageDealt = getPosition().getMonster().takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
            }
            else if (getPosition().towardTheCamp().getMonsters() != null && !getPosition().towardTheCamp().isCamp()) {
                damageDealt = getPosition().towardTheCamp().getMonster().takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
            }
            if (damageDealt != 0) {
                skillpoints = (int) ((BASE_ATTACK_DAMAGE / damageDealt) + 1);
            }

            count++;
        }

        if (count == 1) {
            count = 0;
            return skillpoints;
        }

        return skillpoints;
    }
}
