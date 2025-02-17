package assignment1;

public class MonsterTroop {
    private Monster[] monsters;
    private int numOfMonsters;


    private void set_numOfMonsters(){
        Monster[] temp_array = getMonsters();
        if (temp_array == null) {
            numOfMonsters = 0;
        }
        else {
            numOfMonsters = temp_array.length;
        }
    }



    public MonsterTroop() {
        monsters = new Monster[10];
    }

    public int sizeOfTroop() {
        return numOfMonsters;
    }



    public Monster[] getMonsters(){

        // determining the length of the new array
        int i = 0;
        int arraylength = 0;

        while (i < monsters.length) {
            if (monsters[i] != null) {
                arraylength ++;
            }
            i ++;
        }

        if (arraylength == 0) {
            return null;
        }

        // making the new array with no null elements
        Monster[] array = new Monster[arraylength];
        i = 0;

        while (i < monsters.length) {
            if (monsters[i] != null) {
                array[i] = monsters[i];
            }
            i ++;
        }

        return array;

    }


    public Monster getFirstMonster() {
        int i =0;

        while (i < monsters.length) {
            if (monsters[i] != null) {
                return monsters[i];
            }
        }
        return null;
    }



    private void resize() {
        Monster [] new_array = new Monster[numOfMonsters + 1];

        int i = 0;
        while (i < monsters.length) {
             new_array[i] = monsters[i];
             i++;
        }
        monsters = new_array;
    }



    public void addMonster(Monster newmonster) {
        if (numOfMonsters == monsters.length) {
            resize();
        }
        monsters[monsters.length-1] = newmonster;
        numOfMonsters++;
        getMonsters();
    }



    public boolean removeMonster(Monster monster) {
        int i =0;
        while (i < monsters.length) {
            if (monsters[i] == monster) {
                monsters[i] = null;
                numOfMonsters--;
                getMonsters();
                return true;
            }
            i++;
        }
        return false;
    }


}
