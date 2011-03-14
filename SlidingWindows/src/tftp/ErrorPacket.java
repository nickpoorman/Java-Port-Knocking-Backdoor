package tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ErrorPacket extends TFTPPacket {
	
	/** The type of this packet **/
	public static final int TYPE = 5;
	
	/** Error codes **/
	public static final int NOT_DEFINED = 0;
	public static final int FILE_NOT_FOUND = 1;
	public static final int ACCESS_VIOLATION = 2;
	public static final int DISK_FULL = 3;
	public static final int ILLEGAL_OPERATION = 4;
	public static final int UNKNOWN_TID = 5;
	public static final int FILE_EXISTS = 6;
	public static final int NO_SUCH_USER = 7;
	
	/** The error code for the packet **/
	private int errorCode;
	
	/** The message to go with the error code **/
	private String message;
	

	public ErrorPacket(InetAddress address, int port, int type, int errorCode, String message) {
		super(address, port, type);
		
		//set the errorCode
		this.errorCode = errorCode;
		
		//set the message for the error
		this.message = message;
	}
	
	public ErrorPacket(DatagramPacket packet, int type) {
		super(packet, type);
		
		byte[] data = packet.getData();
		
		//start 2 bytes after the opcode and get the error code (its split into two bytes)
		this.errorCode = (((data[2] & 0xff) << 8) | (data[3] & 0xff));
		
		//store the error message as its bytes are read in into the buffer
		StringBuffer buffer = new StringBuffer();
		
		//now get the error message 4 bytes offset
		for(int i = 4; i < packet.getLength(); i++){
			//we want to stop when we hit a 0 byte
			if(data[i] == 0) break;
			buffer.append((char)data[i]);
		}
		
		this.message = buffer.toString();
	}

	/**
	 * This method will create a new DatagramPacket from itself (ErrorPacket)
	 * @return DatagramPacket
	 */
	@Override
	public DatagramPacket getDatagramPacket() {
		byte[] data = new byte[4 + message.length() + 1];
		
		//set the opcode
		data[0] = 0;
		data[1] = (byte)this.getType();
		
		//set the error code
		data[2] = (byte)((this.errorCode & 0xffff) >> 8);
        data[3] = (byte)(this.errorCode & 0xff);
        
        //set the error message
        System.arraycopy(message.getBytes(), 0, data, 4, message.getBytes().length);
		
		return new DatagramPacket(data, data.length, this.getAddress(), this.getPort());
	}

}















