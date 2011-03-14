package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Home implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		input = input.substring(5,input.length());
		out.println("Echo: " + input);
	}

	@Override
	public String getCommand() {
		return "home";
	}

	@Override
	public String getHelp() {
		String help = "";
		return help;
	}

}
