package util;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import plugins.Command;

public class CommandController {

	private ConcurrentHashMap<String, Command> commandMap;
	private CommandHandler commandHandler;
	private CommandLoader commandLoader;

	public CommandController(PrintWriter out) {
		commandMap = new ConcurrentHashMap<String, Command>();
		commandHandler = new CommandHandler(this, out);
		commandLoader = new CommandLoader(this);
	}

	/**
	 * @param s
	 *            the String command to be referenced
	 * @param c
	 *            the Command object
	 * @return
	 *            the previous value associated with key, or null if there was no mapping for key 
	 */
	public Command putCommand(String s, Command c) {
		return commandMap.put(s, c);
	}

	/**
	 * @param s
	 *            the String command to be referenced
	 * @return
	 *            the value to which the specified key is mapped, or null if this map contains no mapping for the key 
	 */
	public Command getCommand(String command) {
		return commandMap.get(command);
	}

	/**
	 * @return the commandHandler
	 */
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	/**
	 * @param commandHandler
	 *            the commandHandler to set
	 */
	public void setCommandHandler(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	/**
	 * @return the commandLoader
	 */
	public CommandLoader getCommandLoader() {
		return commandLoader;
	}

	/**
	 * @param commandLoader
	 *            the commandLoader to set
	 */
	public void setCommandLoader(CommandLoader commandLoader) {
		this.commandLoader = commandLoader;
	}

	/**
	 * @return the commandMap
	 */
	public ConcurrentHashMap<String, Command> getCommandMap() {
		return commandMap;
	}

	/**
	 * @param commandMap
	 *            the commandMap to set
	 */
	public void setCommandMap(ConcurrentHashMap<String, Command> commandMap) {
		this.commandMap = commandMap;
	}	
}
