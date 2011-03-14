package server;

import java.net.*;
import java.io.*;

public class ClientConnection extends Thread {

    private Socket socket = null;
    private ClientManager clientManager = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String currentUser = null;
    private final static boolean DEBUG = true;

    public ClientConnection(Socket socket, ClientManager manager) {
        super("ClientConnectionThread");
        this.socket = socket;
        this.clientManager = manager;
        if(DEBUG)System.out.println("New user has connected");
    }

    public void setUsername(String user) {
        this.currentUser = user;
    }

    public void run() {

        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true);
            in = new BufferedReader(
                    new InputStreamReader(
                    getSocket().getInputStream()));

            String inputLine, outputLine;
            GameProtocol gp = new GameProtocol(clientManager, this);
            outputLine = gp.processInput("EXECLOGIN0001");
            getOut().println(outputLine);

            while ((inputLine = getIn().readLine()) != null) {
                if(DEBUG)System.out.println("Debug (from client): " + inputLine);
                outputLine = gp.processInput(inputLine);
                getOut().println(outputLine);
                if (outputLine.equals("Bye")) {
                    break;
                }
            }
            getOut().close();
            getIn().close();
            getSocket().close();

        } catch (SocketException e) {

            try {
                getOut().close();
                getIn().close();
                getSocket().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //e.printStackTrace();
            if(DEBUG)System.out.println("User has quit: " + getCurrentUser());
            boolean b = clientManager.removeClient(getCurrentUser());
            if(DEBUG)System.out.println("Removed user: " + b);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return the out
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * @return the in
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * @return the currentUser
     */
    public String getCurrentUser() {
        if (currentUser != null) {
            return currentUser;
        }
        return "null user";
    }
}
