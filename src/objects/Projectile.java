package objects;

import main.Game;

import java.awt.geom.Rectangle2D;
import static utilz.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    // Constructorul clasei Projectile primește coordonatele x și y și direcția în care va fi lansat proiectilul. Inițializează obiectul
    // hitbox de tip Rectangle2D.Float care reprezintă zona de coliziune a proiectilului. Direcția proiectilului este setată și
    // înregistrează starea activă a proiectilului.
    public Projectile(int x, int y, int dir){
        int xOffset = (int)(-3 * Game.SCALE);
        int yOffset = (int)(5 * Game.SCALE);
        if(dir == 1)
            xOffset = (int)(29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    // Metoda updatePos() actualizează poziția proiectilului pe baza direcției și vitezei definite de constanta SPEED. Modifică coordonata
    // x a obiectului hitbox pentru a muta proiectilul pe axa orizontală.
    public void updatePos(){
        hitbox.x = hitbox.x + dir * SPEED;
    }

    // Metoda setPos(int x, int y) setează poziția proiectilului la coordonatele x și y specifice. Actualizează coordonatele x și y ale obiectului hitbox.
    public void setPos(int x, int y){
        hitbox.x = x;
        hitbox.y = y;
    }

    // Metoda getHitbox() returnează obiectul hitbox, care reprezintă zona de coliziune a proiectilului.
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    // Metoda setActive(boolean active) setează starea activă a proiectilului. Dacă parametrul active este true, proiectilul este activ,
    // iar dacă este false, proiectilul este inactiv.
    public void setActive(boolean active){
        this.active = active;
    }

    // Metoda isActive() returnează starea activă a proiectilului. Returnează true dacă proiectilul este activ și false dacă este inactiv.
    public boolean isActive(){
        return active;
    }
}
