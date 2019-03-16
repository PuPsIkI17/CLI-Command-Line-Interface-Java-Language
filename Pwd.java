
// Pislari Vadim 323CB TEMA 3

import java.io.PrintWriter;

// pwd command
public class Pwd implements Command {
	private String place;
	private PrintWriter out;

	public Pwd(String place, PrintWriter out) {
		this.place = place;
		this.out = out;
	}

	@Override
	public int execute() {
		out.println(place);
		return 0;
	}
}
