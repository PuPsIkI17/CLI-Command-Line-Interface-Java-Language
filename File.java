
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;
import java.util.List;

// Fisier
public class File implements Nod {

	private String name;
	private Nod parent;
	private String absolute_path;
	private PrintWriter out;

	public File(String name, Nod parent, PrintWriter out) {
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
		// nu este necesar acestei clase
		return 1;
	}

	@Override
	public void remove(String str) {
		// nu este necesar acestei clase
	}

	@Override
	public void printR(String regex) {
		// nu este necesar acestei clase
	}

	@Override
	public void print_absolute(String regex) {
		out.println(this.get_absolute_path());
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
	public Nod find(String name) {
		return null;
	}

	@Override
	public List<Nod> get_list() {
		// nu are lista
		return null;
	}

	@Override
	public Nod find_all(String name) {
		// nu are copii
		return null;
	}

	@Override
	public Nod find_nod(Nod nod_search) {
		// nu are noduri
		return null;
	}

}