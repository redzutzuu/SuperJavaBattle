package objects;

import main.Game;

public class Cannon extends GameObject{
    private int tileY;

    // Acesta este constructorul clasei Cannon și primește trei parametri: x, y și objType.
    // Apelând constructorul superclasei (GameObject) cu ajutorul super(x, y, objType), se inițializează variabilele membru x, y și objType.
    // Variabila tileY este calculată prin împărțirea valorii y la Game.TILES_SIZE.
    // Este apelată metoda initHitbox(40, 26) pentru a inițializa hitbox-ul (o zonă de coliziune) cu dimensiunile specificate.
    // Se ajustează poziția hitbox-ului prin modificarea valorilor hitbox.x și hitbox.y.
    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        hitbox.x = hitbox.x - (int)(4 * Game.SCALE);
        hitbox.y = hitbox.y + (int)(6 * Game.SCALE);
    }

    // Această metodă este responsabilă de actualizarea stării obiectului Cannon.
    // Verifică dacă doAnimation este setat pe true. În caz afirmativ, se apelează metoda updateAnimationTick().
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }

    // Această metodă returnează valoarea variabilei membru tileY, care reprezintă valoarea y împărțită la Game.TILES_SIZE.
    public int getTileY(){
        return tileY;
    }
}
