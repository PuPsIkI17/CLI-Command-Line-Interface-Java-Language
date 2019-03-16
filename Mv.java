
// Pislari Vadim 323CB

import java.io.PrintWriter;

// mv command
public class Mv implements Command {
	private Position position;
	private String source, destination;
	private PrintWriter out, err;

	public Mv(Position position, String source, String destination, PrintWriter out, PrintWriter err) {
		this.position = position;
		this.source = source;
		this.destination = destination;
		this.out = out;
		this.err = err;
	}

	@Override
	public int execute() {
		Instance instance = new Instance(out, err);
		String aux_source = source;
		Cp cp = new Cp(position, source, destination, 0, out, err);
		int succes = cp.execute();
		if (succes != 0)
			return succes;
		Command command = instance.CreateCommand("rm", position, aux_source, 0);
		command.execute();

		// daca se muta subarbore
		if (position.get_nod() != position.get_rout() && position.get_rout().find_nod(position.get_nod()) == null) {
			position.set_nod(cp.get_dest_mov());
		}
		return 0;
	}
}
