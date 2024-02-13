package UI;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB, unpauseB;
    private AudioOptions audioOptions;

    // Constructorul clasei. Primește o instanță a clasei Playing și inițializează variabilele membru și componente precum imaginea de fundal și butoanele.
    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground();
        createUrmButtons();
        audioOptions = playing.getGame().getAudioOptions();
    }

    // Creează și configurează butoanele pentru meniul pauzei (menuB), reluarea nivelului (replayB) și reluarea jocului (unpauseB).
    private void createUrmButtons() {
        int menuX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpauseX = (int)(462 * Game.SCALE);
        int bY = (int)(325 * Game.SCALE);

        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);

    }

    // Încarcă imaginea de fundal pentru ecranul de pauză și stabilește dimensiunile și poziția acesteia.
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(25 * Game.SCALE);
    }

    // Actualizează starea butoanelor și opțiunilor audio din ecranul de pauză.
    public void update(){
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    // Desenează ecranul de pauză pe baza imaginii de fundal și a componentelor, cum ar fi butoanele și opțiunile audio.
    public void draw(Graphics g){
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOptions.draw(g);
    }

    // Gestionarea evenimentului de trascăiere al mouse-ului. Transmite evenimentul către componenta audioOptions pentru a gestiona modificările
    // de tragere a cursorului.
    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    // Gestionarea evenimentului de apăsare a mouse-ului. Verifică dacă evenimentul are loc pe butoanele menuB, replayB sau unpauseB și setează
    // starea de apăsare corespunzătoare. În caz contrar, transmite evenimentul către componenta audioOptions.
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    // Gestionarea evenimentului de eliberare a mouse-ului. Verifică dacă evenimentul are loc pe butoanele menuB, replayB sau unpauseB și
    // acționează corespunzător, cum ar fi resetarea jocului, revenirea la meniu sau reluarea jocului. În caz contrar, transmite evenimentul
    // către componenta audioOptions.
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        } else
            audioOptions.mouseReleased(e);
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    // Gestionarea evenimentului de deplasare a mouse-ului. Verifică dacă evenimentul are loc pe butoanele menuB, replayB sau unpauseB și setează
    // starea de trecere peste corespunzătoare. În caz contrar, transmite evenimentul către componenta audioOptions.
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    // Verifică dacă evenimentul de mouse are loc în interiorul limitelor unui buton specificat. Returnează true dacă evenimentul este în interiorul
    // butonului și false în caz contrar.
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
