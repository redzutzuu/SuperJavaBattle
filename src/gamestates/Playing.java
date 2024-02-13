package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import UI.GameCompletedOverlay;
import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import utilz.LoadSave;
import effects.Rain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    static public  ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private Rain rain;
    private boolean paused = false;
    private int xLvlOffset;
    private int leftBorder = (int)(0.25 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.75 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;
    private BufferedImage backgroundImg, bigCloud, smallCloud, shipImgs[];
    private int[] smallCloudsPos;
    private Random rnd = new Random();
    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean playerDying;
    private boolean drawRain;
    private boolean drawShip = true;
    private int shipAni, shipTick, shipDir = 1;
    private float shipHeightDelta, shipHeightChange = 0.05f * Game.SCALE;

    // Inițializează obiectele și variabilele necesare pentru starea de joc. Se încarcă imaginile și
    // se setează offsetul nivelului. Se încarcă nivelul inițial și se stabilește dacă va ploua sau nu.
    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for(int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int)(90*Game.SCALE) + rnd.nextInt((int)(100*Game.SCALE));
        shipImgs = new BufferedImage[4];
        calcLvlOffset();
        loadStartLevel();
        setDrawRainBoolean();
    }

    // Această funcție este responsabilă de încărcarea următorului nivel. Aceasta crește indexul nivelului curent în cadrul
    // obiectului levelManager și încarcă nivelul următor utilizând metoda loadNextLevel() a obiectului levelManager.
    // Apoi, se resetează toate stările și variabilele necesare.
    public void loadNextLevel() {
        levelManager.setLevelIndex(levelManager.getLvlIndex() + 1);
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
        drawShip = false;
    }

    // Această funcție este responsabilă de încărcarea nivelului de start. Aceasta încarcă inamicii și
    // obiectele asociate nivelului curent utilizând metodele loadEnemies() și loadObjects() ale obiectului levelManager.
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    // Această funcție calculează valoarea maximă a deplasării orizontale a nivelului (maxLvlOffsetX).
    // Aceasta obține valoarea din obiectul levelManager utilizând metoda getCurrentLevel().getLvlOffset().
    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    //  Această funcție este responsabilă de inițializarea claselor necesare pentru gestionarea jocului. Aceasta creează
    //  obiectele levelManager, enemyManager, objectManager și player, și inițializează variabilele asociate imaginilor și animației navei.
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
        rain = new Rain();
    }

    // Această funcție este responsabilă de actualizarea stării jocului. În funcție de starea jocului (paused, lvlCompleted, gameCompleted,
    // gameOver, playerDying), se apelează metodele corespunzătoare de actualizare ale obiectelor și managerilor implicați. De asemenea,
    // se verifică dacă jucătorul este aproape de marginea ecranului și se actualizează animația navei.
    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (lvlCompleted)
            levelCompletedOverlay.update();
        else if (gameCompleted)
            gameCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if (playerDying)
            player.update();
        else {
            if (drawRain)
                rain.update(xLvlOffset);
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
            checkCloseToBorder();
            if (drawShip)
                updateShipAni();
        }
    }

    // Nefolosit
    private void updateShipAni() {
        shipTick++;
        if (shipTick >= 35) {
            shipTick = 0;
            shipAni++;
            if (shipAni >= 4)
                shipAni = 0;
        }

        shipHeightDelta += shipHeightChange * shipDir;
        shipHeightDelta = Math.max(Math.min(10 * Game.SCALE, shipHeightDelta), 0);

        if (shipHeightDelta == 0)
            shipDir = 1;
        else if (shipHeightDelta == 10 * Game.SCALE)
            shipDir = -1;

    }

    // Această funcție verifică dacă jucătorul se apropie de marginea ecranului și, în caz afirmativ, actualizează poziția orizontală a
    // nivelului (xLvlOffset) pentru a menține jucătorul vizibil în cadrul jocului.
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff > rightBorder)
            xLvlOffset = xLvlOffset + diff - rightBorder;
        else if(diff < leftBorder)
                xLvlOffset = xLvlOffset + diff - leftBorder;

       xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
    }

    // Această funcție este responsabilă de desenarea elementelor jocului pe ecran. Se desenează fundalul, norii, ploaia (dacă este activată),
    // nivelul, obiectele, inamicii, jucătorul și copacii de fundal. În funcție de starea jocului (paused, gameOver, lvlCompleted, gameCompleted),
    // se desenează suprapuneri corespunzătoare.
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);
        if (drawRain)
            rain.draw(g, xLvlOffset);

        if (drawShip)
            g.drawImage(shipImgs[shipAni], (int) (100 * Game.SCALE) - xLvlOffset, (int) ((288 * Game.SCALE) + shipHeightDelta), (int) (78 * Game.SCALE), (int) (72 * Game.SCALE), null);

        levelManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        objectManager.drawBackgroundTrees(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(154, 102, 143, 200));
            g.fillRect(0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
        else if (gameCompleted)
            gameCompletedOverlay.draw(g);

    }

    // Această funcție desenează norii pe ecran. Prin intermediul unui buclă for, se iterează de patru ori și se desenează un nor mare
    // la poziții regulate pe axa X. Apoi, prin intermediul unei alte bucle for, se desenează mai mulți nori mici, fiecare având o
    // poziție și o distanță deplasată pe axa X diferită.
    private void drawClouds(Graphics g) {
        for (int i = 0; i < 4; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    // Pentru depanare
    public void setGameCompleted() {
        gameCompleted = true;
    }

    // Această funcție resetează starea jocului finalizat (gameCompleted = false). Este utilizată pentru a reseta starea de finalizare a jocului.
    public void resetGameCompleted() {
        gameCompleted = false;
    }

    // Această funcție resetează toate variabilele și obiectele necesare pentru a reseta jocul la starea inițială. Aceasta
    // include variabilele gameOver, paused, lvlCompleted, playerDying, drawRain, precum și resetarea jucătorului, inamicilor și obiectelor.
    public void resetAll(){
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        drawRain = false;
        setDrawRainBoolean();
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    // Această funcție stabilește dacă va fi desenată ploaie pe ecran, având o probabilitate de 35%. Dacă valoarea generată aleatoriu
    // (rnd.nextFloat()) este mai mare sau egală cu 0.65, atunci variabila drawRain este setată la true, altfel rămâne false.
    private void setDrawRainBoolean() {
        //35% probabilitate de a ploua cand incep un nivel nou
        if (rnd.nextFloat() >= 0.65f)
            drawRain = true;
    }

    // Această funcție setează starea jocului ca fiind "game over" (gameOver = true) sau "nu este game over" (gameOver = false),
    // în funcție de valoarea booleană furnizată ca argument.
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    // Această funcție verifică dacă atacul jucătorului a lovit un inamic. Este apelată funcția checkEnemyHit() din obiectul
    // enemyManager, furnizând ca argument caseta de atac (attackBox) a jucătorului.
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    // Această funcție verifică dacă atacul jucătorului a lovit un obiect. Este apelată funcția checkObjectHit() din obiectul objectManager,
    // furnizând ca argument caseta de atac (attackBox) a jucătorului.
    public void checkObjectHit(Rectangle2D.Float attackBox){
        objectManager.checkObjectHit(attackBox);
    }

    // Această funcție verifică dacă jucătorul a atins o poțiune. Este apelată funcția checkObjectTouched() din obiectul objectManager,
    // furnizând ca argument caseta de atac (hitbox) a jucătorului.
    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    // Această funcție este apelată atunci când se face clic pe mouse. Dacă jocul nu este în starea de "game over", se verifică butonul
    // mouse-ului apăsat (e.getButton()) și se setează variabilele corespunzătoare în obiectul jucătorului (setAttacking(true)) sau se
    // activează atacul special (powerAttack()).
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if(e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }

    }

    // Această funcție este apelată atunci când se apasă o tastă. Dacă jocul nu este în starea de "game over", "game completed" sau "level completed",
    // se verifică codul tastei (e.getKeyCode()) și se setează variabilele corespunzătoare în obiectul jucătorului (setLeft(true), setRight(true),
    // setJump(true)) sau se activează pauza (paused = !paused).
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:

                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
    }

    // Această funcție este apelată atunci când se eliberează o tastă. Dacă jocul nu este în starea de "game over", "game completed"
    // sau "level completed", se verifică codul tastei (e.getKeyCode()) și se setează variabilele corespunzătoare în obiectul jucătorului
    // (setLeft(false), setRight(false), setJump(false)).
    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }

    // Această funcție este apelată atunci când mouse-ul este tras în timp ce se face clic. Dacă jocul nu este în starea de "game over",
    // "game completed" sau "level completed", se verifică dacă jocul este în pauză (paused) și se apelează funcția mouseDragged() din
    // obiectul pauseOverlay, furnizând evenimentul mouse-ului (e).
    public void mouseDragged(MouseEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    // Această funcție este apelată atunci când se apasă un buton al mouse-ului. În funcție de starea jocului (game over, paused, level completed,
    // game completed), se apelează funcțiile corespunzătoare din obiectele gameOverOverlay, pauseOverlay, levelCompletedOverlay sau
    // gameCompletedOverlay, furnizând evenimentul mouse-ului (e).
    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (paused)
            pauseOverlay.mousePressed(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);

    }

    // Această funcție este apelată atunci când se eliberează un buton al mouse-ului. În funcție de starea jocului (game over, paused,
    // level completed, game completed), se apelează funcțiile corespunzătoare din obiectele gameOverOverlay, pauseOverlay, levelCompletedOverlay
    // sau gameCompletedOverlay, furnizând evenimentul mouse-ului (e).
    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (paused)
            pauseOverlay.mouseReleased(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    // Această funcție este apelată atunci când mouse-ul este mișcat pe ecran. În funcție de starea jocului (game over, paused,
    // level completed, game completed), se apelează funcțiile corespunzătoare din obiectele gameOverOverlay, pauseOverlay, levelCompletedOverlay
    // sau gameCompletedOverlay, furnizând evenimentul mouse-ului (e).
    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
        else if (paused)
            pauseOverlay.mouseMoved(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
    }

    // Această funcție setează starea de "level completed" a jocului. Dacă nivelul curent este ultimul nivel disponibil, atunci se
    // setează starea de "game completed" (gameCompleted = true), se resetează nivelul la primul nivel (levelManager.setLevelIndex(0)) și
    // se încarcă următorul nivel. Altfel, se setează starea de "level completed" (lvlCompleted = levelCompleted).
    public void setLevelCompleted(boolean levelCompleted) {
        game.getAudioPlayer().lvlCompleted();
        if (levelManager.getLvlIndex() + 1 >= levelManager.getAmountOfLevels()) {
            // Nu mai există nivele.
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();
            return;
        }
        this.lvlCompleted = levelCompleted;
    }

    // Această funcție setează valoarea maximă a deplasării nivelului pe axa X (maxLvlOffsetX). Este utilizată pentru a controla distanța
    // maximă pe care nivelul poate fi deplasat în stânga sau în dreapta.
    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    // Această funcție setează starea de pauză a jocului ca fiind "neactivată" (paused = false). Este utilizată pentru a reporni jocul după o pauză.
    public void unpauseGame(){
        paused = false;
    }

    // Această funcție este apelată atunci când fereastra jocului pierde focusul. Resetază variabilele corespunzătoare în obiectul jucătorului
    // (resetDirBooleans()), pentru a opri mișcarea jucătorului.
    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    // Această funcție returnează obiectul jucător (player).
    public Player getPlayer() {
        return player;
    }

    // Această funcție returnează obiectul enemyManager, care gestionează inamicii.
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    // Această funcție returnează obiectul objectManager, care gestionează obiectele din joc.
    public ObjectManager getObjectManager(){
        return objectManager;
    }

    // Această funcție returnează obiectul levelManager, care gestionează nivelele jocului.
    public LevelManager getLevelManager(){
        return levelManager;
    }

    // Această funcție verifică dacă jucătorul a atins vârfurile. Este apelată funcția checkSpikesTouched() din obiectul
    // objectManager, furnizând obiectul jucătorului ca argument (p).
    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    // Această funcție setează starea jucătorului ca fiind "dying" (playerDying = true) sau "nu este dying"
    // (playerDying = false), în funcție de valoarea booleană furnizată ca argument.
    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
