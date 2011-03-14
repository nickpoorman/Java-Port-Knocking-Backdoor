package hooverville.test;

import hooverville.actions.interactive.Curse;
import hooverville.characters.HoovervilleCharacter;
import hooverville.characters.LightKeeper;
import hooverville.characters.ShadowSeeker;

public class CharacterTest {

	public static void main(String[] a) {
		// Create some characters
		ShadowSeeker ss = new ShadowSeeker("Male", "Kendall");
		HoovervilleCharacter lk = new LightKeeper("Female", "Tab");
		// Check mana, health, and types
		System.out.println("Attacking " + ss.getType() + " with " + lk.getType());
		System.out.println("Mana Before: " + lk.getMana());
		System.out.println("Health before: " + ss.getHealth());
		// Do the attack with the Light Keeper
		Curse curse = new Curse(lk, ss);
		lk.curse(curse);
		lk.curse(new Curse(lk, ss));
		lk.curse(new Curse(lk, ss));
		lk.curse(new Curse(lk, ss));
		lk.curse(new Curse(lk, ss));
		lk.curse(new Curse(lk, ss));
		// Re-check the stats
		System.out.println("Attacker's Mana After: " + lk.getMana());
		System.out.println("Health After: " + ss.getHealth());
		System.out.println("\n\n");
		// Now attack with the Shadow Seeker and print stats
		System.out.println("Attacking " + lk.getType() + " with " + ss.getType());
		System.out.println("Mana Before: " + ss.getMana());
		System.out.println("Health before: " + lk.getHealth());
		// Do the curse
		ss.curse(new Curse(ss, lk));
		ss.curse(new Curse(ss, lk));
		ss.curse(new Curse(ss, lk));
		ss.curse(new Curse(ss, lk));
		ss.curse(new Curse(ss, lk));
		// Re-check the stats
		System.out.println("Attacker's Mana After: " + ss.getMana());
		System.out.println("Health After: " + lk.getHealth());


	}

}
