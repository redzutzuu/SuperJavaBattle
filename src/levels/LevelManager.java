package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.Game;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	private BufferedImage[] levelSprite;
	private BufferedImage[] waterSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0, aniTick, aniIndex;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		createWater();
		levels = new ArrayList<>();
		buildAllLevels();
	}

	// Această funcție creează sprite-urile apei utilizate pentru animarea apei în nivel. Ea inițializează un vector de imagini
	// waterSprite cu dimensiunea 5 și apoi încarcă imaginile apei utilizând metoda LoadSave.GetSpriteAtlas(). Primele patru imagini
	// sunt extrase din atlasul de sprite-uri al apei și reprezintă diferite stări ale apei (sus, jos, stânga, dreapta), iar a cincea
	// imagine reprezintă partea inferioară a apei.
	private void createWater() {
		waterSprite = new BufferedImage[5];
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
		waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
	}

	// Această funcție încarcă următorul nivel din lista de niveluri. Ea obține nivelul corespunzător indexului lvlIndex și încarcă
	// datele nivelului în joc, inclusiv inamicii, datele nivelului, obiectele și limitele nivelului.
	public void loadNextLevel() {
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
		game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	// Această funcție construiește toate nivelurile jocului. Ea primește un vector de imagini allLevels care conține imaginile pentru
	// toate nivelurile și pentru fiecare imagine construiește un obiect Level și îl adaugă în lista de niveluri.
	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();
		for(BufferedImage img : allLevels)
			levels.add(new Level(img));
	}

	// Această funcție importă sprite-urile externe pentru niveluri și le stochează într-un vector levelSprite. Ea utilizează metoda
	// LoadSave.GetSpriteAtlas() pentru a obține atlasul de sprite-uri al nivelului și apoi extrage fiecare subimagine de dimensiune 32x32
	// corespunzătoare unui sprite din atlas și le adaugă în vectorul levelSprite.
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS); 
		levelSprite = new BufferedImage[48];
		for(int j = 0; j < 4; j++)
			for(int i = 0; i < 12; i++) {
				int index = j*12 + i;
				levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
			}
	}

	// Această funcție desenează nivelul curent pe ecran. Ea parcurge fiecare poziție din matricea de date a nivelului și extrage indicele
	// sprite-ului corespunzător acelei poziții. Apoi, utilizează indicele sprite-ului pentru a desena sprite-ul la poziția corespunzătoare pe
	// ecran, ajustându-le în funcție de offsetul nivelului. De asemenea, aceasta gestionează animația apei prin desenarea sprite-urilor de apă
	// în funcție de aniIndex și aniTick.
	public void draw(Graphics g, int lvlOffset) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				int x = Game.TILES_SIZE * i - lvlOffset;
				int y = Game.TILES_SIZE * j;
				if (index == 48)
					g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
				else if (index == 49)
					g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
				else
					g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}

	// Această funcție actualizează nivelul, inclusiv animația apei. Ea incrementează aniTick și, atunci când acesta depășește o anumită valoare,
	// actualizează aniIndex pentru a schimba sprite-ul de apă afișat în animație.
	public void update() {
		updateWaterAnimation();
	}

	// Această funcție realizează animația apei.
	private void updateWaterAnimation() {
		aniTick++;
		if (aniTick >= 40) {
			aniTick = 0;
			aniIndex++;

			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	// Această funcție returnează nivelul curent din lista de niveluri, utilizând indexul lvlIndex.
	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}

	// Această funcție returnează numărul total de niveluri disponibile în joc, determinat de lungimea listei de niveluri.
	public int getAmountOfLevels(){
		return levels.size();
	}

	// Această funcție returnează indexul nivelului curent.
	public int getLvlIndex(){
		return lvlIndex;
	}

	// Această funcție setează indexul nivelului curent pe baza valorii primite ca argument.
	public void setLevelIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}
}
