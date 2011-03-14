/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poormanbot;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nick
 */
public class IRCLine {

    private String channel;
    private String sender;
    private String login;
    private String hostname;
    private String message;

    public IRCLine(String channel, String sender, String login, String hostname, String message) {
        this.channel = channel;
        this.sender = sender;
        this.login = login;
        this.hostname = hostname;
        this.message = message;
    }

    public void removeCommand() {
        int pointer = 0;
        while (!message.substring(pointer, pointer + 1).equals(" ")) {
            pointer++;
        }
        pointer++;
        message = message.substring(pointer);
        message = message.trim();

    }

    public void spliceForURL() {
        int pointerBegin = 0;
        int pointerEnd = 0;
        while (!message.substring(pointerBegin, pointerBegin + 7).equals("http://")) {
            pointerBegin++;
            pointerEnd++;
        }


        while (pointerEnd != message.length() && !message.substring(pointerEnd, pointerEnd + 1).equals(" ")) {
            pointerEnd++;
        }


        message = message.substring(pointerBegin, pointerEnd);
        message = message.trim();

    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getMessageArray() {
        String[] tmp = message.split(" ");
        return tmp;
    }
}
