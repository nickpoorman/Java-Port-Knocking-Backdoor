package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tftp.AckPacket;
import tftp.DataPacket;
import tftp.ReadRequestPacket;
import tftp.TFTPInterface;
import tftp.TFTPPacket;

public class RequestHandlerThread extends Thread {

	private final ReadRequestPacket packet;
	private boolean slidingWindow;
	private final boolean useDropSim;

	private final static boolean DEBUG = false;
	volatile URL url;
	volatile PageFetcherManager pageFetcherManager;

	public RequestHandlerThread(ReadRequestPacket packet, boolean slidingWindow, boolean useDropSim) {
		this.packet = packet;
		this.slidingWindow = slidingWindow;
		this.useDropSim = useDropSim;
	}

	public void run() {
		// get the name of the url they are looking for
		String pURL = packet.getUrl();

		// create a URL object from the String url
		try {
			this.url = new URL(pURL);
		} catch (MalformedURLException e) {
			System.out.println("Not a valid url: " + pURL);
			return;
		}
		// fetch the page //BufferedInputStream wouldn't block if id didn't get
		// the length so i had to do some hacking
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(this.url.openConnection().getInputStream());
		} catch (IllegalArgumentException e) {
		} catch (IOException e) {
			System.out.println("Did not get the page for some reason.");
			return;
		}

		// Create a new TFTPInterface here to use from now on
		TFTPInterface tftp = null;
		int numSRetries = 0;
		while (tftp == null) {
			try {
				tftp = new TFTPInterface();
			} catch (SocketException e) {
				// the socket was in use or something
				// e.printStackTrace();
				// try to get a new socket
				if (numSRetries >= 10) {
					// we exceeded the number of retries
					System.out.println("Exceeded the number of socket retries");
					return;
				}
			}
		}

		// going to store it into an array list, note: should be put into a
		// RandomAccessFile or something if its really big
		ArrayList<byte[]> payloads = new ArrayList<byte[]>();

		// read in the bytes from the file into payloads
		boolean done = false;
		// used for debuging numPackets
		int numPackets = 0;
		while (!done) {
			numPackets++;

			// for some reason (probably because the website is being fetched
			// via a tcp socket) the stream doesn't
			// seem to block until the amount of data I have requested is ready,
			// so I created my own that begins
			// filling up a byte buffer until all the data is there that I
			// requested or end of stream
			ByteBuffer bbuf = ByteBuffer.allocate(DataPacket.MAX_DATA_LENGTH);
			while (bbuf.hasRemaining()) {
				byte[] b = new byte[1];
				try {
					int result = bis.read(b);
					if (result == -1) {
						done = true;
						break;
					} else {
						bbuf.put(b);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// if the last packet has less then 512
			if (((bbuf.capacity() - bbuf.remaining()) > 0) && ((bbuf.capacity() - bbuf.remaining()) < DataPacket.MAX_DATA_LENGTH)) {
				// System.out.println("Found a packet less then 512, total read: "
				// + (bbuf.capacity() - bbuf.remaining()));

				// copy the current payload into a smaller payload
				byte[] tmpPayload = new byte[(bbuf.capacity() - bbuf.remaining())];
				bbuf.rewind();
				bbuf.get(tmpPayload);
				// now set it to be payload
				payloads.add(tmpPayload);
				continue;
			}
			byte[] payload = new byte[DataPacket.MAX_DATA_LENGTH];
			bbuf.rewind();
			bbuf.get(payload);
			payloads.add(payload);
		}

		/*
		 * // GOING TO TEST STARTING HERE File f = new File("testoutput"); try {
		 * FileOutputStream s = new FileOutputStream(f); for (int k = 0; k <
		 * payloads.size(); k++) { // byte[] b = payloads.get(k);
		 * s.write(payloads.get(k)); } } catch (FileNotFoundException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } // DONE TEST HERE
		 */

		// System.out.println("Number of packets for server to send: " +
		// payloads.size());

		// get the clients information to send them back the data
		int clientPort = packet.getPort();
		InetAddress clientAddress = packet.getAddress();

		// once we have the file send back the first data packet for the file

		// do sliding windows stuff here or do TFTP spec stuff here

		if (slidingWindow) {
			// // use payloads to hold the total slidingWindow
			//
			// // set up variables for the window
			//
			// /**
			// * The upper bound on the number of unacknowledged frames that the
			// * sender can transmit
			// **/
			// // int sendWindowSize;
			// int sws = 4;
			//
			// /** The sequence number of the last acknowledgment received **/
			// // 8mil terabyte file
			// // int lastAcknowledgmentReceived;
			// int lar = -1;
			//
			// /** The sequence number of the last frame sent **/
			// // int lastFrameSent;
			// int lfs = -1;
			//
			// /** Maintain the following invariant: lfs - lar <= sws **/
			//
			// /**
			// * keep track of the the number of times the block number been
			// * reset. reset = times reset + 1
			// **/
			// int reset = 0;
			//
			// //System.out.println("Payloads size is: " + payloads.size());
			//
			// // break it up into max blockNumbers
			// while (lar + (reset * DataPacket.MAX_BLOCK_NUMBER) <
			// (payloads.size() - 1)) {
			// // while the last frame sent pointer is less then the window
			// // size move it up
			// // and while the last frame sent pointer is less then the max
			// // block number cause we don't want an overflow
			// // and while the current block number is < payloads size
			// // send all the packets up to the window size
			// //System.out.println("Outer loop blockNumber: " + (lfs + (reset *
			// DataPacket.MAX_BLOCK_NUMBER)));
			// while (((lar + sws) > lfs) && (lfs < DataPacket.MAX_BLOCK_NUMBER)
			// && ((lfs + (reset * DataPacket.MAX_BLOCK_NUMBER)) <
			// (payloads.size() - 1))) {
			// //System.out.println("Inner loop blockNumber: " + (lfs + (reset *
			// DataPacket.MAX_BLOCK_NUMBER)));
			// // check to see if the lfs + 1 will be over the
			// // send lfs + 1
			// // create the data packet to send back
			// DataPacket returnPacket = new DataPacket(clientAddress,
			// clientPort, DataPacket.TYPE, (lfs + 1), payloads
			// .get((lfs + (reset * DataPacket.MAX_BLOCK_NUMBER)) + 1));
			//
			// // now send the packet back
			// try {
			// //System.out.println("Server sending packet: " +
			// returnPacket.getBlockNumber());
			// tftp.send(returnPacket, this.useDropSim);
			// } catch (IOException e) {
			// // there was some IO problem and the packet didn't send
			// e.printStackTrace();
			// }
			//
			// // now move up the pointer to fit the packet we just sent
			// lfs++;
			// //System.out.println("LFS is: " + lfs);
			// }
			// // now wait for the ack
			// try {
			// //System.out.println("Waiting for the ack back");
			// // set the timeout to be 1 minute
			// TFTPPacket aPacket = (AckPacket) tftp.receive(3000);
			// // we got an ack back
			//
			// if (aPacket.getType() == AckPacket.TYPE) {
			// AckPacket ack = (AckPacket) aPacket;
			// // set lar = the acked packet //now move up pointers and
			// // send again. up at while loop^
			// int ackBlockNumber = ack.getBlockNumber();
			// // check to make sure the block number being acked is
			// // inside the window
			// //System.out.println("Got an ack");
			// if ((ackBlockNumber > lar) && (ackBlockNumber <= lfs)) {
			// //System.out.println("Ack was in window");
			// lar = ackBlockNumber;
			// //System.out.println("LAR is: " + lar);
			// } else {
			// //System.out.println("Ack was not in window");
			// }
			// } else {
			// //System.out.println("We did not get an ack back like we were supposed to");
			// // TODO: should probably check to see if its and error
			// // packet and do something
			// }
			//
			// } catch (SocketTimeoutException e) {
			// //System.out.println("Socket timed out, going to resend the window");
			// // the receive must have timed out
			// // resend the whole window
			// lfs = lar;
			// continue;
			// } catch (IOException e) {
			// // for some reason we could not receive a packet, IO problem
			// e.printStackTrace();
			// }
			//
			// // check to see if the current sw is full
			// if (lar == DataPacket.MAX_BLOCK_NUMBER) {
			// //System.out.println("DOING A RESET");
			// reset++;
			// lar = -1;
			// lfs = -1;
			// }
			// }

			// starting over
			int packetIndex = 0;
			int windowSize = 4;
			while (packetIndex < payloads.size()) {
				// send the number of packets
				for (int i = 0; i < windowSize; i++) {
					DataPacket returnPacket = new DataPacket(clientAddress, clientPort, DataPacket.TYPE, i, payloads.get(packetIndex));
					try {
						// there was some IO problem and the packet didn't send
						tftp.send(returnPacket, this.useDropSim);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// wait for an ack thats equal to the number of packets we sent
				try {
					TFTPPacket aPacket = (AckPacket) tftp.receive(3000);
					if (aPacket.getType() == AckPacket.TYPE) {
						AckPacket ack = (AckPacket) aPacket;
						int blockNumber = ack.getBlockNumber();
						if(blockNumber == (windowSize - 1)){
							packetIndex += windowSize;
						}
					}
				} catch (SocketTimeoutException e) {
					// It timed out don't do anything, it will send again
					// e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			// dont use sliding window
			// keep track of the number of retries
			int retries = 5;
			int reset = 0;

			// holds the current block number, so we know the offset to read
			// from
			for (int blockNumber = 0; (blockNumber + (reset * DataPacket.MAX_BLOCK_NUMBER)) < payloads.size();) {
				// create the data packet to send back
				DataPacket returnPacket = new DataPacket(clientAddress, clientPort, DataPacket.TYPE, blockNumber, payloads.get(blockNumber
						+ (reset * DataPacket.MAX_BLOCK_NUMBER)));

				byte[] tmp = payloads.get(blockNumber);
				// now send the packet back
				try {
					tftp.send(returnPacket, this.useDropSim);
				} catch (IOException e) {
					// there was some IO problem and the packet didn't send
					e.printStackTrace();
				}

				// when we get the ack for the file send the next data packet
				// based on the ack blockNumber
				TFTPPacket aPacket = null;
				try {
					aPacket = (AckPacket) tftp.receive();
				} catch (IOException e) {
					// for some reason we could not receive a packet, IO problem
					e.printStackTrace();
				}
				if (aPacket.getType() == AckPacket.TYPE) {
					AckPacket ack = (AckPacket) aPacket;
					blockNumber = ack.getBlockNumber() + 1;
					if (blockNumber == DataPacket.MAX_BLOCK_NUMBER) {
						blockNumber = 0;
						reset++;
					}
				} else {
					// System.out.println("We did not get an ack back like we were supposed to");
					// TODO: should probably check to see if its and error
					// packet and do something
					retries--;
					if (retries < 1) {
						// System.out.println("Exceeded the number of retries");
						break;
					}
				}
			}
		}
		System.out.println("Finished sending the data");
		// we are out of the loop so the file must have been sent or it timed
		// out so we can just clean up
		tftp.destroy();
	}
}
