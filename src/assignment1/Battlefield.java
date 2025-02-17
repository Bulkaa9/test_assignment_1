package assignment1;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

/*
 * Main game loop
 * Author: Lili Wei + Giulia Alberini
 */

import javax.swing.BoxLayout;
import javax.swing.Timer;

enum EnumWarriorType {
	bladesworn,
	axebringer,
	// SPECIAL UNITS
	lanceforged
}

public class Battlefield extends Frame {
	private static final Random RND_GEN = new Random();

	private static final String[] MSG_WARRIOR_DEFEAT = {"!!A somber moment on the battlefield: %d brave "
			+ "warriors have sacrificed their lives in defense of the castle.\n", "!!Your valiant warriors fought bravely, "
					+ "but alas, %d fell victim to the invading monsters this round.\n", "!!The buzz of sorrow "
							+ "fills the air as %d of your fiercest warriors meet their end, sacrificing themselves for the safety of the castle.\n"};
	private static final String[] MSG_MONSTER_DEFEAT = {"!!A resounding hum of victory! Your warrior forces deliver a decisive attack, "
			+ "eliminating %d monsters in this round.\n", "!!The castle stands strong as your warriors fought with the enemy, taking down "
					+ "%d monsters with their relentless attack.\n", "!!The air is clear as the threat diminishes — %d monsters "
							+ "succumb to the might of your formidable warriors.\n"};


	private static final int RES_X = 1200;
	private static final int RES_Y = 900;

	// costs
	private static final int BLADESWORN_COST = 4;
	private static final int AXEBRINGER_COST = 3;
	private static final int LANCEFORGED_COST = 6;
	
	// health
	private static final int BLADESWORN_HEALTH = 20;
	private static final int AXEBRINGER_HEALTH = 100;
	private static final int LANCEFORGED_HEALTH = 70;

	// abilities

	private static final int BLADESWORN_ATTACK = 5;
	private static final int AXEBRINGER_ATTACK = 10;
	private static final int LANCEFORGED_ATTACK = 3;

	private static final int LANCEFORGED_PIERCING_POWER = 3;
	private static final int LANCEFORGED_ACTION_RANGE = 3;
	
	private static final double CASTLE_DMG_REDUCTION = 10.0/100.0;
	
	
	


	private static final int MONSTER_ATTACK = 10;
	private static final int MONSTER_HEALTH = 100;

	static final int STARTING_SKILLPOINTS = 10;

	private boolean _inSelection = false;
	private EnumWarriorType _selectedWarriorType;

	private int canvasX;
	private int canvasY;
	private int canvasWidth;
	private int canvasHeight;
	private static GameCanvas _boardCanvas;
	private static Label _msgPanel;
	private static Label _skillPointPanel;
	private static TextArea _logPanel;
	private static Timer tick;
	private static Button bladeswornBtn;
	private static Button axebringerBtn;
	private static Button lanceforgedBtn;
	private static Button startBtn;
	private static Button endBtn;
	private static Button spawnBtn;

	private long _frameCount;
	private int _timePerFrame = 5000;	// 5 seconds per frame, increase to slow down the game
	private boolean _gameOver;

	private int _totalSkillPoints;

	private LinkedList<Fighter> deadFighters;
	private static Map map;
	private static LinkedList<Monster> monsters;
	private static LinkedList<Warrior> warriors;
	private static StringBuffer logBuffer;
	private static int monsterCount;

	public Battlefield(){
		initUI();
		initGame();
	}

	/*************************** Main ***************************/
	public static void main(String args[]){
		Battlefield game = new Battlefield();
	}


	/****************** Game Logic ******************/

	private void initMap() {
		map = new Map(canvasWidth, canvasHeight);
		if (map.build()) {
			map.draw(_boardCanvas);
		}
		else {
			endGame();
			System.out.println("Cannot initilize map");
		}
	}

	private void initGame() {
		_frameCount = 0;
		_gameOver = true;

		//update and show skill points
		_totalSkillPoints = STARTING_SKILLPOINTS;
		UpdateSkillPoints(0);

		//disable and enable UI
		bladeswornBtn.setEnabled(false);
		axebringerBtn.setEnabled(false);
		// SPECIAL UNITS
		lanceforgedBtn.setEnabled(false);
		spawnBtn.setEnabled(false);
		startBtn.setEnabled(true);
		endBtn.setEnabled(false);

		initMap();
		
		initBaseAbilities();

		deadFighters = new LinkedList<Fighter>();
		monsters = new LinkedList<Monster>();
		warriors = new LinkedList<Warrior>();
		logBuffer = new StringBuffer();
	}

	// warriors base abilities
	private void initBaseAbilities() {
		Warrior.CASTLE_DMG_REDUCTION = CASTLE_DMG_REDUCTION;
		// Bladesworn
		Bladesworn.BASE_HEALTH = BLADESWORN_HEALTH;
		Bladesworn.BASE_COST = BLADESWORN_COST;
		Bladesworn.BASE_ATTACK_DAMAGE = BLADESWORN_ATTACK;

		// Axebringer
		Axebringer.BASE_HEALTH = AXEBRINGER_HEALTH;
		Axebringer.BASE_COST = AXEBRINGER_COST;
		Axebringer.BASE_ATTACK_DAMAGE = AXEBRINGER_ATTACK;
		

		// Lancers
		Lanceforged.BASE_HEALTH = LANCEFORGED_HEALTH;
		Lanceforged.BASE_COST = LANCEFORGED_COST;
		Lanceforged.BASE_ATTACK_DAMAGE = LANCEFORGED_ATTACK;
	}

	private void startGame() {
		if (_gameOver == false) return;

		_gameOver = false;

		toggleInput(true);

		tick = new Timer(_timePerFrame, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_frameCount++;

				updateUI();
				simulate();
				render();
			}
		});
		tick.start();

	}

	private void endGame() {
		if (_gameOver == true) return;
		tick.stop();
		resetMap();
		updateUI();
		_boardCanvas.repaint();

		_gameOver = true;
		_frameCount = 0;
		toggleInput(false);
	}

	// spawn one monster
	private void spawnMonster() {
		//spawn monsters at the camp
		monsterCount++;
		int weaponType = RND_GEN.nextInt(3);
		Monster readyToSpawn = new Monster(map.getCampTile(), MONSTER_HEALTH, MONSTER_ATTACK, weaponType);
		monsters.add(readyToSpawn);
		logMessage("The enemy's forces replenish with the appearance of a new monster!\n");
	}


	// game logic
	private void simulate() {
		//TODO: simulate the map based on the rule and update map
		logMessage("\n************* Turn " + _frameCount + " *************\n");

		//for each warrior, do actions
		int skillPointsEarned = 0;
		for (Warrior warrior : warriors) {
			skillPointsEarned += warrior.takeAction();
			//System.out.println(warrior.getHealth());
		}
		UpdateSkillPoints(skillPointsEarned);
		logMessage(warriors.size() + " warriors took actions! A total of " + skillPointsEarned + " earned from these actions.\n");

		// some monsters might have died after the warriors took action
		// remove them to not allow them to take action
		collectDeadMonsters(); 

		//System.out.println(_frameCount);
		//for each monster, do actions
		for (Monster monster : monsters) {
			monster.takeAction();
			if (monster.getPosition()!= null && monster.getPosition().isCastle() && monster.getPosition().getWarrior()==null) {
				collectDeadFigthers();
				UpdateMessage("GAME OVER! The monsters triumph.");
				this.endGame();
			}
			//System.out.println(monsters);
		}
		logMessage(monsters.size() + " monsters took actions!\n");

		//collect dead insects. Both monsters and warriors might have died.
		collectDeadFigthers();

		UpdateLog();
	}

	private void render() {
		_boardCanvas.repaint();
	}

	private Tile calculateSpawnLocation(int x, int y) {
		return map.getTileAtLocation(x, y);
	}

	private boolean spawnWarrior(Tile tileToSpawn) {
		if (_selectedWarriorType == null) return false;

		/* uncomment if you want to limit the player to place warriors only on the path
		if (!tileToSpawn.isOnThePath()) {
			_inSelection = false;
			_selectedWarriorType = null;
			return false;
		}*/

		switch (_selectedWarriorType) {
		case bladesworn:
			System.out.println("Spawn Bladesworn");
			if (_totalSkillPoints >= BLADESWORN_COST) {
				UpdateSkillPoints(-BLADESWORN_COST);
				warriors.add(new Bladesworn(tileToSpawn));
				return true;
			}
			break;
		case axebringer:
			System.out.println("Spawn Axebringer");
			if (_totalSkillPoints >= AXEBRINGER_COST) {
				UpdateSkillPoints(-AXEBRINGER_COST);
				warriors.add(new Axebringer(tileToSpawn));
				return true;
			}
			break;
		case lanceforged:
			System.out.println("Spawn lancer");
			if (_totalSkillPoints >= LANCEFORGED_COST) {
				UpdateSkillPoints(-LANCEFORGED_COST);
				warriors.add(new Lanceforged(tileToSpawn, LANCEFORGED_PIERCING_POWER, LANCEFORGED_ACTION_RANGE));
				return true;
			}
			break;
		default:
			break;
		}

		UpdateMessage("You do not have enough skill points!");
		return false;
	}

	private void updateUI() {
		if (_inSelection) {
			spawnBtn.setEnabled(false);
		}
		else {
			spawnBtn.setEnabled(true);
		}

		if (_gameOver) {
			startBtn.setEnabled(true);
			endBtn.setEnabled(false);
		}
		else {
			endBtn.setEnabled(true);
			startBtn.setEnabled(false);
		}
	}

	private void collectDeadFigthers() {
		int deadWarriors = 0;
		int deadMonsters = 0;

		for (Warrior warrior : warriors) {
			if (warrior.getHealth() <= 0) {
				deadFighters.add(warrior);
				deadWarriors++;
			}
		}
		if (deadWarriors > 0)
			logMessage(String.format(MSG_WARRIOR_DEFEAT[RND_GEN.nextInt(MSG_WARRIOR_DEFEAT.length)], deadWarriors));


		for (Monster monster : monsters) {
			if (monster.getHealth() <= 0) {
				deadFighters.add(monster);
				deadMonsters++;
			}
		}
		if (deadMonsters > 0)
			logMessage(String.format(MSG_MONSTER_DEFEAT[RND_GEN.nextInt(MSG_MONSTER_DEFEAT.length)], deadMonsters));

		for (Fighter fighter : deadFighters) {
			if (fighter instanceof Warrior)
				warriors.remove(fighter);
			if (fighter instanceof Monster)
				monsters.remove(fighter);
		}
		UpdateLog();
		deadFighters.clear();
	}

	private void collectDeadMonsters() {
		int deadMonsters = 0;
		for (Monster monster : monsters) {
			if (monster.getHealth() <= 0) {
				deadFighters.add(monster);
				deadMonsters++;
			}
		}
		if (deadMonsters > 0)
			logMessage(String.format(MSG_MONSTER_DEFEAT[RND_GEN.nextInt(MSG_MONSTER_DEFEAT.length)], deadMonsters));

		for (Fighter fighter : deadFighters) {
			if (fighter instanceof Monster)
				monsters.remove(fighter);
		}
		UpdateLog();
		deadFighters.clear();
	}

	private void resetMap() {
		for (Warrior warrior : warriors) {
			warrior.takeDamage(Integer.MAX_VALUE, 0);
		}
		for (Monster monster : monsters) {
			monster.takeDamage(Integer.MAX_VALUE, 0);
		}
		collectDeadFigthers();
	}

	/**************************** Interface *************************/

	public boolean isGameOver() {
		return _gameOver;
	}

	// This method is used to log message to panel
	public static void logMessage(String s) {
		logBuffer.append(s);
		UpdateLog();
	}

	// This method is necessary to let the game know if any insect is dead
	public static void notifyDeath(Fighter fighter) {
		if (fighter instanceof Warrior) {
			warriors.remove(fighter);
			//System.out.println("remove warrior");
		}
		else {
			monsters.remove(fighter);
			//System.out.println("remove monsters");
		}
	}



	/************************** UI control *********************/

	public void initUI() {
		_msgPanel = new Label();
		_msgPanel.setBounds(RES_X / 2 -  125, 30, 250,20);
		_msgPanel.setAlignment(1);

		_skillPointPanel = new Label();
		_skillPointPanel.setBounds(RES_X - 300, 30, 300, 20);
		_skillPointPanel.setAlignment(1);

		// close window on button click
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				endGame();
				dispose();
			}
		});

		// create canvas
		canvasX = RES_X / 20;
		canvasY = RES_Y / 8;
		canvasWidth = (int)(RES_X * 0.75);
		canvasHeight = (int)(RES_Y * 0.75);
		_boardCanvas = new GameCanvas(canvasX, canvasY, canvasWidth, canvasHeight);
		_boardCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//if click on the canvas then set inSelection to false
				//System.out.println(e.getX() + " " + e.getY());
				_inSelection = false;
				try {
					Tile tile = calculateSpawnLocation(e.getX(), e.getY());
					if (tile != null) { spawnWarrior(tile); }
				} catch (ArrayIndexOutOfBoundsException exception) {
					//Skip
				}

			}
		});

		// create button panel
		Panel _buttonPanel = new Panel();
		_buttonPanel.setLayout(new BoxLayout(_buttonPanel, BoxLayout.Y_AXIS));
		_buttonPanel.setBounds(RES_X - 150, 150, 140, RES_Y / 3);
		createButtons(_buttonPanel);

		// create log panel
		_logPanel = new TextArea();
		_logPanel.setSize(400,400);
		_logPanel.setBounds(150, RES_Y - 130, 600, 110);
		_logPanel.setVisible(true);
		_logPanel.setEditable(false);

		add(_logPanel);
		add(_boardCanvas);
		add(_buttonPanel);
		add(_skillPointPanel);
		add(_msgPanel);
		setSize(RES_X,RES_Y);				//frame size 300 width and 300 height
		setLayout(null);					//no layout manager
		setVisible(true);					//now frame will be visible, by default not visible
	}

	public void UpdateMessage(String s) {
		_msgPanel.setText(s);
	}

	public void UpdateSkillPoints(int num) {
		_totalSkillPoints += num;
		_skillPointPanel.setText("Skill points left: " + Integer.toString(_totalSkillPoints));
	}

	public static void UpdateLog() {
		_logPanel.setText(logBuffer.toString());
	}

	private void createButtons(Panel buttonPanel) {

		startBtn = new Button("Start Game");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});

		endBtn = new Button("End Game");
		endBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				endGame();
			}
		});

		spawnBtn = new Button("Spawn Monster");
		spawnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spawnMonster();
			}
		});


		// button for Bladesworn
		bladeswornBtn = new Button(String.format("Bladesworn ($%d)", BLADESWORN_COST));
		bladeswornBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//set selected warrior type
				//in selection to true
				_selectedWarriorType = EnumWarriorType.bladesworn;
				_inSelection = true;
				UpdateMessage("You selected Bladesworn");
			}
		});

		// button for axebringer
		axebringerBtn = new Button(String.format("Axebringer ($%d)", AXEBRINGER_COST));
		axebringerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//set selected warrior type
				//in selection to true
				_selectedWarriorType = EnumWarriorType.axebringer;
				_inSelection = true;
				UpdateMessage("You selected Axebringer");
			}
		});
		

		// button for Lanceforged
		lanceforgedBtn = new Button(String.format("Lanceforged ($%d)", LANCEFORGED_COST));
		lanceforgedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//set selected warrior type
				//in selection to true
				_selectedWarriorType = EnumWarriorType.lanceforged;
				_inSelection = true;
				UpdateMessage("You selected Lanceforged");
			}
		});


		buttonPanel.add(startBtn);
		buttonPanel.add(endBtn);
		buttonPanel.add(new Label(" "));
		buttonPanel.add(spawnBtn);
		buttonPanel.add(bladeswornBtn);
		buttonPanel.add(axebringerBtn);
		buttonPanel.add(lanceforgedBtn);

	}

	private void toggleInput(boolean b) {
		bladeswornBtn.setEnabled(b);
		axebringerBtn.setEnabled(b);
		lanceforgedBtn.setEnabled(b);
		spawnBtn.setEnabled(b);
	}

}