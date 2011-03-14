package hooverville.worlds;

import hooverville.worlds.regions.Desert;
import hooverville.worlds.regions.Dungeon;
import hooverville.worlds.regions.Forest;
import hooverville.worlds.regions.Mountains;
import hooverville.worlds.regions.Plains;
import hooverville.worlds.regions.Region;
import hooverville.worlds.regions.Ruins;
import hooverville.worlds.regions.Village;

public class WorldGenerator {
	
	static Region desert1 = new Desert(10,10);
	static Region desert2 = new Desert(10,10);
	static Region desert3 = new Desert(10,10);
	static Region desert4 = new Desert(10,10);
	static Region forest = new Forest(10,10);
	static Region dungeon = new Dungeon(10,10);
	static Region mountains = new Mountains(10,10);
	static Region plains1 = new Plains(10,10);
	static Region plains2 = new Plains(10,10);
	static Region plains3 = new Plains(10,10);
	static Region ruins = new Ruins(10,10);
	
	public static World generate() {
		World world = new World("Hooverville", 8, 8);
		addDeserts(world);
		addForests(world);
		addDungeons(world);
		addMountains(world);
		addPlains(world);
		addRuins(world);
		addVillages(world);
		System.out.println(world.toString());
		return world;
	}

	private static void addDungeons(World world) {
		world.addRegionAt(dungeon,7, 5);
		world.addRegionAt(new Dungeon(10,10), 7, 6);
		world.addRegionAt(new Dungeon(10,10), 6, 5);
		world.addRegionAt(new Dungeon(10,10), 6, 6);
		world.addRegionAt(new Dungeon(10,10), 6, 7);
		world.addRegionAt(new Dungeon(10,10), 5, 6);
		world.addRegionAt(new Dungeon(10,10), 5, 7);
	}

	private static void addForests(World world) {
		world.addRegionAt(forest, 7, 1);
		world.addRegionAt(new Forest(10,10), 7, 2);
		world.addRegionAt(new Forest(10,10), 7, 2);
		world.addRegionAt(new Forest(10,10), 6, 0);
		world.addRegionAt(new Forest(10,10), 6, 1);
		world.addRegionAt(new Forest(10,10), 6, 2);
		world.addRegionAt(new Forest(10,10), 5, 0);
		world.addRegionAt(new Forest(10,10), 5, 1);
	}

	private static void addRuins(World world) {
		world.addRegionAt(ruins, 0, 0);
		world.addRegionAt(new Ruins(10,10), 0, 7);
		world.addRegionAt(new Ruins(10,10), 7, 0);
		world.addRegionAt(new Ruins(10,10), 7, 7);
	}

	private static void addMountains(World world) {
		world.addRegionAt(new Mountains(10, 10), 0, 5);
		world.addRegionAt(mountains,0, 6);
		world.addRegionAt(new Mountains(10,10), 1, 5);
		world.addRegionAt(new Mountains(10,10), 1, 6);
		world.addRegionAt(new Mountains(10,10), 1, 7);
		world.addRegionAt(new Mountains(10,10), 2, 6);
		world.addRegionAt(new Mountains(10,10), 2, 7);
	}

	private static void addDeserts(World world) {
		world.addRegionAt(desert1, 0, 1);
		world.addRegionAt(desert2, 1, 0);
		world.addRegionAt(desert3, 1, 1);
		world.addRegionAt(new Desert(10,10), 0, 2);
		world.addRegionAt(new Desert(10, 10), 1, 2);
		world.addRegionAt(new Desert(10,10), 2, 0);
		world.addRegionAt(new Desert(10, 10), 2, 1);
	}
	
	private static void addVillages(World world) {
		world.addRegionAt(new Village(10,10), 2, 2);
		world.addRegionAt(new Village(10,10), 2, 5);
		world.addRegionAt(new Village(10,10), 5, 2);
		world.addRegionAt(new Village(10,10), 5, 5);
	}
	
	private static void addPlains(World world) {
		// The vertical region
		world.addRegionAt(new Plains(10, 10), 0, 3);
		world.addRegionAt(new Plains(10, 10), 0, 4);
		world.addRegionAt(new Plains(10, 10), 1, 3);
		world.addRegionAt(new Plains(10, 10), 1, 4);
		world.addRegionAt(new Plains(10, 10), 2, 3);
		world.addRegionAt(new Plains(10, 10), 2, 4);
		world.addRegionAt(new Plains(10, 10), 3, 3);
		world.addRegionAt(new Plains(10, 10), 3, 4);
		world.addRegionAt(new Plains(10, 10), 4, 3);
		world.addRegionAt(new Plains(10, 10), 4, 4);
		world.addRegionAt(new Plains(10, 10), 5, 3);
		world.addRegionAt(new Plains(10, 10), 5, 4);
		world.addRegionAt(new Plains(10, 10), 6, 3);
		world.addRegionAt(new Plains(10, 10), 6, 4);
		world.addRegionAt(new Plains(10, 10), 7, 3);
		world.addRegionAt(new Plains(10, 10), 7, 4);
		// The horizontal region
		world.addRegionAt(new Plains(10, 10), 3, 0);
		world.addRegionAt(new Plains(10, 10), 4, 0);
		world.addRegionAt(new Plains(10, 10), 3, 1);
		world.addRegionAt(new Plains(10, 10), 4, 1);
		world.addRegionAt(new Plains(10, 10), 3, 2);
		world.addRegionAt(new Plains(10, 10), 4, 2);
		world.addRegionAt(new Plains(10, 10), 3, 5);
		world.addRegionAt(new Plains(10, 10), 4, 5);
		world.addRegionAt(new Plains(10, 10), 3, 6);
		world.addRegionAt(new Plains(10, 10), 4, 6);
		world.addRegionAt(new Plains(10, 10), 3, 7);
		world.addRegionAt(new Plains(10, 10), 4, 7);
	}
	
	public static void main(String[] a) {
		generate();
	}

}
