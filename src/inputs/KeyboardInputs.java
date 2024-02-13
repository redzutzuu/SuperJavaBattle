package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.Game;
import main.GamePanel;
import static utilz.Constants.Directions.*;


public class KeyboardInputs implements KeyListener{
	
	private GamePanel gamePanel;

	// Constructorul clasei KeyboardInputs care primește un obiect GamePanel și îl atribuie variabilei gamePanel.
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	// Această metodă este apelată atunci când un caracter este introdus prin tastatură. Nu se întâmplă nimic în această implementare.
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// Această metodă este apelată atunci când o tastă este apăsată. Se comută între diferite stări ale jocului (MENU, PLAYING,
	// OPTIONS) și apoi se apelează metoda keyPressed(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
			case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
			case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);
		}
	}

	// Această metodă este apelată atunci când o tastă este eliberată. Se comută între diferite stări ale jocului (MENU, PLAYING)
	// și apoi se apelează metoda keyReleased(e) corespunzătoare din starea curentă a jocului.
	@Override
	public void keyReleased(KeyEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
			case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
		}
	}
}
