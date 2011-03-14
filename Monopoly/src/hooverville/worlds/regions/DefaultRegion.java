package hooverville.worlds.regions;

import hooverville.characters.HoovervilleCharacter;

public class DefaultRegion extends Region {

	final int x;
	final int y;
	final int[][] area;

	final HoovervilleCharacter[] players = new HoovervilleCharacter[MAX_PLAYERS_PER_REGION];

	public DefaultRegion() {
		this(Region.X_DEFAULT, Region.Y_DEFAULT);
	}

	public DefaultRegion(int xSize, int ySize) {
		super(Location.Default);
		x = xSize; y = ySize;
		area = new int[x][y];
	}

}
