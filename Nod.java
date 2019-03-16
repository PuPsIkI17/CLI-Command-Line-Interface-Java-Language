
// Pislari Vadim

import java.util.List;

//Composite pattern
public interface Nod {
	// adaugarea unui fisier
	public int add(Nod nod);

	// stergerea fisierului
	public void remove(String str);

	// returneaza numele
	public String getName();

	// afisarea
	public void printR(String regex);

	// printeaza calea absoluta
	public void print_absolute(String regex);

	// returneaza parintele
	public Nod get_parent();

	// returneaza calea absoluta
	public String get_absolute_path();

	// intoarce lista cu copii
	public List<Nod> get_list();

	// cauta in fisier
	public Nod find(String name);

	// cauta un nod
	public Nod find_nod(Nod nod);

	// cauta si fisier si director
	public Nod find_all(String name);
}
