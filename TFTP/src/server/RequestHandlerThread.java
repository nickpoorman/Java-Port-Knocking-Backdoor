package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import tftp.AckPacket;
import tftp.DataPacket;
import tftp.ReadRequestPacket;
import tftp.TFTPInterface;
import tftp.TFTPPacket;

public class RequestHandlerThread extends Thread {

	ReadRequestPacket packet;

	public RequestHandlerThread(ReadRequestPacket packet) {
		this.packet = packet;
	}

	public void run() {
		//get the name of the url they are looking for
		String url = packet.getUrl();

		//Create a new TFTPInterface here to use from now on
		TFTPInterface tftp = null;
		try {
			tftp = new TFTPInterface();
		} catch (SocketException e) {
			// the socket was in use or something
			//TODO: try to get a new socket
			e.printStackTrace();
		}

		//when it gets a request get the file they are asking for
		File file = new File(url);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// the file was not found
			//TODO: Send back an error packet saying that url was not found
			e.printStackTrace();
			return;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		//going to store it into an array list, note: should be put into a RandomAccessFile or something if its really big		
		ArrayList<byte[]> payloads = new ArrayList<byte[]>();

		//read in the bytes from the file into payloads
		int totalRead = 1;
		for (int i = 0; totalRead > 0; i++) {
			System.out.println("Creating packet: " + i);

			//buf will hold the data to send
			byte[] payload = new byte[DataPacket.MAX_DATA_LENGTH];
			try {
				totalRead = bis.read(payload, 0, DataPacket.MAX_DATA_LENGTH);
			} catch (IOException e) {
				// this should only be called if for some reason the BufferedInputStream was closed somewhere else
				e.printStackTrace();
				return;
			}

			System.out.println("totalRead: " + totalRead);

			if (totalRead == -1) break;

			if ((totalRead > 0) && (totalRead < DataPacket.MAX_DATA_LENGTH)) {

				System.out.println("Found a packet less then 512, totalRead: " + totalRead);

				//copy the current payload into a smaller payload
				byte[] tmpPayload = new byte[totalRead];
				System.arraycopy(payload, 0, tmpPayload, 0, totalRead);
				//now set it to be payload
				payload = tmpPayload;

				System.out.println("Payload length < : " + payload.length);

			}
			payloads.add(payload);
		}

		System.out.println("Number of packets for server to send: " + payloads.size());

		//get the clients information to send them back the data
		int clientPort = packet.getPort();
		InetAddress clientAddress = packet.getAddress();

		//once we have the file send back the first data packet for the file

		//keep track of the number of retries
		int retries = 5;

		//holds the current block number, so we know the offset to read from
		for (int blockNumber = 0; blockNumber < payloads.size();) {
			//create the data packet to send back			
			DataPacket returnPacket = new DataPacket(clientAddress, clientPort, DataPacket.TYPE, blockNumber, payloads
					.get(blockNumber));

			byte[] tmp = payloads.get(blockNumber);
			System.out.println("tmpLength: " + tmp.length);
			System.out.println("Server sending packet: " + blockNumber);
			System.out.println("Payload size: " + returnPacket.getPayloadLength());
			//now send the packet back
			try {
				tftp.send(returnPacket);
			} catch (IOException e) {
				// there was some IO problem and the packet didn't send
				e.printStackTrace();
			}

			//when we get the ack for the file send the next data packet based on the ack blockNumber
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
			} else {
				System.out.println("We did not get an ack back like we were supposed to");
				//TODO: should probably check to see if its and error packet and do something
				retries--;
				if (retries < 1) {
					System.out.println("Exceeded the number of retries");
					break;
				}
			}
		}
		System.out.println("Finished sending the data");
		//we are out of the loop so the file must have been sent or it timed out so we can just clean up
		tftp.destroy();
	}
}
