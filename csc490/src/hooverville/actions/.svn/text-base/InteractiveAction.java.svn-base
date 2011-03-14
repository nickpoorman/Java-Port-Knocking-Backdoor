package hooverville.actions;

import hooverville.characters.CareTaker;
import hooverville.characters.Educator;
import hooverville.characters.Harlot;
import hooverville.characters.Herbologist;
import hooverville.characters.HoovervilleCharacter;
import hooverville.characters.LightKeeper;
import hooverville.characters.ShadowSeeker;

public abstract class InteractiveAction {

	protected final HoovervilleCharacter self;
	protected final HoovervilleCharacter opponent;

	public InteractiveAction(HoovervilleCharacter a, HoovervilleCharacter d) {
		self = a;
		opponent = d;
	}

	public int doAction() {
		int damage = 0;
		if (self.getType().equals(LightKeeper.TYPE)) {
			damage = doLightKeeperAction();
		} else if (self.getType().equals(ShadowSeeker.TYPE)) {
			damage = doShadowSeekerAction();
		} else if (self.getType().equals(Herbologist.TYPE)) {
			damage = doHerbologistAction();
		} else if (self.getType().equals(Harlot.TYPE)) {
			damage = doHarlotAction();
		} else if (self.getType().equals(Educator.TYPE)) {
			damage = doEducatorAction();
		} else if (self.getType().equals(CareTaker.TYPE)) {
			damage = doCareTakerAction();
		}
		return damage;
	}

	protected abstract int doLightKeeperAction();
	protected abstract int doShadowSeekerAction();
	protected abstract int doHerbologistAction();
	protected abstract int doHarlotAction();
	protected abstract int doEducatorAction();
	protected abstract int doCareTakerAction();

}
