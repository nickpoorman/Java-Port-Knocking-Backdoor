package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Guard extends InteractiveAction {

	public Guard(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
		int manaDrain = 5;
		return guardWithManaDrain(manaDrain);
	}

	@Override
	protected int doEducatorAction() {
		int manaDrain = 7;
		return guardWithManaDrain(manaDrain);
	}

	@Override
	protected int doHarlotAction() {
		int manaDrain = 10;
		return guardWithManaDrain(manaDrain);
	}

	@Override
	protected int doHerbologistAction() {
		int manaDrain = 8;
		return guardWithManaDrain(manaDrain);
	}

	@Override
	protected int doLightKeeperAction() {
		int manaDrain = 12;
		return guardWithManaDrain(manaDrain);
	}

	@Override
	protected int doShadowSeekerAction() {
		int manaDrain = 15;
		return guardWithManaDrain(manaDrain);
	}

	private int guardWithManaDrain(int manaDrain) {
		if (self.getMana() - manaDrain < 0) {
			System.out.println("Not enough mana to guard " + opponent.getUserName());
			System.out.println("Mana required: " + manaDrain);
			System.out.println("Actual Mana: " + self.getMana());
			return 0;
		}
		int guard = self.getDefense() * (3/100); // 3% gain
		self.setMana(self.getMana() - manaDrain);
		System.out.println("Guarding " + opponent.getUserName() + " with " + guard + " defense power");
		opponent.setDefense(opponent.getDefense() + guard);
		return guard;
	}

}
