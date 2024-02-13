package objects;

import java.util.Random;

public class BackgroundTree {

	private int x, y, type, aniIndex, aniTick;

	// Acesta este constructorul clasei BackgroundTree și primește trei parametri: x, y și type.
	// Inițializează variabilele membru x, y și type cu valorile primite ca argumente.
	// Generează un număr aleatoriu între 0 și 3 folosind clasa Random pentru a seta variabila membru aniIndex.
	public BackgroundTree(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		Random r = new Random();
		aniIndex = r.nextInt(4);

	}

	// Această metodă este responsabilă de actualizarea stării obiectului BackgroundTree.
	// Incrementăm aniTick cu 1.
	// Verificăm dacă aniTick a depășit sau este egal cu 35. În acest caz, resetăm aniTick la 0 și incrementăm aniIndex cu 1.
	// Verificăm dacă aniIndex a depășit sau este egal cu 4. În acest caz, resetăm aniIndex la 0.
	public void update() {
		aniTick++;
		if (aniTick >= 35) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	// Această metodă returnează valoarea variabilei membru aniIndex, care reprezintă indexul animației.
	public int getAniIndex() {
		return aniIndex;
	}

	// Această metodă setează valoarea variabilei membru aniIndex cu valoarea primită ca argument.
	public void setAniIndex(int aniIndex) {
		this.aniIndex = aniIndex;
	}

	// Această metodă returnează valoarea variabilei membru x, care reprezintă coordonata x a obiectului BackgroundTree.
	public int getX() {
		return x;
	}

	// Această metodă setează valoarea variabilei membru x cu valoarea primită ca argument.
	public void setX(int x) {
		this.x = x;
	}

	// Această metodă returnează valoarea variabilei membru y, care reprezintă coordonata y a obiectului BackgroundTree.
	public int getY() {
		return y;
	}

	// Această metodă setează valoarea variabilei membru y cu valoarea primită ca argument.
	public void setY(int y) {
		this.y = y;
	}

	// Această metodă returnează valoarea variabilei membru type, care reprezintă tipul obiectului BackgroundTree.
	public int getType() {
		return type;
	}

	// Această metodă setează valoarea variabilei membru type cu valoarea primită ca argument.
	public void setType(int type) {
		this.type = type;
	}
}
