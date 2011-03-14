package hooverville.worlds.regions;

import hooverville.characters.HoovervilleCharacter;

public class Village extends Region {
	
	final int x;
	final int y;
	final int[][] area;

	final HoovervilleCharacter[] players = new HoovervilleCharacter[MAX_PLAYERS_PER_REGION];
	
	private int count;
	
	public Village(int xSize, int ySize) {
		super(Location.Village);
		x = xSize;
		y = ySize;
		area = new int[x][y];
	}
	
	public int currentPlayers() {
		return count;
	}
	
	public boolean regionFull() {
		return count == MAX_PLAYERS_PER_REGION;
	}
	
	public void addPlayer(HoovervilleCharacter c) {
		for (int i = 0; i < players.length; ++i) {
			if (players[i] == null) {
				players[i] = c;
				++count;
				return;
			}
		}
		throw new RuntimeException("Maximum players reached for region");
	}
	
	public void removePlayer(HoovervilleCharacter c) {
		for (int i = 0; i < players.length; ++i) {
			if (players[i].equals(c)) {
				players[i] = null;
				--count;
			}
		}
	}

}
