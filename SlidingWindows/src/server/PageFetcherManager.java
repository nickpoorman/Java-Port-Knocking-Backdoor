package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tftp.ReadRequestPacket;

public class PageFetcherManager extends Thread {

	int corePoolSize = 30;
	int maxPoolSize = 2000;
	long keepAliveTime = 10500;
	volatile ThreadPoolExecutor threadPool = null;
	volatile ThreadPoolExecutor priorityThreadPool = null;
	private LinkedBlockingQueue<RequestHandlerThread> queue;
	private LinkedBlockingQueue<RequestHandlerThread> priorityQueue;
	private volatile LinkedBlockingQueue<ReadRequestPacket> packets;
	private volatile LinkedBlockingQueue<ReadRequestPacket> priorityPackets;
	private boolean useSlidingWindow;
	private final boolean useDropSim;

	public PageFetcherManager(boolean useSlidingWindow, boolean useDropSim) {
		this.useSlidingWindow = useSlidingWindow;
		this.useDropSim = useDropSim;
		this.initializeQueues();
		this.initializeExecutor();		
	}

	@SuppressWarnings("unchecked")
	private void initializeExecutor() {
		threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
				(BlockingQueue) getQueue());
		priorityThreadPool = new ThreadPoolExecutor(4, 6, keepAliveTime, TimeUnit.MILLISECONDS,
				(BlockingQueue) getPriorityQueue());
	}

	private void initializeQueues() {
		queue = new LinkedBlockingQueue<RequestHandlerThread>();
		priorityQueue = new LinkedBlockingQueue<RequestHandlerThread>();
		packets = new LinkedBlockingQueue<ReadRequestPacket>();
		priorityPackets = new LinkedBlockingQueue<ReadRequestPacket>();
	}

	public void addToPacketsQueue(ReadRequestPacket packet) {
		try {
			getPackets().put(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToPriorityPacketsQueue(ReadRequestPacket packet) {
		try {
			getPriorityPackets().put(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		for (;;) {
			try {
				if (!this.getPriorityPackets().isEmpty()) {
					priorityThreadPool.submit(new RequestHandlerThread(getPriorityPackets().take(), this.useSlidingWindow, this.useDropSim));
					continue;
				}
			} catch (RejectedExecutionException e) {
				//remove this stack trace if its becomes a problem
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				threadPool.submit(new RequestHandlerThread(getPackets().take(), this.useSlidingWindow, this.useDropSim));
			} catch (RejectedExecutionException e) {
				//remove this stack trace if its becomes a problem
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}		
	}

	public void shutDown() {
		threadPool.shutdown();
	}

	/**
	 * @return the queue
	 */
	public LinkedBlockingQueue<RequestHandlerThread> getQueue() {
		return queue;
	}

	/**
	 * @return the packets
	 */
	public LinkedBlockingQueue<ReadRequestPacket> getPackets() {
		return packets;
	}

	/**
	 * @return the priorityPackets
	 */
	public LinkedBlockingQueue<ReadRequestPacket> getPriorityPackets() {
		return priorityPackets;
	}

	/**
	 * @return the priorityQueue
	 */
	public LinkedBlockingQueue<RequestHandlerThread> getPriorityQueue() {
		return priorityQueue;
	}
}
