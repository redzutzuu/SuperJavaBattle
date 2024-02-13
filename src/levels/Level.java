package levels;

import entities.Enemy1;
import entities.Enemy2;
import entities.Enemy3;
import main.Game;
import objects.*;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.*;

public class Level {
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Enemy1> enemy1 = new ArrayList<>();
	private ArrayList<Enemy2> enemy2 = new ArrayList<>();
	private ArrayList<Enemy3> enemy3 = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<BackgroundTree> trees = new ArrayList<>();
	private ArrayList<Grass> grass = new ArrayList<>();
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	// Constructorul clasei Level primește o imagine și o atribuie variabilei img. Inițializează matricea lvlData cu dimensiunile
	// și valorile pixelilor din imagine și apoi calculează anumite offseturi pentru nivel.
	public Level(BufferedImage img) {
		this.img = img;
		lvlData = new int[img.getHeight()][img.getWidth()];
		loadLevel();
		calcLvlOffsets();
	}

	// Această metodă încarcă datele nivelului din imagine. Parcurge fiecare pixel din imagine și extrage valorile componente Red, Green
	// și Blue ale fiecărui pixel. Apoi, în funcție de valorile respective, încarcă datele nivelului, entitățile și obiectele corespunzătoare.
	private void loadLevel() {
		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
	}

	// Această metodă încarcă datele nivelului în matricea lvlData. Valorile Red sunt utilizate pentru a determina tipul de teren/grass
	// și sunt adăugate la lista grass.
	private void loadLevelData(int redValue, int x, int y) {
		if (redValue >= 50)
			lvlData[y][x] = 0;
		else
			lvlData[y][x] = redValue;
		switch (redValue) {
			case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
					grass.add(new Grass((int) (x * Game.TILES_SIZE), (int) (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
		}
	}

	// Această metodă returnează un tip de iarbă aleatoriu în funcție de poziția x.
	private int getRndGrassType(int xPos) {
		return xPos % 2;
	}

	// Această metodă încarcă entitățile (inamicii) în nivel în funcție de valorile Green din imagine.
	private void loadEntities(int greenValue, int x, int y) {
		switch (greenValue) {
			case ENEMY1 -> enemy1.add(new Enemy1(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
			case ENEMY2 -> enemy2.add(new Enemy2(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
			case ENEMY3 -> enemy3.add(new Enemy3(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
			case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
		}
	}

	// Această metodă încarcă obiectele în nivel în funcție de valorile Blue din imagine. Obiectele pot fi poțiuni, containere, cuie, tunuri sau arbori.
	private void loadObjects(int blueValue, int x, int y) {
		switch (blueValue) {
			case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
			case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
			case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
			case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
			case TREE_ONE, TREE_TWO, TREE_THREE -> trees.add(new BackgroundTree(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		}
	}

	// Această metodă încarcă obiectele în nivel în funcție de valorile Blue din imagine. Obiectele pot fi poțiuni, containere, cuie, tunuri sau arbori.
	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	// Această metodă returnează indexul sprite-ului din matricea lvlData pentru o poziție specificată.
	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	// Aceste metode returnează diferite liste sau valori asociate nivelului, cum ar fi datele nivelului, offsetul nivelului,
	// poziția de spawn a jucătorului și liste de entități sau obiecte din nivel.
	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}

	public ArrayList<Enemy1> getEnemy1() {
		return enemy1;
	}

	public ArrayList<Enemy2> getEnemy2() {
		return enemy2;
	}

	public ArrayList<Enemy3> getEnemy3() {
		return enemy3;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Cannon> getCannons() {
		return cannons;
	}

	public ArrayList<BackgroundTree> getTrees() {
		return trees;
	}

	public ArrayList<Grass> getGrass() {
		return grass;
	}

}