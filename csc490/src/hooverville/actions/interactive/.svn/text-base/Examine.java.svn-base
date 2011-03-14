package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Examine extends InteractiveAction {

	public Examine(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
			examine();
		return 0;
	}

	@Override
	protected int doEducatorAction() {
		examine();
		return 0;
	}

	@Override
	protected int doHarlotAction() {
		examine();
		return 0;
	}

	@Override
	protected int doHerbologistAction() {
		examine();
		return 0;
	}

	@Override
	protected int doLightKeeperAction() {
		examine();
		return 0;
	}

	@Override
	protected int doShadowSeekerAction() {
		examine();
		return 0;
	}

	private void examine() {
		System.out.println("Examining character: " + opponent.getUserName() + ": " + opponent.getType());
		System.out.println(opponent.getDescription());
	}

}
