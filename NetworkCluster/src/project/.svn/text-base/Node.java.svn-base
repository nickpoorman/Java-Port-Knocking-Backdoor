package project;

import java.util.concurrent.RecursiveAction;

public class Node extends RecursiveAction {

	final long[][] cellsA;
	final long[][] cellsB;
	final int totalRowLength;
	final int totalColumnLength;
	final int rowMin;
	final int columnMin;
	final int rowMax;
	final int columnMax;
	final boolean readA;
	final double[] metalConstants;
	final int[][][] percentageOfMetals;
	final int rowOffset;
	final int columnOffset;
	final long[] ghostEastEdgeData;
	final long[] ghostWestEdgeData;

	// final double[][] percentageOfMetal2;
	// final double[][] percentageOfMetal3;

	// each node will take in an two dimensional array of data 
	// then the node will check to see if its at its base case leaf or
	// it will attempt to split the data some more and create two new nodes left
	// and right
	public Node(long[][] cellsA, long[][] cellsB, int totalRowLength, int totalColumnLength, int rowMin, int columnMin,
			int rowMax, int columnMax, boolean readA, double[] metalConstants, int[][][] percentageOfMetals,
			int rowOffset, int columnOffset, long[] ghostEastEdgeData, long[] ghostWestEdgeData) {
		this.cellsA = cellsA;
		this.cellsB = cellsB;
		this.totalRowLength = totalRowLength;
		this.totalColumnLength = totalColumnLength;
		this.rowMin = rowMin;
		this.columnMin = columnMin;
		this.rowMax = rowMax;
		this.columnMax = columnMax;
		// System.out.println("Setting rowMin: " + rowMin + " columnMin: " +
		// columnMin + " rowMax: " + rowMax + " columnMax: " + columnMax);
		this.readA = readA;
		this.metalConstants = metalConstants;
		this.percentageOfMetals = percentageOfMetals;
		this.rowOffset = rowOffset;
		this.columnOffset = columnOffset;
		// this.percentageOfMetal2 = percentageOfMetal2;
		// this.percentageOfMetal3 = percentageOfMetal3;
		this.ghostEastEdgeData = ghostEastEdgeData;
		this.ghostWestEdgeData = ghostWestEdgeData;

	}

	protected void compute() {
		if (isLeaf()) {
			// do the leaf compute
			doLeaf();
		} else {
			// do node split
			doNode();
		}
		return;
	}

	private boolean isLeaf() {
		return ((this.columnMax - this.columnMin) < 4);
	}

	private void doLeaf() {
		// if readA == true
		// do the calculation on (read) a and store it into (write) b
		// else
		// do the calculation on (read) b and store it into (write) a
		if (readA) {
			doCalculation(this.cellsA, this.cellsB);
		} else {
			doCalculation(this.cellsB, this.cellsA);
		}
		return;
	}

	public void doCalculation(final long[][] cellsRead, final long[][] cellsWrite) {
		// boolean DEBUG = false;
		// calculate the temperature
		// for each cell in this node
		// TODO: this might be backwards?
		// inclusive of max
		//		System.out.println("ON CALC: rowMax: " + rowMax + " rowMin: " + rowMin + " columnMin: " + columnMin + " columnMax: " + columnMax);
		for (int row = rowMin; row <= rowMax; row++) {
			for (int column = columnMin; column <= columnMax; column++) {
				//System.out.println("Row is: " + row + " column is: " + column);
				//				if (row == 1) {
				//					if (column == 0) {
				//						//DEBUG = true;
				//						//print out the current temperature
				//						System.out.println("The temp of r1c0 is: " + cellsRead[1][0]);
				//					}
				//				}
				// don't set the first cell (static heat)
				if (row == 0) {
					if (column == 0) {
						// System.out.println("trying to do calc on first");
						continue;
					}
				}
				// don't set the last cell (static heat)
				if (row == (this.totalRowLength - 1)) {
					if (column == (this.totalColumnLength - 1)) {
						// System.out.println("trying to do calc on last");
						continue;
					}
				}
				// System.out.println("doing calculation on row: " + row +
				// " column: " + column);
				// BigDecimal totalTemp = new BigDecimal(0);
				long totalTemp = 0;
				// for each of the three base metals
				for (int i = 0; i < 3; i++) {

					// for each of the neighboring regions

					// check to see if north exists
					// if it does then get the temperature and get the
					// percentage of metal
					long northBD = -1;
					long eastBD = -1;
					long southBD = -1;
					long westBD = -1;
					//System.out.println("checking north, current point: r: " + row + " c: " + column);
					if (northExists(new CellPoint(row, column, this.rowOffset, this.columnOffset), cellsRead)) {
						long northTemperature = getTemperature(getNorthPoint(new CellPoint(row, column, this.rowOffset,
								this.columnOffset)), cellsRead);

						int percentageOfMetalInNeighbor = getPercentageOfMetal(i, getNorthPoint(new CellPoint(row,
								column, this.rowOffset, this.columnOffset)));
						// if (DEBUG)
						//						 System.out.println("northTemperature: " +
						//						 northTemperature + " percentMetal in north: " +
						//						 percentageOfMetalInNeighbor + " point: r: " + row + " c: " + column);
						northBD = (long) (northTemperature * (percentageOfMetalInNeighbor / 100.0));
					}
					if (eastExists(new CellPoint(row, column, this.rowOffset, this.columnOffset), cellsRead)) {
						long eastTemperature = getTemperature(getEastPoint(new CellPoint(row, column, this.rowOffset,
								this.columnOffset)), cellsRead);
						// if (DEBUG)
						//						 System.out.println("eastTemperature: " +
						//						 eastTemperature + " point: r: " + row + " c: " + column);
						int percentageOfMetalInNeighbor = getPercentageOfMetal(i, getEastPoint(new CellPoint(row,
								column, this.rowOffset, this.columnOffset)));
						eastBD = (long) (eastTemperature * (percentageOfMetalInNeighbor / 100.0));
					}
					if (southExists(new CellPoint(row, column, this.rowOffset, this.columnOffset), cellsRead)) {
						long southTemperature = getTemperature(getSouthPoint(new CellPoint(row, column, this.rowOffset,
								this.columnOffset)), cellsRead);
						// if (DEBUG)
						//						 System.out.println("southTemperature: " +
						//						 southTemperature + " point: r: " + row + " c: " + column);
						int percentageOfMetalInNeighbor = getPercentageOfMetal(i, getSouthPoint(new CellPoint(row,
								column, this.rowOffset, this.columnOffset)));
						southBD = (long) (southTemperature * (percentageOfMetalInNeighbor / 100.0));
					}
					if (westExists(new CellPoint(row, column, this.rowOffset, this.columnOffset), cellsRead)) {
						long westTemperature = getTemperature(getWestPoint(new CellPoint(row, column, this.rowOffset,
								this.columnOffset)), cellsRead);
						// if (DEBUG)
						//						 System.out.println("westTemperature: " +
						//						 westTemperature + " point: r: " + row + " c: " + column);
						int percentageOfMetalInNeighbor = getPercentageOfMetal(i, getWestPoint(new CellPoint(row,
								column, this.rowOffset, this.columnOffset)));
						westBD = (long) (westTemperature * (percentageOfMetalInNeighbor / 100.0));
					}
					// add the regions together
					// BigDecimal regionsTotal = new BigDecimal(0);
					long regionsTotal = 0;
					int numNeighbors = 0;
					if (northBD != -1) {
						regionsTotal = regionsTotal + northBD;
						numNeighbors++;
						// if (DEBUG)
						//						 System.out.println("northBD: " + northBD + " point: r: " + row + " c: " + column);
					}
					if (eastBD != -1) {
						regionsTotal = regionsTotal + eastBD;
						numNeighbors++;
						// if (DEBUG)
						//						 System.out.println("eastBD: " + eastBD + " point: r: " + row + " c: " + column);
					}
					if (southBD != -1) {
						regionsTotal = regionsTotal + southBD;
						numNeighbors++;
						// if (DEBUG)
						//						 System.out.println("southBD: " + southBD + " point: r: " + row + " c: " + column);
					}
					if (westBD != -1) {
						regionsTotal = regionsTotal + westBD;
						numNeighbors++;
						// if (DEBUG)
						//						 System.out.println("westBD: " + westBD + " point: r: " + row + " c: " + column);
					}
					// if (DEBUG)
					//					 System.out.println("numNeighbors: " + numNeighbors + " point: r: " + row + " c: " + column);
					// if (DEBUG)
					//					 System.out.println("RRRRegionsTotal before divide: " +
					//					 regionsTotal + " point: r: " + row + " c: " + column);

					// regionsTotal = regionsTotal.divide(new
					// BigDecimal(numNeighbors), 6, BigDecimal.ROUND_HALF_UP);
					regionsTotal = (long) (regionsTotal / numNeighbors);

					// if (DEBUG)
					// System.out.println("regionsTotal: " + regionsTotal);
					// multiply the metal constant by the the regions total
					totalTemp = (long) (totalTemp + (regionsTotal * metalConstants[i]));
				}
				// if (DEBUG)
				// System.out.println("Going to set temperature to: " +
				// totalTemp);
				setCellTemperature(new CellPoint(row, column, this.rowOffset, this.columnOffset), totalTemp, cellsWrite);
				// DEBUG = false;

			}
		}

	}

	private void doNode() {
		// if its a node split the node in two
		// the left will fork with half the current node
		// int xMinLeft = this.xMin;
		// int yMinLeft = this.yMin;
		// double half = ((this.xMax + this.xMin) / 2.0);
		// int xMaxLeft = (int) Math.floor(half);
		// int yMaxLeft = this.yMax;
		// int xMinRight = (int) Math.ceil(half);
		// int yMinRight = this.yMin;
		// int xMaxRight = this.xMax;
		// int yMaxRight = this.yMax;

		// left
		int rowMinLeft = this.rowMin;
		int rowMaxLeft = this.rowMax;
		int columnMinLeft = this.columnMin;
		double half = ((this.columnMax + this.columnMin) / 2.0);
		int columnMaxLeft = (int) Math.floor(half);
		// right
		int rowMinRight = this.rowMin;
		int rowMaxRight = this.rowMax;
		int columnMinRight = (int) Math.ceil(half);
		int columnMaxRight = this.columnMax;

		// System.out.println("xMin: " + this.xMin + " yMin: " + this.yMin +
		// " xMax: " + this.xMax + " yMax: " + this.yMax
		// + " xMinLeft: " + xMinLeft + " yMinLeft: " + yMinLeft + " xMaxLeft: "
		// + xMaxLeft + " yMaxLeft: "
		// + yMaxLeft + " xMinRight: " + xMinRight + " yMinRight: " + yMinRight
		// + " xMaxRight: " + xMaxRight
		// + " yMaxRight: " + yMaxRight)

		Node left = new Node(this.cellsA, this.cellsB, totalRowLength, totalColumnLength, rowMinLeft, columnMinLeft,
				rowMaxLeft, columnMaxLeft, this.readA, this.metalConstants, percentageOfMetals, rowOffset, columnOffset, ghostEastEdgeData, ghostWestEdgeData);
		left.fork();
		Node right = new Node(this.cellsA, this.cellsB, totalRowLength, totalColumnLength, rowMinRight, columnMinRight,
				rowMaxRight, columnMaxRight, this.readA, this.metalConstants, percentageOfMetals, rowOffset,
				columnOffset, ghostEastEdgeData, ghostWestEdgeData);
		right.invoke();
		left.join();

	}

	// public long temp(Point p, long[][] cellsRead){
	// //get the temp of the point
	// return
	// }

	public static CellPoint getNorthPoint(CellPoint p) {
		int pX = p.getRow() - 1;
		int pY = p.getColumn();

		return new CellPoint(pX, pY);
	}

	public static CellPoint getEastPoint(CellPoint p) {
		int pX = p.getRow();
		int pY = p.getColumn() + 1;

		return new CellPoint(pX, pY);
	}

	public static CellPoint getSouthPoint(CellPoint p) {
		int pX = p.getRow() + 1;
		int pY = p.getColumn();

		return new CellPoint(pX, pY);
	}

	public static CellPoint getWestPoint(CellPoint p) {
		int pX = p.getRow();
		int pY = p.getColumn() - 1;

		return new CellPoint(pX, pY);
	}

	public static boolean isInBounds(CellPoint p, long[][] cellsRead) {
		if (p.getRow() < 0) {
			return false;
		}
		if (p.getColumn() < 0) {
			return false;
		}
		if (p.getRow() >= cellsRead.length) {
			return false;
		}
		if (p.getColumn() >= cellsRead[0].length) {
			return false;
		}
		return true;
	}

	public static boolean northExists(CellPoint current, long[][] cellsRead) {
		return isInBounds(getNorthPoint(current), cellsRead);
	}

	public static boolean eastExists(CellPoint current, long[][] cellsRead) {
		return isInBounds(getEastPoint(current), cellsRead);
	}

	public static boolean southExists(CellPoint current, long[][] cellsRead) {
		return isInBounds(getSouthPoint(current), cellsRead);
	}

	public static boolean westExists(CellPoint current, long[][] cellsRead) {
		return isInBounds(getWestPoint(current), cellsRead);
	}

	public long getTemperature(CellPoint p, long[][] cellsRead) {
		return cellsRead[p.getRow()][p.getColumn()];
	}

	public int getPercentageOfMetal(int metal, CellPoint neighbor) {
		// return this.percentageOfMetals[metal][neighbor.x][neighbor.y];
		return this.percentageOfMetals[neighbor.getRow()][neighbor.getColumn()][metal];
	}

	public void setCellTemperature(CellPoint p, long temp, long cellsWrite[][]) {
		// if (p.getRow() == 0 && p.getColumn() == 0) {
		// System.out.println("trying to set 00");
		// return;
		// }
		// if (p.getRow() == (cellsWrite.length - 1) && p.getColumn() ==
		// (cellsWrite[0].length - 1)) {
		// System.out.println("trying to set last");
		// return;
		// }
		// System.out.println("p.getRow(): " + p.getRow() + " p.getColumn(): " +
		// p.getColumn());
		// System.out.println("Setting temp: " + temp + " of row: " + p.getRow()
		// + " column: " + p.getColumn());
		cellsWrite[p.getRow()][p.getColumn()] = temp;
		// updeate the gui array
	}

}
