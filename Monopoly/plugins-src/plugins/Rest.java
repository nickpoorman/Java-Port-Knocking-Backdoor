package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;
import hooverville.characters.HoovervilleCharacter;

public class Rest implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.equalsIgnoreCase("Rest")) {
			out.println(getHelp());
			return;
		}
		HoovervilleCharacter rester = user.getHoovervilleCharacter();
		out.println("You lay down to rest... \n");
		new hooverville.actions.solo.Rest(rester).doAction();
		out.println("You wake up and feel rested") ;
	}

	@Override
	public String getCommand() {
		return "rest";
	}

	@Override
	public String getHelp() {
		String help = "";
		help += "Usage of the Rest Command\n";
		help += "There are no available inputs for this command. All non-help inputs will be ignored.\n";
		help += "Sample Usage: Rest\n";
		help += "Sample Usage: Rest Help\n";
		return help;
	}

}
