package assignment1;

public abstract class Fighter {
    private Tile position;
    private double health;
    private int weaponType;
    private int attackDamage;

    public Fighter(Tile position, double health, int weaponType, int attackDamage) {
        this.position = position;
        if (position != null) {
            if (position.addFighter(this) == false) {
                throw new IllegalArgumentException("Fighter could not be added");
            }
        }
        this.health = health;
        this.weaponType = weaponType;
        this.attackDamage = attackDamage;
    }

    public final Tile getPosition(){
        return position;
    }

    public final double getHealth(){
        return health;
    }

    public final int getWeaponType() {
        return weaponType;
    }

    public final int getAttackDamage(){
        return attackDamage;
    }

    public void setPosition(Tile newposition) {
        this.position = newposition;
    }

    public double takeDamage (double rawDamage, int weapon_type) {
        if (weapon_type > weaponType) {
            rawDamage = 1.5 * rawDamage;
        }
        if (weapon_type < weaponType) {
            rawDamage = 0.5 * rawDamage;
        }

        this.health -= rawDamage;

        if (health <= 0) {
            position.removeFighter(this);
            this.health = 0;
            this.position = null;
        }

        return rawDamage;
    }

    public abstract int takeAction();


    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass() && ((Fighter) obj).getPosition() == this.getPosition() &&
                ((Fighter) obj).getHealth() - this.getHealth() <= 0.001 ) {
            return true;
        }

        return false;
    }

}

