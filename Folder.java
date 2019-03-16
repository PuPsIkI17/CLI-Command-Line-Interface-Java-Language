
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;
import java.util.*;

//Director
public class Folder implements Nod {

	private String name;
	private Nod parent;
	private String absolute_path;
	private List<Nod> nods_arr = new ArrayList<Nod>();
	private PrintWriter out;

	public Folder(String name, Nod parent, PrintWriter out) {
		this.name = name;
		this.parent = parent;
		this.out = out;

		// calea absoluta
		if (parent == null)
			this.absolute_path = "/";
		else if (parent.get_absolute_path().equals("/"))
			this.absolute_path = "/" + name;
		else
			this.absolute_path = parent.get_absolute_path() + "/" + name;
	}

	@Override
	public int add(Nod nod) {
		Nod aux = this.find_all(nod.getName());
		if (aux == null) {
			nods_arr.add(nod);

			// sortarea nodurilor opii dintr-un folder
			Collections.sort(nods_arr, new Comparator<Nod>() {
				@Override
				public int compare(Nod n1, Nod n2) {
					return n1.getName().compareTo(n2.getName());
				}
			});
			return 0;
		} else
			return 1;
	}

	// sterge un copil a unui nod
	@Override
	public void remove(String str) {
		Nod nod = find_all(str);
		nods_arr.remove(nod);
	}

	// printarea caii absolute si a tuturor copiilor unui nod
	@Override
	public void print_absolute(String regex) {
		Iterator<Nod> iter = nods_arr.iterator();
		out.println(this.get_absolute_path() + ":");
		if (!iter.hasNext())
			out.println();
		int bonus_enter = 0, bonus_space = 0;

		// printarea tuturor nodurilor
		while (iter.hasNext()) {
			Nod nod = iter.next();
			if (regex == null) {
				if (!iter.hasNext()) {
					out.print(nod.get_absolute_path() + "\n");
				} else {
					out.print(nod.get_absolute_path() + " ");
				}
				// printare bonus 1
			} else if (nod.getName().matches(regex)) {
				if (!iter.hasNext()) {
					bonus_enter = 0;
					if (bonus_space == 1)
						out.print(" ");
					out.print(nod.get_absolute_path() + "\n");
				} else {
					bonus_enter = 1;
					if (bonus_space == 0)
						bonus_space = 1;
					else
						out.print(" ");
					out.print(nod.get_absolute_path());
				}
			} else
				bonus_enter = 1;
		}
		if (bonus_enter == 1)
			out.println();
		out.println();
	}

	// printarea recursiva a intregului subarbore
	@Override
	public void printR(String regex) {
		print_absolute(regex);
		Iterator<Nod> iter = nods_arr.iterator();
		while (iter.hasNext()) {
			Nod nod = iter.next();
			nod.printR(regex);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Nod get_parent() {
		return parent;
	}

	@Override
	public String get_absolute_path() {
		return absolute_path;
	}

	@Override
	public List<Nod> get_list() {
		return nods_arr;
	}

	// cauta folder
	@Override
	public Nod find(String name) {
		Iterator<Nod> iter = nods_arr.iterator();
		while (iter.hasNext()) {
			Nod nod = iter.next();
			if (nod.getName().equals(name) && nod instanceof Folder) {
				return nod;
			}
		}
		return null;
	}

	// cauta si folder si fisier
	@Override
	public Nod find_all(String name) {
		Iterator<Nod> iter = nods_arr.iterator();
		while (iter.hasNext()) {
			Nod nod = iter.next();
			if (nod.getName().equals(name)) {
				return nod;
			}
		}
		return null;
	}

	// pentru stergere
	@Override
	public Nod find_nod(Nod nod_search) {
		Iterator<Nod> iter = nods_arr.iterator();
		while (iter.hasNext()) {
			Nod nod = iter.next();
			if (nod.equals(nod_search)) {
				return nod;
			}
			Nod new_nod = nod.find_nod(nod_search);
			if (new_nod != null)
				return new_nod;
		}
		return null;
	}
}