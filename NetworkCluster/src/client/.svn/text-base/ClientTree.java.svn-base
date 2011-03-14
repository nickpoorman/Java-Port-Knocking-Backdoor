package client;

import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import packet.Packet;
import project.Node;

public class ClientTree{

	//final int rows;
	//final int columns;
	//GUI gui;
	final long[][] cellsA;
	final long[][] cellsB;
	final Socket socket;
	final int rowMin;
	final int rowMax;
	final int columnMin;
	final int columnMax;
	final double[] metalConstants;
	final int[][][] percentageOfMetals;
	final int totalRowLength;
	final int totalColumnLength;
	final int rowOffset;
	final int columnOffset;
	AtomicBoolean readA;
	final long[] ghostEastEdgeData;
	final long[] ghostWestEdgeData;

	public ClientTree(Packet packet, Socket socket) {
		this.socket = socket;
		//first get the two array's by cloning the first one
		this.cellsA = packet.getData().clone();
		this.cellsB = packet.getData().clone();
		//set the array constraints
		rowMin = packet.getRowMin();
		rowMax = packet.getRowMax();
		columnMin = packet.getColumnMin();
		columnMax = packet.getColumnMax();
		metalConstants = packet.getMetalConstants();
		percentageOfMetals = packet.getPercentageOfMetals();
		this.totalRowLength = packet.getTotalRowLength();
		this.totalColumnLength = packet.getTotalColumnLength();
		//we have to figure out our own offset from 0,0
		this.rowOffset = (0 - rowMin);
		this.columnOffset = (0 - columnMin);
		System.out.println("rowMin is: " + rowMin);
		System.out.println("rowMax is: " + rowMax);
		System.out.println("columnMin is: " + columnMin);
		System.out.println("columnMax is: " + columnMax);
		readA = new AtomicBoolean(true);
		//this.rows = rows;
		//this.columns = columns;
		//cellsA = new long[rows][columns];
		//cellsB = new long[rows][columns];
		//updateGUI(cellsA);
		ghostEastEdgeData = packet.getEastEdgeData();
		ghostWestEdgeData = packet.getWestEdgeData();
	}

	//@Override
	//public void run() {
	public void doIteration() {
		//boolean readA = true;
		//		final double[] metalConstants = new double[] { 1.0, 1.0, 1.0 };
		//		cellsA[0][0] = Long.MAX_VALUE >> 2;
		//		cellsA[rows - 1][columns - 1] = Long.MAX_VALUE >> 2;
		//		cellsB[0][0] = Long.MAX_VALUE >> 2;
		//		cellsB[rows - 1][columns - 1] = Long.MAX_VALUE >> 2;
		//
		//		final int[][][] percentageOfMetals = new int[rows][columns][3];
		//		final int rowMin = 0;
		//		final int columnMin = 0;
		//		final int rowMax = rows - 1;
		//		final int columnMax = columns - 1;
		//
		//		generateRandomPercentageOfMetals(percentageOfMetals);

		ForkJoinPool pool = new ForkJoinPool();

		pool.invoke(new Node(cellsA, cellsB, totalRowLength, totalColumnLength, rowMin, columnMin, rowMax,
				columnMax, readA.get(), metalConstants, percentageOfMetals, rowOffset, columnOffset, ghostEastEdgeData, ghostWestEdgeData));
		if (readA.get()) {
			//				updateGUI(cellsB);
			readA.set(false);
		} else {
			//				updateGUI(cellsA);
			readA.set(true);
		}

		//split up the data to the two servers
		//		long[][] readFrom;
		//		if (readA) {
		//			readFrom = cellsA;
		//		} else {
		//			readFrom = cellsB;
		//		}

		// check to see if there are any servers that can do this work for us
		// if there is then set do self to be false and send them the work
		//		if (clientManager.getClients().size() == 2) {
		//			// split the work up to the two clients
		//			Collection<ClientConnection> clients = clientManager.getClients().values();
		//			ClientConnection left = null;
		//			ClientConnection right = null;
		//			for (ClientConnection c : clients) {
		//				if (left == null) {
		//					left = c;
		//				} else if (right == null) {
		//					right = c;
		//				}
		//			}
		//			// split the data in half
		//			// left
		//			int rowMinLeft = rowMin;
		//			int rowMaxLeft = rowMax;
		//			int columnMinLeft = columnMin;
		//			double half = ((columnMax + columnMin) / 2.0);
		//			int columnMaxLeft = (int) Math.floor(half);
		//			// right
		//			int rowMinRight = rowMin;
		//			int rowMaxRight = rowMax;
		//			int columnMinRight = (int) Math.ceil(half);
		//			int columnMaxRight = columnMax;
		//
		//			// create a new Packet to send to the client
		//			// it will need everything at first to create the initial tree on
		//			// its end
		//			// create the left
		//			long[][] leftData = getCenterData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft, cellsA);
		//			long[] leftEastEdgeData = getEastEdgeData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft, cellsA);
		//			long[] leftWestEdgeData = getWestEdgeData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft, cellsA);
		//			Packet leftPacket = new Packet(true, true, rowMinLeft, rowMaxLeft, columnMinLeft, columnMaxLeft, leftData,
		//					leftEastEdgeData, leftWestEdgeData);
		//
		//			// create the right
		//			long[][] rightData = getCenterData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight, cellsA);
		//			long[] rightEastEdgeData = getEastEdgeData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight, cellsA);
		//			long[] rightWestEdgeData = getWestEdgeData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight, cellsA);
		//			Packet rightPacket = new Packet(true, true, rowMinRight, rowMaxRight, columnMinRight, columnMaxRight,
		//					rightData, rightEastEdgeData, rightWestEdgeData);
		//
		//			// send the left packet to one server
		//			// send the right packet to another server
		//			FullCircleTask leftFullCircleTask = new FullCircleTask(leftPacket, left.getSocket());
		//			FullCircleTask rightFullCircleTask = new FullCircleTask(rightPacket, right.getSocket());
		//			leftFullCircleTask.start();
		//			rightFullCircleTask.start();
		//			try {
		//				leftFullCircleTask.join();
		//			} catch (InterruptedException e) {
		//				e.printStackTrace();
		//			}
		//			try {
		//				rightFullCircleTask.join();
		//			} catch (InterruptedException e) {
		//				e.printStackTrace();
		//			}
		//			// when they are finished running we just have to update our data
		//			// now with the data they sent us back
		//			Packet leftResult = leftFullCircleTask.getResult();
		//			Packet rightResult = rightFullCircleTask.getResult();
		//
		//			// we need to check to see if they sent back all their data
		//			if (leftResult.isSendingAllData()) {
		//				// they sent all their data back so now we must update all our
		//				// data with theirs
		//				setCenterData(leftResult.getRowMin(), leftResult.getColumnMin(), leftResult.getRowMax(), leftResult
		//						.getColumnMax(), leftResult.getData(), cellsA);
		//			}
		//			// do the same thing for the data returned from the right
		//			if (rightResult.isSendingAllData()) {
		//				setCenterData(rightResult.getRowMin(), rightResult.getColumnMin(), rightResult.getRowMax(), rightResult
		//						.getColumnMax(), rightResult.getData(), cellsA);
		//			}
		//			// update the gui
		//			// if (readA) {
		//			// updateGUI(cellsB);
		//			// readA = false;
		//			// } else {
		//			updateGUI(cellsA);
		//			// readA = true;
		//			// }
		//
		//		}

	}
	
	public long[][] lastWroteTo(){
		if (readA.get()) {
			return cellsA;
		} else {
			return cellsB;
		}
	}

//	public void setCenterData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] read, long[][] write) {
//		for (int a = rowMin; a < (rowMax + 1); a++) {
//			System.arraycopy(read[a], 0, write[a], columnMin, (columnMax - columnMin) + 1);
//		}
//	}
//
//	public long[][] getCenterData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
//		long[][] centerData = new long[((rowMax - rowMin) + 1)][((columnMax - columnMin) + 1)];
//		for (int a = rowMin; a < (rowMax + 1); a++) {
//			System.arraycopy(data[a], columnMin, centerData[a], 0, (columnMax - columnMin) + 1);
//		}
//		return centerData;
//	}

	public void setWestEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write) {
		// set the west edge
//		if ((columnMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");
//		if ((rowMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");

		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			write[a][(columnMin)] = read[ptr];
			ptr++;
		}
	}
	
	public long[] getThisWestEdge(){
		return getWestEdgeData(this.rowMin, this.columnMin, this.rowMax, this.columnMax, getResults());
	}

	public long[] getWestEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
		// we can't get the leftChunk because it doesn't exist
//		if ((columnMin - 1) < 0) return null;
//		if ((rowMin - 1) < 0) return null;

		long[] leftData = new long[((rowMax - rowMin) + 1)];
		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			leftData[ptr] = data[a][(columnMin)];
			ptr++;
		}
		return leftData;
	}

	
	
	public void setEastEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write) {
		// we can't get the rightChunk if it doesn't exist
//		if ((columnMax + 1) > (write[0].length - 1))
//			System.err.println("trying to set a west edge that doesn't exist");
//		if ((rowMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");

		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			write[a][(columnMax)] = read[ptr];
			ptr++;
		}
	}

	public long[] getThisEastEdge(){
		return getEastEdgeData(this.rowMin, this.columnMin, this.rowMax, this.columnMax, getResults());
	}
	
	public long[] getEastEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
		// we can't get the rightChunk if it doesn't exist
//		if ((columnMax + 1) > (data[0].length - 1)) return null;
//		if ((rowMin - 1) < 0) return null;
		long[] rightData = new long[((rowMax - rowMin) + 1)];
		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			rightData[ptr] = data[a][(columnMax)];
			ptr++;
		}
		return rightData;
	}

	public static void setInitialTemp() {
		// long temp = (Long.MAX_VALUE - 1000);
		// long temp = 0;
		// for (int row = 0; row < cells.length; row++) {
		// for (int column = 0; column < cells[0].length; column++) {
		// cells[row][column] = temp;
		// }
		// }

	}

	public static void generateRandomPercentageOfMetals(int[][][] percentageOfMetals) {
		Random r = new Random();

		for (int row = 0; row < percentageOfMetals.length; row++) {
			for (int column = 0; column < percentageOfMetals[0].length; column++) {
				// System.out.println("generating percentage");
				int totalPercentLeft = 100;
				for (int a = 0; a < percentageOfMetals[0][0].length; a++) {

					// percentageOfMetals[row][column][a] = 33;

					if (a == 0) {
						int d = r.nextInt(101);
						percentageOfMetals[row][column][a] = d;
						totalPercentLeft = totalPercentLeft - d;
						// System.out.println("end 1st: " + d);
					} else if (a == 1) {
						// int d = 200;
						// while (d > totalPercentLeft) {
						int d = r.nextInt(totalPercentLeft + 1);
						// }
						percentageOfMetals[row][column][a] = d;
						totalPercentLeft = totalPercentLeft - d;
						// System.out.println("end 2nd: " + d);
					} else {
						percentageOfMetals[row][column][a] = totalPercentLeft;
						// System.out.println("end 3rd: " + totalPercentLeft);
					}
				}
			}
		}
	}

	/**
	 * @return the rowMin
	 */
	public int getRowMin() {
		return rowMin;
	}

	/**
	 * @return the rowMax
	 */
	public int getRowMax() {
		return rowMax;
	}

	/**
	 * @return the columnMin
	 */
	public int getColumnMin() {
		return columnMin;
	}

	/**
	 * @return the columnMax
	 */
	public int getColumnMax() {
		return columnMax;
	}

	// public void updateGUI(final long[][] pixels) {
	// // Thread t = new Thread(new Runnable(){
	// // public void run(){
	// // gui.pixelCanvas.setPixels(pixels);
	// // //for(;;){
	// // //update the gui with the right array
	// // gui.pixelCanvas.repaint();
	// // //}
	// // }
	// // });
	//
	// if (gui.pixelCanvas.pixelsA == null) {
	// gui.pixelCanvas.setPixels(pixels);
	// }
	//
	// javax.swing.SwingUtilities.invokeLater(new Runnable() {
	//
	// public void run() {
	// // for (;;) {
	// // gui.pixelCanvas.setPixels(pixels);
	// // gui.pixelCanvas.setAllPixelsRandom();
	// gui.pixelCanvas.repaint();
	// // }
	// }
	// });
	//
	// }

	//	public void updateGUI(final long[][] pixels) {
	//		// Thread t = new Thread(new Runnable(){
	//		// public void run(){
	//		// gui.pixelCanvas.setPixels(pixels);
	//		// //for(;;){
	//		// //update the gui with the right array
	//		// gui.pixelCanvas.repaint();
	//		// //}
	//		// }
	//		// });
	//
	//		// if (gui.pixelCanvas.pixelsA == null) {
	//
	//		// }
	//
	//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	//
	//			public void run() {
	//				gui.pixelCanvas.setPixels(pixels);
	//				// for (;;) {
	//				// gui.pixelCanvas.setPixels(pixels);
	//				// gui.pixelCanvas.setAllPixelsRandom();
	//				gui.pixelCanvas.repaint();
	//				// }
	//			}
	//		});
	//
	//	}
	
	public long[][] getResults(){
		return lastWroteTo();
	}

}
