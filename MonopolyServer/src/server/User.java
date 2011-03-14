package server;

import java.net.Socket;

public class User {

    private String login;    
    private ClientConnection clientConnection;

    public User(String username, ClientConnection c) {
        this.login = username;        
        this.clientConnection = c;
    }   

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the clientConnection
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * @param clientConnection the clientConnection to set
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

}
