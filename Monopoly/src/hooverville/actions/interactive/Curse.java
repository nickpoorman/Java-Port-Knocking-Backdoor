package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Curse extends InteractiveAction {

	public Curse(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doLightKeeperAction() {
		int damageMultiplier = 2;
		int manaDrain = 20;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	@Override
	protected int doShadowSeekerAction() {
		int damageMultiplier = 3;
		int manaDrain = 25;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	@Override
	protected int doHerbologistAction() {
		int damageMultiplier = 1;
		int manaDrain = 10;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	@Override
	protected int doHarlotAction() {
		int damageMultiplier = 0;
		int manaDrain = 5;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	@Override
	protected int doEducatorAction() {
		int damageMultiplier = 2;
		int manaDrain = 20;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	@Override
	protected int doCareTakerAction() {
		int damageMultiplier = 1;
		int manaDrain = 10;
		return attackWithMultiplier(damageMultiplier, manaDrain);
	}

	private int attackWithMultiplier(int damageMultiplier, int manaDrain) {
		System.out.println("Cursing " + opponent.getType() + ".");
		int damage = 0;
		int attMana = self.getMana();
		if (attMana < 20) {
			System.out.println("Not enough mana to attack... Must have at least 20 mana to perform curse");
			return damage;
		}
		int defHP = opponent.getHealth();
		int attLevel = self.getLevel().get();
		int defLevel = opponent.getLevel().get();
		int defense = opponent.getDefense();
		int diff = (attLevel - defLevel != 0)? (attLevel - defLevel) : 1;
		if (defLevel > (attLevel + 10)) {
			System.out.println("Can't attack " + opponent.getType() + ". You are more than 10 levels lower.");
			return damage;
		}
		if (defense > 150) {
			damage = attackAndSetLevels((attMana-manaDrain), defHP, (diff + damageMultiplier));
		} else if (defense > 100 && defense <= 150) {
			damage = attackAndSetLevels((attMana-manaDrain), defHP, ((diff + damageMultiplier) * 5));
		} else if (defense > 50 && defense <= 100) {
			damage = attackAndSetLevels((attMana-manaDrain), defHP, ((diff + damageMultiplier) * 10));
		} else {
			damage = attackAndSetLevels((attMana-manaDrain), defHP, ((diff + damageMultiplier) * 15));
		}
		System.out.println("Attacked " + opponent.getType() + " for " + damage + " damage.");
		return damage;
	}

	private int attackAndSetLevels(int manaDrain, int defHP, int attackDamage) {
		int damage = (attackDamage);
		self.setMana(manaDrain);
		opponent.setHealth(defHP - damage);
		return damage;
	}

}
