package hooverville.actions.interactive;

import hooverville.actions.InteractiveAction;
import hooverville.characters.HoovervilleCharacter;

public class Spar extends InteractiveAction {

	public Spar(HoovervilleCharacter self, HoovervilleCharacter opponent) {
		super(self, opponent);
	}

	@Override
	protected int doCareTakerAction() {
		int attMultiplier = 1;
		return attackWithMultiplier(attMultiplier);
	}

	@Override
	protected int doEducatorAction() {
		int attMultiplier = 2;
		return attackWithMultiplier(attMultiplier);
	}

	@Override
	protected int doHarlotAction() {
		int attMultiplier = 1;
		return attackWithMultiplier(attMultiplier);
	}

	@Override
	protected int doHerbologistAction() {
		int attMultiplier = 1;
		return attackWithMultiplier(attMultiplier);
	}

	@Override
	protected int doLightKeeperAction() {
		int attMultiplier = 2;
		return attackWithMultiplier(attMultiplier);
	}

	@Override
	protected int doShadowSeekerAction() {
		int attMultiplier = 3;
		return attackWithMultiplier(attMultiplier);
	}

	private int attackWithMultiplier(int damageMultiplier) {
		System.out.println("Attacking " + opponent.getType() + ".");
		int damage = 0;
		int defHP = opponent.getHealth();
		int attLevel = self.getLevel().get();
		int defLevel = opponent.getLevel().get();
		int defense = opponent.getDefense();
		int diff = (attLevel - defLevel != 0)? (attLevel - defLevel) : 1;
		if (defLevel > (attLevel + 20)) {
			System.out.println("Can't attack " + opponent.getType() + ". You are more than 20 levels lower.");
			return damage;
		}
		if (defense > 150) {
			damage = attackAndSetLevels(defHP, (diff + damageMultiplier));
		}
		else if (defense > 100 && defense <= 150) {
			damage = attackAndSetLevels(defHP, ((diff + damageMultiplier) * 2));
		}
		else if (defense > 50 && defense <= 100) {
			damage = attackAndSetLevels(defHP, ((diff + damageMultiplier) * 3));
		}
		else {
			damage = attackAndSetLevels(defHP, ((diff + damageMultiplier) * 4));
		}
		System.out.println("Attacked " + opponent.getType() + " for " + damage + " damage.");
		return damage;
	}

	private int attackAndSetLevels(int defHP, int attackDamage) {
		int damage = (attackDamage);
		opponent.setHealth(defHP - damage);
		return damage;
	}

}
