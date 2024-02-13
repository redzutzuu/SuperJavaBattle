package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.VolumeButtons.*;


public class VolumeButton extends PauseButton{

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    // Primește patru parametri x, y, width și height care reprezintă poziția și dimensiunile butonului.
    // Apelând constructorul superclasei PauseButton și setând poziția și dimensiunile butonului.
    // Inițializează câmpurile buttonX, minX și maxX utilizate pentru gestionarea mișcării butonului pe axa x.
    // Apelul funcției loadImgs() pentru a încărca imaginile butonului și sliderului.
    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    // Folosește clasa LoadSave pentru a obține o atlas de sprite-uri.
    // Inițializează câmpurile imgs cu 3 subimagini din atlas, care reprezintă stările butonului (normal, mouse hover și mouse apăsat).
    // Inițializează câmpul slider cu o subimagine care reprezintă sliderul volumului.
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i*VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT );
        slider = temp.getSubimage(3*VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

    }

    // Actualizează indicele index pentru a determina starea de afișare a butonului, în funcție de starea mouse-ului (mouseOver și mousePressed).
    // Indexul 0 reprezintă starea normală, 1 reprezintă starea mouse hover, iar 2 reprezintă starea mouse apăsat.
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    // Desenează sliderul volumului și butonul utilizând imaginile încărcate în funcția loadImgs. Desenarea se realizează utilizând obiectul Graphics.
    public void draw(Graphics g){
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    // Modifică poziția butonului pe axa x în funcție de valoarea parametrului x.
    // Verifică dacă noua poziție x este în interiorul intervalului valid (minX și maxX). Dacă este în afara intervalului, butonul va rămâne
    // la una dintre limite.
    // Actualizează valoarea float a volumului (floatValue) în funcție de noua poziție a butonului.
    // Actualizează coordonata x a dreptunghiului de coliziune al butonului.
    public void changeX(int x){
        if(x < minX)
            buttonX = minX;
        else
            if(x > maxX)
                buttonX = maxX;
            else
                buttonX = x;
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    // Calculează valoarea volumului în format float în funcție de poziția butonului pe axa x.
    // range reprezintă intervalul total al pozițiilor valide ale butonului pe axa x.
    // value reprezintă distanța dintre poziția butonului și valoarea minimă a poziției valide.
    // floatValue reprezintă valoarea volumului în format float (un număr între 0 și 1).
    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    // Resetează câmpurile mousePressed și mouseOver la false. Această funcție este apelată pentru a reseta stările mouse-ului asociate butonului.
    public void resetBools(){
        mousePressed = false;
        mouseOver = false;
    }

    // Returnează starea curentă a câmpului mouseOver (true sau false), care indică dacă cursorul mouse-ului se află deasupra butonului.
    public boolean isMouseOver() {
        return mouseOver;
    }

    // Setează starea câmpului mouseOver cu valoarea primită ca parametru. Această funcție este utilizată pentru a actualiza
    // starea mouse-ului asociată butonului.
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    // Returnează starea curentă a câmpului mousePressed (true sau false), care indică dacă butonul a fost apăsat cu mouse-ul.
    public boolean isMousePressed() {
        return mousePressed;
    }

    // Setează starea câmpului mousePressed cu valoarea primită ca parametru. Această funcție este utilizată pentru
    // a actualiza starea apăsării mouse-ului asociată butonului.
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    // Returnează valoarea volumului în format float (floatValue). Această valoare reprezintă proporția poziției
    // butonului în intervalul valid al pozițiilor pe axa x.
    public float getFloatValue(){
        return floatValue;
    }
}
