package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.HoovervilleCharacter;
import hooverville.items.Item;

public class Inventory extends SoloAction {

	public Inventory(HoovervilleCharacter c) {
		super(c);
	}

	@Override
	public void doAction() {
		System.out.println("Item Inventory for " + self.getUserName() + ": " + self.getType());
		for (Item i : self.getInventory()) {
			System.out.println(i.toString());
		}
	}
	
	public String getInventory() {
		String inven = "Item Inventory for " + self.getUserName() + ": " + self.getType() + "\n";
		for (Item i : self.getInventory()) {
			inven = inven + i.toString() + "\n";
		}
		return inven;
	}

}
