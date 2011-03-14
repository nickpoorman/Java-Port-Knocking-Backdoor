package plugins;

import java.io.PrintWriter;

import hooverville.characters.HoovervilleCharacter;
import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Inventory implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.equalsIgnoreCase("Help")) {
			out.println(getHelp());
			return;
		}
		HoovervilleCharacter c = user.getHoovervilleCharacter();
		String inventory = new hooverville.actions.solo.Inventory(c).getInventory();
		out.println(inventory);
	}

	@Override
	public String getCommand() {
		return "inventory";
	}

	@Override
	public String getHelp() {
		String help = "";
		help += "Usage of the inventory Command\n";
		help += "There are no available inputs for this command. All non-help inputs will be ignored.\n";
		help += "Sample Usage: inventory\n";
		help += "Sample Usage: inventory Help\n";
		return help;
	}

}
