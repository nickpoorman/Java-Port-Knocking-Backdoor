package poormanbot;

import java.util.LinkedList;
import java.util.List;

public class SSBotMain {

    public static void main(String[] args) throws Exception {
        
//        List <BotCore> bots = new LinkedList<BotCore>();
//
//        for (int i = 0; i < 100; i++) {
//            // Now start our bot up.
//            bots.add(new BotCore(i));
//            
//            // Enable debugging output.
//            bots.get(i).setVerbose(true);
//
//            // Connect to the IRC server.
//            bots.get(i).connect("irc.infamousgamers.info");
//
//            // Join the #pircbot channel.
//            bots.get(i).joinChannel("#CSA");
//        }
        
        // Now start our bot up.
            BotCore bot = new BotCore();
            
            
            
            // Enable debugging output.
            bot.setVerbose(true);

            // Connect to the IRC server.
            bot.connect("irc.infamousgamers.info");

            // Join the #pircbot channel.
            bot.joinChannel("#CSA");

    }
}
