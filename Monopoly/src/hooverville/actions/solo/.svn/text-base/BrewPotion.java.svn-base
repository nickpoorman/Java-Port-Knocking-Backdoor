package hooverville.actions.solo;

import hooverville.actions.SoloAction;
import hooverville.characters.CareTaker;
import hooverville.characters.Educator;
import hooverville.characters.Harlot;
import hooverville.characters.Herbologist;
import hooverville.characters.HoovervilleCharacter;
import hooverville.characters.LightKeeper;
import hooverville.characters.ShadowSeeker;
import hooverville.items.Item;
import hooverville.items.self.BluePlant;
import hooverville.items.self.GreenPlant;
import hooverville.items.self.Plant;
import hooverville.items.self.RedPlant;
import hooverville.items.self.YellowPlant;

import java.util.ArrayList;
import java.util.List;

public class BrewPotion extends SoloAction {

	private final List<Plant> plants;

	public BrewPotion(HoovervilleCharacter c) {
		super(c);
		plants = getPlants();
	}

	private List<Plant> getPlants() {
		List<Plant> plants = new ArrayList<Plant>();
		if (!self.emptyInventory()) {
			for (Item i : self.getInventory()) {
				if (i instanceof Plant) {
					plants.add((Plant) i);
				}
			}
		}
		return plants;
	}

	@Override
	public void doAction() {
		brewHealthRegenPotion();
	}

	public void brewHealthRegenPotion() {
		RedPlant plant = getRedPlantFromInventory(plants);
		if (plant == null) {
			System.out.println("You do not have any red plants.\n" +
					"Red plants are essential for healing potions");
			return;
		}
		if (self.getType().equals(LightKeeper.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(ShadowSeeker.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(CareTaker.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Educator.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Harlot.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
		else if(self.getType().equals(Herbologist.TYPE)) {
			int brewingPower = 10;
			brewHealingPotionWithPotionPower(plant, brewingPower);
		}
	}

	private RedPlant getRedPlantFromInventory(List<Plant> plants) {
		RedPlant plant = null;
		for (Plant p : plants) {
			if (p instanceof RedPlant) {
				plant = (RedPlant)p;
			}
		}
		return plant;
	}

	private void brewHealingPotionWithPotionPower(RedPlant plant, int brewingPower) {

	}

	public void brewManaRegenPotion() {
		BluePlant plant = getBluePlantFromInventory();
		if (plant == null) {
			System.out.println("You do not have any blue plants.\n" +
					"Blue plants are essential for mana regeneration potions");
			return;
		}
		if (self.getType().equals(LightKeeper.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(ShadowSeeker.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(CareTaker.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Educator.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Harlot.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
		else if(self.getType().equals(Herbologist.TYPE)) {
			int brewingPower = 10;
			brewManaPotionWithPotionPower(plant, brewingPower);
		}
	}

	private BluePlant getBluePlantFromInventory() {
		BluePlant plant = null;
		for (Plant p : plants) {
			if (p instanceof BluePlant) {
				plant = (BluePlant)p;
			}
		}
		return plant;
	}

	private void brewManaPotionWithPotionPower(BluePlant plant, int brewingPower) {

	}

	public void brewDivingPowerPotion() {
		GreenPlant plant = getGreenPlantFromInventory();
		if (plant == null) {
			System.out.println("You do not have any green plants.\n" +
					"Red plants are essential for divine potions");
			return;
		}
		if (self.getType().equals(LightKeeper.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(ShadowSeeker.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(CareTaker.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Educator.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Harlot.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
		else if(self.getType().equals(Herbologist.TYPE)) {
			int brewingPower = 10;
			brewDivinePowerPotionWithPotionPower(plant, brewingPower);
		}
	}

	private GreenPlant getGreenPlantFromInventory() {
		GreenPlant plant = null;
		for (Plant p : plants) {
			if (p instanceof GreenPlant) {
				plant = (GreenPlant)p;
			}
		}
		return plant;
	}

	private void brewDivinePowerPotionWithPotionPower(GreenPlant plant, int brewingPower) {

	}

	public void brewFullRegenPotion() {
		YellowPlant plant = getPlantsForFullRegen();
		if (plant == null) {
			System.out.println("You do not have any yellow plants.\n" +
					"Red plants are essential for full regeneration potions");
			return;
		}
		if (self.getType().equals(LightKeeper.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(ShadowSeeker.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(CareTaker.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Educator.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
		else if (self.getType().equals(Harlot.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
		else if(self.getType().equals(Herbologist.TYPE)) {
			int brewingPower = 10;
			brewFullRegenPotionWithPotionPower(plant, brewingPower);
		}
	}

	private YellowPlant getPlantsForFullRegen() {
		YellowPlant plant = null;
		for (Plant p : plants) {
			if (p instanceof YellowPlant) {
				plant = (YellowPlant)p;
			}
		}
		return plant;
	}
	
	private void brewFullRegenPotionWithPotionPower(YellowPlant plant, int brewingPower) {

	}

}