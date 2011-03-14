package server;

import java.io.IOException;
import java.net.SocketException;

import tftp.ReadRequestPacket;
import tftp.TFTPInterface;
import tftp.TFTPPacket;

public class Server {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException {
		TFTPInterface tftpServer = null;
		//create a new TFTPInterface
		try {
			//have it listen on port 69 for requests
			tftpServer = new TFTPInterface(69);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		if (tftpServer != null) {
			for (;;) {
				//get a request
				TFTPPacket packet;
				try {
					System.out.println("Waiting for a packet");
					packet = tftpServer.receive();
					System.out.println("Got a packet");

					//check to see if its a request packet
					if (packet.getType() == ReadRequestPacket.TYPE) {
						//create a handler thread
						RequestHandlerThread handler = new RequestHandlerThread((ReadRequestPacket)packet);
						handler.start();
						//handler.join();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Something went wrong with creating the TFTPInterface");
		}
	}
}
