package tftp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TFTPInterface {
	
	DatagramSocket socket;	
	
	public TFTPInterface() throws SocketException{
		socket = new DatagramSocket();
	}
	
	public TFTPInterface(int port) throws SocketException{
		socket = new DatagramSocket(port);
	}
	
	public TFTPPacket receive() throws IOException{
		//create a new DatagramPacket to hold the data
		DatagramPacket dpacket = new DatagramPacket(new byte[TFTPPacket.DEFAULT_PACKET_SIZE], TFTPPacket.DEFAULT_PACKET_SIZE);
		
		//get the datagram packet from the socket
		socket.receive(dpacket);
		
		//using the factory method create the new TFTPPacket and return it
		return TFTPPacket.packetFactory(dpacket);
	}
	
	public void send(TFTPPacket packet) throws IOException{
		socket.send(packet.getDatagramPacket());
	}
	
	public void destroy(){
		socket.close();
	}

}
