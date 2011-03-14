import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientCon extends Thread {
	Socket socket;
	char EOF = (char) 0x00;
	
	public ClientCon(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader data_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter data_out = new PrintWriter(socket.getOutputStream());

			data_out.println("Welcome! type EXIT to quit." + EOF);
			data_out.flush();

			boolean quit = false;

			// Waits for the EXIT command
			while (!quit) {
				String msg = data_in.readLine();

				if (msg == null) quit = true;

				if (!msg.trim().equals("EXIT")) {
					System.out.println("Client said: " + msg.trim());
					data_out.println("You sayed: " + msg.trim() + EOF);
					data_out.flush();
				} else {
					quit = true;
				}
			}
		} catch (SocketException e) {
			System.out.println("Connection lost");
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("Connection lost2");
		}
	}
}
