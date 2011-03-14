package hooverville.characters;

import hooverville.items.Item;
import hooverville.skillsets.Defense;
import hooverville.skillsets.Dexterity;
import hooverville.skillsets.Health;
import hooverville.skillsets.Intelligence;
import hooverville.skillsets.Mana;
import hooverville.skillsets.PotionMakingAbility;
import hooverville.skillsets.Skill;
import hooverville.skillsets.Telepathy;
import hooverville.skillsets.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class ShadowSeeker extends HoovervilleCharacter {

	public static final String TYPE = "Shadow Seeker";
	private final List<Item> sac;

	private final Level level = new Level(1);

	private final Skill health = new Health(100);
	private final Skill mana = new Mana(120);
	private final Skill defense = new Defense(70);
	private final Skill intel = new Intelligence(20);
	private final Skill telepathy = new Telepathy(60);
	private final Skill dex = new Dexterity(60);
	private final Skill potion = new PotionMakingAbility(20);

	public ShadowSeeker(String gender, String userName) {
		super(gender, userName);
		sac = new ArrayList<Item>();
		initSkillSets();
	}

	private void initSkillSets() {
		skillsets.add(health);
		skillsets.add(mana);
		skillsets.add(defense);
		skillsets.add(intel);
		skillsets.add(telepathy);
		skillsets.add(dex);
		skillsets.add(potion);
	}

	@Override
	public boolean emptyInventory() {
		return sac.size() == 0;
	}

	public List<Skill> getSkillSets() {
		return skillsets;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public int getHealth() {
		return health.get();
	}

	@Override
	public void setHealth(int delta) {
		health.set(delta);
	}

	@Override
	public int getMana() {
		return mana.get();
	}

	@Override
	public void setMana(int delta) {
		mana.set(delta);
	}

	@Override
	public int getDefense() {
		return defense.get();
	}

	@Override
	public void setDefense(int delta) {
		defense.set(delta);
	}

	@Override
	public int getIntelligence() {
		return intel.get();
	}

	@Override
	public void setIntelligence(int delta) {
		intel.set(delta);
	}

	@Override
	public int getTelepathy() {
		return telepathy.get();
	}

	@Override
	public void setTelepathy(int delta) {
		telepathy.set(delta);
	}

	@Override
	public int getDexterity() {
		return dex.get();
	}

	@Override
	public void setDexterity(int delta) {
		dex.set(delta);
	}

	@Override
	public int getPotionMakingAbility() {
		return potion.get();
	}

	@Override
	public void setPotionMakingAbility(int delta) {
		dex.set(delta);
	}

	@Override public Level getLevel() {
		return level;
	}

	@Override public List<Item> getInventory() {
		return sac;
	}

	@Override
	public String getDescription() {
		return TYPE;
	}

}
