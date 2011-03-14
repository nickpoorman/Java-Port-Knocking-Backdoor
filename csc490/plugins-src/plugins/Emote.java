package plugins;

import java.io.PrintWriter;

import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.Server;
import hooverville.server.User;
import hooverville.actions.solo.Dance;

public class Emote implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.length() > 5){
			input = input.substring(6,input.length());
			
			String message = "";
			message = user.getLogin();
			if(input.equals("dance")){
				message += " dances around like a little girl.";
			}else if(input.equals("run")){
				message += " runs like the wind.";
			}else if(input.equals("smile")){
				message += " cracks a big smile.";
			}else if(input.equals("fart")){
				message += " farts silently but deadly.";
			}else if(input.equals("spit")){
				message += " spits into the wind.";
			}else if(input.equals("shiver")){
				message += "'s teeth begin to clatter.";
			}else if(input.equals("angry")){
				message += " is very angry now!.";
			}else if(input.equals("whistle")){
				message += " begins to whistle mary had a little lamb!.";
			}else if(input.equals("sing")){
				message += " begins to sing \n"
					+ "\"Home is behind the world ahead\n"
					+ "And there are many paths to tread\n"
					+ "Through shadow to the edge of night\n"
					+ "Until the stars are all alight.\n"
					
					+ "Mist and shadow\n"
					+ "Cloud and shade\n"
					+ "All shall fade\n"
					+ "All shall fade\".";
			}
			Server.sendMessageToEveryoneInRegion(cc, message, user.getHoovervilleCharacter().getCurrentRegion());
		}else{
			out.println(getHelp());
		}
//		user.getHoovervilleCharacter().getCurrentRegion().getCharactersForRegion().contains(arg0);
//		if(charactersInRegion.){
//			
//		}
		
		
	}

	@Override
	public String getCommand() {
		return "emote";
	}

	@Override
	public String getHelp() {		
		return "Type 'emote' {[dance][run][smile][fart][spit][shiver][angry][whistle][sing]}";
	}

}
