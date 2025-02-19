package assignment1;

public class Lanceforged extends Warrior {
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 3;
    public static int BASE_ATTACK_DAMAGE;

    private int piercingPower;
    private int actionRange;

    public Lanceforged(Tile position, int piercingPower, int actionRange) {
        super(position, BASE_HEALTH, BASE_COST, WEAPON_TYPE, BASE_ATTACK_DAMAGE);
        this.piercingPower = piercingPower;
        this.actionRange = actionRange;
    }


    @Override
    public int takeAction() {
        int skillpoints = 0;
        double damageDealt = 0;

        int i = 0;
        Tile int_tile = this.getPosition();
        Tile target = null;

        while (i < actionRange) {
            int_tile = int_tile.towardTheCamp();
            if (int_tile.getMonsters() != null && !int_tile.isCamp() && int_tile.isOnThePath()){
                target = this.getPosition();
                break;
            }
           else {
                i ++;
            }
        }

        if (target != null) {
            i = 0;

            while (i < piercingPower && target.getMonsters()[i] != null) {
                Monster victim = target.getMonsters()[i];
                damageDealt = victim.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
                skillpoints += (int) ((BASE_ATTACK_DAMAGE/damageDealt) + 1);
                i++;

            }

            skillpoints = skillpoints / i;
        }

        return skillpoints;
    }
}
