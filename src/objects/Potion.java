package objects;

import main.Game;

public class Potion extends GameObject{

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    // Constructorul clasei Potion primește trei parametri: x și y, care reprezintă coordonatele poțiunii, și objType, care indică
    // tipul obiectului. Constructorul inițializează variabilele de stare specifice clasei, setează doAnimation la true, inițializează
    // hitbox-ul cu dimensiunile corespunzătoare și setează offset-urile de desenare pe axele X și Y în funcție de scala jocului.
    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int)(3 * Game.SCALE);
        yDrawOffset = (int)(2 * Game.SCALE);
        maxHoverOffset = (int)(10 * Game.SCALE);
    }

    //  Această funcție este responsabilă pentru actualizarea stării poțiunii. Ea apelează updateAnimationTick() pentru a actualiza
    //  animația poțiunii și updateHover() pentru a actualiza offset-ul de hover.
    public void update(){
        updateAnimationTick();
        updateHover();
    }

    // Această funcție actualizează offset-ul de hover al poțiunii. Offset-ul de hover (hoverOffset) se modifică cu o valoare
    // specifică (0.075f * Game.SCALE * hoverDir). Dacă offset-ul atinge sau depășește valoarea maximă (maxHoverOffset), direcția de
    // hover (hoverDir) se inversează. Offset-ul de hover este aplicat și la hitbox pentru a actualiza poziția de coliziune a poțiunii.
    private void updateHover() {
        hoverOffset = hoverOffset + (0.075f * Game.SCALE * hoverDir);
        if(hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else
            if(hoverOffset < 0)
                hoverDir = 1;
        hitbox.y = y + hoverOffset;
    }
}
