
// Pislari Vadim 323CB TEMA 3

// cd command
public class Cd implements Command {
	private Position position;
	String parameter;

	public Cd(Position position, String parameter) {
		this.position = position;
		this.parameter = parameter;
	}

	@Override
	public int execute() {
		// calea relativa
		Nod aux_nod = position.get_nod();
		// verificarea daca e calea absoluta
		if (parameter.equals("/")) {
			aux_nod = position.get_rout();
		} else if (parameter.charAt(0) == '/') {
			aux_nod = position.get_rout();
			parameter = parameter.substring(1);
		}

		String[] parts = parameter.split("/");
		int length = parts.length;

		// daca exista .. sau .
		for (int i = 0; i != length; i++) {
			if (parts[i].equals("..")) {
				aux_nod = aux_nod.get_parent();
			} else if (parts[i].equals("."))
				continue;
			else {
				aux_nod = aux_nod.find(parts[i]);
			}
			if (aux_nod == null)
				break;
		}

		if (aux_nod != null) {
			position.set_nod(aux_nod);
			return 0;
		} else {
			return 1;
		}
	}
}
