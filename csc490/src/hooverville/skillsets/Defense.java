package hooverville.skillsets;

public class Defense extends Skill {
	
	private int defensePoints;
	
	public Defense(int defense) {
		defensePoints = defense;
	}
	
	public int get() { return defensePoints; }
	
	public void set(int delta) {
		defensePoints += delta;
	}

}
