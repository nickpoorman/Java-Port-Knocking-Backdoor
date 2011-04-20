package server;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DatagramSocketThread implements Runnable {

	private final int port;

	public DatagramSocketThread(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			DatagramSocket datagramSocket = new DatagramSocket(this.port);

			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (true) {
				datagramSocket.receive(packet);
				System.out.println("Got packet [" + this.port + "]: " + new String(packet.getData()));
			}

		} catch (BindException e) {
			System.out.println("-" + this.port);
		} catch (SocketException e) {
			System.out.println("--" + this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
