import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class displays using Swing a guid interface for an address screen.
 * 
 * @author Nick Poorman
 * 
 */
public class AddressScreen extends JFrame {

	/**
	 * The serial version ID of this GUI.
	 */
	private static final long serialVersionUID = 1L;

	/* All the JLabel objects for the gui */
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JLabel cityLabel;
	private JLabel stateLabel;
	private JLabel zipLabel;

	/* All the JTextField object for the gui */
	private JTextField nameField;
	private JTextField addressField;
	private JTextField cityField;
	private JTextField stateField;
	private JTextField zipField;

	/* All the JButtons object for the gui */
	private JButton addButton;
	private JButton modifyButton;
	private JButton deleteButton;

	/* The global layout object to make things easy */
	private BorderLayout layout;

	/* Globial initial window width and height */
	public static final int WINDOW_WIDTH = 350;
	public static final int WINDOW_HEIGHT = 200;

	public AddressScreen() {
		super("Address Information"); // set the title of the window

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
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		AddressScreen frame = new AddressScreen();

		// set the default close operation of the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Display the window.
		frame.setVisible(true);
	}

	public void createComponents() {
		// create the JLabels with their names
		this.nameLabel = new JLabel("Name");
		this.addressLabel = new JLabel("Address");
		this.cityLabel = new JLabel("City");
		this.stateLabel = new JLabel("State");
		this.zipLabel = new JLabel("Zip");

		// create all the JTextFields
		this.nameField = new JTextField();
		this.addressField = new JTextField();
		this.cityField = new JTextField();
		this.stateField = new JTextField();
		this.zipField = new JTextField();

		// create the JButtons with their names
		this.addButton = new JButton("Add");
		this.modifyButton = new JButton("Modify");
		this.deleteButton = new JButton("Delete");
	}

	public void addComponents() {
		// add the components to the pane
		// looks like a few different JPannels
		JPanel labelsPanel = new JPanel();
		JPanel fieldsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel topPanel = new JPanel();

		// set the layouts of the panels
		topPanel.setLayout(new GridLayout(1, 2));
		labelsPanel.setLayout(new GridLayout(5, 1));
		fieldsPanel.setLayout(new GridLayout(5, 1));

		// start with the JLabels
		labelsPanel.add(this.nameLabel);
		labelsPanel.add(this.addressLabel);
		labelsPanel.add(this.cityLabel);
		labelsPanel.add(this.stateLabel);
		labelsPanel.add(this.zipLabel);

		// then the JTextFields
		fieldsPanel.add(this.nameField);
		fieldsPanel.add(this.addressField);
		fieldsPanel.add(this.cityField);
		fieldsPanel.add(this.stateField);
		fieldsPanel.add(this.zipField);

		// then add the JButtons
		buttonsPanel.add(this.addButton);
		buttonsPanel.add(this.modifyButton);
		buttonsPanel.add(this.deleteButton);

		// now add the panels
		topPanel.add(labelsPanel);
		topPanel.add(fieldsPanel);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(buttonsPanel, BorderLayout.SOUTH);

	}

	private static void initLookAndFeel() {
		try {
			// stub this for now			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
