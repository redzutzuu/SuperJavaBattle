package gamestates;

import UI.AudioOptions;
import UI.PauseButton;
import UI.UrmButton;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class GameOptions extends State implements Statemethods{
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;

    // Este constructorul clasei GameOptions care primește un obiect Game și inițializează câmpurile clasei. Apelează metodele loadImgs()
    // și loadButton() și obține o referință la obiectul AudioOptions din obiectul game.
    public GameOptions(Game game){
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    // Încarcă imaginile necesare pentru opțiunile jocului. Inițializează câmpurile backgroundImg, optionsBackgroundImg, bgX, bgY,
    // bgW și bgH pe baza imaginilor încărcate și a dimensiunilor jocului.
    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
        bgW = (int)(optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(33 * Game.SCALE);
    }

    // Inițializează butonul menuB utilizat în opțiunile jocului. Setează poziția și dimensiunile butonului.
    private void loadButton() {
        int menuX = (int)(387 * Game.SCALE);
        int menuY = (int)(325 * Game.SCALE);
        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    // Actualizează starea opțiunilor jocului. Apelează metodele update() ale obiectelor menuB și audioOptions.
    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    // Desenează opțiunile jocului pe ecran. Desenează imaginile de fundal, butonul menuB și
    // obiectul audioOptions. Utilizează obiectul Graphics pentru a desena elementele.
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        audioOptions.draw(g);
    }

    // Gestionarea evenimentului de tragere a mouse-ului. Transmite evenimentul obiectului audioOptions pentru a gestiona tragerea.
    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    // Gestionarea evenimentului de click al mouse-ului. Nu are niciun comportament definit în această clasă.
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // Gestionarea evenimentului de apăsare a mouse-ului. Verifică dacă evenimentul are loc pe butonul menuB. Dacă da,
    // setează starea mousePressed a butonului. În caz contrar, transmite evenimentul obiectului audioOptions pentru a gestiona apăsarea.
    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)){
            menuB.setMousePressed(true);
        }
        else
            audioOptions.mousePressed(e);
    }

    // Gestionarea evenimentului de eliberare a mouse-ului. Verifică dacă evenimentul are loc pe butonul menuB. Dacă da,
    // verifică dacă butonul a fost apăsat și stabilește starea jocului ca fiind starea de meniu. În caz contrar, transmite evenimentul
    // obiectului audioOptions pentru a gestiona eliberarea. Resetază stările butonului menuB.
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)) {
            if (menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        }
        else
            audioOptions.mouseReleased(e);
        menuB.resetBools();
    }

    // Gestionarea evenimentului de mișcare a mouse-ului. Verifică dacă evenimentul are loc pe butonul menuB. Dacă da, setează starea
    // mouseOver a butonului ca fiind true. În caz contrar, transmite evenimentul obiectului audioOptions pentru a gestiona mișcarea mouse-ului.
    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        if(isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    // Gestionarea evenimentului de apăsare a unei taste. Verifică dacă tasta apăsată este tasta "ESC". Dacă da,
    // stabilește starea jocului ca fiind starea de meniu.
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    // Gestionarea evenimentului de eliberare a unei taste. Nu are niciun comportament definit în această clasă.
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Verifică dacă evenimentul de mouse se află în interiorul dreptunghiului delimitat de butonul b. Returnează true dacă
    // evenimentul se află în interiorul butonului, altfel returnează false.
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
