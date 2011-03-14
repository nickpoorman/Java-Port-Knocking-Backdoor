package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;

import tftp.AckPacket;
import tftp.DataPacket;
import tftp.ReadRequestPacket;
import tftp.TFTPInterface;
import tftp.TFTPPacket;

public class Client {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String usage = "usage: java client.Client 4|6 <remote address> <url> [s] [d]";
		String host = "";
		int version = 0;
		String url = "";
		boolean useSlidingWindow = false;
		boolean dropSim = false;
		if (args.length < 3) {
			System.out.println(usage);
			return;
		} else if (args.length == 3) {
			version = Integer.parseInt(args[0]);
			host = args[1];
			url = args[2];
		} else if (args.length == 4) {
			version = Integer.parseInt(args[0]);
			host = args[1];
			url = args[2];
			if (args[3].equals("s")) {
				useSlidingWindow = true;
			} else if (args[3].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
		} else if (args.length == 5) {
			version = Integer.parseInt(args[0]);
			host = args[1];
			url = args[2];
			if (args[3].equals("s")) {
				useSlidingWindow = true;
			} else if (args[3].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
			if (args[4].equals("s")) {
				useSlidingWindow = true;
			} else if (args[4].equals("d")) {
				dropSim = true;
			} else {
				System.out.println(usage);
				return;
			}
		}

		// start the time
		long startTime = System.nanoTime();

		InetAddress serverAddress = InetAddress.getByName(host);
		int addressPort = 63010;
		String pageName = "data";
		// String urlRequest = "http://cs.oswego.edu/~poorman/index.html";
		boolean finished = true;

		// File to write to
		File save = new File(pageName);

		// a stream to hold the data
		FileOutputStream fos = new FileOutputStream(save);

		// Create a new TFTPInterface
		TFTPInterface tftp = new TFTPInterface();

		// create the request packet
		ReadRequestPacket requestPacket = new ReadRequestPacket(serverAddress, addressPort, ReadRequestPacket.TYPE, url);

		// send a request
		tftp.send(requestPacket, true);

		// keep track of the last packet size
		int lastDataSize = 0;

		// this will hold the total data size of the file
		int dataSize = 0;

		if (useSlidingWindow) {
			//
			// // the advertisedWindow size should be either smaller or equal to
			// // the sending window size
			//
			// // all the pointers for the window
			// /**
			// * The upper bound on the number of out-of-order frames that the
			// * receiver is willing to accept
			// **/
			// // int receiveWindowSize;
			// int rws = 4;
			//
			// /** The sequence number of the last frame received **/
			// // int lastFrameReceived;
			// int lfr = -1;
			//
			// /** The sequence number of the largest acceptable frame **/
			// // int largestAcceptableFrame;
			// int laf = lfr + rws;
			//
			// /** The next sequence number that should be received **/
			// int ntr = 0;
			//			
			// // advertisedWindow will hold the data that comes in for the
			// window
			// LinkedList<DataPacket> advertisedWindow = new
			// LinkedList<DataPacket>();
			//
			// do {
			// // get the request response (should be a data packet)
			// TFTPPacket dPacket = tftp.receive();
			//
			// // save the data if its a data packet
			// if (dPacket.getType() == DataPacket.TYPE) {
			// DataPacket dataPacket = (DataPacket) dPacket;
			//
			// // get the packets block number
			// int blockNumber = dataPacket.getBlockNumber();
			//
			// //System.out.println("server received packet with blockNumber: "
			// + blockNumber);
			//
			// if ((blockNumber <= lfr || blockNumber > laf)) {
			// // its outside the window so just throw it away
			// //System.out.println("Throwing the packet away");
			// /*
			// //send an ack back?
			// // get the return info
			// InetAddress returnServerAddress = dataPacket.getAddress();
			// int port = dataPacket.getPort();
			//
			// // create the ack packet
			// AckPacket ackPacket = new AckPacket(returnServerAddress, port,
			// AckPacket.TYPE, lfr);
			//
			// // send back an ack of the block number
			// tftp.send(ackPacket, dropSim);
			// */
			//
			// continue;
			// } else if (((blockNumber == 0) && (lfr ==
			// DataPacket.MAX_BLOCK_NUMBER)) || ((lfr < blockNumber) &&
			// (blockNumber <= laf))) {
			// //if the block number is 0 we are starting a new sliding window
			// if(blockNumber == 0){
			// lfr = -1;
			// laf = lfr + rws;
			// }
			//						
			// // its in the window so we can do something with it
			// //System.out.println("Packet landed in the receive window");
			//
			// // set the size of its payload
			// lastDataSize = dataPacket.getPayloadLength();
			//
			// // all of this search is not really needed since we look
			// // to see if blockNumber <= lfr before hand
			// // but it might be useful in the future if we use
			// // selective acknowledgment
			// boolean added = false;
			// // go through each packet we already have
			// for (int i = 0; i < advertisedWindow.size(); i++) {
			// // add the packet into the window where its supposed
			// // to go
			// if (blockNumber < advertisedWindow.get(i).getBlockNumber()) {
			// //System.out.println("ADDING IN LOOP FOR CLIENT");
			// advertisedWindow.add(i, dataPacket);
			// added = true;
			// break;
			// }
			// }
			// // if it was larger then any of the packets in the list
			// // add it to the end
			// if (!added) {
			// advertisedWindow.add(dataPacket);
			// }
			//
			// //System.out.println("Added the packet to the window");
			//
			// // set the lastFrameReceived to be the new packet
			// lfr = blockNumber;
			//
			// // one of two things can happen now, either the window
			// // is filled or a packet was received out of order
			// // if the window is full (laf == lfr) send an ack for
			// // lfr, set laf = laf + rws
			// // OR if its the last packet we flush the buffer as well
			// if ((laf == lfr) || (lastDataSize < DataPacket.MAX_DATA_LENGTH))
			// {
			// //System.out.println("writing window to file output stream");
			// // get the return info
			// InetAddress returnServerAddress = dataPacket.getAddress();
			// int port = dataPacket.getPort();
			//
			// // create the ack packet
			// AckPacket ackPacket = new AckPacket(returnServerAddress, port,
			// AckPacket.TYPE, lfr);
			//
			// // send back an ack of the block number
			// tftp.send(ackPacket, dropSim);
			//
			// // when all the data has been received into the
			// // window dump it to the stream
			// for (int i = 0; i < advertisedWindow.size(); i++) {
			// // write the payload of the packet to the stream
			// fos.write(advertisedWindow.get(i).getPayload());
			// dataSize += advertisedWindow.get(i).getPayloadLength();
			// }
			//
			// // clear the window
			// advertisedWindow.clear();
			//
			// // move the window
			// laf = laf + rws;
			// //System.out.println("done writing window to file output stream");
			// }
			// // else if ntr != blockNumber send back an ack for lfr
			//
			// } else {
			// System.out.println("Something went wrong and we ended up in a strange state");
			// }
			// } else {
			// System.out.println("Something went wrong. Supposed to get a data packet but didn't");
			// finished = false;
			// break;
			// }
			// } while (lastDataSize >= DataPacket.MAX_DATA_LENGTH);

			int rws = 4;
			ArrayList<DataPacket> advertisedWindow = new ArrayList<DataPacket>();

			int lastDataLength = 0;

			do {
				// get a data packet
				TFTPPacket dPacket = tftp.receive();

				if (dPacket.getType() == DataPacket.TYPE) {
					DataPacket dataPacket = (DataPacket) dPacket;

					// get the packets block number
					int blockNumber = dataPacket.getBlockNumber();

					lastDataLength = dataPacket.getPayloadLength();

					boolean added = false;
					// put the packet in the right spot in a buffer
					for (int i = 0; i < advertisedWindow.size(); i++) {
						if (dataPacket.getBlockNumber() < advertisedWindow.get(i).getBlockNumber()) {
							// put it here
							advertisedWindow.add(i, dataPacket);
							added = true;
						}
					}
					if (!added) {
						advertisedWindow.add(dataPacket);
					}
					
					if(advertisedWindow.size() == rws){
						//flush it
						for(int i = 0; i < advertisedWindow.size(); i++){
							fos.write(advertisedWindow.get(i).getPayload());
						}
						advertisedWindow.clear();
					}

				} else {
					System.out.println("Didn't get a data packet");
				}
			} while (lastDataLength == DataPacket.MAX_DATA_LENGTH);

		} else {
			// keep track of the last packet size
			// int lastDataSize = 0;

			// keep track of the number of packets received, used for debugging
			// int numReceived = -1;

			do {
				// System.out.println("getting the data packet back");
				// get the request response (should be a data packet)
				TFTPPacket dPacket = tftp.receive();

				// this is used for debugging
				// numReceived++;
				// System.out.println("numReceived: " + numReceived);

				// save the data if its a data packet
				if (dPacket.getType() == DataPacket.TYPE) {
					DataPacket dataPacket = (DataPacket) dPacket;

					// set the size of its payload
					lastDataSize = dataPacket.getPayloadLength();

					// write the payload of the packet to the stream
					fos.write(dataPacket.getPayload());

					// adjust the total data size received
					dataSize += lastDataSize;

					// get the return info
					InetAddress returnServerAddress = dataPacket.getAddress();
					int port = dataPacket.getPort();
					int blockNumber = dataPacket.getBlockNumber();

					// create the ack packet
					AckPacket ackPacket = new AckPacket(returnServerAddress, port, AckPacket.TYPE, blockNumber);

					// send back an ack of the block number
					tftp.send(ackPacket, dropSim);

				} else {
					System.out.println("Something went wrong. Supposed to get a data packet but didn't");
					finished = false;
					break;
				}
				// System.out.println("lastDataSize: " + lastDataSize);
			} while (lastDataSize >= DataPacket.MAX_DATA_LENGTH);
		}
		if (finished) {
			// flush the stream
			fos.flush();

			// stop the time
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;

			System.out.println("Done!");
			System.out.println("Total data size received: " + dataSize);
			System.out.println("Total time: " + totalTime);
			// double throughput = (dataSize * 8) / (totalTime / 1000000000);
			double throughput = (dataSize * 8) / (totalTime);
			System.out.println("Throughput (bits/sec): " + throughput);
		} else {
			System.out.println("Didn't finish!");
		}

	}

	public static String generateRandomString() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
