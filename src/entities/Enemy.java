package entities;
import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
public abstract class Enemy extends Entity {
    protected int enemyType;
    protected int aniTick;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected int attackBoxOffsetX;

    // onstructorul clasei Enemy care primește coordonatele, dimensiunile și tipul inamicului. Inițializează valorile de bază
    // ale inamicului, inclusiv sănătatea maximă și viteza de deplasare.
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    // Actualizează poziția și dimensiunile dreptunghiului de atac al inamicului.
    protected void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    // Actualizează poziția și dimensiunile dreptunghiului de atac al inamicului în cazul în care acesta este întors.
    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;

        attackBox.y = hitbox.y;
    }

    // Inițializează dreptunghiul de atac al inamicului cu dimensiunile și compensarea specificate.
    protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
        this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
    }

    // Verifică dacă acesta este primul update al inamicului și efectuează acțiuni specifice, cum ar fi verificarea dacă este în aer.
    protected void firstUpdateCheck(int[][] lvlData){
        if(!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    // Verifică dacă inamicul se află în aer și aplică acțiuni corespunzătoare, cum ar fi verificarea coliziunii cu spike-urile.
    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
            if (IsEntityInWater(hitbox, lvlData))
                hurt(maxHealth);
        }
    }

    // Actualizează poziția inamicului în aer pe baza gravitației și coliziunilor cu pereții.
    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y = hitbox.y + airSpeed;
            airSpeed = airSpeed + GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    // Deplasează inamicul în funcție de direcția de mers și verifică coliziunile cu pereții.
    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkDir == LEFT)
            xSpeed = xSpeed - walkSpeed;
        else
            xSpeed = xSpeed + walkSpeed;
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox, xSpeed, lvlData)){
                hitbox.x = hitbox.x + xSpeed;
                return;
            }
        changeWalkDir();
    }

    // Îndreaptă inamicul spre jucător în funcție de poziția acestuia.
    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // Verifică dacă inamicul poate vedea jucătorul pe baza poziției relative și a obstacolelor din nivel.
    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player)){
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        return false;
    }

    // Verifică dacă jucătorul se află în raza de atac a inamicului.
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    // Verifică dacă jucătorul se află la distanța potrivită pentru a fi atacat de către inamic.
    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        switch (enemyType) {
            case ENEMY1 -> {
                return absValue <= attackDistance;
            }
            case ENEMY3 -> {
                return absValue <= attackDistance * 2;
            }
        }
        return false;
    }

    // Scade sănătatea inamicului cu o anumită valoare și schimbă starea inamicului în funcție de rezultatul atacului.
    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    // Verifică dacă inamicul atinge jucătorul cu atacul său.
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        else {
            if (enemyType == ENEMY3)
                return;
        }
        attackChecked = true;
    }

    // Actualizează indicatorul de animație al inamicului și schimbă starea acestuia în funcție de trecerea timpului.
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                if (enemyType == ENEMY1 || enemyType == ENEMY2) {
                    aniIndex = 0;
                    switch (state) {
                        case ATTACK, HIT -> state = IDLE;
                        case DEAD -> active = false;
                    }
                } else if (enemyType == ENEMY3) {
                    if (state == ATTACK)
                        aniIndex = 3;
                    else {
                        aniIndex = 0;
                        if (state == HIT) {
                            state = IDLE;

                        } else if (state == DEAD)
                            active = false;
                    }
                }
            }
        }
    }

    // Schimbă direcția de mers a inamicului.
    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // Resetează starea inamicului la valorile implicite.
    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
        pushDrawOffset = 0;
    }

    // Returnează poziția X a inamicului în funcție de direcția sa de mers pentru a realiza efectul de oglindire.
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    // Returnează factorul de scalare pentru lățimea inamicului în funcție de direcția sa de mers.
    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    // Actualizează starea inamicului și indicele de animație.
    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    // Verifică dacă inamicul este activ sau nu.
    public boolean isActive(){
        return active;
    }

    // Returnează valoarea de deplasare pentru animația de lovitură a inamicului.
    public float getPushDrawOffset() {
        return pushDrawOffset;
    }
}
