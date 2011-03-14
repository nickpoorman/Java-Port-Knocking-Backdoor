package plugins;

import java.io.PrintWriter;

import hooverville.worlds.regions.Region;
import hooverville.characters.HoovervilleCharacter;
import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Look implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {		
		if (input.length() > 4) input = input.substring(5,input.length());
		if (input.equalsIgnoreCase("Help")) {
			out.println(getHelp());
			return;
		}
		HoovervilleCharacter current = user.getHoovervilleCharacter();
		Region r = current.getCurrentRegion();
		out.println("Region Description:\n");
		out.println("Visible Players: (" + r.getTotalCharacters() + ")");
		for (HoovervilleCharacter hc : r.getCharactersForRegion()) {
			out.println(hc.getUserName() + " - " + hc.getType());
		}
	}

	@Override
	public String getCommand() {
		return "look";
	}

	@Override
	public String getHelp() {
		String help = "";
		help += "Usage of the look Command\n";
		help += "There are no available inputs for this command. All non-help inputs will be ignored.\n";
		help += "Sample Usage: look\n";
		help += "Sample Usage: look Help\n";
		return help;
	}

}
