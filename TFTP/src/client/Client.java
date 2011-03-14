package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

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
		InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
		int addressPort = 69;
		String urlRequest = "test.jpg";
		boolean finished = true;

		//File to write to
		File save = new File("REC" + urlRequest);

		//a stream to hold the data
		FileOutputStream fos = new FileOutputStream(save);

		//Create a new TFTPInterface
		TFTPInterface tftp = new TFTPInterface();

		//create the request packet		
		ReadRequestPacket requestPacket = new ReadRequestPacket(serverAddress, addressPort, ReadRequestPacket.TYPE,
				urlRequest);

		//send a request
		tftp.send(requestPacket);

		//keep track of the last packet size
		int lastDataSize = 0;
		
		//keep track of the number of packets received, used for debugging
		int numReceived = -1;

		do {
			System.out.println("getting the data packet back");
			//get the request response (should be a data packet)		
			TFTPPacket dPacket = tftp.receive();

			//this is used for debugging
			numReceived++;
			System.out.println("numReceived: " + numReceived);
			
			//save the data if its a data packet
			if (dPacket.getType() == DataPacket.TYPE) {
				DataPacket dataPacket = (DataPacket) dPacket;
				
				//set the size of its payload
				lastDataSize = dataPacket.getPayloadLength();
				
				//write the payload of the packet to the stream
				fos.write(dataPacket.getPayload());

				//get the return info
				InetAddress returnServerAddress = dataPacket.getAddress();
				int port = dataPacket.getPort();
				int blockNumber = dataPacket.getBlockNumber();

				//create the ack packet
				AckPacket ackPacket = new AckPacket(returnServerAddress, port, AckPacket.TYPE, blockNumber);

				//send back an ack of the block number
				tftp.send(ackPacket);

			} else {
				System.out.println("Something went wrong. Supposed to get a data packet but didn't");
				finished = false;
				break;
			}
			System.out.println("lastDataSize: " + lastDataSize);
		} while (lastDataSize >= DataPacket.MAX_DATA_LENGTH);
		
		if(finished){
			//flush the stream
			fos.flush();
			System.out.println("Done!");
		}else{
			System.out.println("Didn't finish!");
		}

	}

}
