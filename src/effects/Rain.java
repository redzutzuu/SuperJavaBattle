package effects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import utilz.LoadSave;

public class Rain {

	private Point2D.Float[] drops;
	private Random rand;
	private float rainSpeed = 1f;
	private BufferedImage rainParticle;

	// Constructorul clasei inițializează variabilele și încarcă imaginea asociată particulelor de ploaie dintr-un fișier.
	// Se initializează obiectul Random pentru a genera pozițiile aleatoare ale picăturilor de ploaie și se creează un tablou de
	// obiecte Point2D.Float pentru a stoca pozițiile picăturilor de ploaie.
	public Rain() {
		rand = new Random();
		drops = new Point2D.Float[1000];
		rainParticle = LoadSave.GetSpriteAtlas(LoadSave.RAIN_PARTICLE);
		initDrops();
	}

	// Această metodă inițializează pozițiile inițiale ale picăturilor de ploaie. Se parcurge tabloul de picături și se
	// atribuie fiecărei picături o poziție aleatoare utilizând metoda getRndPos().
	private void initDrops() {
		for (int i = 0; i < drops.length; i++)
			drops[i] = getRndPos();
	}

	// Această metodă returnează o poziție aleatoare pentru o picătură de ploaie. Coordonata x este generată aleator între valori negative
	// și poziții suplimentare în afara ecranului, în timp ce coordonata y este generată aleator între 0 și înălțimea jocului.
	private Point2D.Float getRndPos() {
		return new Point2D.Float((int) getNewX(0), rand.nextInt(Game.GAME_HEIGHT));
	}

	// Această metodă actualizează pozițiile picăturilor de ploaie. Coordonata y a fiecărei picături este incrementată cu rainSpeed,
	// reprezentând viteza de deplasare a picăturii. Dacă o picătură ajunge în afara ecranului, aceasta este readusă la începutul ecranului
	// și se actualizează coordonata x utilizând metoda getNewX().
	public void update(int xLvlOffset) {
		for (Point2D.Float p : drops) {
			p.y += rainSpeed;
			if (p.y >= Game.GAME_HEIGHT) {
				p.y = -20;
				p.x = getNewX(xLvlOffset);
			}
		}
	}

	// Această metodă returnează o valoare aleatoare pentru coordonata x a unei picături de ploaie. Valoarea este generată aleator
	// între limitele ecranului de joc ajustate cu un decalaj dat de xLvlOffset.
	private float getNewX(int xLvlOffset) {
		float value = (-Game.GAME_WIDTH) + rand.nextInt((int) (Game.GAME_WIDTH * 3f)) + xLvlOffset;
		return value;
	}

	// Această metodă desenează picăturile de ploaie pe ecran. Se parcurge tabloul de picături și se desenează imaginea rainParticle
	// la coordonatele corespunzătoare, ajustate cu xLvlOffset.
	public void draw(Graphics g, int xLvlOffset) {
		for (Point2D.Float p : drops)
			g.drawImage(rainParticle, (int) p.getX() - xLvlOffset, (int) p.getY(), 3, 12, null);
	}

}
