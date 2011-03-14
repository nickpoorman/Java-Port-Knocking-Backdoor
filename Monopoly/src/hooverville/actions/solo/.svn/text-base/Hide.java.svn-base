package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;

public class Hide extends SoloAction {

	public Hide(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {
		if (self.isFighting()) {
			System.out.println("Can't hide while engaged in a fight... ");
		}
		else {
			self.setHiding(true);
		}
	}

	public void hide(HoovervilleCharacter c, boolean hiding) {
		if (c.isFighting() && hiding) {
			System.out.println("Can't hide while engaged in a fight... ");
		}
		else {
			c.setHiding(hiding);
		}
	}

}
