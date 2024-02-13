package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	// Constructorul clasei MouseInputs primește un obiect GamePanel și îl atribuie variabilei gamePanel.
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	// Această metodă este apelată atunci când mouse-ul este tras. Comută între diferite stări ale jocului (PLAYING,
	// OPTIONS) și apoi apelează metoda mouseDragged(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
			case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
			case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
		}
	}

	// Această metodă este apelată atunci când mouse-ul este mișcat. Comută între diferite stări ale jocului (MENU, PLAYING,
	// OPTIONS) și apoi apelează metoda mouseMoved(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
			case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
		}
	}

	// Această metodă este apelată atunci când mouse-ul face clic. Comută la starea jocului PLAYING și apoi apelează metoda
	// mouseClicked(e) din starea curentă a jocului.
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
			case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(e);
		}
	}

	// Această metodă este apelată atunci când un buton al mouse-ului este apăsat. Comută între diferite stări ale jocului (MENU, PLAYING,
	// OPTIONS) și apoi apelează metoda mousePressed(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
			case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);
		}
	}

	// Această metodă este apelată atunci când un buton al mouse-ului este eliberat. Comută între diferite stări ale jocului (MENU, PLAYING,
	// OPTIONS) și apoi apelează metoda mouseReleased(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
			case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);
		}
	}

	// Această metodă este apelată atunci când cursorul mouse-ului intră într-o componentă. Nu se întâmplă nimic în această implementare.
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	// Această metodă este apelată atunci când cursorul mouse-ului părăsește o componentă. Nu se întâmplă nimic în această implementare.
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
