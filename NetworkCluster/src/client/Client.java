package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import packet.Packet;
import packet.ResultsPacket;

public class Client extends Thread {

	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String host;
	private final static boolean DEBUG = true;

	public Client(String host) {
		this.host = host;
	}

	public void run() {
		try {

			try {
				socket = new Socket(getHost(), 31337);
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true);
				in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + getHost() + ".");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + getHost() + ".");
				System.exit(1);
			}

			ClientTree clientTree = null;

			//for (int i = 0; i < 30000; i++) {
			for(;;){
				//wait for the server to send us a packet object
				ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
				Packet packet = null;
				try {
					System.out.println("Getting data back from server");
					packet = (Packet) objectIn.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				//Thread clientTree = new Thread(new ClientTree(packet, socket));
				//clientTree.start();
				//ClientTree clientTree = null;
				if (packet.isSendingAllData()) {
					clientTree = new ClientTree(packet, socket);
				}
				if (clientTree != null) {
					//					clientTree.setEastEdgeData(packet.getRowMin(), packet.getColumnMin(), packet.getRowMax(), packet
					//							.getColumnMax(), packet.getEastEdgeData(), clientTree.ghostEastEdgeData);
					//					clientTree.setWestEdgeData(packet.getRowMin(), packet.getColumnMin(), packet.getRowMax(), packet
					//							.getColumnMax(), packet.getWestEdgeData(), clientTree.ghostWestEdgeData);
					clientTree.doIteration();
					//			boolean giveBackAllData, boolean sendingAllData, int totalRowLength, int totalColumnLength,
					//			int rowMin, int rowMax, int columnMin, int columnMax, long[][] data, long[] eastEdgeData,
					//			long[] westEdgeData, double[] metalConstants, int[][][] percentageOfMetals

					System.out.println("Sending data back");
					ResultsPacket resultsPacket = null;
					if (packet.isGiveBackAllData()) {
						resultsPacket = new ResultsPacket(clientTree.getRowMin(), clientTree.getRowMax(), clientTree
								.getColumnMin(), clientTree.getColumnMax(), clientTree.getResults());
					} else {
						resultsPacket = new ResultsPacket(clientTree.getRowMin(), clientTree.getRowMax(), clientTree
								.getColumnMin(), clientTree.getColumnMax(), clientTree.getThisEastEdge(), clientTree
								.getThisWestEdge());
					}
					objectOut.writeObject(resultsPacket);
				}
			}

			/* clean up if for some reason the input is null */
			//			if (DEBUG) System.out.println("POINT 1");
			//			getOut().close();
			//			if (DEBUG) System.out.println("POINT 2");
			//			getIn().close();
			//			if (DEBUG) System.out.println("POINT 3");
			//			getSocket().close();
			//			if (DEBUG) System.out.println("POINT 4");
			//			System.exit(0);
			//			if (DEBUG) System.out.println("POINT 5");

		} catch (SocketException e) {
			if (DEBUG) System.err.println("Server Died");
			/* Put into the output of the client that the server disconnected */
			//if (DEBUG) e.printStackTrace();
		} catch (IOException e) {
			if (DEBUG) e.printStackTrace();
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

}
