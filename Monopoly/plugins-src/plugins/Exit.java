package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;
import hooverville.characters.HoovervilleCharacter;

public class Exit implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		input = input.substring(5,input.length());
		HoovervilleCharacter c = user.getHoovervilleCharacter();

	}

	@Override
	public String getCommand() {
		return "exit";
	}

	@Override
	public String getHelp() {
		String help = "";
		return help;
	}

}
