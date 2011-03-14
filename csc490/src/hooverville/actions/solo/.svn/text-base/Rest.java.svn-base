package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;

public class Rest extends SoloAction {

	public Rest(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {
		System.out.println("You lay down to take a quick nap...");
		self.setHealth(100);
		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
