package UI;

import gamestates.Gamestate;
import utilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.Buttons.*;

public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetcenter = B_WIDTH / 2;
    private int yOffsetcenter = B_HEIGHT / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    // Constructorul primește coordonatele xPos și yPos ale butonului, un rowIndex care specifică rândul imaginilor butoanelor din atlasul de
    // sprite-uri, și un state care reprezintă starea de joc asociată butonului. Constructorul inițializează variabilele respective și apelează
    // metodele loadImgs() și initBounds() pentru încărcarea imaginilor butonului și inițializarea dreptunghiului de coliziune (bounds).
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    // Creează un dreptunghi de coliziune pentru buton, bazat pe coordonatele și dimensiunile butonului.
    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetcenter, yPos, B_WIDTH, B_HEIGHT);
    }

    // Încarcă imaginile butonului dintr-un atlas de sprite-uri și le salvează într-un tablou de BufferedImage numit imgs.
    private void loadImgs(){
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for(int i = 0; i < imgs.length; i++)
           imgs[i] = temp.getSubimage(i*B_WIDTH_DEFAULT, rowIndex*B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
    }

    // Desenează butonul pe ecran utilizând imaginea corespunzătoare stării curente (index).
    public void draw(Graphics g){
        g.drawImage(imgs[index], xPos - xOffsetcenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    // Actualizează starea butonului în funcție de starea cursorului mouse-ului (mouseOver și mousePressed). Setează index în funcție de aceste
    // stări pentru a selecta imaginea potrivită pentru desenare.
    public void update(){
        index = 0;
        if(mouseOver){
            index = 1;
        }
        if(mousePressed){
            index = 2;
        }
    }

    // Setează starea de suprapunere (mouseOver) a butonului.
    public void setMouseOver(boolean mouseOver){
        this.mouseOver = mouseOver;
    }

    // Returnează true sau false în funcție de starea apăsării (mousePressed) a butonului.
    public boolean isMousePressed() {
        return mousePressed;
    }

    // Setează starea de apăsare (mousePressed) a butonului.
    public void setMousePressed(boolean mousePressed){
        this.mousePressed = mousePressed;
    }

    // Returnează dreptunghiul de coliziune (bounds) al butonului.
    public Rectangle getBounds(){
        return bounds;
    }

    // Setează starea de joc (state) la starea asociată butonului.
    public void applyGamestate(){
        Gamestate.state = state;
    }

    // Resetează variabilele mouseOver și mousePressed la false.
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    // Returnează starea de joc (state) asociată butonului.
    public Gamestate getState(){
        return state;
    }
}
