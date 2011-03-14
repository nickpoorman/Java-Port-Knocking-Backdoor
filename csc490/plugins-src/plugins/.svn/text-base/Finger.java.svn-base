package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Finger implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.length() > 6) input = input.substring(7,input.length());
	}

	@Override
	public String getCommand() {
		return "finger";
	}

	@Override
	public String getHelp() {
		String help = "";
		return help;
	}

}
