
// Pislari Vadim

import java.io.PrintWriter;
import java.util.Iterator;

// mkdir command
public class Mkdir implements Command {
	private Position position;
	private String parameter;
	private PrintWriter out, err;

	public Mkdir(Position position, String parameter, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.parameter = parameter;
		this.out = out;
		this.err = err;
	}

	@Override
	public int execute() {
		Instance instance = new Instance(out, err);

		// memorizarea pozitiei actuale
		Nod pos_nod_aux = position.get_nod();

		// aflarea caii si denumirii noului fisier
		int strend = parameter.lastIndexOf("/");
		String child_name = parameter;
		if (strend != -1) {
			if (strend == 0) {
				child_name = parameter.substring(1);
				parameter = "/";
			}

			else {
				child_name = parameter.substring(strend + 1);
				parameter = parameter.substring(0, strend);
			}

			// bonus 2
			if (parameter != null && parameter.indexOf('*') >= 0) {

				// daca se duce in rout
				Nod nod;
				if (parameter.charAt(0) == '/') {
					nod = position.get_rout();
					parameter = parameter.substring(1);
				} else
					nod = position.get_nod();

				// transformarea in parti pe care se poate face match
				for (int j = 0; j != parameter.length(); j++) {
					if (parameter.charAt(j) == '*') {
						parameter = parameter.substring(0, j) + '.' + parameter.substring(j);
						j++;
					}
				}

				String names[] = parameter.split("/");
				search(nod, names, child_name, 0, names.length, position);
				return 0;
			}

			// se apeleaza cd pentru a se muta in directorul necesar
			Command command = instance.CreateCommand("cd", position, parameter);
			int succes = command.execute();
			if (succes == 1) {
				err.println("mkdir: " + parameter + ": No such directory");
				position.set_nod(pos_nod_aux);
				return 1;
			}
		}
		Nod child = new Folder(child_name, position.get_nod(), out);

		int succes = position.get_nod().add(child);
		if (succes == 1) {
			Nod another_nod = position.get_nod().find_all(child_name);
			err.println("mkdir: cannot create directory " + another_nod.get_absolute_path() + ": Node exists");
			position.set_nod(pos_nod_aux);
			return 2;
		}

		position.set_nod(pos_nod_aux);
		return 0;
	}

	// bonus 2
	public void search(Nod nod, String[] names, String child_name, int k, int lung, Position position) {

		// daca s-a ajuns la pathul unde se sterge
		if (k == lung) {
			Nod aux_nod = position.get_nod();
			position.set_nod(nod);
			Instance instance = new Instance(out, err);
			Command command = instance.CreateCommand("mkdir", position, child_name);
			command.execute();
			position.set_nod(aux_nod);
			return;
		}

		// se ajunge pana la sectiunea care contine *
		int i = k;
		for (i = 0; i != names.length; i++) {
			if (names[i].indexOf("*") >= 0) {
				break;
			}
			nod = nod.find(names[i]);
			if (nod == null) {
				err.println("mkdir: " + parameter + ": No such directory");
				return;
			}
			k++;
		}

		Iterator<Nod> iter = nod.get_list().iterator();
		while (iter.hasNext()) {
			Nod aux = iter.next();
			if (aux.getName().matches(names[k]) && aux instanceof Folder) {
				search(aux, names, child_name, k + 1, lung, position);
			}
		}
	}
}
