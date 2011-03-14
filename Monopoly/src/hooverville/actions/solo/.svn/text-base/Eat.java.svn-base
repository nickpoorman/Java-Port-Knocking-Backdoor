package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;
import hooverville.items.Item;
import hooverville.items.self.Food;

import java.util.List;

public class Eat extends SoloAction {

	public Eat(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {
		List<Item> items = self.getInventory();
		for (Item i : items) {
			if (i instanceof Food) {
				Food f = (Food)i;
				int currentHealth = self.getHealth();
				int healing = f.eat();
				if (100 - currentHealth > healing) { }
				else { }
			}
		}
	}

}
