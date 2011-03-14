package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.actions.solo.Teleport;
import hooverville.characters.HoovervilleCharacter;

public class Flee extends InteractiveAction {

	public Flee(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
		flee();
		return 0;
	}

	@Override
	protected int doEducatorAction() {
		flee();
		return 0;
	}

	@Override
	protected int doHarlotAction() {
		flee();
		return 0;
	}

	@Override
	protected int doHerbologistAction() {
		flee();
		return 0;
	}

	@Override
	protected int doLightKeeperAction() {
		flee();
		return 0;
	}

	@Override
	protected int doShadowSeekerAction() {
		flee();
		return 0;
	}

	private void flee() {
		Teleport teleport = new Teleport(self);
		self.setMana((self.getMana() <= 5)? self.getMana() : 5);
		self.setHealth(1);
		opponent.setHealth(opponent.getHealth() + 5);
		opponent.setMana(opponent.getMana() + 5);
		self.setFighting(false);
		opponent.setFighting(false);
		teleport.doAction();
	}

}
