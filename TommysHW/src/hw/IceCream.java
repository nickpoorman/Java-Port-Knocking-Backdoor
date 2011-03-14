package hw;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class IceCream extends JApplet implements ActionListener, ItemListener {

	private Panel displayResults;
	private JComboBox box1, box2, box3;
	private JButton go;
	private JLabel flavorLabel, scoopsLable, priceLabel;
	String[] cones = { "Select a cone", "cone ", "bowl" };
	String[] flavors = { "Select a flavor", "Strawberry", "Vinilla", "Chocolate" };
	String[] scoops = { "Select number of scoops", "1", "2", "3" };
	String[] pictures = { "conestrawberyy", "conechocolate", "conevinilla", "bowlstrawberry", "bowlvinilla",
			"bowlchocolate" };

	public static final double CONE_PRICE = 1;
	public static final double BOWL_PRICE = 1.20;
	public static final double STRAWBERRY_FLAVOR = 1.30;
	public static final double VINILLA_FLAVOR = 1.50;
	public static final double CHOCOLATE_FLAVOR = 1.75;
	public static final double ONE_SCOOP = .50;
	public static final double TWO_SCOOPS = 1;
	public static final double THREE_SCOOPS = 1.50;

	private double totalPrice = 0;

	public void init() {

		getContentPane().setBackground(Color.WHITE);
		setLayout(new FlowLayout());

		box1 = new JComboBox(cones);
		box1.addActionListener(this);
		box1.addItemListener(this);

		add(box1);

		box2 = new JComboBox(flavors);
		box2.addActionListener(this);
		box2.addItemListener(this);
		add(box2);

		box3 = new JComboBox(scoops);
		box3.addActionListener(this);
		box3.addItemListener(this);
		add(box3);

		go = new JButton(" Go!");
		go.setActionCommand("Go"); //use this to look for which command it belongs to
		go.setForeground(Color.green);
		go.setForeground(Color.GREEN);
		go.addActionListener(this);
		add(go);

		displayResults = new Panel();
		displayResults.setLayout(null);
		displayResults.setBounds(200, 200, 200, 200);

		flavorLabel = new JLabel("Test");
		flavorLabel.setBounds(50, 50, 50, 50);
		displayResults.add(flavorLabel);

		scoopsLable = new JLabel("Test2");
		scoopsLable.setBounds(50, 50, 50, 50);
		displayResults.add(scoopsLable);

		priceLabel = new JLabel("favor");
		priceLabel.setBounds(50, 50, 50, 50);
		displayResults.add(priceLabel);

		add(displayResults);

	}

	public void itemStateChanged(ItemEvent ie) {
		//select the price for the selection (box1)
		System.out.println("this event is firing");
		if (ie.getSource() == box2) {

			switch (box2.getSelectedIndex()) {
			case 1:
				flavorLabel.setText("strawberry");
				break;

			case 2:
				flavorLabel.setText("vinilla");
				break;

			case 3:
				//add the price of chocolate
				flavorLabel.setText("Chocolate");
				break;
			}
		}

		repaint();

		if (ie.getSource() == box3) {
			switch (box3.getSelectedIndex()) {
			//add the price of scoop
			case 1:
				scoopsLable.setText("one");
				break;
			//add the price of two scoops

			case 2:
				scoopsLable.setText("two");
				break;
			//add the price of three scoops
			case 3:
				scoopsLable.setText("three");
				break;

			}

		}

		repaint();
	}

	public void actionPerformed(ActionEvent event) {

		totalPrice = 0;

		if (event.getActionCommand().equals("Go")) {
			// button stuff here

			//select the price for the selection (box1)
			switch (box1.getSelectedIndex()) {
			case 1:
				//add the price for cone 1
				totalPrice += CONE_PRICE;
				break;
			case 2:
				//add the price for cone 2
				totalPrice += BOWL_PRICE;
				break;
			}

			//TODO: select the price for the selection (box2)
			switch (box2.getSelectedIndex()) {
			case 1:
				//add the price of flavor
				totalPrice += STRAWBERRY_FLAVOR;
				break;

			case 2:
				//add the price of flavor
				totalPrice += VINILLA_FLAVOR;
				break;

			case 3:
				//add the price of chocolate
				totalPrice += CHOCOLATE_FLAVOR;
				break;
			}

			switch (box3.getSelectedIndex()) {
			//add the price of scoop
			case 1:
				totalPrice += ONE_SCOOP;
				break;
			//add the price of two scoops

			case 2:
				totalPrice += TWO_SCOOPS;
				break;
			//add the price of three scoops
			case 3:
				totalPrice += THREE_SCOOPS;
				break;

			}

			System.out.println("clicked go!  " + totalPrice);
		}

	}

	public void paint(Graphics g) {
		super.paint(g);

		Image img = getImage(getCodeBase(), "icecreamgirl.jpg");
		g.drawImage(img, 700, 450, this);

		g.setColor(Color.red);
		Font small = new Font("Monospaced", Font.PLAIN, 20);
		g.setFont(small);
		g.drawString("flavor:", 120, 290);
		g.drawString("scoops:", 120, 330);
		g.drawString("price:", 120, 370);
		g.setColor(Color.PINK);
		Font big = new Font("sansSerif", Font.BOLD + Font.ITALIC, 45);
		g.setFont(big);
		g.drawString("Welcome to Tommy Ice Cream Shop", 250, 150);

	}

}