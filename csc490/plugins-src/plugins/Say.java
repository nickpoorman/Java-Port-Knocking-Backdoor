package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;
import hooverville.server.Server;;

public class Say implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		//get all the people in the current region and then send them all the message
		//strip away the 'say' command
		String messageToSend = input.substring(4, input.length());
		//add who it was from
		messageToSend = user.getLogin() + ": \"" + messageToSend + "\".";		
		Server.sendMessageToEveryoneInRegion(cc, messageToSend, user.getHoovervilleCharacter().getCurrentRegion());
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return "say";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Type 'say' <message> to send a message to everyone in your current region.";
	}

}
