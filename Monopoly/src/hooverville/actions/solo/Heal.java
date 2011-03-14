package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;
import hooverville.items.Item;
import hooverville.items.self.Food;

import java.util.List;

public class Heal extends SoloAction {

	public Heal(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {
		int curHealth = self.getHealth();
		if (curHealth >= 100) {
			return;
		}
		List<Item> items = self.getInventory();
		for (Item i : items) {
			if (i instanceof Food) {
				Food f = (Food)i;
				self.setHealth(f.eat());
			}
		}
	}

}
