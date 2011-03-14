package hooverville.worlds.regions;


public final class Dungeon extends hooverville.worlds.regions.Region {

	public Dungeon() {
		this(Region.X_DEFAULT, Region.Y_DEFAULT);
	}

	public Dungeon(int xSize, int ySize) {
		super(Location.Dungeon, xSize, ySize);
	}

}
