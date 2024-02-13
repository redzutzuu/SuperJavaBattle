package UI;

import java.awt.*;

public class PauseButton {

    protected int x, y, width, height;
    protected Rectangle bounds;

    // Inițializează obiectul PauseButton cu coordonatele (x, y) și dimensiunile (width, height) specificate. Apelul funcției createBounds()
    // creează dreptunghiul de coliziune.
    public PauseButton(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    // Creează dreptunghiul de coliziune utilizând coordonatele și dimensiunile butonului.
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    // Funcțiile getX(), getY(), getWidth(), getHeight(): Returnează valorile corespunzătoare proprietăților butonului.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Returnează dreptunghiul de coliziune al butonului.
    public Rectangle getBounds() {
        return bounds;
    }

    // Funcțiile setX(), setY(), setWidth(), setHeight(): Setează noile valori pentru proprietățile butonului.
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Setează dreptunghiul de coliziune al butonului cu un alt dreptunghi specificat.
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
