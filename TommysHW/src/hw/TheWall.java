package hw;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class TheWall extends Applet {

	Random r = new Random();

	public void init() {
		this.setSize(300, 300);
	}

	public void paint(Graphics g) {
		int blockWidth = 25;
		int blockHeight = 10;
		int halfBlockWidth = (blockWidth / 2);

		int xMax = 10;
		int yMax = 30;

		Graphics2D gr = (Graphics2D) g;
		for (int i = 0; i < xMax; i++) {
			for (int j = 0; j < yMax; j++) {
				//Color c = new Color(r.nextInt(254), r.nextInt(254), r.nextInt(254));
				Color c = Color.red;
				gr.setColor(c);
				if (j % 2 == 0) {
					gr.fill3DRect(i * blockWidth, j * blockHeight, blockWidth, blockHeight, true);
					if (i == (xMax - 1)) {
						gr.fill3DRect((i+1) * blockWidth, j * blockHeight, halfBlockWidth, blockHeight, true);
					}
				} else {
					if (i == 0) {
						gr.fill3DRect(i * halfBlockWidth, j * blockHeight, halfBlockWidth, blockHeight, true);
					}
					gr.fill3DRect((i * blockWidth) + (blockWidth / 2), (j * blockHeight), blockWidth, blockHeight, true);

				}
			}
		}
	}
}
