package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Enlighten extends InteractiveAction {

	public Enlighten(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
		int enlightenMultiplier = 2;
		int manaDrain = 2;
		return enlightenWithMultiplier(enlightenMultiplier, manaDrain);
	}

	@Override
	protected int doEducatorAction() {
		int enlightenMultiplier = 5;
		int manaDrain = 0;
		return enlightenWithMultiplier(enlightenMultiplier, manaDrain);
	}

	@Override
	protected int doHarlotAction() {
		System.out.println("Dirty disgusting harlots do no enlightening.");
		return 0;
	}

	@Override
	protected int doHerbologistAction() {
		int enlightenMultiplier = 2;
		int manaDrain = 2;
		return enlightenWithMultiplier(enlightenMultiplier, manaDrain);
	}

	@Override
	protected int doLightKeeperAction() {
		int enlightenMultiplier = 1;
		int manaDrain = 3;
		return enlightenWithMultiplier(enlightenMultiplier, manaDrain);
	}

	@Override
	protected int doShadowSeekerAction() {
		int enlightenMultiplier = 1;
		int manaDrain = 3;
		return enlightenWithMultiplier(enlightenMultiplier, manaDrain);
	}

	private int enlightenWithMultiplier(int multiplier, int manaDrain) {
		System.out.println("Enlightening " + opponent);
		self.setMana(self.getMana() - manaDrain);
		int cap = 5;
		int intel = self.getIntelligence();
		int oldLearnerIntel = opponent.getIntelligence();
		int boost = (intel / 100) * (multiplier * (1/5));
		int gain = (boost >= cap)? cap : boost;
		opponent.setIntelligence(opponent.getIntelligence() + (gain+1));
		System.out.println("Boosted " + opponent.getUserName() + "'s intelligence from " + oldLearnerIntel + " to " + (oldLearnerIntel + gain) + "!");
		return gain;
	}

}
