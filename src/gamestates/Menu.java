package gamestates;

import UI.MenuButton;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    // Constructorul clasei care primește un obiect Game și inițializează starea meniului, încărcând butoanele și imaginea de fundal.
    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    // Metodă privată care încarcă imaginea de fundal a meniului și setează dimensiunile și poziția acesteia.
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    // Metodă privată care încarcă butoanele meniului și le setează pozițiile.
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150*Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220*Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(290*Game.SCALE), 2, Gamestate.QUIT);
    }

    // Suprascrie metoda update() din clasa de bază State și actualizează starea butoanelor meniului.
    @Override
    public void update() {
        for(MenuButton mb : buttons)
            mb.update();
    }

    // Suprascrie metoda draw(Graphics g) din clasa de bază State și desenează imaginea de fundal și butoanele meniului.
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for(MenuButton mb : buttons)
            mb.draw(g);
    }

    // Suprascrie metoda mouseClicked(MouseEvent e) din interfața Statemethods și nu implementează nicio acțiune.
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    // Suprascrie metoda mousePressed(MouseEvent e) din interfața Statemethods și verifică dacă un buton al meniului a fost apăsat și actualizează starea acestuia.
    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                mb.setMousePressed(true);
            }
        }
    }

    // Suprascrie metoda mouseReleased(MouseEvent e) din interfața Statemethods și verifică dacă un buton al meniului a fost eliberat.
    // Dacă da, se aplică starea asociată butonului și se actualizează melodia de fundal în funcție de starea jocului. Se resetează starea butoanelor.
    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                if(mb.isMousePressed())
                    mb.applyGamestate();
                if(mb.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                break;
            }
        }
        resetButtons();
    }

    // Metodă privată care resetează starea butoanelor meniului.
    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    // Suprascrie metoda mouseMoved(MouseEvent e) din interfața Statemethods și verifică dacă cursorul se află deasupra unui buton al meniului.
    // Se actualizează starea butoanelor în funcție de poziția cursorului.
    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons){
            mb.setMouseOver(false);
        }
        for(MenuButton mb : buttons){
            if(isIn(e,mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    // Suprascrie metoda keyPressed(KeyEvent e) din interfața Statemethods și verifică dacă tasta Enter a fost apăsată.
    // Dacă da, se setează starea jocului la PLAYING.
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;
    }

    // Suprascrie metoda keyReleased(KeyEvent e) din interfața Statemethods și nu implementează nicio acțiune.
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
