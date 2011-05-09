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
	public static final String FULL_USAGE = "Use the following format to run the program: java Client -p<comma separated port sequence> -r<port number> [-l]";
	public static final String REVERSE_PORT = "Use -r<port number> to specify a port the reverse connection should attempt to connect back on";
	public static final String LISTEN_USAGE = "Use -l to create a listening socket to handle reverse connections";
	public static final String PORT_SEQUENCE_USAGE = "Use -p<comma separated port sequence> to specifiy the knock sequence to send";
	private static final String LISTEN_PORT_SWITCH = "-l";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println(Client.FULL_USAGE);
			System.out.println(Client.REVERSE_PORT);
			System.out.println(Client.LISTEN_USAGE);
			return;
		} else {
			// first try getting the list of ports
			String portsString = Server.findArgument(Server.PORT_SEQUENCE_SWITCH, args);
			if (portsString == "") {
				System.out.println("Port sequence must be entered.");
				System.out.println(Client.FULL_USAGE);
				System.out.println(Client.REVERSE_PORT);
				System.out.println(Client.LISTEN_USAGE);
				return;
			}
			final List<Integer> ports = new ArrayList<Integer>();
			try {
				Server.getPortSequenceList(portsString, ports);
			} catch (NumberFormatException e) {
				System.out.println("Invalid port entered.");
				return;
			}
			
			// then try getting the reverse port
			String reversePort = Server.findArgument(Server.PORT_SEQUENCE_SWITCH, args);
			
		}

		DatagramSocket datagramSocket;
		final boolean USE_JNETCAT = true;

		try {
			if (USE_JNETCAT) {

				// create a new thread to listen for a connection
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							ServerSocket server = new ServerSocket(31337);

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
													for (;;) {
														System.out.println(in.readLine());
													}
												} catch (IOException e) {
													e.printStackTrace();
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
			byte[] buffer = "31337".getBytes();

			int port = 8090;
			for (int i = 0; i < 4; i++) {
				SocketAddress receiverAddress = new InetSocketAddress("127.0.0.1", port + i);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress);

				datagramSocket.send(packet);
				System.out.println("Sent packet: " + port + i);
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
