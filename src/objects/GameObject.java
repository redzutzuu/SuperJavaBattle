package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    // Acesta este constructorul clasei GameObject și primește trei parametri: x, y și objType.
    // Initializează variabilele membru x, y și objType cu valorile primite ca parametri.
    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    // Această metodă este protejată (accesibilă doar din clasele derivate) și este responsabilă de actualizarea indicelui și ticăitului animației.
    // Crește aniTick cu 1.
    // Verifică dacă aniTick a depășit ANI_SPEED. În caz afirmativ, resetează aniTick la 0 și crește aniIndex cu 1.
    // Verifică dacă aniIndex a depășit numărul de sprite-uri disponibile pentru tipul de obiect (objType). În caz afirmativ, resetează aniIndex la 0.
    // Verifică și gestionează anumite condiții specifice tipurilor de obiecte. Dacă objType este BARREL sau BOX, setează doAnimation pe false și active
    // pe false. Dacă objType este CANNON_LEFT sau CANNON_RIGHT, setează doAnimation pe false.
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;
                if(objType == BARREL || objType == BOX){
                    doAnimation = false;
                    active = false;
                }
                else
                    if(objType == CANNON_LEFT || objType == CANNON_RIGHT){
                        doAnimation = false;
                    }
            }
        }
    }

    // Această metodă resetează starea animației obiectului la valorile implicite.
    // Setează aniIndex și aniTick la 0.
    // Setează active pe true.
    // Verifică objType și gestionează condițiile specifice pentru anumite tipuri de obiecte. Dacă objType este BARREL, BOX, CANNON_LEFT sau
    // CANNON_RIGHT, setează doAnimation pe false. În caz contrar, setează doAnimation pe true.
    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;
        if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;
    }

    // Această metodă protejată este responsabilă de inițializarea hitbox-ului obiectului.
    // Creează un nou obiect Rectangle2D.Float cu coordonatele și dimensiunile specificate.
    // Dimensiunile hitbox-ului sunt scalate cu Game.SCALE (o constantă definită în clasa Game).
    protected void initHitbox ( int width, int height){
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    // Pentru debugging.
    // Această metodă desenează hitbox-ul obiectului.
    // Setează culoarea desenului la roz (Color.PINK).
    // Desenează un dreptunghi în jurul hitbox-ului cu coordonatele și dimensiunile corespunzătoare.
    public void drawHitbox (Graphics g, int xLvlOffset){
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    // Aceste metode furnizează acces la variabilele membru private ale clasei GameObject.
    // Metodele getObjType(), getHitbox(), isActive(), getxDrawOffset(), getyDrawOffset(), getAniIndex() și getAniTick() furnizează
    // valoarea corespunzătoare a variabilei membru.
    // Metodele setActive(boolean active) și setAnimation(boolean doAnimation) setează valorile corespunzătoare pentru variabilele membru.
    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }
    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getAniTick(){
        return aniTick;
    }

}
