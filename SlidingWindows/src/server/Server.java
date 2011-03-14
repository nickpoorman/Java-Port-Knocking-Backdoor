package server;

import java.io.IOException;

import java.net.SocketException;
import java.net.UnknownHostException;

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
		String usage = "usage: java server.Server 4|6 <local address> [s] [d]";
		String host = "";
		int version = 0;
		boolean useSlidingWindow = false;
		boolean dropSim = false;
		if (args.length < 2) {
			System.out.println(usage);
			return;
		} else if (args.length == 2) {
			version = Integer.parseInt(args[0]);
			host = args[1];
		} else if (args.length == 3) {
			version = Integer.parseInt(args[0]);
			host = args[1];
			if (args[2].equals("s")) {
				useSlidingWindow = true;
			} else if (args[2].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
		} else if (args.length == 4) {
			version = Integer.parseInt(args[0]);
			host = args[1];
			if (args[2].equals("s")) {
				useSlidingWindow = true;
			} else if (args[2].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
			if (args[3].equals("s")) {
				useSlidingWindow = true;
			} else if (args[3].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
		}

		// boolean useSlidingWindow = true;
		PageFetcherManager pfm = new PageFetcherManager(useSlidingWindow, dropSim);
		pfm.start();

		TFTPInterface tftpServer = null;
		// create a new TFTPInterface
		try {
			// have it listen on port 69 for requests
			tftpServer = new TFTPInterface(63010, host);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (tftpServer != null) {
			for (;;) {
				// get a request
				TFTPPacket packet;
				try {
					System.out.println("Waiting for a packet");
					packet = tftpServer.receive();
					System.out.println("Got a packet");

					// check to see if its a request packet
					if (packet.getType() == ReadRequestPacket.TYPE) {
						// create a handler thread
						// OLD WAY
						// RequestHandlerThread handler = new
						// RequestHandlerThread((ReadRequestPacket)packet,
						// useSlidingWindow);
						// handler.start();
						// //handler.join();

						// NEW WAY
						// use the PageFetcherManager to add requests to a
						// thread pool
						pfm.addToPacketsQueue((ReadRequestPacket) packet);

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
