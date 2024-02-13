package entities;
import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

public class Enemy1 extends Enemy {

    // Constructorul clasei Enemy1 primește coordonatele x și y și inițializează obiectul inamic cu valorile specifice pentru tipul
    // de inamic Enemy1. Apelează metodele initHitbox și initAttackBox pentru a inițializa hitbox-ul și zona de atac a inamicului.
    public Enemy1(float x, float y) {
        super(x, y, ENEMY1_WIDTH, ENEMY1_HEIGHT, ENEMY1);
        initHitbox(22, 19);
        initAttackBox(82, 19, 30);
    }

    // Această funcție actualizează comportamentul inamicului în funcție de starea sa curentă și de interacțiunea cu mediul de joc și jucătorul.
    // Apelează funcțiile updateBehaviour, updateAnimationTick și updateAttackBox.
    public void update(int[][] lvlData, Playing playing) {
        updateBehaviour(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }

    // Această funcție gestionează comportamentul inamicului în funcție de starea sa curentă. Verifică dacă este prima actualizare a inamicului și
    // apoi în funcție de starea curentă a inamicului (IDLE, RUNNING, ATTACK, HIT), ia decizii și aplică acțiuni corespunzătoare. Verifică,
    // de asemenea, dacă inamicul se află în aer sau pe podea și aplică acțiuni specifice.
    private void updateBehaviour(int[][] lvlData, Playing playing) {
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
                    if (aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }

    // Această funcție returnează o valoare care reprezintă poziția de flipare pe axa X a inamicului în funcție de direcția sa de mers.
    // Dacă inamicul se deplasează spre dreapta, returnează valoarea width a inamicului, altfel returnează 0.
    public int flipX(){
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    // Această funcție returnează o valoare care reprezintă scalarea pe axa W a inamicului în funcție de direcția sa de mers.
    // Dacă inamicul se deplasează spre dreapta, returnează -1, altfel returnează 1.
    public int flipW(){
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
