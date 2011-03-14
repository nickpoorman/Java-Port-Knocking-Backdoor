package hooverville.server;

import hooverville.characters.HoovervilleCharacter;
import hooverville.commands.util.CommandController;
import hooverville.worlds.regions.Region;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.List;

public class Server {

	public static void main(String[] args) throws IOException {
		//load up the command api
		CommandController cc = new CommandController();
		//ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<String, Command>();
		//cc.setCommandMap(commands);
		//CommandLoader commandLoader = new CommandLoader(commands);
		//cc.setCommandLoader(commandLoader);
		cc.getCommandLoader().loadAllCommandsInDir(new File("commands\\"), null);
//		PrintWriter out = new PrintWriter(System.out);
//		CommandHandler commandHandler = new CommandHandler(commands, out);
		
		//load up the serve
		ServerSocket serverSocket = null;
		boolean listening = true;
		//ClientManager clientManager = new ClientManager();

		try {
			serverSocket = new ServerSocket(31337);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(-1);
		}

		System.out.println("Waiting for connections.");
		while (listening) {
			new ClientConnection(serverSocket.accept(), cc.getClientManager(), cc).start();
		}

		serverSocket.close();
	}
	
	public static void sendMessageToEveryoneInRegion(CommandController cc, String message, Region r){
		//Set<String> usersLoggedIn = cc.getClientManager().getClients().keySet();		
		//get all the characters in a given region
		//then from the characters in that region get their output streams from the clientsmap
		//then send the message to each person from the intersection of the two lists
		List<HoovervilleCharacter> characters = r.getCharactersForRegion();
		for(HoovervilleCharacter c : characters){
			String username = c.getUserName();
			//if the user is logged in thats in the region
			if(cc.getClientManager().getClients().containsKey(username)){
				//get that users output stream
				User user = cc.getClientManager().getUser(username);
				PrintWriter out = user.getClientConnection().getOut();
				out.println(message);
			}
		}
		
	}
	
	

}
