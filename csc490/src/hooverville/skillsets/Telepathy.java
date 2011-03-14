package hooverville.skillsets;

public class Telepathy extends Skill {
	
	private int tel;
	
	public Telepathy(int telepathy) {
		tel = telepathy;
	}
	
	public int get() { return tel; }
	
	public void set(int delta) {
		tel += delta;
	}

}
