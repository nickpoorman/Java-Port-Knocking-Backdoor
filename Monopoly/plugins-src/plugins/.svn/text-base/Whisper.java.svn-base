package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;

public class Whisper implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		String target = "";
		String message = "";
		//sends a message to a given person
		//check to see if that person is in the room
		//to "kitty cat" any more hairballs lately?
		//first strip out the input
		input = input.substring(getCommand().length() + 1, input.length());
		//check to see if the name starts with quotes
		if (input.startsWith("\"")) {
			int nextQuoteIndex = -1;
			//contianed quotes
			//look for the next quote
			for (int i = 1; i < input.length(); i++) {
				char tmp = input.charAt(i);
				if (tmp == '\"') {
					nextQuoteIndex = i;
				}
			}
			if (nextQuoteIndex == -1) {
				//didnt find it
				out.println("Command incorrectly formatted. \n" + getHelp());
				return;
			} else {
				//found the next quote
				//strip out the name
				target = input.substring(1, nextQuoteIndex);
				//reset the input with name stripped out
				if (input.length() > (target.length() + 2)) {
					message = input.substring(nextQuoteIndex + 2, input.length());
				} else {
					out.println("You didn't enter a message.");
				}

			}
		} else {
			//didn't contain quotes
			//get the targets name
			String[] nameTmp = input.split(" ");
			target = nameTmp[0].trim();
			if (input.length() > target.length()) {
				message = input.substring(target.length() + 1, input.length());
			} else {
				out.println("You didn't enter a message.");
			}

		}
		if (!target.equals("")) {
			if (!message.equals("")) {
				message = user.getLogin() + ": \"" + message + "\".";
				if (cc.getClientManager().contains(target)) {
					//get that targets socket
					//User u = cc.getClientManager().getUser(target);
					cc.getClientManager().getUser(target).getClientConnection().getOut().println(message);
				}
			}

		}

	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return "whisper";
	}

	@Override
	public String getHelp() {
		return "Type 'whisper' <person> <message> \n"
				+ "Same as the 'to' command except the conversation will be private.\n"
				+ "Will say something to a given person.\n" 
				+ " If a person has more then one word in their name\n"
				+ " you must sorround their name with double quotes.\n"
				+ "ex: 'to' \"Kitty Cat\" any more hairballs lately?";
	}

}
