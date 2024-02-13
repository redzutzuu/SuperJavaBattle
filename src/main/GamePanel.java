package main;

import java.awt.Graphics;
import java.awt.*;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel{

	private MouseInputs mouseInputs;
	private Game game;

	// Constructorul clasei GamePanel care primește obiectul Game și inițializează obiectul MouseInputs. Setează dimensiunea panoului
	// la dimensiunea jocului și adaugă ascultători pentru tastatură și mouse.
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	// Metodă privată care setează dimensiunea panoului la dimensiunea jocului specificată în obiectul Game.
	private void setPanelSize() {
		Dimension size = new Dimension(game.GAME_WIDTH, game.GAME_HEIGHT);
		setPreferredSize(size);
	}	

	// Suprascrierea metodei paintComponent pentru a desena componentele grafice pe panou. Apelul super.paintComponent(g) asigură
	// funcționalitatea de desenare a clasei de bază. Apoi se apelează metoda render a obiectului Game pentru a desena jocul pe panou.
	public void paintComponent(Graphics g) {
		super.paintComponent(g);		
		game.render(g);
	}

	// Metodă care returnează obiectul Game asociat panoului.
	public Game getGame() {
		return game;
	}
	
}
