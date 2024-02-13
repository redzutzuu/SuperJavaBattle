package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;
import objects.*;

public class HelpMethods {

	// Verifică dacă un obiect poate fi mutat într-o anumită poziție în funcție de coordonatele sale, dimensiunile și datele nivelului furnizate.
	// Returnează true dacă obiectul poate fi mutat în poziția specificată, altfel false.
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;

	}

	// Verifică dacă un anumit punct specificat de coordonatele X și Y este solid în nivelul de joc furnizat.
	// Returnează true dacă punctul este solid în nivel, altfel false.
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth) {
			return true;
		}
		if (y < 0 || y >= Game.GAME_HEIGHT) {
			return true;
		}

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	// Verifică dacă un proiectil atinge un nivel solid într-un anumit moment.
	// Returnează true dacă proiectilul atinge un nivel solid, altfel false.
	public static boolean IsProjectileHittingLevel(Projectile p, int [][] lvlData){
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}

	// Verifică dacă o entitate se află în apă în funcție de hitbox-ul său și datele nivelului furnizate.
	// Returnează true dacă entitatea se află în apă, altfel false.
	public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
			if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
				return false;
		return true;
	}

	// Returnează valoarea unui anumit tile (glosă) în nivelul de joc, pe baza coordonatelor sale X și Y.
	// Returnează valoarea tile-ului în nivel.
	private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
		int xCord = (int) (xPos / Game.TILES_SIZE);
		int yCord = (int) (yPos / Game.TILES_SIZE);
		return lvlData[yCord][xCord];
	}

	// Verifică dacă un anumit tile în nivelul de joc este solid.
	// Returnează true dacă tile-ul este solid, altfel false.
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];
		switch (value) {
			case 11, 48, 49:
				return false;
			default:
				return true;
		}
	}

	// Returnează poziția X a entității în apropierea unui perete (dreapta sau stânga) în funcție de hitbox-ul său și viteza de deplasare pe orizontală.
	// Returnează poziția X a entității în apropierea peretelui.
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			//Dreapta
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else {
			//Stanga
			return currentTile * Game.TILES_SIZE;
		}
	}

	// Returnează poziția Y a entității sub un acoperiș sau deasupra podelei, în funcție de hitbox-ul său și viteza de deplasare în aer.
	// Returnează poziția Y a entității sub acoperiș sau deasupra podelei.
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			//podea
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			//tavan
			return currentTile * Game.TILES_SIZE;
		}
	}

	// Verifică dacă o entitate se află pe podea în funcție de hitbox-ul său și datele nivelului furnizate.
	// Returnează true dacă entitatea se află pe podea, altfel false.
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	// Verifică dacă există podea sub o entitate în funcție de hitbox-ul său, viteza de deplasare pe orizontală și datele nivelului furnizate.
	// Returnează true dacă există podea sub entitate, altfel false.
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	// Verifică dacă un tun poate vedea un jucător în nivel, având în vedere datele nivelului și hitbox-urile tunului și jucătorului.
	// Returnează true dacă tunul poate vedea jucătorul, altfel false.
	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}

	// Verifică dacă toate tile-urile într-un anumit interval orizontal sunt libere (nu sunt solide).
	// Returnează true dacă toate tile-urile din interval sunt libere, altfel false.
	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}

	// Verifică dacă toate tile-urile dintr-un interval orizontal pot fi parcurse (nu sunt solide și au o podea sub ele).
	// Returnează true dacă toate tile-urile din interval sunt parcurse, altfel false.
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	// Verifică dacă există o vedere clară între două hitbox-uri (de exemplu, între un inamic și un jucător) în funcție de datele nivelului și hitbox-urile.
	// Returnează true dacă există o vedere clară între hitbox-urile furnizate, altfel false.
	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
		int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

		int secondXTile;
		if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
			secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
		else
			secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}

}
