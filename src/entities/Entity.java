package entities;

import main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.CanMoveHere;

public abstract class Entity {

	protected float x,y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed = 1.0f * Game.SCALE;
	protected int pushBackDir;
	protected float pushDrawOffset;
	protected int pushBackOffsetDir = UP;
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// Această funcție actualizează offsetul de tragere înapoi (pushDrawOffset) care este utilizat pentru a desena entitatea deviată în urma unei lovituri.
	// Offsetul este modificat în funcție de direcția de deviere și atinge o limită superioară și inferioară.
	protected void updatePushBackDrawOffset() {
		float speed = 0.95f;
		float limit = -30f;

		if (pushBackOffsetDir == UP) {
			pushDrawOffset = pushDrawOffset - speed;
			if (pushDrawOffset <= limit)
				pushBackOffsetDir = DOWN;
		} else {
			pushDrawOffset = pushDrawOffset + speed;
			if (pushDrawOffset >= 0)
				pushDrawOffset = 0;
		}
	}

	// Această funcție aplică o forță de împingere asupra entității pentru a o deplasa înapoi în urma unei lovituri. Direcția și viteza
	// de deplasare sunt specificate prin argumentele pushBackDir și speedMulti. Verifică dacă mutarea este posibilă și actualizează poziția entității.
	protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == LEFT)
			xSpeed = xSpeed - walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x = hitbox.x + xSpeed * speedMulti;
	}

	// Această funcție inițializează hitbox-ul entității cu lățimea și înălțimea specificate. Hitbox-ul este un dreptunghi care
	// reprezintă zona în care entitatea poate intra în coliziune cu alte obiecte din joc.
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	// Această funcție returnează hitbox-ul entității.
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	// Această funcție returnează starea curentă a entității.
	public int getState() {
		return state;
	}

	// Această funcție returnează indexul de animație curent al entității.
	public int getAniIndex() {
		return aniIndex;
	}

	// Această funcție setează o nouă stare pentru entitate, resetând indexul de animație și numărătoarea pentru următoarea animație.
	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}
}
