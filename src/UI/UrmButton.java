package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton{
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    // Constructorul clasei UrmButton care primește coordonatele x și y ale butonului, lățimea și înălțimea acestuia și indicele rândului
    // la care se află butonul în atlasul de imagini. Apelează constructorul clasei părinte PauseButton și încarcă imaginile butonului.
    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    // Metoda privată loadImgs care încarcă imaginile butonului din atlasul de imagini folosind clasa LoadSave. Aceasta utilizează indicele
    // rândului pentru a extrage imaginile corespunzătoare din atlas și le stochează în array-ul imgs.
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i*URM_DEFAULT_SIZE, rowIndex*URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
    }

    // Metoda update care actualizează starea butonului în funcție de interacțiunea utilizatorului. Setează valoarea variabilei index în funcție
    // de starea butonului (normală, mouse hover, apăsare).
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    // Metoda draw care desenează butonul pe componenta grafică specificată. Folosește variabila index pentru a determina imaginea corespunzătoare
    // stării butonului și desenează imaginea la coordonatele x și y ale butonului, utilizând dimensiunea URM_SIZE.
    public void draw(Graphics g){
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    // Metoda resetBools care resetează stările de interacțiune ale butonului. Setează variabilele mousePressed și mouseOver la false.
    public void resetBools(){
        mousePressed = false;
        mouseOver = false;
    }

    // Metoda setMouseOver care setează starea de hover a butonului. Primeste un parametru boolean mouseOver și setează variabila corespunzătoare
    // la valoarea specificată.
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    // Metoda isMousePressed care verifică dacă butonul este apăsat. Returnează valoarea variabilei mousePressed.
    public boolean isMousePressed() {
        return mousePressed;
    }

    // Metoda setMousePressed care setează starea de apăsare a butonului. Primeste un parametru boolean mousePressed și setează variabila
    // corespunzătoare la valoarea specificată.
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
