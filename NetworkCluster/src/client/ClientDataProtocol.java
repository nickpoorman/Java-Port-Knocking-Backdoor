package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientDataProtocol {

	private static final String HOSTNAME = "HOSTNAME";
	private static final int WAITING = 0;
	private static final int WAITING_FOR_SYN = 1;
	private static final int WAITING_FOR_ACT = 2;
	private AtomicInteger state;
	private Socket in = null;

	public ClientDataProtocol(Socket in) {
		this.in = in;
		state = new AtomicInteger(WAITING);
	}

	public String processInput(String theInput) {
		String theOutput = null;
		if (state.get() == WAITING) {
			if (theInput.equals(HOSTNAME)) {
				// send back the hostname
				InetAddress addr = null;
				try {
					addr = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				theOutput = addr.getHostName();
				state.set(WAITING_FOR_SYN);
			}
		} else if (state.get() == WAITING_FOR_SYN) {
			// wait to get the message that we are getting data
			if (theInput.equals("SYN")) {
				state.set(WAITING_FOR_ACT);
			}
		} else if (state.get() == WAITING_FOR_ACT) {
			// load the data into the array until we get a RST packet
			// get the all data or edge //1 = all 0 = edge
			// in.getInputStream().
//			int BUFFER_SIZE = Long.SIZE / 8;
//			byte[] buffer = new byte[BUFFER_SIZE];
//			int read;
//			int totalRead = 0;
//			int timesRead = 0;
//			InputStream clientInputStream = null;
//			try {
//				clientInputStream = in.getInputStream();
//				while ((read = clientInputStream.read(buffer)) != -1) {
//					totalRead += read;
//					timesRead++;
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("totalRead: " + totalRead);
//			System.out.println("timeRead: " + timesRead);
			

		}

		return theOutput;
	}

}
