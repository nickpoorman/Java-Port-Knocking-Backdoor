package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class KnockStateMachine {

	private AtomicInteger currentState;
	private CopyOnWriteArrayList<Integer> ports;
	private AtomicInteger attempts;
	private final InetAddress source;
	
	public String getPorts(){
		return this.ports.toString();
	}

	//private static KnockStateMachine _instance;

	public KnockStateMachine(byte[] source, List<Integer> portsList) throws UnknownHostException {
		this.currentState = new AtomicInteger(0);
		this.ports = new CopyOnWriteArrayList<Integer>();
		this.attempts = new AtomicInteger(0);
		this.source = InetAddress.getByAddress(source);
		this.setPorts(portsList);
		/*
		this.ports.add(new Integer(8090));
		this.ports.add(new Integer(8091));
		this.ports.add(new Integer(8092));
		this.ports.add(new Integer(8093));
		*/
	}
	
	public void setPorts(List<Integer> p){
		for(Integer i : p){
			this.ports.add(new Integer(i.intValue()));
		}
	}

	public InetAddress getSource() {
		return this.source;
	}

	/*
	 * // For lazy initialization public static synchronized KnockStateMachine
	 * getInstance() { if (_instance == null) { _instance = new
	 * KnockStateMachine(); } return _instance; }
	 */

	public synchronized boolean checkAndIncState(int port) {
		int numOfAttempts = 3;
		if (this.attempts.getAndIncrement() > numOfAttempts) {
			this.attempts.set(0);
			this.currentState.set(0);
			return false;
		}
		// if the current port is the correct one
		if (this.ports.get(this.currentState.get()) == port) {
			// check to see if the sequence is finished
			if ((this.ports.size() - 1) == this.currentState.get()) {
				// first reset the state machine so we can reconnect again
				//TODO: Maybe reset the sequence?
				this.attempts.set(0);
				this.currentState.set(0);				
				return true;
			}
			this.currentState.getAndIncrement();
		}
		return false;
	}

}
