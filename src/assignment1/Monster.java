package assignment1;

public class Monster extends Fighter {
    public Monster (Tile position, double health, int weaponType, int attackDamage){
        super(position, health, weaponType, attackDamage);
    }
    private int rageLevel = 0;
    private boolean win;
    public static int BERSERK_THRESHOLD;

    @Override
    public int takeAction() {
        if (rageLevel >= BERSERK_THRESHOLD ) {
            int action_count = 0;
            while (action_count < 2) {
                if (getPosition().getWarrior() != null) {
                    getPosition().getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
                    getPosition().removeFighter(this);
                    getPosition().addFighter(this);
                    action_count++;
                }
                if (!getPosition().isCastle() &&  getPosition().towardTheCastle() != null ) {
                    getPosition().removeFighter(this);
                    setPosition(getPosition().towardTheCastle());
                    getPosition().addFighter(this);
                    action_count++;
                }

                if (getPosition().isCastle()) {
                    win = true;
                    break;
                }


                if (getPosition().getWarrior() != null) {
                    getPosition().getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
                    getPosition().removeFighter(this);
                    getPosition().addFighter(this);
                    action_count++;
                }
                if (!getPosition().isCastle() &&  getPosition().towardTheCastle() != null ) {
                    getPosition().removeFighter(this);
                    setPosition(getPosition().towardTheCastle());
                    getPosition().addFighter(this);
                    action_count++;
                }

                if (getPosition().isCastle()) {
                    win = true;
                    break;
                }

            }
            rageLevel = 0;
            return 0;
        }

        else {
            if (getPosition().getWarrior() != null) {
                getPosition().getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
                getPosition().removeFighter(this);
                getPosition().addFighter(this);
            } else {
                getPosition().removeFighter(this);
                setPosition(getPosition().towardTheCastle());
                getPosition().addFighter(this);
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass() && ((Fighter) obj).getPosition() == this.getPosition() &&
                ((Fighter) obj).getHealth() - this.getHealth() <= 0.001 &&
                ((Fighter) obj).getAttackDamage() == this.getAttackDamage() ) {
            return true;
        }
        return false;
    }

    @Override
    public double takeDamage (double rawDamage, int weapon_type) {
        double result = takeDamage (rawDamage,weapon_type);
        if (this.getWeaponType() < weapon_type && weapon_type - this.getWeaponType() >= 0 && this.getHealth() >= 0) {
            rageLevel += weapon_type - this.getWeaponType();
        }
        return result;
    }


}