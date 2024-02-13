package main;

import java.awt.Graphics;

import UI.AudioOptions;
import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import utilz.LoadSave;

public class Game implements Runnable {

	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private AudioOptions audioOptions;
	private GameOptions gameOptions;
	private AudioPlayer audioPlayer;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.75f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	private final boolean SHOW_FPS_UPS = true;

	// Inițializează clasele și obiectele necesare pentru joc.
	// Creează și afișează fereastra jocului.
	// Pornește bucla principală a jocului.
	public Game() {
		initClasses();
		gamePanel = new GamePanel(this);
		new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		startGameLoop();
	}

	// Inițializează instanțele claselor necesare pentru joc (AudioOptions, AudioPlayer, Menu, Playing, GameOptions).
	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
	}

	// Creează și pornește un fir de execuție pentru bucla principală a jocului.
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	// Actualizează starea jocului în funcție de valoarea variabilei Gamestate.state.
	// Apelează metodele update() corespunzătoare pentru meniu, jocul principal sau opțiuni de joc.
	public void update() {
		switch (Gamestate.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				playing.update();
				break;
			case OPTIONS:
				gameOptions.update();
				break;
			case QUIT:
			default:
				System.exit(0);
				break;

		}
	}

	// Desenează elementele jocului în funcție de valoarea variabilei Gamestate.state.
	// Apelează metodele draw(Graphics g) corespunzătoare pentru meniu, jocul principal sau opțiuni de joc.
	public void render(Graphics g) {
		switch (Gamestate.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				playing.draw(g);
				break;
			case OPTIONS:
				gameOptions.draw(g);
				break;
			default:
				break;
		}
	}

	// Execută bucla principală a jocului.
	// Calculează timpul necesar pentru fiecare cadru și pentru fiecare actualizare.
	// Actualizează și randează jocul la intervale regulate de timp.
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {

				update();
				updates++;
				deltaU--;

			}

			if (deltaF >= 1) {

				gamePanel.repaint();
				frames++;
				deltaF--;

			}

			if (SHOW_FPS_UPS)
				if (System.currentTimeMillis() - lastCheck >= 1000) {

					lastCheck = System.currentTimeMillis();
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					frames = 0;
					updates = 0;

				}

		}
	}

	// Este apelată atunci când fereastra jocului își pierde focusul.
	// Resetează direcțiile de deplasare ale jucătorului în cazul în care starea jocului este "PLAYING".
	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	// Returnează instanța clasei Menu.
	public Menu getMenu() {
		return menu;
	}

	// Returnează instanța clasei Playing.
	public Playing getPlaying() {
		return playing;
	}

	// Returnează instanța clasei GameOptions.
	public GameOptions getGameOptions(){
		return gameOptions;
	}

	// Returnează instanța clasei AudioOptions.
	public AudioOptions getAudioOptions(){
		return audioOptions;
	}

	// Returnează instanța clasei AudioPlayer.
	public AudioPlayer getAudioPlayer(){
		return audioPlayer;
	}
}