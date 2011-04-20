package server;

import java.util.ArrayList;

public class DatagramSocketGroup {

	private ArrayList<DatagramSocketThread> sockets;

	public DatagramSocketGroup(int startPort, int numSockets) {
		
		sockets = new ArrayList<DatagramSocketThread>();

		// bind to the number of sockets
		for (int i = startPort; i < (startPort + numSockets); i++) {
			this.createDatagramSocket(i);
		}

	}

	public void createDatagramSocket(int port) {
		DatagramSocketThread dst = new DatagramSocketThread(port);
		sockets.add(dst);
		Thread t = new Thread(dst);
		t.start();
	}
}
