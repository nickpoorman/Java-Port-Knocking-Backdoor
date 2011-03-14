package plugins;

import java.io.PrintWriter;

import hooverville.worlds.regions.RegionIndexOutOfBoundsException;
import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Go implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.length() > 2) input = input.substring(3,input.length());
		if (input.equalsIgnoreCase("North")) {
			try {
				user.getHoovervilleCharacter().goNorth();
			} 
			catch (RegionIndexOutOfBoundsException e) {
				out.println("No such region exists to the North. Perhaps try a different direction?");
			}
		}
		else if (input.equalsIgnoreCase("South")) {
			try {	
				user.getHoovervilleCharacter().goSouth();
			} 
			catch (RegionIndexOutOfBoundsException e) {
				out.println("No such region exists to the South. Perhaps try a different direction?");
			}
		}
		else if (input.equalsIgnoreCase("East")) {
			try {	
				user.getHoovervilleCharacter().goEast();
			} 
			catch (RegionIndexOutOfBoundsException e) {
				out.println("No such region exists to the East. Perhaps try a different direction?");
			}
		}
		else if (input.equalsIgnoreCase("West")) {
			try {	
				user.getHoovervilleCharacter().goWest();
			} 
			catch (RegionIndexOutOfBoundsException e) {
				out.println("No such region exists to the West. Perhaps try a different direction?");
			}
		}
		else if (input.equalsIgnoreCase("Help")) {
			out.println(getHelp());
		}
		else
			out.println("SJKHfiuwehhfc: -- Unknown Direction: " + input + "\n" + getHelp());
	}

	@Override
	public String getCommand() {
		return "go";
	}

	@Override
	public String getHelp() {
		String help = "";
		help += "Usage of the Go command: \n";
		help += "Possible inputs are North, South, East, West, or Help";
		help += "\nGo North Description - Moves the current character to the north one position.\n";
		help += "\tIf required, the player will automatically be moved to a bordering region";
		help += "\nGo South Description - Moves the current character to the south one position.\n";
		help += "\tIf required, the player will automatically be moved to a bordering region";
		help += "\nGo East Description - Moves the current character to the east one position.\n";
		help += "\tIf required, the player will automatically be moved to a bordering region";
		help += "\nGo West Description - Moves the current character to the west one position.\n";
		help += "\tIf required, the player will automatically be moved to a bordering region";
		help += "\n\nSample Usage: Go North";
		return help;
	}

}
