
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;
import java.util.Iterator;

// ls command
public class Ls implements Command {
	private Position position;
	private int r = 0;
	private String path = null;
	private PrintWriter out, err;
	private String regex = null;

	// ls
	public Ls(Position position, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.out = out;
		this.err = err;
	}

	// ls -R
	public Ls(Position position, int r, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.r = r;
		this.out = out;
		this.err = err;
	}

	// ls path
	public Ls(Position position, String path, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.path = path;
		this.out = out;
		this.err = err;
	}

	// ls path -R sau ls -R path
	public Ls(Position position, String path, int r, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.r = r;
		this.path = path;
		this.out = out;
		this.err = err;
	}

	// ls path -R | regex
	public Ls(Position position, String path, String regex, int r, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.r = r;
		this.path = path;
		this.out = out;
		this.err = err;
		this.regex = regex.substring(1, regex.length() - 1);
	}

	@Override
	public int execute() {
		Instance instance = new Instance(out, err);

		// memorizarea pozitiei actuale
		Nod pos_nod_aux = position.get_nod();

		// bonus 2
		if (path != null && path.indexOf('*') >= 0) {
			String aux_path = path;

			// aflarea caii si denumirii fisierului care trebuie copiat
			int strend = aux_path.lastIndexOf("/");
			String child_name = path;
			if (strend == -1) {
				aux_path = ".";
			} else if (strend == 0) {
				child_name = aux_path.substring(1);
				aux_path = "/";
			} else {
				child_name = aux_path.substring(strend + 1);
				aux_path = aux_path.substring(0, strend);
			}

			// transformarea path-ului ca sa se poata face match
			child_name = child_name.substring(0, child_name.length() - 1) + ".*";
			Command command = instance.CreateCommand("cd", position, aux_path);
			int succes = command.execute();
			if (succes == 1) {
				err.println("ls: " + path + ": No such directory");
				position.set_nod(pos_nod_aux);
				return 0;
			}
			
			// ls pe fiecare folder din path
			Iterator<Nod> iter = position.get_nod().get_list().iterator();
			while (iter.hasNext()) {
				Nod nod = iter.next();
				if (nod instanceof Folder) {
					if (nod.getName().matches(child_name))
						nod.print_absolute(null);

				}
			}
			position.set_nod(pos_nod_aux);
			return 0;
		}

		// fara path
		if (path == null && r == 0) {
			position.get_nod().print_absolute(regex);
			return 0;
		}
		if (path == null && r == 1) {
			position.get_nod().printR(regex);
			return 0;
		}

		Command command = instance.CreateCommand("cd", position, path);

		int succes = command.execute();
		if (succes == 1) {
			err.println("ls: " + path + ": No such directory");
			position.set_nod(pos_nod_aux);
			return 0;
		}

		// fara -R
		if (r == 0)
			position.get_nod().print_absolute(regex);
		else
			position.get_nod().printR(regex);

		// restabilirea pozitiei initiale
		position.set_nod(pos_nod_aux);
		return 0;
	}
}
