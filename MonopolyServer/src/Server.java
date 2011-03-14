import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import server.ClientConnection;

public class Server implements Runnable {

	public Server() {

	}

	@Override
	public void run() {
		while (true) {
			// Message terminator
			

			try {

				// create a serverSocket connection on port 9999
				ServerSocket s = new ServerSocket(9999);

				System.out.println("Server started. Waiting for connections...");
				while (true) {
					new ClientCon(s.accept()).start();
					System.out.println("Got new connection from the client");
				}
				// wait for incoming connections
				//Socket incoming = s.accept();
				

			} catch (Exception e) {
				System.out.println("Connection lost3");

			}
		}

	}
}
