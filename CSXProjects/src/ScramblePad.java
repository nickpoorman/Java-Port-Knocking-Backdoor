import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * This class displays using Swing a GUI interface for a DCS Scramble Pad.
 * 
 * @author Nick Poorman
 * 
 */
public class ScramblePad extends JFrame implements ActionListener {

	/**
	 * The serial version ID of this GUI.
	 */
	private static final long serialVersionUID = 1L;

	/* All the JLabel objects for the gui */
	private JLabel topLabel;

	/* All the JButtons object for the gui */
	private List<JButton> numButtons;
	private JButton startButton;
	private JButton okayButton;

	/* The global layout object to make things easy */
	private BorderLayout layout;

	/* Globial initial window width and height */
	public static final int WINDOW_WIDTH = 200;
	public static final int WINDOW_HEIGHT = 300;

	/* The number of buttons on the pad */
	public static final int NUMBER_OF_NUM_BUTTONS = 10;

	/* Grid layout width */
	public static final int GRID_LAYOUT_WIDTH = 3;

	/* Grid layout height */
	public static final int GRID_LAYOUT_HEIGHT = 4;

	/* The secret code to the lock */
	private String secretCode = "";

	/* Current secret code sequence */
	private String secretCodeSequence = "";

	public ScramblePad(String code) {
		super("DCS Scramble Pad"); // set the title of the window

		// set the secret code of the program
		this.secretCode = code;

		// set the look and feel of the GUI
		initLookAndFeel();

		// create the inset for window at 100 x 100px from the top left
		int inset = 100;

		// create the initial dimension of the window
		Dimension newDimension = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

		// use the setBounds method to set the dimension and inset
		setBounds(inset, inset, (int) newDimension.getWidth(),
				(int) newDimension.getHeight());

		// set the window to be resizable
		setResizable(true);

		// get the current content pane
		Container contentPane = this.getContentPane();

		// create a new layout object
		layout = new BorderLayout();

		// set the layout to be a border layout
		getContentPane().setLayout(layout);

		// set the preferred size of the winow
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

		// create the various components of the window
		createComponents();

		// add all the components to the window's pane
		addComponents();
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out
					.println("You must enter a number as an argument to the program.");
			return;
		}

		int code = -1;
		// check the argument to make sure its a number
		try {
			code = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out
					.println("You did not enter a valid number to be the code.");
			return;
		}

		// now check the number is valid for the pad
		if (code < 0) {
			System.out
					.println("You must enter a positive number to be the code.");
			return;
		}

		final String sCode = code + "";

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI(sCode);
			}
		});
	}

	private static void createAndShowGUI(String code) {
		//Create and set up the window.
		ScramblePad frame = new ScramblePad(code);

		// set the default close operation of the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Display the window.
		frame.setVisible(true);
	}

	public void createComponents() {
		// create the top JLabel with its name and color
		this.topLabel = new JLabel("LOCKED");
		this.topLabel.setForeground(Color.RED);

		// instantiate the list of buttons
		this.numButtons = new LinkedList<JButton>();

		// create all the JButtons
		for (int i = 0; i < NUMBER_OF_NUM_BUTTONS; i++) {
			// create the new button
			JButton b = new JButton(i + "");

			// set the action command to be the button's number
			b.setActionCommand(b.getText());

			// set the listener for this button to be the implemented
			// action performed method in this class
			b.addActionListener(this);

			// add the button to the list of buttons
			this.numButtons.add(b);
		}

		// create bottom JButtons with their names
		this.startButton = new JButton("Start");

		// set the command for this button to be the text of the button
		this.startButton.setActionCommand(this.startButton.getText());
		// set the action listener to be this implementation of actionPerformed
		this.startButton.addActionListener(this);

		// set the button color to light gray
		this.startButton.setBackground(Color.LIGHT_GRAY);

		this.okayButton = new JButton("Okay");
		// set the command for this button to be the text of the button
		this.okayButton.setActionCommand(this.okayButton.getText());
		// set the action listener to be this implementation of actionPerformed
		this.okayButton.addActionListener(this);

		// set the button color to light gray
		this.okayButton.setBackground(Color.LIGHT_GRAY);
	}

	public void addComponents() {
		// add the components to the pane
		// looks like a few different JPannels
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();

		// set the layouts of the panels		
		centerPanel.setLayout(new GridLayout(GRID_LAYOUT_HEIGHT,
				GRID_LAYOUT_WIDTH));

		//set the backgrounds of the panels
		topPanel.setBackground(Color.BLACK);
		centerPanel.setBackground(Color.BLACK);
		bottomPanel.setBackground(Color.BLACK);

		// start with adding the JLabel
		topPanel.add(this.topLabel);

		// then the number JButtons
		// save a reference to the JPanel that holds 0
		JPanel zeroPanel = null;

		// this loop does a little hack to put the buttons where
		// 	they are supposed to go on the grid
		for (int i = 0; i < (GRID_LAYOUT_HEIGHT * GRID_LAYOUT_WIDTH); i++) {
			JPanel tmp = new JPanel();
			tmp.setBackground(Color.BLACK);
			tmp.setBorder(LineBorder.createGrayLineBorder());
			tmp.setLayout(new BorderLayout());

			if (i == 10) {
				// save the reference to the JPanel
				zeroPanel = tmp;
				JButton b = this.numButtons.get(0);
				b.setBackground(Color.BLACK);
				b.setForeground(Color.YELLOW);
			}

			if (i < 9) {
				JButton b = this.numButtons.get(i + 1);
				b.setBackground(Color.BLACK);
				b.setForeground(Color.YELLOW);
				tmp.add(b);
			}

			centerPanel.add(tmp, BorderLayout.CENTER);
		}

		// now add the zero button		
		zeroPanel.add(this.numButtons.get(0));

		// then add the bottom JButtons
		bottomPanel.add(this.startButton);
		bottomPanel.add(this.okayButton);

		// now add the panels		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	private static void initLookAndFeel() {
		try {
			// stub this for now
			// UIManager.setLookAndFeel("");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Print out the given action command for the button. 
	 * 
	 * Note: I am going to assume that in the directions where it says:
	 *  "Place the scramble pad in the locked state" when the 0-9 buttons are
	 *  pressed that it means set the label to LOCKED.
	 */
	public void actionPerformed(ActionEvent event) {
		// set the topLabel to say "LOCKED"
		this.topLabel.setText("LOCKED");
		this.topLabel.setForeground(Color.RED);

		// check if the button is start
		if (event.getActionCommand().equals(this.startButton.getText())) {
			// reset the secretCodeSequence to be blank
			this.secretCodeSequence = "";
			return;
		} else if (event.getActionCommand().equals(this.okayButton.getText())) {
			if (this.secretCodeSequence.equals(secretCode)) {
				// the code is correct
				this.topLabel.setText("OPEN");
				this.topLabel.setForeground(Color.GREEN);
			}
			// reset the secretCodeSequence to be blank
			this.secretCodeSequence = "";
			return;
		}

		// maybe its a number button?
		// get the number of the button pressed
		try {
			int buttonNumber = Integer.parseInt(event.getActionCommand());
			// if its a valid number button
			if (buttonNumber < NUMBER_OF_NUM_BUTTONS) {
				// append it to the current sequence
				this.secretCodeSequence += buttonNumber;
			}
			return;
		} catch (NumberFormatException e) {
		}
		// Guess its not a number just print out the text
	}
}
