package tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class DataPacket extends TFTPPacket {
	
	/** The type of this packet **/
	public static final int TYPE = 3;
	
    /** The largest data packet size = 512 **/
    public static final int MAX_DATA_LENGTH = 512;
    
    /** The smallest data packet size = 0 **/
    public static final int MIN_DATA_LENGTH = 0;
    
    /** The largest block number size = 65536**/
    public static final int MAX_BLOCK_NUMBER = ((1 << 16) - 1);
    
    /** The data to be sent in the data section of the packet **/
    private byte[] payload;
    
    /** The block number for the packet **/
    private int blockNumber;

	/** The length of the payload **/
    private int payloadLength;

	public DataPacket(InetAddress address, int port, int type, int blockNumber, byte[] payload) {
		super(address, port, type);	
		
		//set the data to be sent in the data section of the packet
		this.payload = payload;
		
		//set the block number for the packet
		this.blockNumber = blockNumber;
		
		//set the payload length
		this.payloadLength = payload.length;
	}
	
	public DataPacket(DatagramPacket packet, int type){
		super(packet, type);
		
		byte[] data = packet.getData();
		
		//start 2 bytes after the opcode and get the block number
		this.blockNumber = (((data[2] & 0xff) << 8) | (data[3] & 0xff));
		
		//set the payload length
		this.payloadLength = packet.getLength() - 4;
				
		this.payload = new byte[this.payloadLength];
		
		//copy the data from the packet into the payload array
		System.arraycopy(data, 4, this.payload, 0, this.payloadLength);
	}
	
	/**
	 * This method will create a new DatagramPacket from itself (DataPacket)
	 * @return DatagramPacket
	 */
	@Override
	public DatagramPacket getDatagramPacket(){
		byte[] data = new byte[this.payloadLength + 4];
		
		//set the opcode
		data[0] = 0;
		data[1] = (byte)this.getType();
		
		//set the block number (we have to split the number up into 2 bytes)
		data[2] = (byte)((this.blockNumber & 0xffff) >> 8);
        data[3] = (byte)(this.blockNumber & 0xff);
        
        //now set the payload
        System.arraycopy(payload, 0, data, 4, this.payloadLength);
		
        return new DatagramPacket(data, data.length, this.getAddress(), this.getPort());
	}
	
	/**
	 * @return the payload
	 */
	public byte[] getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	/**
	 * @return the blockNumber
	 */
	public int getBlockNumber() {
		return blockNumber;
	}

	/**
	 * @param blockNumber the blockNumber to set
	 */
	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}

	/**
	 * @return the payloadLength
	 */
	public int getPayloadLength() {
		return payloadLength;
	}

	/**
	 * @param payloadLength the payloadLength to set
	 */
	public void setPayloadLength(int payloadLength) {
		this.payloadLength = payloadLength;
	}
	
}
