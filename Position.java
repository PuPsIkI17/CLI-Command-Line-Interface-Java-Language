
// Pislari Vadim 323CB TEMA 3

// Singelton Pattern
// Pozitia actuala a "userului"
public class Position {
	private static Position Instance;
	private Nod nod_pos;
	private Nod rout;

	private Position(Nod nod_pos, Nod parent, Nod rout) {
		this.nod_pos = nod_pos;
		this.rout = rout;
	}

	public static Position getInstance(Nod nod, Nod parent, Nod rout) {
		if (Instance == null) {
			Instance = new Position(nod, parent, rout);
		}
		return Instance;
	}

	// returneaza nodul
	public Nod get_nod() {
		return nod_pos;
	}

	// seteaza nodul
	public void set_nod(Nod nod) {
		this.nod_pos = nod;
	}

	// returneaza calea catre rout
	public Nod get_rout() {
		return rout;
	}

}
