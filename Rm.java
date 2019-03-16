
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

// rm command
public class Rm implements Command {
	private Position position;
	private int rm_or_mv;
	private String parameter;
	private PrintWriter out, err;

	public Rm(Position position, String parameter, int rm_or_mv, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.parameter = parameter;
		this.rm_or_mv = rm_or_mv;
		this.out = out;
		this.err = err;
	}

	@Override
	public int execute() {
		Instance instance = new Instance(out, err);
		String aux_par = parameter;
		// memorizarea pozitiei actuale
		Nod pos_nod_aux = position.get_nod();

		if (parameter.length() > 1 && parameter.charAt(parameter.length() - 1) == '/') {
			parameter = parameter.substring(0, parameter.length() - 1);
		}

		// aflarea caii si denumirii noului fisier
		int strend = parameter.lastIndexOf("/");
		String child_name = parameter;
		if (strend == -1)
			parameter = ".";
		else {
			if (strend == 0) {
				child_name = parameter.substring(1);
				parameter = "/";
			}

			else {
				child_name = parameter.substring(strend + 1);
				parameter = parameter.substring(0, strend);
			}
		}

		// bonus 2
		if (aux_par != null && aux_par.indexOf('*') != -1) {
			// daca se duce in rout
			Nod nod;
			int rout_var = 0;
			if (parameter.equals("/"))
				rout_var = 1;
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

			// transformarea fisierului care trebuie sters
			// pentru a se putea face match
			for (int j = 0; j != child_name.length(); j++) {
				if (child_name.charAt(j) == '*') {
					child_name = child_name.substring(0, j) + '.' + child_name.substring(j);
					j++;
				}
			}

			// verificare daca se poate executa cel putin un rm pe una dint path-uri
			if (!parameter.equals("")) {
				Nod auxit = position.get_nod();
				Command command = instance.CreateCommand("cd", position, parameter);
				int succes = command.execute();
				position.set_nod(auxit);
				if (succes == 1 && parameter.indexOf("*") == -1)
					return 1;
			}
			String names[] = parameter.split("/");
			if (rout_var == 1)
				names[0] = "/";
			search(nod, names, child_name, 0, names.length, position);
			return 0;
		}

		// se apeleaza cd pentru a se muta in directorul necesar
		Command command = instance.CreateCommand("cd", position, parameter);
		command.execute();
		Nod nod_ver = null;
		if (child_name.equals("."))
			nod_ver = position.get_nod();
		else if (child_name.equals(".."))
			nod_ver = position.get_nod().get_parent();
		else
			nod_ver = position.get_nod().find_all(child_name);

		if (nod_ver == null) {
			position.set_nod(pos_nod_aux);
			return 1;
		}

		// daca se foloseste ptr rm
		if (rm_or_mv == 1) {
			Nod aux_nod = position.get_nod();
			position.set_nod(pos_nod_aux);
			command = instance.CreateCommand("cd", position, aux_par);
			int succes = command.execute();
			// daca nu se sterge parintele
			if (succes == 0 && position.get_nod() == pos_nod_aux || position.get_nod().find_nod(pos_nod_aux) != null) {
				position.set_nod(pos_nod_aux);
				return 0;
			}
			position.set_nod(aux_nod);
		}

		// daca se foloseste ptr mv
		else if (rm_or_mv == 0) {
			// daca se sterge path-ul care se termina cu . sau ..
			if (child_name.equals(".")) {
				child_name = position.get_nod().getName();
				position.set_nod(position.get_nod().get_parent());
			} else if (child_name.equals("..")) {
				child_name = position.get_nod().get_parent().getName();
				position.set_nod(position.get_nod().get_parent().get_parent());
			}
		}
		position.get_nod().remove(child_name);
		position.set_nod(pos_nod_aux);
		return 0;
	}

	// bonus 2
	public void search(Nod nod, String[] names, String child_name, int k, int lung, Position position) {
		// daca s-a ajuns la pathul unde se sterge
		if (k == lung) {
			Nod aux_nod = position.get_nod();
			position.set_nod(nod);
			Iterator<Nod> iter = nod.get_list().iterator();
			int new_str = 0;
			String[] str = new String[100];
			while (iter.hasNext()) {
				Nod aux = iter.next();
				if (aux.getName().matches(child_name)) {
					str[new_str++] = aux.getName();
				}
			}
			for (int i = 0; i != new_str; i++)
				nod.remove(str[i]);
			position.set_nod(aux_nod);
			return;
		}

		// daca se sterg toate directoarele rm /*
		if (names[k].equals("/") && child_name.equals(".*")) {
			List<Nod> nods_arr = nod.get_list();
			nods_arr.clear();
			if (position.get_nod().getName() != "/") {
				nods_arr.add(position.get_nod());
			}
			return;
		}

		// daca se sterge doar un director rm /a*
		else if (names[0].equals("/") || names[0].equals(".")) {
			search(nod, names, child_name, lung, lung, position);
			return;
		}

		// se ajunge pana la sectiunea care contine *
		int i = k;
		for (i = k; i != names.length; i++) {
			if (names[i].indexOf("*") >= 0)
				break;
			nod = nod.find(names[i]);
			k++;
			if (nod == null) {
				err.println("rm: " + parameter + ": No such directory");
				return;
			}
		}

		// se apeleaza recursiv pentru a se gasi si alte destinatii
		Iterator<Nod> iter = nod.get_list().iterator();
		while (iter.hasNext()) {
			Nod aux = iter.next();
			if (aux.getName().matches(names[i]) && aux instanceof Folder)
				search(aux, names, child_name, k + 1, lung, position);
		}
		return;
	}
}
