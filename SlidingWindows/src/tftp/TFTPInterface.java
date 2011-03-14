package tftp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;

public class TFTPInterface {

	DatagramSocket socket;

	public TFTPInterface() throws SocketException {
		//InetAddress addr = InetAddress.getByName(host);
		socket = new DatagramSocket();
	}

	public TFTPInterface(int port, String host) throws SocketException, UnknownHostException {
		socket = new DatagramSocket(port, InetAddress.getByName(host));
	}

	/** This method calls the receive method with an infinite timeout **/
	public TFTPPacket receive() throws IOException {
		return receive(0);
	}

	public TFTPPacket receive(int timeout) throws SocketTimeoutException, IOException {		
		//create a new DatagramPacket to hold the data
		DatagramPacket dpacket = new DatagramPacket(new byte[TFTPPacket.DEFAULT_PACKET_SIZE],
				TFTPPacket.DEFAULT_PACKET_SIZE);

		//set the timeout
		socket.setSoTimeout(timeout);

		//try {
		//get the datagram packet from the socket
		socket.receive(dpacket);
		//} catch (SocketException e) {
		//the receive timed out so we will return null
		//	return null;
		//}

		//set the timeout back to 0 (inf)
		socket.setSoTimeout(0);
	
		//using the factory method create the new TFTPPacket and return it
		return TFTPPacket.packetFactory(dpacket);

	}

	public void send(TFTPPacket packet, boolean useDropSim) throws IOException {
		//to simulate a dropped packet we will simply 1% of the time discard the packet		
		int num = 42;
		int random;
		
		if(useDropSim){
			Random r = new Random();
			random = r.nextInt(100);
		}else{
			random = 1;
		}
		if (num != random) {
			socket.send(packet.getDatagramPacket());
		}else{
			System.out.println("Dropping packet on send");
		}
		
		
	}

	public void destroy() {
		socket.close();
	}

}
