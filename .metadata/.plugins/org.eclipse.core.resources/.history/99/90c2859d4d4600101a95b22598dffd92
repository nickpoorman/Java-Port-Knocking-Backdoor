package head;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

	
	ConcurrentHashMap<String, ClientConnection> clients;

	public ClientManager() {
		clients = new ConcurrentHashMap<String, ClientConnection>();
	}

	public void addClient(String hostname, ClientConnection c) {
		clients.put(hostname, c);
	}

	public boolean removeClient(String hostname) {
		if (!contains(hostname)) {
			return false;
		}
		clients.remove(hostname);
		return true;

	}

	public boolean contains(String hostname) {
		return clients.containsKey(hostname);

	}

	public Socket getClientSocket(String hostname) {
		return clients.get(hostname).getSocket();

	}

	public boolean isLoggedIn(String hostname) {
		return clients.containsKey(hostname);

	}

	public ConcurrentHashMap<String, ClientConnection> getClients() {
		return new ConcurrentHashMap<String, ClientConnection>(clients);
	}

	public ClientConnection getClientConnection(String hostname) {
		return clients.get(hostname);

	}
}
