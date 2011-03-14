package hooverville.commands.util;

import hooverville.commands.plugins.Command;
import hooverville.server.User;

import java.io.PrintWriter;
import java.util.ArrayList;

public class CommandHandler {

	CommandController cc;
	PrintWriter out;

	public CommandHandler(CommandController cc, PrintWriter out) {
		this.cc = cc;
		this.out = out;
	}

	public void handleCommand(String input, User user) {
		// take the first command and see if its in the hashmap
		//String[] commandArray = input.split("[\\s]+");		
		input = input.trim();
		String firstCommand = getCommandArrayList(input).get(0);
		Command command = null;
		if ((command = cc.getCommandMap().get(firstCommand)) != null) {
			// then call that given commands doAction
			//			System.out.println("Calling here");
			command.doAction(input, out, cc, user);
		} else {
			//print out some sort of message			
			//right now we will print out not a valid command along with the help command
			if ((command = cc.getCommandMap().get("help")) != null) {
				// then call the help commands doAction
				//command.doAction("COMMANDNOTFOUND", out, commands);
				command.doAction("help", out, cc, user);
			} else {
				//shut down the program or something because the help command isn't there and thats bad
				System.err.println("Error! The help command plugin is not loaded!");
				//try reloading the commands
				//cc.getCommandLoader().loadCommands();
			}
		}
	}

	public static ArrayList<String> getCommandArrayList(String a) {
		String[] tmp = a.split("[\\s]+");
		ArrayList<String> newList = new ArrayList<String>(tmp.length);
		for (String s : tmp) {
			newList.add(s);
		}
		return newList;
	}
}