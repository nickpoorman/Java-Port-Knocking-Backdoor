package hooverville.skillsets;

public class Mana extends Skill {
	
	private int mp;
	
	public Mana(int mana) {
		mp = mana;
	}
	
	public int get() { return mp; }
	
	public void set(int delta) {
		mp = delta;
	}

}
