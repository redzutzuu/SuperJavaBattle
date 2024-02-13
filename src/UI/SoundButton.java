package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton{

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();
    }

    // Această funcție încarcă imaginile corespunzătoare butonului de sunet dintr-un fișier de atlas al imaginilor. Se creează o matrice bidimensională
    // de imagini soundImgs pentru a stoca imaginile pentru diferite stări ale butonului (sonor activat sau dezactivat, stare de mouse deasupra și
    // stare de apăsare).
    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];
        for(int j = 0; j < soundImgs.length; j++)
            for(int i = 0; i < soundImgs[j].length; i++)
                soundImgs[j][i] = temp.getSubimage(i*SOUND_SIZE_DEFAULT, j*SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    // Această funcție actualizează starea butonului de sunet în funcție de diferitele variabile de stare, cum ar fi muted (daca sunetul este
    // dezactivat), mouseOver (dacă mouse-ul este deasupra butonului) și mousePressed (dacă butonul este apăsat). Se actualizează rowIndex și
    // colIndex în funcție de aceste stări pentru a selecta imaginea corespunzătoare din matricea soundImgs.
    public void update(){
        if(muted)
            rowIndex = 1;
        else
            rowIndex = 0;
        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;
    }

    // Această funcție resetează variabilele mouseOver și mousePressed la valoarea false, indicând că butonul nu este în stare de mouse deasupra sau apăsat.
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    // Această funcție desenează butonul de sunet utilizând imaginea corespunzătoare din matricea soundImgs. Imaginea este desenată folosind valorile de
    // poziție (x, y) și dimensiune (width, height) ale butonului.
    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
    }

    // Această funcție returnează valoarea variabilei mouseOver, indicând dacă mouse-ul este deasupra butonului de sunet.
    public boolean isMouseOver() {
        return mouseOver;
    }

    // Această funcție setează valoarea variabilei mouseOver în funcție de starea dată.
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    // Această funcție returnează valoarea variabilei mousePressed, indicând dacă butonul de sunet este apăsat.
    public boolean isMousePressed() {
        return mousePressed;
    }

    // Această funcție setează valoarea variabilei mousePressed în funcție de starea dată.
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    // Această funcție returnează valoarea variabilei muted, indicând dacă sunetul este dezactivat.
    public boolean isMuted() {
        return muted;
    }

    // Această funcție setează valoarea variabilei muted în funcție de starea dată.
    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
