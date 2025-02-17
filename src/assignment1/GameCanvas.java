package assignment1;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/*
 * Game board rendering class
 * Author: Lili Wei + Giulia Alberini
 */

public class GameCanvas extends Canvas {

    final static Color CASTLE_COLOR = new Color(255,187,8); // dark yellow
    final static Color CAMP_COLOR = Color.MAGENTA;
    final static Color PATH_COLOR = Color.lightGray;
    final static Color SWORDSMAN_COLOR = new Color(247,238,81); // light yellow
    final static Color AXEMAN_COLOR = new Color(118,154,249); // light blue
    final static Color LANCER_COLOR = new Color(97,67,239); // purple
    final static Color GRASSLAND_COLOR = new Color(148,214,133); //new Color(102,255,102); // light green

    Map _map;

    private boolean _mapGenerated = false;


    public GameCanvas(int canvasX, int canvasY, int canvasWidth, int canvasHeight) {
        //this.setBackground();

        this.setBounds(canvasX, canvasY, canvasWidth, canvasHeight);
    }

    public void registerMap(Map map) {
        _map = map;
    }

    // paint is called automatically every turn
    // update map based on map object status
    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);

        if (_map == null) return;

        Tile[][] tiles = _map.getAllTiles();
        int size = _map.getTileSize();

        for (int i=0; i<tiles.length; i++) {
            for (int j=0; j<tiles[i].length; j++) {
                int x = size * i + 1;
                int y = size * j + 1;

                if (tiles[i][j] != null) {
                    if (tiles[i][j].isCastle()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(CASTLE_COLOR);
                        g.fillRect(x, y, size, size);
                    }
                    else if (tiles[i][j].isCamp()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(CAMP_COLOR);
                        g.fillRect(x, y, size, size);
                    } else if (tiles[i][j].isOnThePath()) {
                        g.setColor(Color.black);
                        g.drawRect(x, y, size, size);
                        g.setColor(PATH_COLOR);
                        g.fillRect(x, y, size, size);
                    } else {
                    	g.setColor(Color.BLACK);
                        g.drawRect(x, y, size, size);
                        g.setColor(GRASSLAND_COLOR);
                        g.fillRect(x, y, size, size);
                    }

                    if (tiles[i][j].getWarrior() != null) {
                        Warrior warrior = tiles[i][j].getWarrior();
                        if (warrior instanceof Bladesworn) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(SWORDSMAN_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                        else if (warrior instanceof Axebringer) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(AXEMAN_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                        else if (warrior instanceof Lanceforged) {
                            g.setColor(Color.black);
                            g.drawRect(x, y, size, size);
                            g.setColor(LANCER_COLOR);
                            g.fillRect(x, y, size, size);
                        }
                    }
                    if (tiles[i][j].getNumOfMonsters() > 0) {
                        String string = "" + tiles[i][j].getNumOfMonsters();
                        int fontSize = 20;
                        g.setColor(Color.BLACK);

                        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
                        
                        g.drawString(string, x + 5, y + size - 5);
                    }
                }
                else {
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, size, size);
                }
            }
        }
    }

}
