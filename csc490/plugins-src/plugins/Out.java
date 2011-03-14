package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Out implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		input = input.substring(5,input.length());
		if (input.equalsIgnoreCase("Help")) {
			out.println(getHelp());
			return;
		}
	}

	@Override
	public String getCommand() {
		return "out";
	}

	@Override
	public String getHelp() {
		String help = "";
		return help;
	}

}
