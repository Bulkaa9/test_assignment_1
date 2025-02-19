package assignment1;

public class Tile {
    private boolean isCastle;
    private boolean isCamp;
    private boolean onThePath;
    private Tile towardTheCastle;
    private Tile towardTheCamp;
    private Warrior warrior;
    private MonsterTroop troop;

    public Tile() {
        this.isCastle = false;
        this.isCamp = false;
        this.onThePath = false;
        this.towardTheCastle = null;
        this.towardTheCamp = null;
        this.warrior = null;
        this.troop = new MonsterTroop();
    }

    public Tile (boolean isCastle, boolean isCamp, boolean onThePath, Tile towardTheCastle, Tile towardTheCamp, Warrior warrior, MonsterTroop troop) {
        this.isCastle = isCastle;
        this.isCamp = isCamp;
        this.onThePath = onThePath;
        this.towardTheCastle = towardTheCastle;
        this.towardTheCamp = towardTheCamp;
        this.warrior = warrior;
        this.troop = troop;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public boolean isCamp() {
        return isCamp;
    }

    public void buildCastle() {
        this.isCastle = true;
    }

    public void buildCamp() {
        this.isCamp = true;
    }

    public boolean isOnThePath() {
        return onThePath;
    }

    public Tile towardTheCastle() {
        if (isCastle || towardTheCastle == null) {
            return null;
        }
        return towardTheCastle;
    }

    public Tile towardTheCamp() {
        if (isCamp || towardTheCamp == null) {
            return null;
        }
        return towardTheCamp;
    }

    public void createPath(Tile tile_castle, Tile tile_camp) {
        if (tile_castle == null && !this.isCastle) {
            throw new IllegalArgumentException("error: incorrect pth inputs");
        }
        if (tile_camp == null && !this.isCamp) {
            throw new IllegalArgumentException("error: incorrect pth inputs");
        }
        else {
            this.towardTheCastle = tile_castle;
            this.towardTheCamp = tile_camp;
        }
    }

    public int getNumOfMonsters() {
        return troop.sizeOfTroop();
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public Monster getMonster() {
        return troop.getFirstMonster();
    }

    public Monster[] getMonsters() {
        return troop.getMonsters();
    }

    public boolean addFighter (Fighter fighter) {
        if (fighter instanceof Warrior) {
            if (this.warrior == null && !this.isCamp) {
                this.warrior = (Warrior) fighter;
                ((Warrior) fighter).setPosition(this);
                return true;
            }
            return false;
        }

        else if (fighter instanceof Monster){
            if (this.isOnThePath() ) {
                this.troop.addMonster((Monster)fighter);
                ((Monster)fighter).setPosition(this);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean removeFighter (Fighter fighter) {
        if (fighter instanceof Warrior){
            this.warrior = null;
            fighter.setPosition(null);
            return true;
        }

        if (fighter instanceof Monster){
            troop.removeMonster((Monster) fighter);
            fighter.setPosition(null);
            return true;
        }

        return false;

    }

}
