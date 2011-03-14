package hooverville.skillsets.levels;

public class Level {

	public static final int GREATER = 1;
	public static final int LOWER = -1;

	int level;
	int currentPoints;

	public Level(int initial) {
		level = initial;
	}

	public int get() { return level; }

	public void set(int updated) { level = updated; }

	public void addPoints(int delta) {
		if (currentPoints + delta < getLevelCap()) {
			currentPoints += delta;
		} else {
			int cap = getLevelCap();
			int nextLevelStart = (currentPoints + delta) - cap;
			++level;
			currentPoints = nextLevelStart;
		}
	}

	public int getLevelCap() {
		return level * 1000;
	}

	public int getExperience() { return currentPoints; }
	public void setExperience(int exp) { currentPoints = exp; }

	public int getPointsToNextLevel() {
		return (level * 1000) - currentPoints;
	}

}
