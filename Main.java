
// Pislari Vadim 323CB TEMA 3
// 14:25 inceputul 17.12.2018

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

	public static void main(String[] args) throws IOException {
		// deschiderea fisierelor
		File inputfile = new File(args[0]);
		File outputfile = new File(args[1]);
		File errorfile = new File(args[2]);

		// crearea output fisierilor
		outputfile.createNewFile();
		errorfile.createNewFile();

		PrintWriter out = new PrintWriter(outputfile);
		PrintWriter err = new PrintWriter(errorfile);
		Scanner in = new Scanner(inputfile);

		Nod rout = new Folder("/", null, out);
		Instance instance = new Instance(out, err);
		// pozitia "user - ului"
		Position position = Position.getInstance(rout, null, rout);
		int i = 1, succes;
		while (in.hasNext()) {
			out.println(i);
			err.println(i++);
			String line = in.nextLine();
			String[] parameters = line.split(" ");
			if (parameters[0].equals("mkdir")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1]);
				succes = command.execute();
			} else if (parameters[0].equals("touch")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1]);
				succes = command.execute();
			} else if (parameters[0].equals("ls")) {
				Command command = instance.CreateCommand(parameters, position);
				command.execute();
			} else if (parameters[0].equals("cd")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1]);
				succes = command.execute();
				if (succes == 1)
					err.println("cd: " + parameters[1] + ": No such directory");
			} else if (parameters[0].equals("pwd")) {
				Command command = instance.CreateCommand(parameters[0], position);
				command.execute();
			} else if (parameters[0].equals("cp")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1], parameters[2], 1);
				succes = command.execute();
				if (succes == 1)
					err.println("cp: cannot copy " + parameters[1] + ": No such file or directory");
				if (succes == 2)
					err.println("cp: cannot copy into " + parameters[2] + ": No such directory");
			} else if (parameters[0].equals("rm")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1], 1);
				succes = command.execute();
				if (succes == 1)
					err.println("rm: cannot remove " + parameters[1] + ": No such file or directory");
			} else if (parameters[0].equals("mv")) {
				Command command = instance.CreateCommand(parameters[0], position, parameters[1], parameters[2], 0);
				succes = command.execute();
				if (succes == 1)
					err.println("mv: cannot move " + parameters[1] + ": No such file or directory");
				if (succes == 2)
					err.println("mv: cannot move into " + parameters[2] + ": No such directory");
				if (succes == 3)
					err.println("mv: cannot move " + parameters[1] + ": Node exists at destination");
			}
		}
		out.close();
		err.close();
		in.close();
	}
}