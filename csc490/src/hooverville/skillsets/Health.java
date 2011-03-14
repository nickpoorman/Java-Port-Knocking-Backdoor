package hooverville.skillsets;

public class Health extends Skill {
	
	private int hp;
	
	public Health(int initialHealth) {
		hp = initialHealth;
	}
	
	public int get() { return hp; }
	
	public void set(int delta) {
		if (delta > 100) hp = 100;
		else hp = delta;
	}

}
