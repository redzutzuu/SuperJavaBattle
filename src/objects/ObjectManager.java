package objects;

import dataBase.DB;
import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs, grassImgs;
    private BufferedImage[][] treeImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private Level currentLevel;
    private DB obj = new DB();
    public int score;

    // Constructorul clasei ObjectManager.
    // Primește un obiect de tip Playing și inițializează câmpurile clasei.
    // Se ocupă de încărcarea imaginilor și de crearea unei tabele de bază de date.
    // Inițializează scorul.
    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
        obj.createTable();
        score = obj.getScore();
    }

    // Verifică dacă jucătorul a atins vreun obiect de tip "spike" (acele).
    // Primește un obiect de tip Player și verifică coliziunea cu obiectele de tip "spike".
    // Dacă există coliziune, jucătorul este ucis (metoda kill() este apelată pentru jucător).
    public void checkSpikesTouched(Player p) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    // Verifică dacă un inamic a atins vreun obiect de tip "spike".
    // Primește un obiect de tip Enemy și verifică coliziunea cu obiectele de tip "spike".
    // Dacă există coliziune, inamicul este rănit (metoda hurt() este apelată pentru inamic).
    public void checkSpikesTouched(Enemy e) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(e.getHitbox()))
                e.hurt(200);
    }

    // Verifică dacă o anumită zonă de coliziune a fost atinsă de jucător.
    // Primește un obiect de tip Rectangle2D.Float care reprezintă zona de coliziune.
    // Verifică coliziunea cu obiectele de tip "potion".
    // Dacă există coliziune, efectul obiectului de tip "potion" este aplicat jucătorului, scorul este incrementat și scorul este actualizat în baza de date.
    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                    score++;
                    obj.setScore(score);
                    System.out.println("scor = " + score);
                }
            }
    }

    // Aplică efectul unui obiect de tip "potion" asupra jucătorului.
    // Primește un obiect de tip Potion și modifică starea jucătorului în funcție de tipul obiectului "potion" (schimbă viața sau puterea jucătorului).
    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        }
        else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    // Verifică dacă jucătorul a lovit un anumit obiect.
    // Primește un obiect de tip Rectangle2D.Float care reprezintă zona de atac a jucătorului.
    // Verifică coliziunea cu obiectele de tip "container".
    // Dacă există coliziune, containerul este lovit și un nou obiect de tip "potion" este adăugat în joc.
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type = 0;
                    if (gc.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                            (int) (gc.getHitbox().y - gc.getHitbox().height / 2),
                            type));
                    return;
                }
            }
    }

    // Această funcție primește un obiect de tip Level și încarcă obiectele din acel nivel în managerul de obiecte.
    // Mai întâi, nivelul curent este setat ca nivelul nou primit ca argument.
    // Apoi, se creează o copie a listei de poțiuni și a listei de containere din noul nivel și se înlocuiesc listele existente cu aceste copii.
    //Lista de proiectile este curățată (golită) pentru a se pregăti pentru încărcarea proiectilelor nivelului nou.
    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        projectiles.clear();
    }

    // Această funcție este responsabilă de încărcarea imaginilor necesare în joc.
    // Se încarcă imaginile pentru poțiuni, containere, capcane, tunuri, mingi de tun, arbori și iarbă.
    // Imaginile sunt împărțite în sprite-uri și stocate în tablouri pentru utilizarea ulterioară.
    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
        treeImgs = new BufferedImage[2][4];
        BufferedImage treeOneImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_ONE_ATLAS);
        for (int i = 0; i < 4; i++)
            treeImgs[0][i] = treeOneImg.getSubimage(i * 39, 0, 39, 92);

        BufferedImage treeTwoImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_TWO_ATLAS);
        for (int i = 0; i < 4; i++)
            treeImgs[1][i] = treeTwoImg.getSubimage(i * 62, 0, 62, 54);

        BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
        grassImgs = new BufferedImage[2];
        for (int i = 0; i < grassImgs.length; i++)
            grassImgs[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
    }

    // Această funcție actualizează starea obiectelor din joc în funcție de nivelul de date și jucătorul curent.
    // Mai întâi, se actualizează arborii din fundal.
    // Apoi, se actualizează poțiunile active și containerele active.
    // Se actualizează tunurile și proiectilele.
    public void update(int[][] lvlData, Player player) {
        updateBackgroundTrees();
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    // Această funcție actualizează starea arborilor din fundal ai nivelului curent.
    private void updateBackgroundTrees() {
        for (BackgroundTree bt : currentLevel.getTrees())
            bt.update();
    }

    // Această funcție actualizează starea proiectilelor în joc.
    // Pentru fiecare proiectil activ:
    // Se actualizează poziția proiectilului.
    // Dacă proiectilul se intersectează cu hitbox-ul jucătorului, jucătorul își schimbă viața și proiectilul este dezactivat.
    // În caz contrar, dacă proiectilul lovește nivelul, proiectilul este dezactivat.
    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    // Această funcție verifică dacă jucătorul se află în raza de acțiune a unui tun dat.
    // Calculează distanța absolută dintre poziția hitbox-ului jucătorului și poziția hitbox-ului tunului și o compară cu o valoare fixă.
    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    // Această funcție verifică dacă jucătorul se află în fața unui tun dat.
    // Verifică poziția tunului și poziția jucătorului pentru a determina dacă jucătorul este în fața tunului, în funcție de tipul tunului.
    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;

        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    // Această funcție actualizează starea tunurilor din nivelul curent în funcție de jucător și datele nivelului.
    // Pentru fiecare tun:
    // Dacă tunul nu are animație și are aceeași poziție verticală ca și jucătorul și jucătorul se află în raza de acțiune și în fața tunului
    // și tunul poate vedea jucătorul pe baza datelor nivelului, atunci se activează animația tunului.
    // Se actualizează starea tunului.
    // Dacă tunul se află la indexul de animație 4 și tick-ul de animație este 0, se trage cu tunul.
    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : currentLevel.getCannons()) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }

    // Această funcție adaugă un nou proiectil în joc la poziția tunului dat.
    // Direcția proiectilului este determinată în funcție de tipul tunului.
    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT)
            dir = -1;

        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    // Această funcție desenează obiectele jocului pe ecran.
    // Desenează poțiunile, containerele, capcanele, tunurile, proiectilele și iarba.
    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawGrass(g, xLvlOffset);
    }

    // Această funcție desenează iarbă pe ecran.
    // Desenează iarbă pentru fiecare obiect de tip "Grass" din nivelul curent.
    private void drawGrass(Graphics g, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass())
            g.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY(), (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
    }

    // Această funcție desenează arborii din fundal pe ecran.
    // Desenează arborii de fundal pentru fiecare obiect de tip "BackgroundTree" din nivelul curent.
    public void drawBackgroundTrees(Graphics g, int xLvlOffset) {
        for (BackgroundTree bt : currentLevel.getTrees()) {

            int type = bt.getType();
            if (type == 9)
                type = 8;
            g.drawImage(treeImgs[type - 7][bt.getAniIndex()], bt.getX() - xLvlOffset + GetTreeOffsetX(bt.getType()), (int) (bt.getY() + GetTreeOffsetY(bt.getType())), GetTreeWidth(bt.getType()),
                    GetTreeHeight(bt.getType()), null);
        }
    }

    // Această funcție desenează proiectilele pe ecran.
    // Desenează proiectilele active pe baza imaginii proiectilului și poziției acestora.
    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
    }

    // Această funcție desenează tunurile pe ecran.
    // Desenează tunurile din nivelul curent pe baza imaginii tunului și poziției acestora.
    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : currentLevel.getCannons()) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    // Această funcție desenează capcanele pe ecran.
    // Desenează capcanele din nivelul curent pe baza imaginii capcanei și poziției acestora.
    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes())
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

    }

    // Această funcție desenează containerele pe ecran.
    // Desenează containerele active din nivelul curent pe baza imaginii containerului și poziției acestora.
    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
            }
    }

    // Această funcție desenează poțiunile pe ecran.
    // Desenează poțiunile active din nivelul curent pe baza imaginii poțiunii și poziției acestora.
    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
                        null);
            }
    }

    // Această funcție resetează toate obiectele jocului.
    // Se încarcă obiectele din nivelul curent.
    // Se resetează starea poțiunilor, containerelor și tunurilor din nivelul curent.
    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Cannon c : currentLevel.getCannons())
            c.reset();
    }

}
