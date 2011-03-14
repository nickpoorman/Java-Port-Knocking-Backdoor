package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Befriend extends InteractiveAction {

	public Befriend(HoovervilleCharacter a, HoovervilleCharacter d) {
		super(a,d);
	}

	@Override
	protected int doCareTakerAction() {
		addFriend();
		return 0;
	}

	@Override
	protected int doEducatorAction() {
		addFriend();
		return 0;
	}

	@Override
	protected int doHarlotAction() {
		addFriend();
		return 0;
	}

	@Override
	protected int doHerbologistAction() {
		addFriend();
		return 0;
	}

	@Override
	protected int doLightKeeperAction() {
		addFriend();
		return 0;
	}

	@Override
	protected int doShadowSeekerAction() {
		addFriend();
		return 0;
	}

	public void addFriend() {
		self.getFriends().add(opponent);
	}

	public void removeFriend() {
		self.getFriends().remove(opponent);
	}

}
