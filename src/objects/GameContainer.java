package objects;
import main.Game;

import static utilz.Constants.ObjectConstants.*;

public class GameContainer extends GameObject{

    // Acesta este constructorul clasei GameContainer și primește trei parametri: x, y și objType.
    // Apelând constructorul superclasei (GameObject) cu ajutorul super(x, y, objType), se inițializează variabilele membru x, y și objType.
    // Este apelată metoda createHitbox() pentru a crea hitbox-ul obiectului.
    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    // Această metodă privată este responsabilă de crearea hitbox-ului obiectului GameContainer în funcție de tipul acestuia (objType).
    // Verifică dacă objType este egal cu BOX. În caz afirmativ, inițializează hitbox-ul cu dimensiunile 25x18 și setează valorile xDrawOffset și yDrawOffset pentru ajustarea desenului obiectului.
    // În caz contrar, inițializează hitbox-ul cu dimensiunile 23x25 și setează valorile xDrawOffset și yDrawOffset în consecință.
    // Se ajustează poziția hitbox-ului prin adăugarea offset-urilor xDrawOffset și yDrawOffset la coordonatele hitbox-ului.
    private void createHitbox() {
        if(objType == BOX){
            initHitbox(25, 18);
            xDrawOffset = (int)(7 * Game.SCALE);
            yDrawOffset = (int)(12 * Game.SCALE);
        }else{
            initHitbox(23, 25);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(5 * Game.SCALE);
        }
        hitbox.y = hitbox.y + yDrawOffset + (int)(Game.SCALE * 2);
        hitbox.x = hitbox.x + xDrawOffset / 2;
    }

    // Această metodă este responsabilă de actualizarea stării obiectului GameContainer.
    // Verifică dacă doAnimation este setat pe true. În caz afirmativ, se apelează metoda updateAnimationTick().
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
}
