package server;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineManager {

	private ConcurrentHashMap<Integer, KnockStateMachine> machines;

	private static StateMachineManager _instance;

	public StateMachineManager() {
		this.machines = new ConcurrentHashMap<Integer, KnockStateMachine>();
	}

	public KnockStateMachine addMachineIfNotExists(int sourceInt, byte[] sourceArray, List<Integer> ports) throws UnknownHostException {
		if (!this.machines.containsKey(new Integer(sourceInt))) {
			KnockStateMachine tmpKSM = new KnockStateMachine(sourceArray, ports);
			this.machines.put(new Integer(sourceInt), tmpKSM);
			return tmpKSM;
		}
		return this.machines.get(new Integer(sourceInt));
	}

	public void getMachine(int host) {
		this.machines.get(new Integer(host));
	}

	public static synchronized StateMachineManager getInstance() {
		if (_instance == null) {
			_instance = new StateMachineManager();
		}
		return _instance;
	}

}
