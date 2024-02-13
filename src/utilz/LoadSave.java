package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import entities.Enemy1;
import main.Game;

import static utilz.Constants.EnemyConstants.ENEMY1;

public class LoadSave {

	/*public static final String PLAYER_ATLAS = "spritejavanou.png";
	public static final String LEVEL_ATLAS = "spriteharta.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String ENEMY1_SPRITE = "sprite_python.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "options_background.png";
	public static final String ENEMY2_SPRITE = "pinkstar_atlas.png";
	public static final String ENEMY3_SPRITE = "question_atlas.png";
	public static final String GRASS_ATLAS = "grass_atlas.png";
	public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
	public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String RAIN_PARTICLE = "rain_particle.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";
	public static final String SHIP = "ship.png";*/
	public static final String PLAYER_ATLAS = "spritejavanou.png";
	public static final String LEVEL_ATLAS = "spriteharta.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String ENEMY1_SPRITE = "sprite_python.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "options_background.png";
	public static final String ENEMY2_SPRITE = "spritecplusplus.png";
	public static final String ENEMY3_SPRITE = "spritesuperc.png";
	public static final String GRASS_ATLAS = "grass_atlas.png";
	public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
	public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String RAIN_PARTICLE = "rain_particle.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";


	// Această funcție primește numele unui fișier și returnează o imagine BufferedImage care reprezintă un atlas de sprite-uri. Funcția
	// încearcă să încarce imaginea din fișierul specificat utilizând clasa ImageIO.
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/res/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	// Această funcție returnează un tablou de imagini BufferedImage care reprezintă toate nivelele disponibile în joc. Funcția caută
	// într-un director specific (directorul /res/lvls/) fișierele de nivel și le sortează în ordinea corectă. Apoi, încarcă fiecare
	// imagine de nivel utilizând clasa ImageIO și le adaugă într-un tablou, pe care îl returnează.
	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/res/lvls/");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];

			}

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}
}
