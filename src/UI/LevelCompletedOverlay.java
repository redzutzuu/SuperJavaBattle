package UI;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    // Primește un obiect Playing ca parametru și inițializează variabila playing cu acesta. Apoi, apelează metodele initImg() și initButtons() pentru
    // a inițializa imaginea și butoanele.
    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    // Inițializează butoanele necesare pentru ecranul de finalizare a nivelului. Acestea sunt menu și next. Se stabilesc coordonatele și dimensiunile
    // lor în funcție de Game.SCALE și Game.GAME_WIDTH.
    private void initButtons() {
        int menuX = (int)(330 * Game.SCALE);
        int nextX = (int)(445 * Game.SCALE);
        int y = (int)(195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    // Inițializează imaginea de fundal pentru ecranul de finalizare a nivelului. Se încarcă imaginea utilizând LoadSave.GetSpriteAtlas() și se stabilesc
    // coordonatele și dimensiunile imaginii în funcție de Game.SCALE și Game.GAME_WIDTH.
    private void initImg(){
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int)(img.getWidth() * Game.SCALE);
        bgH = (int)(img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(75 * Game.SCALE);
    }

    // Desenează ecranul de finalizare a nivelului. Se colorează fundalul cu o anumită nuanță de culoare, se desenează imaginea de fundal și se desenează
    // butoanele next și menu pe ecran.
    public void draw(Graphics g){
        g.setColor(new Color(154, 102, 143, 200));
        g.fillRect(0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    // Actualizează starea butoanelor next și menu. Aceasta trebuie apelată în fiecare ciclu de actualizare al jocului pentru a asigura funcționarea
    // corectă a interacțiunii cu butoanele.
    public void update(){
        next.update();
        menu.update();
    }

    // Verifică dacă evenimentul de mouse se află în interiorul dreptunghiului de coliziune al unui buton specificat.
    private boolean isIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    // Gestionează mișcarea cursorului mouse-ului pe ecran și actualizează starea butoanelor next și menu în funcție de poziția cursorului.
    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false);
        menu.setMouseOver(false);
        if(isIn(menu, e))
            menu.setMouseOver(true);
        else if(isIn(next, e))
            next.setMouseOver(true);
    }

    // Gestionează eliberarea unui buton al mouse-ului și realizează acțiunile corespunzătoare în funcție de butonul apăsat. Dacă evenimentul de
    // se află în interiorul butonului menu, se resetează jocul și se revine la meniul principal. Dacă evenimentul de mouse se află în interiorul
    // butonului next, se încarcă nivelul următor și se setează melodia corespunzătoare nivelului.
    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)) {
            if (menu.isMousePressed()){
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }
        else if(isIn(next, e))
            if(next.isMousePressed()){
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        menu.resetBools();
        next.resetBools();
    }

    // Gestionează apăsarea unui buton al mouse-ului și setează starea mousePressed a butoanelor next și menu în funcție de butonul apăsat.
    public void mousePressed(MouseEvent e){
        if(isIn(menu, e))
            menu.setMousePressed(true);
        else if(isIn(next, e))
            next.setMousePressed(true);
    }
}
