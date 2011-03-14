package tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public abstract class TFTPPacket {

	public static final int READ_REQUEST = 1;

	public static final int DATA = 3;

	public static final int ACKNOWLEDGEMENT = 4;

	public static final int ERROR = 5;
	
	public static final int DEFAULT_PACKET_SIZE = 516;

	/** The type of packet **/
	private int type;

	/** The port for the packet **/
	private int port;

	/** The address for the packet **/
	private InetAddress address;
	
	public TFTPPacket(InetAddress address, int port, int type){
		this.type = type;
		this.port = port;
		this.address = address;
	}
	
	public TFTPPacket(DatagramPacket packet, int type){
		this.type = type;
		this.port = packet.getPort();
		this.address = packet.getAddress();
	}
	
	/**
	 * Factory method to create the type of packet
	 */
	public final static TFTPPacket packetFactory(DatagramPacket packet){		
		TFTPPacket tmpPacket = null;
		
		//get the data out of the packet
		byte[] data = packet.getData();
		
		//get the type
		byte type = data[1];
		if(type == READ_REQUEST){
			tmpPacket = new ReadRequestPacket(packet, type);
		}else if(type == DATA){
			tmpPacket = new DataPacket(packet, type);
		}else if(type == ACKNOWLEDGEMENT){
			tmpPacket = new AckPacket(packet, type);
		}else if(type == ERROR){
			tmpPacket = new ErrorPacket(packet, type);
		}
		
		return tmpPacket;
	}
	
	/**
	 * This method will create a new DatagramPacket from itself (E)
	 * @return DatagramPacket
	 */
	public abstract DatagramPacket getDatagramPacket();

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

}
