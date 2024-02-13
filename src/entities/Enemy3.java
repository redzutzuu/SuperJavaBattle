package entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

import gamestates.Playing;

public class Enemy3 extends Enemy {

	// Constructorul clasei Enemy3. Inițializează coordonatele și dimensiunile inamicului utilizând valorile specifice ENEMY3_WIDTH și
	// ENEMY3_HEIGHT din clasa EnemyConstants. Inițializează hitbox-ul inamicului utilizând valorile specifice pentru lățime și înălțime.
	public Enemy3(float x, float y) {
		super(x, y, ENEMY3_WIDTH, ENEMY3_HEIGHT, ENEMY3);
		initHitbox(18, 22);
		initAttackBox(20, 20, 20);
	}

	// Actualizează starea și animația inamicului. Această funcție este apelată în fiecare cadru pentru a actualiza comportamentul inamicului în
	// funcție de datele nivelului și starea jocului. Actualizează comportamentul inamicului prin apelul funcției updateBehavior(lvlData, playing),
	// actualizează animația inamicului prin apelul funcției updateAnimationTick() și actualizează orientarea și poziționarea cutiei de atac prin
	// apelul funcției updateAttackBoxFlip().
	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	// Actualizează comportamentul inamicului în funcție de starea curentă a acestuia și de datele nivelului și starea jocului.
	// Verifică dacă este primul update și, în funcție de aceasta, realizează anumite verificări. Dacă inamicul se află în aer, se
	// efectuează verificări specifice inamicilor în aer. În caz contrar, se realizează verificări și acțiuni specifice fiecărei stări
	// posibile a inamicului (IDLE, RUNNING, ATTACK, HIT).
	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, lvlData))
					newState(RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(ATTACK);
				}

				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(lvlData, playing);
				}

				break;
	 		case HIT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	// Realizează deplasarea înamicului în timpul atacului. Calculează viteza deplasării pe axa X în funcție de orientarea inamicului
	// (LEFT sau RIGHT). Verifică dacă inamicul poate să se deplaseze în direcția calculată, luând în considerare offset-ul adițional în
	// timpul atacului (xSpeed * 4). Dacă inamicul poate să se deplaseze, se modifică coordonata X a hitbox-ului și se continuă deplasarea.
	// În caz contrar, inamicul intră în starea IDLE prin apelul funcției newState(IDLE).
	protected void attackMove(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
				hitbox.x += xSpeed * 4;
				return;
			}
		newState(IDLE);
	}
}
