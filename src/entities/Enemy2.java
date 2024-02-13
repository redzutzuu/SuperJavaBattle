package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;
import static utilz.Constants.Directions.*;

import gamestates.Playing;

public class Enemy2 extends Enemy {

	private boolean preRoll = true;
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;

	// Constructorul clasei Enemy2 care inițializează un obiect de tip inamic de tipul 2 cu poziția specificată (x, y).
	// Se apelează constructorul clasei de bază Enemy și se inițializează hitbox-ul inamicului.
	public Enemy2(float x, float y) {
		super(x, y, ENEMY2_WIDTH, ENEMY2_HEIGHT, ENEMY2);
		initHitbox(17, 21);
	}

	// Actualizează starea inamicului, comportamentul și animația inamicului. Această funcție este apelată în fiecare frame din joc.
	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
	}

	// Actualizează comportamentul inamicului în funcție de starea sa curentă și de starea jocului.
	// Verifică dacă inamicul se află pe podea sau în aer și în funcție de aceasta ia decizii legate de următoarea acțiune.
	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);
		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				preRoll = true;
				if (tickAfterRollInIdle >= 120) {
					if (IsFloor(hitbox, lvlData))
						newState(RUNNING);
					else
						inAir = true;
					tickAfterRollInIdle = 0;
					tickSinceLastDmgToPlayer = 60;
				} else
					tickAfterRollInIdle++;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					newState(ATTACK);
					setWalkDir(playing.getPlayer());
				}
				move(lvlData, playing);
				break;
			case ATTACK:
				if (preRoll) {
					if (aniIndex >= 3)
						preRoll = false;
				} else {
					move(lvlData, playing);
					checkDmgToPlayer(playing.getPlayer());
					checkRollOver(playing);
				}
				break;
			case HIT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				tickAfterRollInIdle = 120;

				break;
			}
		}
	}

	// Verifică dacă inamicul a atins jucătorul și aplică daune jucătorului în funcție de o perioadă de timp prestabilită.
	private void checkDmgToPlayer(Player player) {
		if (hitbox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-GetEnemyDmg(enemyType), this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	// Setează direcția de mers a inamicului în funcție de poziția jucătorului. Dacă poziția jucătorului este mai mare decât poziția inamicului,
	// acesta se va deplasa spre dreapta, altfel se va deplasa spre stânga.
	private void setWalkDir(Player player) {
		if (player.getHitbox().x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	// Actualizează poziția inamicului în funcție de direcția de mers și de posibilitatea de a se deplasa pe podea sau în aer.
	// Verifică, de asemenea, dacă inamicul se află în starea de atac și ajustează viteza de deplasare în consecință.
	protected void move(int[][] lvlData, Playing playing) {
		float xSpeed = 0;
		if (walkDir == LEFT)
			xSpeed = xSpeed - walkSpeed;
		else
			xSpeed = walkSpeed;
		if (state == ATTACK)
			xSpeed = xSpeed * 2;
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}
		if (state == ATTACK) {
			rollOver(playing);
			rollDurationTick = 0;
		}
		changeWalkDir();
	}

	// Verifică dacă a expirat durata de rulare a atacului inamicului și, în caz afirmativ, declanșează o tranziție către starea de repaus.
	private void checkRollOver(Playing playing) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(playing);
			rollDurationTick = 0;
		}
	}

	// Efectuează tranziția inamicului către starea de repaus.
	private void rollOver(Playing playing) {
		newState(IDLE);
	}

}
