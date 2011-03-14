package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;
import hooverville.items.Item;
import hooverville.items.self.Food;

public class Feed extends InteractiveAction {

	public Feed(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
		return feed();
	}

	@Override
	protected int doEducatorAction() {
		return feed();
	}

	@Override
	protected int doHarlotAction() {
		return feed();
	}

	@Override
	protected int doHerbologistAction() {
		return feed();
	}

	@Override
	protected int doLightKeeperAction() {
		return feed();
	}

	@Override
	protected int doShadowSeekerAction() {
		return feed();
	}

	private int feed() {
		int healthGain = 0;
		boolean found = false;
		for (Item i : self.getInventory()) {
			if (i instanceof Food) {
				found = true;
				Food f = (Food)i;
				int hpBonus = f.eat();
				healthGain = hpBonus;
				System.out.println("Giving " + opponent.getUserName() + " " + hpBonus + " more health points from " + f.toString());
				opponent.setHealth(opponent.getHealth() + hpBonus);
				self.getInventory().remove(i);
				break;
			}
		}
		if (!found) {
			System.out.println("No food was found in inventory to give to " + opponent.getUserName());
		}
		return healthGain;
	}

}
