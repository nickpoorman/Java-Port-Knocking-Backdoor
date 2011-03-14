package project;

public class CellPoint {

	private int row;
	private int column;
	private int rowOffset;
	private int columnOffset;
	
	public CellPoint(int row, int column) {
		this.setRow(row);
		this.setColumn(column);
		this.setRowOffset(0);
		this.setColumnOffset(0);
	}

	public CellPoint(int row, int column, int rowOffset, int columnOffset) {
		this.setRow(row);
		this.setColumn(column);
		this.setRowOffset(rowOffset);
		this.setColumnOffset(columnOffset);
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row + rowOffset;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column + columnOffset;
	}

	/**
	 * @return the rowOffset
	 */
	public int getRowOffset() {
		return rowOffset;
	}

	/**
	 * @param rowOffset the rowOffset to set
	 */
	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	/**
	 * @return the columnOffset
	 */
	public int getColumnOffset() {
		return columnOffset;
	}

	/**
	 * @param columnOffset the columnOffset to set
	 */
	public void setColumnOffset(int columnOffset) {
		this.columnOffset = columnOffset;
	}

}
