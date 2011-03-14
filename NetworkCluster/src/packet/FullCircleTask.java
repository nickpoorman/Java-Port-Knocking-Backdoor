package packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FullCircleTask extends Thread {

	private final Packet packet;
	private final Socket socket;
	private volatile ResultsPacket result;

	public FullCircleTask(Packet packet, Socket socket) {
		this.packet = packet;
		this.socket = socket;
		this.result = null;
	}

	public void run() {
		long start = System.nanoTime();
		// create the object streams for out and in
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectIn = null;
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// first send the packet and then wait for a response back
		if (objectOut == null || objectIn == null)
			System.err.println("The socket must have closed its streams");
		long startSend = System.nanoTime();
		try {
			objectOut.writeObject(packet);
			//objectOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Send: " + (System.nanoTime() - startSend));
		long startRec = System.nanoTime();
		// now wait for a response back
		try {
			result = (ResultsPacket) objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// now that we have our response we are done
		System.out.println("GetB: " + (System.nanoTime() - startRec));
		System.out.println("Full: " + (System.nanoTime() - start));
	}

	/**
	 * @return the result
	 */
	public ResultsPacket getResultsPacket() {
		return result;
	}
}
