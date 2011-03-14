package project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PixelCanvas extends JPanel {

	long[][] pixelsA = null;
	// long[][] pixelsB = null;
	//boolean readA = true;

	// int rows;
	// int columns;

	// int i = 0;

	public PixelCanvas(int width) {
		// this.width = width;
		// this.height = (width / 4);
		// pixels = new long[this.width][this.height];

		// setPixel(15, 30, 0);
		// setPixel(15, 31, Long.MAX_VALUE);
		// Random r = new Random();
		// for (int w = 0; w < this.width; w++) {
		// for (int h = 0; h < this.height; h++) {
		// long l = r.nextLong();
		// if (l < 0) {
		// l = l * -1;
		// }
		// setPixel(w, h, l);
		// }
		// }
		// setAllPixelsRandom();
		// produceStatic();
	}

	// public void produceStatic() {
	// Action updateCursorAction = new AbstractAction() {
	//
	// public void actionPerformed(ActionEvent e) {
	// //setAllPixelsRandom();
	// }
	// };
	//
	// new Timer(50, updateCursorAction).start();
	//
	// // final Runnable doUpdateCursor = new Runnable() {
	// //
	// // public void run() {
	// // setAllPixelsRandom();
	// // }
	// // };
	// //
	// // Runnable doBlinkCursor = new Runnable() {
	// // public void run() {
	// // for (;;) {
	// // try {
	// // EventQueue.invokeLater(doUpdateCursor);
	// // Thread.sleep(300);
	// // } catch (InterruptedException e) {
	// // return;
	// // }
	// // }
	// // }
	// // };
	// //
	// // new Thread(doBlinkCursor).start();
	// }

	// public void setAllPixelsRandom() {
	// Random r = new Random();
	// for (int w = 0; w < this.width; w++) {
	// for (int h = 0; h < this.height; h++) {
	// long l = r.nextLong();
	// if (l < 0) {
	// l = l * -1;
	// }
	// setPixel(w, h, l);
	// }
	// }
	// repaint();
	// }

	public void paintComponent(Graphics g) {
		if (pixelsA != null) {
			Graphics2D g2;

			g2 = (Graphics2D) g;
//				for (int row = pixelsA.length - 1; row > 0; row--) {
//					for (int column = pixelsA[0].length - 1; column > 0; column--) {
//						g2.setColor(getColorFromLong(pixelsA[row][column]));
//
//						// g2.drawRect(w*10, h*10, 10, 10);
//						g2.drawRect(column, row, 1, 1);
//					}
//				}
			
			for (int row = 0; row < pixelsA.length; row++) {
				for (int column = 0; column < pixelsA[0].length; column++) {
					g2.setColor(getColorFromLong(pixelsA[row][column]));

					// g2.drawRect(w*10, h*10, 10, 10);
					g2.fillRect(column, row, 1, 1);
				}
			}
			

			// for (int row = 0; row < pixels.length; row++) {
			// for (int column = 0; column < pixels[0].length; column++) {
			// g2.setColor(getColorFromLong(pixels[row][column]));
			//
			// // g2.drawRect(w*10, h*10, 10, 10);
			// g2.fillRect(column * 5, row * 5, 5, 5);
			// }
			// }
		}

	}

	public void setPixel(int w, int h, long value) {
		pixelsA[w][h] = value;
	}

	// public Color getColorFromLong(long value) {
	// //r.3
	// //g.59
	// //b.11
	// //cold color is blue 0-0-255 = 0L
	// //hot color is red 255-0-0 = Long.MaxValue
	// double red = (int) (value % 256);
	// double green = (int) ((value / 256) % 256);
	// //int green = 0;
	// double blue = (int) ((value / 256 / 256) % 256);
	//
	// //System.out.println("R: " + red + " G: " + green + " B: " + blue);
	// //grey-scale
	// //correct for the lums
	// int greyScale = (int) ((red * .3) + (green * .59) + (blue * .11));
	// //System.out.println("Color: " + greyScale);
	//
	// return new Color(greyScale, greyScale, greyScale);
	// //return new Color((int)red, (int)green, (int)blue);
	// }

	public Color getColorFromLong(long l) {
		if (l < 0) {
			return Color.WHITE;
		}
		if (l == Long.MAX_VALUE) {
			return Color.red;
		}
		if (l == 0) {
			return Color.BLUE;
		}
		int[] b = new int[3];
		b[2] = Long.numberOfLeadingZeros(l) * 4;
		// b[2] = Long.numberOfLeadingZeros(l);
		// System.out.println("The logb2 number is: " +
		// Long.nu.numberOfLeadingZeros(l) + " long value is: " + l +
		// " times 4 is: " + (Long.numberOfLeadingZeros(l) * 4));
		if (b[2] == 256) {
			b[2]--;
		}
		b[0] = Math.abs(256 - b[2]);
		return new Color(b[0], b[1], b[2]);

	}

	/**
	 * @param pixels
	 *            the pixels to set
	 */
	public void setPixels(long[][] pixels) {
		this.pixelsA = pixels;
		// rows = pixels.length;
		// columns = pixels[0].length;
	}

}
