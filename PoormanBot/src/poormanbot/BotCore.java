package poormanbot;

import org.jibble.pircbot.*;

public class BotCore extends PircBot {

    IRCLine currentIRCLine;
    //VotingMachine vm = new VotingMachine(this);
    boolean copy = false;
    int i = 0;

    public BotCore(int i) {
        this.i = i;
        this.setName("[IFG]sS" + this.i);
    }
    
    public BotCore() {
        this.setName("[IFG]sS" + this.i);
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        currentIRCLine = new IRCLine(channel, sender, login, hostname, message);

    }
}
