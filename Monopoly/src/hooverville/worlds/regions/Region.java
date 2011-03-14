package hooverville.worlds.regions;

import hooverville.characters.HoovervilleCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representation of a Region of the Hooverville World. Region is the parent
 * class of any subtype regions. Each region can vary in size, but will default
 * to a 10x10 grid if not otherwise specified.
 *
 * @see DefaultRegion.java
 * @see Desert.java
 * @see Dungeon.java
 * @see Forest.java
 * @see Mountians.java
 * @see Plains.java
 * @see Ruins.java
 * @see Village.java
 * @author Kendall
 *
 */
public abstract class Region { // Abstract so only subtype can be implemented

	public enum Location {

		Default(0),
		Desert(1),
		Dungeon(2),
		Forest(3),
		Mountains(4),
		Plains(5),
		Ruins(6),
		Village(7);

		private final int id;

		private Location(int i) {
			id = i;
		}

		int getId() { return id; };

	}

	static final int MAX_PLAYERS_PER_REGION = 1000;
	static final int X_DEFAULT = 10;
	static final int Y_DEFAULT = 10;

	final Location loc;
	final int x;
	final int y;

	protected int[][] regionSize;
	final Random enemyGen = new Random();

	protected List<HoovervilleCharacter> characters = new ArrayList<HoovervilleCharacter>();

	public Region(Location l) {
		this(l, X_DEFAULT, Y_DEFAULT);
	}

	public Region(Location l, int maxX, int maxY) {
		loc = l;
		x = maxX;
		y = maxY;
		initRegion();
	}

	public void addCharacter(HoovervilleCharacter c) {
		if (characters.contains(c))
			characters.remove(c);
		characters.add(c);
	}

	public List<HoovervilleCharacter> getCharactersForRegion() {
		return characters;
	}

	public Location getLocation() { return loc; }
	public int getLocationID() { return loc.id; }
	public int getTotalCharacters() { return characters.size(); }
	int[][] getUnderlyingRegion() {
		return regionSize;
	}
	public int getXLength() { return x; }
	public int getYLength() { return y; }

	private void initRegion() {
		regionSize = new int[x][y];
	}

	public void moveEast(HoovervilleCharacter c) throws RegionIndexOutOfBoundsException {
		for (HoovervilleCharacter hc : characters) {
			if (hc.equals(c)) {
				if (c.getX() >= x) {
					throw new RegionIndexOutOfBoundsException();
				} else {
					c.setX(c.getX()+1);
				}
			}
		}
	}

	public void moveNorth(HoovervilleCharacter c) throws RegionIndexOutOfBoundsException {
		for (HoovervilleCharacter hc : characters) {
			if (hc.equals(c)) {
				if (c.getY() <= 0) {
					throw new RegionIndexOutOfBoundsException();
				} else {
					c.setY(c.getY()-1);
				}
			}
		}
	}

	public void moveSouth(HoovervilleCharacter c) throws RegionIndexOutOfBoundsException {
		for (HoovervilleCharacter hc : characters) {
			if (hc.equals(c)) {
				if (c.getY() >= y) {
					throw new RegionIndexOutOfBoundsException();
				} else {
					c.setX(c.getX()+1);
				}
			}
		}
	}

	public void moveWest(HoovervilleCharacter c) throws RegionIndexOutOfBoundsException {
		for (HoovervilleCharacter hc : characters) {
			if (hc.equals(c)) {
				if (c.getX() <= 0) {
					throw new RegionIndexOutOfBoundsException();
				} else {
					c.setX(c.getX()-1);
				}
			}
		}
	}

	public boolean regionFull() { return characters.size() == MAX_PLAYERS_PER_REGION; }

	public void removeCharacter(HoovervilleCharacter c) {
		characters.remove(c);
	}

	@Override
	public String toString() {
		String s = "";
		s += ( loc.id == 0 ? "Default" :
			   loc.id == 1 ? "Desert" :
			   loc.id == 2 ? "Dungeon" :
			   loc.id == 3 ? "Forest" :
			   loc.id == 4 ? "Mountains" :
			   loc.id == 5 ? "Plains" :
			   loc.id == 6 ? "Ruins" :
				   			 "Village");
		s += ": \n";
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				for (HoovervilleCharacter c : characters) {
					if (c.getX() == i && c.getY() == j) {
						s += "c ";
						break;
					}
					else {
						s += "x ";
					}
				}
			}
			s+= "\n";
		}
		return s;
	}

}
