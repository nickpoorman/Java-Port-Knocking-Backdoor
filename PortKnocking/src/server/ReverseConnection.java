package server;

import java.net.InetAddress;

public class ReverseConnection implements Runnable {
	
	private final InetAddress source;
	
	public ReverseConnection(InetAddress source){
		this.source = source;
	}
	
	public void run(){
		// start the reverse connection
		System.out.println("Started the reverse connection thread");
	}

}
