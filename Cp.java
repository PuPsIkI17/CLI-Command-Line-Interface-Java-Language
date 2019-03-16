
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;
import java.util.Iterator;

// cp command
public class Cp implements Command {
	private Position position;
	private String source, destination;
	private int cp_or_mv = 0;
	private Nod dest_mov = null;
	private PrintWriter out, err;

	public Cp(Position position, String source, String destination, int cp, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.source = source;
		this.destination = destination;
		this.cp_or_mv = cp;
		this.out = out;
		this.err = err;
	}

	@Override
	public int execute() {
		Instance instance = new Instance(out, err);

		// memorizarea pozitiei actuale
		Nod pos_nod_aux = position.get_nod();
		String aux_source = source;

		// aflarea caii si denumirii fisierului care trebuie copiat
		int strend = source.lastIndexOf("/");
		String child_name = source;

		// daca path - ul contine . .. sau /
		if (strend == -1) {
			source = ".";
		} else if (strend == 0) {
			child_name = source.substring(1);
			source = "/";
		} else {
			child_name = source.substring(strend + 1);
			source = source.substring(0, strend);
		}

		// se apeleaza cd
		Command command = instance.CreateCommand("cd", position, source);
		int succes = command.execute();
		if (succes == 1) {
			position.set_nod(pos_nod_aux);
			return 1;
		}

		// verificarea daca exista source path
		Nod source_nod = null;
		if (child_name.equals("."))
			source_nod = position.get_nod();
		else if (child_name.equals(".."))
			source_nod = position.get_nod().get_parent();
		else
			source_nod = position.get_nod().find_all(child_name);
		if (source_nod == null) {
			position.set_nod(pos_nod_aux);
			return 1;
		}
		child_name = source_nod.getName();

		position.set_nod(pos_nod_aux);

		// se muta in destinatie
		command = instance.CreateCommand("cd", position, destination);
		succes = command.execute();
		if (succes == 1) {
			position.set_nod(pos_nod_aux);
			return 2;
		}

		// verificare daca nu se sterge parent path
		Nod verif = position.get_nod().find_all(child_name);
		if (verif != null && cp_or_mv == 1) {
			position.set_nod(pos_nod_aux);
			err.println("cp: cannot copy " + aux_source + ": Node exists at destination");
			return 3;
		} else if ((verif != null && cp_or_mv == 0)) {
			position.set_nod(pos_nod_aux);
			return 3;
		}
		copy(source_nod, position.get_nod(), position.get_nod());

		position.set_nod(pos_nod_aux);
		return 0;
	}

	// copierea unui subarbore recursiv
	public void copy(Nod src_nod, Nod des, Nod initial_nod) {
		if (src_nod == null || src_nod == initial_nod)
			return;
		Nod new_nod = null;
		if (src_nod instanceof File) {
			new_nod = new File(src_nod.getName(), des, out);
		} else
			new_nod = new Folder(src_nod.getName(), des, out);
		des.add(new_nod);
		if (dest_mov == null)
			dest_mov = new_nod;

		if (src_nod.get_list() == null)
			return;
		// copierea si continutului nodului
		Iterator<Nod> iter = src_nod.get_list().iterator();
		while (iter.hasNext()) {
			Nod nod = iter.next();
			copy(nod, new_nod, initial_nod);
		}
	}

	// returneaza dest_mov
	public Nod get_dest_mov() {
		return dest_mov;
	}
}
