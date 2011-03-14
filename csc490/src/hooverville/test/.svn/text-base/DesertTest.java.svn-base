package hooverville.test;

import hooverville.worlds.regions.Desert;
import hooverville.worlds.regions.Region;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;


public class DesertTest extends TestCase {
	
	final Region region = new Desert(10, 10);
	
	@Test
	public void testRegionId() {
		Assert.assertTrue(region.getLocationID() == 1);
	}
	
	@Test
	public void testRegionCapacity() {
		assertEquals(region.regionFull(), false);
	}

}
