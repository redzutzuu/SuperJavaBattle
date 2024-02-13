package objects;

import main.Game;

public class Spike extends GameObject{

    // Constructorul Spike(int x, int y, int objType) este utilizat pentru a inițializa un obiect Spike la o anumită poziție (x, y)
    // și cu un anumit tip objType. Constructorul primește aceste valori și le pasează constructorului clasei de bază GameObject
    // utilizând super(x, y, objType). Apoi, sunt apelate funcțiile initHitbox(), setXDrawOffset() și setYDrawOffset() pentru a seta
    // hitbox-ul și offset-ul de desenare al obiectului.
    public Spike(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y = hitbox.y + yDrawOffset;
    }
}
