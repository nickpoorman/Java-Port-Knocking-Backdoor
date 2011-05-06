package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DatagramSocket datagramSocket;

		try {

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
									// create two treads. one to listen and one
									// to write
									final Socket socket = client;

									Thread readThread;
									Thread writeThread;

									readThread = new Thread(new Runnable() {

										public void run() {
											try {
												BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
												for (;;) {
													System.out.print(in.readLine());
												}
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									});									

									writeThread = new Thread(new Runnable() {

										public void run() {
											Scanner sc = new Scanner(System.in);
											while (sc.hasNext()) {
												try {
													socket.getOutputStream().write(sc.nextLine().getBytes());
													socket.getOutputStream().flush();
												} catch (IOException e) {
													e.printStackTrace();
												}

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
