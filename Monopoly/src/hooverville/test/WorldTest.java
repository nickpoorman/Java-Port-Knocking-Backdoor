package hooverville.test;

import hooverville.worlds.World;
import hooverville.worlds.regions.Desert;
import hooverville.worlds.regions.Forest;
import hooverville.worlds.regions.Mountains;
import hooverville.worlds.regions.Region;
import hooverville.worlds.regions.Ruins;

public class WorldTest {
	
	public static void main(String[] a) {
		// Create the world
		World test = new World("Hooverville");
		System.out.println("Name: " + test.getName());
		System.out.println("Size: " + test.size());
		
		// Add a desert
		Region desert = new Desert(10, 10);
		test.addRegionAt(desert, 0, 2);
		System.out.println("World:\n" + test.toString());
		
		// Add a forest
		System.out.println("\n\nAdding Forest. at index 2,3 ..");
		test.addRegionAt(new Forest(10, 10), 2, 3);
		System.out.println(test.toString());
		
		// Add some ruins
		System.out.println("\n\nAdding Ruins...");
		Region ruins = new Ruins(10,10);
		test.addRegion(ruins);
		System.out.println(test.toString());
		
		// Add some mountains
		System.out.println("\n\nAdding Mountains...");
		test.addRegion(new Mountains(10, 10));
		System.out.println(test.toString());
		
		// Add another desert
		System.out.println("\n\nAdding Second Desert...");
		Region desert2 = new Desert(10, 10);
		test.addRegion(desert2);
		System.out.println(test.toString());
		
		// Add a third desert
		System.out.println("\n\nAdding Third Desert...");
		Region desert3 = new Desert(10, 10);
		test.addRegion(desert3);
		System.out.println(test.toString());
		
		// Remove the Second desert
		System.out.println("\n\nRemoving Second Desert...");
		test.removeRegionAt(desert2, 0, 3);
		System.out.println(test.toString());
		
		// Remove all of the deserts
		System.out.println("\n\nRemoving All Deserts...");
		test.removeAllRegionsOfType(desert);
		System.out.println(test.toString());
	}

}
