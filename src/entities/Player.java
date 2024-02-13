package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;
import static utilz.Constants.Directions.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffset = 22 * Game.SCALE;
	private float yDrawOffset = 8 * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int)(192 * Game.SCALE);
	private int statusBarHeight = (int)(58 * Game.SCALE);
	private int statusBarX = (int)(10 * Game.SCALE);
	private int statusBarY = (int)(10 * Game.SCALE);
	private int healthBarWidth = (int)(150 * Game.SCALE);
	private int healthBarHeight = (int)(4 * Game.SCALE);
	private int healthBarXStart = (int)(34 * Game.SCALE);
	private int healthBarYStart = (int)(14 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked;
	private Playing playing;
	private int tileY = 0;
	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;

	private int powerBarWidth = (int)(104 * Game.SCALE);
	private int powerBarHeight = (int)(2 * Game.SCALE);
	private int powerBarXStart = (int)(44 * Game.SCALE);
	private int powerBarYStart = (int)(34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	// Constructorul clasei Player. Inițializează jucătorul cu poziția, dimensiunea, instanța jocului curent și alte variabile specifice.
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(21, 28);
		initAttackBox();
	}

	// Metodă care setează poziția de spawn a jucătorului pe harta jocului.
	public void setSpawn(Point spawn){
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	// Inițializează dreptunghiul de atac al jucătorului.
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(29*Game.SCALE), (int)(24*Game.SCALE));
		resetAttackBox();
	}

	// Actualizează starea jucătorului și verifică diverse interacțiuni, cum ar fi atacul, coliziunile, sănătatea etc.
	public void update() {
		updateHealthBar();
		updatePowerBar();
		if(currentHealth <= 0) {
			if(state != DEAD){
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
				if (!IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;
					airSpeed = 0;
				}
			}
			else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1){
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			}else{
				updateAnimationTick();
				if (inAir)
					if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
						hitbox.y += airSpeed;
						airSpeed += GRAVITY;
					} else
						inAir = false;
			}
			return;
		}
		updateAttackBox();
		if (state == HIT) {
			if (aniIndex <= GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, lvlData, 1.25f);
			updatePushBackDrawOffset();
		} else
			updatePos();
		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}
		if (attacking || powerAttackActive)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}

	// Verifică dacă jucătorul se află în apă și îi scade viața dacă este cazul.
	private void checkInsideWater() {
		if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	// Verifică dacă jucătorul a atins un obiect cu tepi și îi scade viața dacă este cazul.
	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	// Verifică dacă jucătorul a atins o pocionare și actualizează scorul.
	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	// Verifică dacă jucătorul a atins un inamic cu atacul său și aplică daunele corespunzătoare.
	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		if(powerAttackActive)
			attackChecked = false;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	// Setează poziția dreptunghiului de atac al jucătorului pe partea dreaptă.
	private void setAttackBoxOnRightSide() {
		attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 5);
	}

	// Setează poziția dreptunghiului de atac al jucătorului pe partea stângă.
	private void setAttackBoxOnLeftSide() {
		attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
	}

	// Actualizează poziția și dimensiunea dreptunghiului de atac al jucătorului în funcție de direcția și starea acestuia.
	private void updateAttackBox() {
		if (right && left) {
			if (flipW == 1) {
				setAttackBoxOnRightSide();
			} else {
				setAttackBoxOnLeftSide();
			}
		} else if (right || (powerAttackActive && flipW == 1))
			setAttackBoxOnRightSide();
		else if (left || (powerAttackActive && flipW == -1))
			setAttackBoxOnLeftSide();
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	// Actualizează bara de viață a jucătorului în funcție de valoarea curentă a vieții.
	private void updateHealthBar() {
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
	}

	// Actualizează bara de putere a jucătorului în funcție de valoarea curentă a puterii.
	private void updatePowerBar(){
		powerWidth = (int)((powerValue / (float) powerMaxValue) * powerBarWidth);
		powerGrowTick++;
		if(powerGrowTick >= powerGrowSpeed){
			powerGrowTick = 0;
			changePower(1);
		}
	}

	// Desenează jucătorul și elementele de interfață grafică pe ecran.
	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex],
				(int)(hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int)(hitbox.y - yDrawOffset),
				width * flipW, height, null);
		//drawHitbox(g, lvlOffset);
		//drawAttackBox(g, lvlOffset);
		drawUI(g);
	}

	// Desenează elementele de interfață grafică, cum ar fi bara de viață și bara de putere.
	private void drawUI(Graphics g) {
		//background
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.WHITE);
		//potiunile colectate
		g.setFont(g.getFont().deriveFont(Font.TRUETYPE_FONT,25f));
		g.drawString("POTIUNI: " + Integer.toString(Playing.objectManager.score),statusBarX+25,statusBarY+125);
		//bara viata
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		//bara putere
		g.setColor(Color.YELLOW);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	// Actualizează indicele și tick-ul de animație pentru animațiile jucătorului.
	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				if (state == HIT) {
					newState(IDLE);
					airSpeed = 0f;
					if (!IsFloor(hitbox, 0, lvlData))
						inAir = true;
				}
			}
		}
	}

	// Setează animația corespunzătoare stării și direcției jucătorului.
	private void setAnimation() {
		int startAni = state;
		if (state == HIT)
			return;
		if (moving)
			state = RUNNING;
		else
			state = IDLE;
		if (inAir) {
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}
		if (powerAttackActive) {
			state = ATTACK;
			aniIndex = 1;
			aniTick = 0;
			return;
		}
		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (startAni != state)
			resetAniTick();
	}

	// Resetează variabilele aniTick și aniIndex la valoarea 0.
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	// Actualizează poziția jucătorului pe baza stării sale. Verifică dacă jucătorul sare sau nu și setează viteza orizontală în
	// funcție de direcția în care se mișcă jucătorul (stânga sau dreapta). De asemenea, verifică dacă jucătorul este în aer și actualizează
	// poziția în consecință. Setează și variabila moving pentru a indica dacă jucătorul se mișcă sau nu.
	private void updatePos() {
		moving = false;
		if(jump)
			jump();
		if(!inAir)
			if(!powerAttackActive)
				if((!left && !right) || (right && left))
					return;
		float xSpeed = 0;
		if(left && !right) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right && !left) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		if(powerAttackActive) {
			if((!left && !right) || (left && right)) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}
			xSpeed = xSpeed * 3;
		}

		if(!inAir)
			if(!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if(inAir && !powerAttackActive) {
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y = hitbox.y + airSpeed;
				airSpeed = airSpeed + GRAVITY;
				updateXPos(xSpeed);
			}
			else
			{
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		}
		else
			updateXPos(xSpeed);
		moving = true;
	}

	// Verifică dacă jucătorul este în aer și dacă nu este, îl face să sară prin setarea variabilelor corespunzătoare.
	private void jump() {
		if(inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	// Resetează variabilele inAir și airSpeed la valorile implicite.
	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	// Actualizează poziția jucătorului pe axa X în funcție de viteza sa orizontală (xSpeed). Verifică și coliziunile cu pereții și ajustează poziția în consecință.
	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
		hitbox.x = hitbox.x + xSpeed;
		}
		else
		{
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if(powerAttackActive){
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}

	// Modifică valoarea sănătății jucătorului cu o anumită valoare (value). Verifică dacă valoarea este negativă și în funcție de asta, schimbă starea jucătorului.
	public void changeHealth(int value) {
		if (value < 0) {
			if (state == HIT)
				return;
			else
				newState(HIT);
		}

		currentHealth = currentHealth + value;
		currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
	}

	// Modifică valoarea sănătății jucătorului cu o anumită valoare (value) în urma atacului unui inamic (e). Verifică dacă jucătorul este în
	// starea de lovit și în funcție de poziția inamicului, setează direcția împingerii.
	public void changeHealth(int value, Enemy e) {
		if (state == HIT)
			return;
		changeHealth(value);
		pushBackOffsetDir = UP;
		pushDrawOffset = 0;
		if (e.getHitbox().x < hitbox.x)
			pushBackDir = RIGHT;
		else
			pushBackDir = LEFT;
	}

	// Modifică valoarea puterii jucătorului cu o anumită valoare (value).
	public void changePower(int value) {
		powerValue += value;
		powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
	}

	// Încarcă animațiile pentru jucător dintr-o imagine atlas. Creează o matrice de imagini pentru animații și le atribuie din imaginea atlas.
	private void loadAnimations() {
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			animations = new BufferedImage[7][8];
			for(int j = 0; j < animations.length; j++)
				for(int i = 0; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i*64, j*40, 64, 40);

			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

	}

	//  Încarcă datele nivelului pentru a verifica coliziunile jucătorului cu mediul.
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	// Resetează variabilele booleene left și right la valoarea falsă.
	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	// Setează variabila attacking la valoarea specificată (true sau false).
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	// Setează variabila left la valoarea specificată (true sau false).
	public void setLeft(boolean left) {
		this.left = left;
	}

	// Setează variabila right la valoarea specificată (true sau false).
	public void setRight(boolean right) {
		this.right = right;
	}

	// Setează variabila jump la valoarea specificată (true sau false).
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	// Resetează toate variabilele și stările jucătorului la valorile implicite.
	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		powerValue = powerMaxValue;
		hitbox.x = x;
		hitbox.y = y;
		resetAttackBox();
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	// Resetează dimensiunea și poziția zonei de atac în funcție de orientarea jucătorului.
	private void resetAttackBox() {
		if (flipW == 1)
			setAttackBoxOnRightSide();
		else
			setAttackBoxOnLeftSide();
	}

	// Setează sănătatea jucătorului la 0, adică îl omoară.
	public void kill() {
		currentHealth = 0;
	}

	// Returnează valoarea tileY a jucătorului.
	public int getTileY(){
		return tileY;
	}

	// Activează atacul special al jucătorului dacă acesta are suficientă putere.
	public void powerAttack() {
		if(powerAttackActive)
			return;
		if(powerValue >= 60){
			powerAttackActive = true;
			changePower(-60);
		}
	}
}
