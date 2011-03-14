package project;

import head.ClientConnection;
import head.ClientManager;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import packet.FullCircleTask;
import packet.Packet;
import packet.ResultsPacket;

public class Tree implements Runnable {

	final int rows;
	final int columns;
	GUI gui;
	final long[][] cellsA;
	final long[][] cellsB;
	final ClientManager clientManager;

	public Tree(GUI gui, int columns, int rows, ClientManager clientManager) {
		this.clientManager = clientManager;
		this.gui = gui;
		this.rows = rows;
		this.columns = columns;
		cellsA = new long[rows][columns];
		cellsB = new long[rows][columns];
		updateGUI(cellsA);
	}

	@Override
	public void run() {
		boolean readA = true;
		final double[] metalConstants = new double[] { 1.0, 1.0, 1.0 };
		cellsA[0][0] = Long.MAX_VALUE >> 2;
		cellsA[rows - 1][columns - 1] = Long.MAX_VALUE >> 2;
		cellsB[0][0] = Long.MAX_VALUE >> 2;
		cellsB[rows - 1][columns - 1] = Long.MAX_VALUE >> 2;

		final int[][][] percentageOfMetals = new int[rows][columns][3];
		final int rowMin = 0;
		final int columnMin = 0;
		final int rowMax = rows - 1;
		final int columnMax = columns - 1;

		generateRandomPercentageOfMetals(percentageOfMetals);
		// ForkJoinPool pool = new ForkJoinPool();
		// for (;;) {
		// pool.invoke(new Node(cellsA, cellsB, 0, 0, (rows - 1), (columns - 1),
		// readA, metalConstants,
		// percentageOfMetals));
		// if (readA) {
		// updateGUI(cellsB);
		// readA = false;
		// } else {
		// updateGUI(cellsA);
		// readA = true;
		// }
		// }
		// split up the data to the two servers
		// Collection<ClientConnection> c = clientManager.getClients().values();
		// c.size();
		// long[][] readFrom;
		// if(readA){
		// readFrom = cellsA;
		// }else{
		// readFrom = cellsB;
		// }

		// check to see if there are any servers that can do this work for us
		// if there is then set do self to be false and send them the work
		if (clientManager.getClients().size() == 2) {
			// split the work up to the two clients
			Collection<ClientConnection> clients = clientManager.getClients().values();
			ClientConnection left = null;
			ClientConnection right = null;
			for (ClientConnection c : clients) {
				if (left == null) {
					left = c;
				} else if (right == null) {
					right = c;
				}
			}
			// split the data in half
			// left
			int rowMinLeft = rowMin;
			int rowMaxLeft = rowMax;
			int columnMinLeft = columnMin;
			double half = ((columnMax + columnMin) / 2.0);
			int columnMaxLeft = (int) Math.floor(half);
			// right
			int rowMinRight = rowMin;
			int rowMaxRight = rowMax;
			int columnMinRight = (int) Math.ceil(half);
			int columnMaxRight = columnMax;

			// create a new Packet to send to the client
			// it will need everything at first to create the initial tree on
			// its end
			// create the left
			//send the initial all data, then from there out only send the edges

			//every 30 iterations update all data

			boolean firstTime = true;
			for (int itr = 0; itr < 30000; itr++) {
				long[] leftEastEdgeData = getEastEdgeData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft,
						useA(readA));
				long[] leftWestEdgeData = getWestEdgeData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft,
						useA(readA));

				// create the right
				long[] rightEastEdgeData = getEastEdgeData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight,
						useA(readA));
				long[] rightWestEdgeData = getWestEdgeData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight,
						useA(readA));

				//if this is the very first time sending anything send them all of it
				Packet leftPacket = null;
				Packet rightPacket = null;
				if (firstTime) {
					long[][] leftData = getCenterData(rowMinLeft, columnMinLeft, rowMaxLeft, columnMaxLeft, useA(readA));
					long[][] rightData = getCenterData(rowMinRight, columnMinRight, rowMaxRight, columnMaxRight,
							useA(readA));
					leftPacket = new Packet(rows, columns, rowMinLeft, rowMaxLeft, columnMinLeft, columnMaxLeft,
							leftData, leftEastEdgeData, leftWestEdgeData, metalConstants, percentageOfMetals);
					rightPacket = new Packet(rows, columns, rowMinRight, rowMaxRight, columnMinRight, columnMaxRight,
							rightData, rightEastEdgeData, rightWestEdgeData, metalConstants, percentageOfMetals);
				} else {
					boolean giveBackAllData = ((itr & 100) == 100);
					//only send the edges
					//every 30 iterations ask for all our data back
					//(boolean giveBackAllData, int rowMin, int rowMax, int columnMin, int columnMax, long[] eastEdgeData,	long[] westEdgeData)
					//rows, columns, rowMinLeft, rowMaxLeft, columnMinLeft, columnMaxLeft
					leftPacket = new Packet(giveBackAllData, rowMinLeft, rowMaxLeft, columnMinLeft, columnMaxLeft,
							leftEastEdgeData, leftWestEdgeData);
					rightPacket = new Packet(giveBackAllData, rowMinRight, rowMaxRight, columnMinRight, columnMaxRight,
							rightEastEdgeData, rightWestEdgeData);
				}

				////////////////////// send the left packet to one server
				// send the right packet to another server
				FullCircleTask leftFullCircleTask = new FullCircleTask(leftPacket, left.getSocket());
				FullCircleTask rightFullCircleTask = new FullCircleTask(rightPacket, right.getSocket());
				leftFullCircleTask.start();
				rightFullCircleTask.start();
				try {
					leftFullCircleTask.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					rightFullCircleTask.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/////////////////////// when they are finished running we just have to update our data
				// now with the data they sent us back
				ResultsPacket leftResult = leftFullCircleTask.getResultsPacket();
				ResultsPacket rightResult = rightFullCircleTask.getResultsPacket();

				// we need to check to see if they sent back all their data
				if (leftResult.isSendingAllData()) {
					// they sent all their data back so now we must update all our
					// data with theirs
					setCenterData(leftResult.getRowMin(), leftResult.getColumnMin(), leftResult.getRowMax(), leftResult
							.getColumnMax(), leftResult.getData(), useA(!readA));
				} else {
					//the only sent the edges
					//setWestEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write)
					//setEastEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write)
					this.setWestEdgeData(leftResult.getRowMin(), leftResult.getColumnMin(), leftResult.getRowMax(),
							leftResult.getColumnMax(), leftResult.getWestEdgeData(), useA(!readA));
					this.setEastEdgeData(leftResult.getRowMin(), leftResult.getColumnMin(), leftResult.getRowMax(),
							leftResult.getColumnMax(), leftResult.getEastEdgeData(), useA(!readA));
				}
				// do the same thing for the data returned from the right
				if (rightResult.isSendingAllData()) {
					setCenterData(rightResult.getRowMin(), rightResult.getColumnMin(), rightResult.getRowMax(),
							rightResult.getColumnMax(), rightResult.getData(), useA(!readA));
				} else {
					//they only sent the edges
					this.setWestEdgeData(rightResult.getRowMin(), rightResult.getColumnMin(), rightResult.getRowMax(),
							rightResult.getColumnMax(), rightResult.getWestEdgeData(), useA(!readA));
					this.setEastEdgeData(rightResult.getRowMin(), rightResult.getColumnMin(), rightResult.getRowMax(),
							rightResult.getColumnMax(), rightResult.getEastEdgeData(), useA(!readA));
				}

				////////////////////// update the gui
				updateGUI(useA(!readA));
				if (readA) {
					//updateGUI(cellsB);
					readA = false;
				} else {
					readA = true;
				}

			}
		}

	}

	public long[][] useA(boolean b) {
		if (b) {
			return cellsA;
		} else {
			return cellsB;
		}
	}

	//0-7   8-15
	//0-3   0-3
	// 8     8
	//                             0             8             3            15          smaller         larger
	public void setCenterData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] read, long[][] write) {
		for (int a = rowMin; a < (rowMax + 1); a++) {
			System.arraycopy(read[a], 0, write[a], columnMin, (columnMax - columnMin) + 1);
		}
	}

	public long[][] getCenterData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
		long[][] centerData = new long[((rowMax - rowMin) + 1)][((columnMax - columnMin) + 1)];
		for (int a = rowMin; a < (rowMax + 1); a++) {
			System.arraycopy(data[a], columnMin, centerData[a], 0, (columnMax - columnMin) + 1);
		}
		return centerData;
	}

	public void setWestEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write) {
		// set the west edge
		if ((columnMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");
		if ((rowMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");

		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			write[a][(columnMin - 1)] = read[ptr];
			ptr++;
		}
	}

	public long[] getWestEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
		// we can't get the leftChunk because it doesn't exist
		if ((columnMin - 1) < 0) return null;
		if ((rowMin - 1) < 0) return null;

		long[] leftData = new long[((rowMax - rowMin) + 1)];
		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			leftData[ptr] = data[a][(columnMin - 1)];
			ptr++;
		}
		return leftData;
	}

	public void setEastEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[] read, long[][] write) {
		// we can't get the rightChunk if it doesn't exist
		if ((columnMax + 1) > (write[0].length - 1))
			System.err.println("trying to set a west edge that doesn't exist");
		if ((rowMin - 1) < 0) System.err.println("trying to set a west edge that doesn't exist");

		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			write[a][(columnMax + 1)] = read[ptr];
			ptr++;
		}
	}

	public long[] getEastEdgeData(int rowMin, int columnMin, int rowMax, int columnMax, long[][] data) {
		// we can't get the rightChunk if it doesn't exist
		if ((columnMax + 1) > (data[0].length - 1)) return null;
		if ((rowMin - 1) < 0) return null;

		long[] rightData = new long[((rowMax - rowMin) + 1)];
		int ptr = 0;
		for (int a = rowMin; a < (rowMax + 1); a++) {
			rightData[ptr] = data[a][(columnMax + 1)];
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

	public void updateGUI(final long[][] pixels) {
		// Thread t = new Thread(new Runnable(){
		// public void run(){
		// gui.pixelCanvas.setPixels(pixels);
		// //for(;;){
		// //update the gui with the right array
		// gui.pixelCanvas.repaint();
		// //}
		// }
		// });

		// if (gui.pixelCanvas.pixelsA == null) {

		// }

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				gui.pixelCanvas.setPixels(pixels);
				// for (;;) {
				// gui.pixelCanvas.setPixels(pixels);
				// gui.pixelCanvas.setAllPixelsRandom();
				gui.pixelCanvas.repaint();
				// }
			}
		});

	}

}
