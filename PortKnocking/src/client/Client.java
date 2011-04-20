package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DatagramSocket datagramSocket;
		try {

			datagramSocket = new DatagramSocket();
			byte[] buffer = "012345678".getBytes();
			
			int port = 8090;
			for (int i = 0; i < 4; i++) {
				SocketAddress receiverAddress = new InetSocketAddress("192.168.1.7", port + i);
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
