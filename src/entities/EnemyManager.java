package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] enemy1Arr, enemy2Arr, enemy3Arr;
    private Level currentLevel;

    // Constructorul clasei. Primește o instanță a clasei Playing și inițializează managerul inamicilor.
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    // Încarcă inamicii pentru nivelul dat în parametru.
    public void loadEnemies(Level level) {
        this.currentLevel = level;
    }

    // Actualizează starea și animația inamicilor pe baza datelor nivelului. Verifică și dacă există inamici activi.
    // Dacă nu există inamici activi, setează nivelul ca fiind completat.
    public void update(int[][] lvlData){
        boolean isAnyActive = false;
        for(Enemy1 e: currentLevel.getEnemy1())
            if(e.isActive()) {
                e.update(lvlData, playing);
                isAnyActive = true;
            }
        for(Enemy2 e2: currentLevel.getEnemy2())
            if (e2.isActive()) {
                e2.update(lvlData, playing);
                isAnyActive = true;
            }
        for(Enemy3 e3: currentLevel.getEnemy3())
            if(e3.isActive()){
                e3.update(lvlData, playing);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    // Desenează inamicii pe ecran. Apelurile sunt realizate pentru fiecare tip de inamic (Enemy1, Enemy2, Enemy3).
    public void draw(Graphics g, int xLvlOffset){
        drawEnemy1(g, xLvlOffset);
        drawEnemy2(g, xLvlOffset);
        drawEnemy3(g, xLvlOffset);
    }

    // Desenează inamicii de tipul Enemy1 pe ecran.
    private void drawEnemy1(Graphics g, int xLvlOffset) {
        for(Enemy1 e: currentLevel.getEnemy1())
            if(e.isActive()) {
                g.drawImage(enemy1Arr[e.getState()][e.getAniIndex()],
                        (int) e.getHitbox().x - xLvlOffset - ENEMY1_DRAWOFFSET_X + e.flipX(),
                        (int) e.getHitbox().y - ENEMY1_DRAWOFFSET_Y,
                        ENEMY1_WIDTH * e.flipW(), ENEMY1_HEIGHT, null);
             //   e.drawAttackBox(g, xLvlOffset);
        }
    }

    // Desenează inamicii de tipul Enemy2 pe ecran.
    private void drawEnemy2(Graphics g, int xLvlOffset){
        for(Enemy2 e2: currentLevel.getEnemy2())
            if(e2.isActive()){
                g.drawImage(enemy2Arr[e2.getState()][e2.getAniIndex()], (int) e2.getHitbox().x - xLvlOffset - ENEMY2_DRAWOFFSET_X + e2.flipX(),
                        (int) e2.getHitbox().y - ENEMY2_DRAWOFFSET_Y + (int) e2.getPushDrawOffset(), ENEMY2_WIDTH * e2.flipW(), ENEMY2_HEIGHT, null);
            }
    }

    // Desenează inamicii de tipul Enemy3 pe ecran.
    private void drawEnemy3(Graphics g, int xLvlOffset){
        for(Enemy3 e3: currentLevel.getEnemy3())
            if(e3.isActive()){
                g.drawImage(enemy3Arr[e3.getState()][e3.getAniIndex()], (int) e3.getHitbox().x - xLvlOffset - ENEMY3_DRAWOFFSET_X + e3.flipX(),
                        (int) e3.getHitbox().y - ENEMY3_DRAWOFFSET_Y + (int) e3.getPushDrawOffset(), ENEMY3_WIDTH * e3.flipW(), ENEMY3_HEIGHT, null);
            }
    }

    // Verifică dacă există coliziuni între caseta de atac și inamici. Dacă există coliziune, se aplică daune inamicilor corespunzător.
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Enemy1 e : currentLevel.getEnemy1())
            if(e.isActive())
                if(e.getState() != DEAD && e.getState() != HIT) {
                    if (attackBox.intersects(e.getHitbox())) {
                        e.hurt(20);
                        return;
                    }
                }
        for(Enemy2 e2 : currentLevel.getEnemy2())
            if(e2.isActive()){
                if(e2.getState() == ATTACK && e2.getAniIndex() >= 3)
                    return;
                else {
                    if(e2.getState() != DEAD && e2.getState() != HIT)
                        if(attackBox.intersects(e2.getHitbox())){
                            e2.hurt(20);
                            return;
                        }
                }
            }
        for(Enemy3 e3 : currentLevel.getEnemy3())
            if(e3.isActive()){
                if(e3.getState() != DEAD && e3.getState() != HIT)
                    if(attackBox.intersects(e3.getHitbox())){
                        e3.hurt(20);
                        return;
                    }
            }
    }

    // Încarcă imaginile inamicilor din fișierele de atlas.
    private void loadEnemyImgs() {
        enemy1Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMY1_SPRITE), 9, 5, ENEMY1_WIDTH_DEFAULT, ENEMY1_HEIGHT_DEFAULT);
        enemy2Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMY2_SPRITE), 8, 5, ENEMY2_WIDTH_DEFAULT, ENEMY2_HEIGHT_DEFAULT);
        enemy3Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMY3_SPRITE), 8, 5, ENEMY3_WIDTH_DEFAULT, ENEMY3_HEIGHT_DEFAULT);
    }

    // Returnează un tablou bidimensional de imagini pentru atlasul dat și dimensiunile sprite-urilor.
    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }

    // Resetează toți inamicii la starea inițială.
    public void resetAllEnemies(){
        for(Enemy1 e: currentLevel.getEnemy1())
            e.resetEnemy();
        for(Enemy2 e2: currentLevel.getEnemy2())
            e2.resetEnemy();
        for(Enemy3 e3: currentLevel.getEnemy3())
            e3.resetEnemy();
    }
}
