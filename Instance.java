
// Pislari Vadim

import java.io.PrintWriter;

//Factory pattern 
public class Instance {
	Command command = null;
	private PrintWriter out, err;

	public Instance(PrintWriter out, PrintWriter err) {
		this.out = out;
		this.err = err;
	}

	public Command CreateCommand(String type, Position position) {
		if (type.equals("pwd")) {
			command = new Pwd(position.get_nod().get_absolute_path(), out);
		}
		return command;
	}

	public Command CreateCommand(String type, Position position, String parameter) {
		if (type.equals("cd")) {
			command = new Cd(position, parameter);
		} else if (type.equals("mkdir")) {
			command = new Mkdir(position, parameter, out, err);
		} else if (type.equals("touch")) {
			command = new Touch(position, parameter, out, err);
		}
		return command;
	}

	public Command CreateCommand(String type, Position position, String parameter, int rm_or_mv) {
		if (type.equals("rm")) {
			command = new Rm(position, parameter, rm_or_mv, out, err);
		}
		return command;
	}

	public Command CreateCommand(String[] parameters, Position position) {
		if (parameters[0].equals("ls")) {
			if (parameters.length == 6) {
				// bonus 1
				command = new Ls(position, parameters[2], parameters[5], 1, out, err);
			} else if (parameters.length == 5) {
				command = new Ls(position, parameters[1], parameters[4], 0, out, err);
			} else if (parameters.length == 3) {
				if (parameters[1].equals("-R")) {
					command = new Ls(position, parameters[2], 1, out, err);
				} else {
					command = new Ls(position, parameters[1], 1, out, err);
				}
			} else if (parameters.length == 2) {
				if (parameters[1].equals("-R")) {
					command = new Ls(position, 1, out, err);
				} else {
					command = new Ls(position, parameters[1], out, err);
				}
			} else
				command = new Ls(position, out, err);
		}
		return command;
	}

	public Command CreateCommand(String type, Position position, String source, String destination, int cp_or_mv) {
		if (type.equals("cp")) {
			command = new Cp(position, source, destination, cp_or_mv, out, err);
		} else if (type.equals("mv")) {
			command = new Mv(position, source, destination, out, err);
		}
		return command;
	}

}
