package newtest;

public abstract class Cell {	

	private boolean damaged;	
	
	public Cell(){
		this.damaged = false;
	}
	
	public void setDamaged(){
		this.damaged = true;		
	}
	
	public boolean isDamaged(){
		return this.damaged;
	}
	
	public abstract char getElement();

}
