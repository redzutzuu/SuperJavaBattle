package objects;

public class Grass {

	private int x, y, type;

	// Acesta este constructorul clasei Grass și primește trei parametri: x, y și type.
	// Initializează variabilele membru x, y și type cu valorile primite ca parametri.
	public Grass(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	// Această metodă furnizează valoarea variabilei membru x.
	// Returnează valoarea variabilei x.
	public int getX() {
		return x;
	}

	// Această metodă furnizează valoarea variabilei membru y.
	// Returnează valoarea variabilei y.
	public int getY() {
		return y;
	}

	// Această metodă furnizează valoarea variabilei membru type.
	// Returnează valoarea variabilei type.
	public int getType() {
		return type;
	}
}
