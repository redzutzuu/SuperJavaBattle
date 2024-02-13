package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class GameCompletedOverlay {
	private Playing playing;
	private BufferedImage img;
	private MenuButton quit, credit;
	private int imgX, imgY, imgW, imgH;

	// Este constructorul clasei GameCompletedOverlay. Primește un obiect de tip Playing ca parametru și îl utilizează pentru a inițializa
	// variabila playing din clasă. De asemenea, apelează metodele createImg() și createButtons() pentru a inițializa imaginile și butoanele asociate.
	public GameCompletedOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	// Această metodă creează butoanele necesare. În acest caz, este creat un obiect de tip MenuButton numit quit, cu coordonate și parametri
	// specificați, care pare să fie utilizat pentru a reveni la meniul principal al jocului.
	private void createButtons() {
		quit = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 2, Gamestate.MENU);
	}

	// Această metodă încarcă imaginea asociată ecranului de finalizare a jocului și setează coordonatele și dimensiunile imaginii în funcție
	// de aceasta. Imaginea este obținută prin apelul metodei GetSpriteAtlas() din clasa LoadSave și este stocată în variabila img de tip BufferedImage.
	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);

	}

	// Această metodă desenează imaginea de fundal și butoanele pe ecran. Începe prin desenarea unui dreptunghi opac pentru a acoperi întregul
	// ecran, utilizând o culoare specificată. Apoi, imaginea este desenată pe ecran folosind metoda drawImage(), iar butoanele sunt desenate
	// prin apelul metodei draw() a obiectului quit.
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(img, imgX, imgY, imgW, imgH, null);
		quit.draw(g);
	}

	// Această metodă actualizează starea butoanelor. În acest caz, apelează metoda update() a obiectului quit.
	public void update() {
		quit.update();
	}

	// Această metodă verifică dacă evenimentul de mouse e se află în interiorul dreptunghiului de coliziune al unui buton specificat b.
	// Returnează true dacă evenimentul de mouse este în interiorul butonului și false în caz contrar.
	private boolean isIn(MenuButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	// Această metodă este apelată atunci când cursorul mouse-ului este mutat pe ecran. Verifică dacă cursorul se află pe butonul quit și
	// setează starea mouseOver a acestuia în consecință.
	public void mouseMoved(MouseEvent e) {
		quit.setMouseOver(false);
		if (isIn(quit, e))
			quit.setMouseOver(true);
	}

	// Această metodă este apelată atunci când un buton al mouse-ului este eliberat. Verifică dacă evenimentul de mouse este în interiorul
	// butonului quit și, în caz afirmativ, verifică dacă butonul a fost apăsat. Dacă da, se resetează starea jocului și se revine la meniul principal.
	public void mouseReleased(MouseEvent e) {
		if (isIn(quit, e)) {
			if (quit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				playing.setGameState(Gamestate.MENU);
			}
			quit.resetBools();
		}
	}

	// Această metodă este apelată atunci când un buton al mouse-ului este apăsat. Verifică dacă evenimentul de mouse se află în interiorul butonului
	// quit și, în caz afirmativ, setează starea mousePressed a acestuia la true.
	public void mousePressed(MouseEvent e) {
		if (isIn(quit, e))
			quit.setMousePressed(true);
	}
}
