package plugins;

import java.io.PrintWriter;
import java.util.List;

import hooverville.actions.interactive.Charm;
import hooverville.actions.interactive.Curse;
import hooverville.actions.interactive.Entice;
import hooverville.actions.interactive.Spar;
import hooverville.actions.interactive.Spell;
import hooverville.actions.solo.Teleport;
import hooverville.characters.HoovervilleCharacter;
import hooverville.commands.plugins.Command;
import hooverville.commands.util.CommandController;
import hooverville.server.User;
import hooverville.worlds.regions.Region;

public class Cast implements Command {

	@Override
	public void doAction(String input, PrintWriter out, CommandController cc, User user) {
		if (input.length() > 4) input = input.substring(5,input.length());
		if (input.startsWith("Help")) {
			out.println(getHelp());
			return;
		}
		String[] inputs = input.split(" ");
		String userName = inputs[0];
		HoovervilleCharacter currentCharacter = user.getHoovervilleCharacter();
		Region r = currentCharacter.getCurrentRegion();
		List<HoovervilleCharacter> players = r.getCharactersForRegion();
		boolean found = false;
		int damageTotal = 0;
		for (HoovervilleCharacter c : players) {
			if (c.getUserName().equals(userName)) {
				found = true;
				for (int i = 1; i < inputs.length; ++i) {
					if (inputs[i].equalsIgnoreCase("Curse")) {
						Curse curse = new Curse(currentCharacter, c);
						int damage = curse.doAction();
						damageTotal += damage;
						out.println("Damage done from curse: " + damage); 
					}
					if (inputs[i].equalsIgnoreCase("Spell")) {
						Spell spell = new Spell(currentCharacter, c);
						int damage = spell.doAction();
						damageTotal += damage;
						out.println("Damage done from spell: " + damage); 
					}
					else if (inputs[i].equalsIgnoreCase("Charm")) {
						Charm charm = new Charm(currentCharacter, c);
						int damage = charm.doAction();
						damageTotal += damage;
						out.println("Damage done from charm: " + damage); 
					}
					else if (inputs[i].equalsIgnoreCase("Entice")) {
						Entice entice = new Entice(currentCharacter, c);
						int damage = entice.doAction();
						damageTotal += damage;
						out.println("Damage done from entice: " + damage); 
					}
					else {
						out.println(inputs[i] + " you say? There be no such move you fool... ");
						out.println(getHelp());
					}
					if (c.getHealth() <= 0) {
						out.println("\n Player " + c.getUserName() + " is no longer with us and will be respanwed to a safe zone.");
						r.removeCharacter(c);
						Region placeToReSpawn = cc.getWorld().getRegionAt(2, 2);
						Teleport teleport = new Teleport(c);
						teleport.goToNewRegion(placeToReSpawn);
						c.setHealth(20);
						c.setMana(20);
					}
				}
				break;
			}
		}
		if (!found) {
			out.println("Really...? How might you expect to attack " + input + " if they aren't even here?");
			out.println(getHelp());
		}
		else {
			out.println("Whoa! You did " + damageTotal + " damage. Nice!");
		}
	}

	@Override
	public String getCommand() {
		return "cast";
	}

	@Override
	public String getHelp() {
		String help = "";
		help += "Usage of the Cast command: \n";
		help += "Possible inputs are a players name in the current region followed by a sequence of actions\n";
		help += "These actions may be any of the following: Charm, Curse, Entice, or Spell\n";
		help += "\n Attack <player name> Charm Description - Will automatically perform a charm on the \n";
		help += "\t specified player and report the damage done.";
		help += "\n Attack <player name> Curse Description - Will automatically perform a curse on the \n";
		help += "\t specified player and report the damage done.";
		help += "\n Attack <player name> Entice Description - Will automatically perform an enticement on the \n";
		help += "\t specified player and report the damage done.";
		help += "\n Attack <player name> Spar Description - Will automatically engage in a sparring battle with the \n";
		help += "\t specified player and report the damage done.";
		help += "\n\nSample Usages: \n";
		help += "\t 1) Cast <player name> Charm\n";
		help += "\t 2) Attack <player name> Charm Curse Entice Spell\n";
		return help;
	}

}
