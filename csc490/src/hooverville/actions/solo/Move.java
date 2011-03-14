package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;
import hooverville.worlds.regions.RegionIndexOutOfBoundsException;

public class Move extends SoloAction {

	public Move(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {

	}

	public void moveNorth() throws RegionIndexOutOfBoundsException {
		self.goNorth();
	}

	public void moveSouth() throws RegionIndexOutOfBoundsException {
		self.goSouth();
	}

	public void moveEast() throws RegionIndexOutOfBoundsException {
		self.goEast();
	}

	public void moveWest() throws RegionIndexOutOfBoundsException {
		self.goWest();
	}

}
