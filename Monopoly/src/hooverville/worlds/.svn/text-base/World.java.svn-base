package hooverville.worlds;

import hooverville.worlds.regions.DefaultRegion;
import hooverville.worlds.regions.NoSuchRegionException;
import hooverville.worlds.regions.Region;

import java.awt.Point;

public class World {

	/**
	 * Creates a world with a default of 16 regions. Some of the regions may be blank,
	 * meaning they are just space to pass through
	 */
	private static final int DEFAULT_SIZE = 4;

	final String name;
	static int maxX;
	static int maxY;
	private static Region[][] area;

	/**
	 * Creates a default virtual world with the given name and the
	 * default size parameters. By default, a this world will have a
	 * total of 16 regions that can be added.
	 *
	 * @param n - the name of the World
	 */
	public World(String n) {
		this(n, DEFAULT_SIZE, DEFAULT_SIZE);
	}

	/**
	 * Creates a virtual world with the specified name, and X,Y
	 * coordinates. The number of regions allowed for this world
	 * is calculated by multiplying the X coord by the Y coord.
	 * i.e. (#Regions = X * Y).
	 *
	 * @param n - the name of the world
	 * @param initX - the X size of the virtual world
	 * @param initY - the Y size of the virtual world
	 */
	public World(String n, int initX, int initY) {
		name = n;
		maxX = initX;
		maxY = initY;
		initWorld();
	}

	/**
	 * Gets the name of this virtual world.
	 *
	 * @return the name of the World
	 */
	public String getName() { return name; }

	/**
	 * Returns the size of the world. i.e., the number
	 * of regions that can be put into the world.
	 *
	 * @return the size of this world.
	 */
	public int size() { return maxX * maxY; }

	private void initWorld() {
		area = new Region[maxX][maxY];
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				area[i][j] = new DefaultRegion(1,1);
			}
		}
	}

	/**
	 * Gets the number of people currently playing in the virtual
	 * world. This is done by getting the total players from each
	 * region and summing them together.
	 *
	 * @return the number of players currently playing in the world.
	 */
	public int getTotalPlayerCount() {
		int count = 0;
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				count += area[i][j].getTotalCharacters();
			}
		}
		return count;
	}

	/**
	 * Adds the given region in the first available location. If there
	 * are no spaces, the add will fail.
	 *
	 * @param r - the region to be added
	 */
	public boolean addRegion(Region r) {
		boolean shouldBreak = false;
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (area[i][j].getLocationID() == 0) {
					area[i][j] = r;
					shouldBreak = true;
					break;
				}
			}
			if (shouldBreak) {
				break;
			}
		}
		return shouldBreak; //This works because shouldBreak will only be true if a region was added
	}

	/**
	 * Adds a new region at the specified location. If the space is already occupied by a
	 * non-default default region, the add is not successful.
	 *
	 * @param r - the region to add
	 * @param xStart - the x coordinate where the region should be placed
	 * @param yStart - the y coordinate where the region should be placed
	 * @return true, if the region was successfully added. False if there is already
	 * a different region at the specified location
	 */
	public boolean addRegionAt(Region r, int xStart, int yStart) {
		if (area[xStart][yStart].getLocationID() != 0) {
			return false;
		} else {
			area[xStart][yStart] = r;
			return true;
		}
	}

	/**
	 * Removes all instances of the specified region from the world. If no region is found
	 * with the same type as the region passed in, no change to the world will occur.
	 *
	 * @param r - the type of region to remove from the world.
	 */
	public void removeAllRegionsOfType(Region r) {
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (area[i][j].getLocationID() == r.getLocationID()) {
					area[i][j] = new DefaultRegion(1,1);
				}
			}
		}
	}

	/**
	 * Removes the specified region at the given coordinates.
	 * If no such region exists, and exception is thrown indicating
	 * as such.
	 *
	 * @throws NoSuchRegionException if the given region doesn't exist
	 * @param r - the region to be removed
	 * @param x - the x location to remove the region from
	 * @param y - the y location to remove the region from
	 */
	public void removeRegionAt(Region r, int x, int y) {
		if (area[x][y].getLocationID() == r.getLocationID()) {
			area[x][y] = new DefaultRegion(1,1);
		} else {
			throw new NoSuchRegionException("Region not found at specified location");
		}
	}

	public Region getRegionAt(int x, int y) {
		return area[x][y];
	}

	public Point getIndicesOf(Region r) {
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (area[i][j].equals(r)) {
					return new Point(i,j);
				}
			}
		}
		return null;
	}

	/**
	 * Gets the region north of the specified region. If no such region exists,
	 * the return value will be null. This happens if the getRegionNorthOf method
	 * is invoked with a region who is already on the northern border of the map.
	 * If the region to the north of the specified region is empty, then this world's
	 * copy of the default region will be returned.
	 *
	 * @param r - the region
	 * @return the region north of the given region.
	 */
	public static Region getRegionNorthOf(Region r) {
		int[] indexes = getIndexes(r);
		int x = indexes[0];
		int y = indexes[1];
		if (x == 0) {
			return null;
		}
		return area[x-1][y];
	}

	/**
	 * Gets the region south of the specified region. If no such region exists,
	 * the return value will be null. This happens if the getRegionSouthOf method
	 * is invoked with a region who is already on the southern border of the map.
	 * If the region to the south of the specified region is empty, then this world's
	 * copy of the default region will be returned.
	 *
	 * @param r - the region
	 * @return the region south of the given region
	 */
	public static Region getRegionSouthOf(Region r) {
		int[] indexes = getIndexes(r);
		int x = indexes[0];
		int y = indexes[1];
		if (x == maxX) {
			return null;
		}
		return area[x+1][y];
	}

	/**
	 * Gets the region east of the specified region. If no such region exists,
	 * the return value will be null. This happens if the getRegionEastOf method
	 * is invoked with a region who is already on the eastern border of the map.
	 * If the region to the east of the specified region is empty, then this world's
	 * copy of the default region will be returned.
	 *
	 * @param r - the region
	 * @return the region east of the given region
	 */
	public static Region getRegionEastOf(Region r) {
		int[] indexes = getIndexes(r);
		int x = indexes[0];
		int y = indexes[1];
		if (y == maxY) {
			return null;
		}
		return area[x][y+1];
	}

	/**
	 * Gets the region west of the specified region. If no such region exists,
	 * the return value will be null. This happens if the getRegionWestOf method
	 * is invoked with a region who is already on the western border of the map.
	 * If the region to the west of the specified region is empty, then this world's
	 * copy of the default region will be returned.
	 *
	 * @param r - the region
	 * @return the region west of the given region
	 */
	public static Region getRegionWestOf(Region r) {
		int[] indexes = getIndexes(r);
		int x = indexes[0];
		int y = indexes[1];
		System.out.println(x + " : " + y);
		if (y == 0) {
			return null;
		}
		return area[x][y-1];
	}

	/* Helper for getNeighborRegion methods */
	private static int[] getIndexes(Region r) {
		int[] indexes = new int[2];
		boolean shouldBreak = false;
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (area[i][j] != null) {
					if (area[i][j].getLocationID() == r.getLocationID()) {
						indexes[0] = i;
						indexes[1] = j;
						shouldBreak = true;
						break;
					}
				}
			}
			if (shouldBreak) {
				break;
			}
		}
		return indexes;
	}

	/**
	 * Returns a string representation of this virtual world. The
	 * virtual world has a 0 in place of the default region, and the
	 * region code of any other region.
	 *
	 * @return the virtual world
	 */
	@Override
	public String toString() {
		String world = "";
		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				world += (area[i][j].getLocationID() == 0)? "0 " : Integer.toString(area[i][j].getLocationID()) + " ";
			}
			world += "\n";
		}
		return world;
	}
	
	public static Region getDefault() {
		return area[2][2];
	}

}
