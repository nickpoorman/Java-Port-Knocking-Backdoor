package tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ReadRequestPacket extends TFTPPacket {
	
	/** The type of this packet **/
	public static final int TYPE = 1;
	
	/** holds the url to be requested **/
	private String url;	

	/** mode will hold our octet mode for us since its 
	 * the only mode we need to support for this assignment **/
	private byte[] modeArray = { (byte)'o', (byte)'c', (byte)'t', (byte)'e', (byte)'t',};
	private String mode;

	public ReadRequestPacket(InetAddress address, int port, int type, String url) {
		super(address, port, type);
		//set the url to be requested
		this.url = url;
	}
	
	public ReadRequestPacket(DatagramPacket packet, int type) {
		super(packet, type);
		
		byte[] data = packet.getData();
		
		//holds where the mode is starting
		int modeOffset = -1;
		
		//read in the urlRequest into a stringbuffer
		StringBuffer buffer = new StringBuffer();
		
		//start after the 2 bytes opcode and read in the url
		for(int i = 2; i < packet.getLength(); i++){
			//we want to stop when we hit the 0
			if(data[i] == 0){
				modeOffset = i + 1;
				break;
			}
			buffer.append((char)data[i]);
		}
		
		//set the url from the StringBuffer
		url = buffer.toString();
		
		if(modeOffset == -1){
			System.out.println("Incorrect Format!");
		}
		
		buffer = new StringBuffer();
		for(int i = modeOffset; i < packet.getLength(); i++){
			//same thing, read in until we hit the 0
			if(data[i] == 0) break;
			buffer.append((char)data[i]);
		}
		
		//now we have the mode even though we really don't need it for this assignment
		this.setMode(buffer.toString());
	}
	
	/**
	 * This method will create a new DatagramPacket from itself (ReadRequestPacket)
	 * @return DatagramPacket
	 */
	@Override
	public DatagramPacket getDatagramPacket(){
		//we need to make space for the header 2 bytes + sizeof(url) + 1 + sizeof(mode) + 1
		byte[] data = new byte[this.url.length() + this.modeArray.length + 4];
		
		//we need to set up the data portion of the packet
		
		//first thing in the packet is the opcode
		data[0] = 0;
		data[1] = (byte)this.getType();
		
		//next we need the url
		System.arraycopy(this.url.getBytes(), 0, data, 2, this.url.length());
		
		//append a 0 after the url to finalize the url (+2 for the opcode)
		data[this.url.length() + 2] = 0;
		
		//append the mode (+2 for opcode, +1 for end of url byte)
		System.arraycopy(this.modeArray, 0, data, this.url.length() + 3, this.modeArray.length);
		
		//append the last 0 to finalize the mode
		data[2 + this.url.length() + 1 + this.modeArray.length] = 0;		
		
		return new DatagramPacket(data, data.length, this.getAddress(), this.getPort());
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}	
	
}

















