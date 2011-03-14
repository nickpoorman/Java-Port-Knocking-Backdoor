package test;

/**
 * @(#)Frame.java
 *
 * Frame Applet application
 *
 * @author 
 * @version 1.00 2010/2/15
 */
 
import java.awt.*;
import java.applet.*;

public class Frame extends Applet {
	
	public void init() {
	}

	public void paint(Graphics g) {
		String cost = "The cost is: " + costOfFraming(2, 10);
		g.drawString(cost, 3, 50);
	}	
	
	public double costOfFraming(int width, int height){ 
		
 		double costOfFrame = ((((width+6)*(height+6))-(width*height))*2.50);
		
		double sizeOfBackingCard = (width+5.5)*(height+5.5);

		double costOfBackingCard = sizeOfBackingCard * 1.50;
		double costOfGlass = sizeOfBackingCard * 5.50;
		
		return costOfFrame + costOfBackingCard + costOfGlass;
		
	}	
		
}