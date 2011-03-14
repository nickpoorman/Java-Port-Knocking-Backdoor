package server;

import java.net.*;
import java.io.*;

public class GameProtocol {

    private static final int WAITING = 0;
    private static final int LOGIN = 1;
    private static final int NEXT = 2;
    private int state = WAITING;
    private ClientManager clientManager = null;
    private ClientConnection clientConnection = null;

    public GameProtocol(ClientManager manager, ClientConnection c) {
        this.clientManager = manager;
        this.clientConnection = c;
    }

    public String processInput(String theInput) {
        String theOutput = null;

        if (theInput.equals("Bye")) {
            theOutput = "Bye";
        } else {
            if (state == WAITING) {
                theOutput = "Please Login";
                state = LOGIN;
            } else if (state == LOGIN) {
                if (theInput.equals("nick")) {
                    if (clientManager.isLoggedIn(theInput)) {
                        try {
                            clientManager.getUser(theInput).getClientConnection().getOut().println("term");
                           // new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientManager.getClient(theInput).getOutputStream())), true).println("TERMINATE");
                            System.out.println("Terminating client");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    clientManager.addClient(theInput, clientConnection);
                    clientConnection.setUsername(theInput);
                    theOutput = "loginconfirmed";
                    state = NEXT;
                    System.out.println("User: " + theInput + " has logged in.");
                } else {
                    theOutput = "You did not enter a valid login. Please try again.";
                    state = LOGIN;
                }
            } else if (state == NEXT) {
                theOutput = "NEXT ifstatement";
            }
        }
        return theOutput;
    }
    
    public boolean Login(){
       // if(clientConnection.)
        return true;
    }


}
