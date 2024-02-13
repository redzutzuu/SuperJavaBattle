package UI;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menu, play;

    // Este constructorul clasei GameOverOverlay. Primește un obiect de tip Playing ca parametru și îl utilizează pentru a inițializa
    // variabila playing din clasă. De asemenea, apelează metodele createImg() și createButtons() pentru a inițializa imaginea de fundal
    // și butoanele asociate.
    public GameOverOverlay(Playing playing){
        this.playing = playing;
        createImg();
        createButtons();
    }

    // Această metodă creează butoanele necesare. În acest caz, sunt create două obiecte de tip UrmButton numite menu și play, cu coordonate
    // și parametri specificați, care vor fi utilizate pentru a reveni la meniul principal și pentru a relua jocul.
    private void createButtons() {
        int menuX = (int)(335 * Game.SCALE);
        int playX = (int)(440 * Game.SCALE);
        int y = (int)(195 * Game.SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    // Această metodă încarcă imaginea asociată ecranului de înfrângere a jocului și setează coordonatele și dimensiunile imaginii în funcție
    // de aceasta. Imaginea este obținută prin apelul metodei GetSpriteAtlas() din clasa LoadSave și este stocată în variabila img de tip BufferedImage.
    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int)(img.getWidth() * Game.SCALE);
        imgH = (int)(img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int)(100 * Game.SCALE);
    }

    // Această metodă desenează imaginea de fundal și butoanele pe ecran. Începe prin desenarea unui dreptunghi opac pentru a acoperi întregul
    // ecran, utilizând o culoare specificată. Apoi, imaginea este desenată pe ecran folosind metoda drawImage(), iar butoanele sunt desenate
    // prin apelul metodei draw() a obiectelor menu și play.
    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);
    }

    // Această metodă actualizează starea butoanelor. În acest caz, apelează metodele update() ale obiectelor menu și play.
    public void update(){
        menu.update();
        play.update();
    }

    // Această metodă verifică dacă evenimentul de mouse e se află în interiorul dreptunghiului de coliziune al unui buton specificat b.
    // Returnează true dacă evenimentul de mouse se află în interiorul butonului și false în caz contrar.
    private boolean isIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    // Această metodă este apelată atunci când cursorul mouse-ului este mutat pe ecran. Verifică dacă cursorul se află pe butonul menu
    // sau pe butonul play și setează starea mouseOver a acestora în consecință.
    public void mouseMoved(MouseEvent e){
        play.setMouseOver(false);
        menu.setMouseOver(false);
        if(isIn(menu, e))
            menu.setMouseOver(true);
        else if(isIn(play, e))
            play.setMouseOver(true);
    }

    // Această metodă este apelată atunci când un buton al mouse-ului este eliberat. Verifică dacă evenimentul de mouse se află în interiorul
    // butonului menu sau al butonului play și, în caz afirmativ, verifică dacă butonul respectiv a fost apăsat. Dacă da, se resetează starea
    // jocului și se revine la meniul principal sau se reia jocul, în funcție de butonul apăsat.
    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)) {
            if (menu.isMousePressed()){
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }
        else if(isIn(play, e))
            if(play.isMousePressed()){
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        menu.resetBools();
        play.resetBools();
    }

    // Această metodă este apelată atunci când un buton al mouse-ului este apăsat. Verifică dacă evenimentul de mouse se află în interiorul
    // butonului menu sau al butonului play și, în caz afirmativ, setează starea mousePressed a butonului respectiv la true.
    public void mousePressed(MouseEvent e){
        if(isIn(menu, e))
            menu.setMousePressed(true);
        else if(isIn(play, e))
            play.setMousePressed(true);
    }
}
