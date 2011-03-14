package project;

import head.Server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GUI extends JFrame implements ActionListener {

	BorderLayout layout;
	PixelCanvas pixelCanvas;
	int width = 800;
	//long[][] pixels;

	public GUI() {
		super("CSC375 - Pixel Canvas");
		//this.pixels = pixels;
		initLookAndFeel();
		int inset = 100;
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds(inset, inset,
		// screenSize.width - inset * 2,
		// screenSize.height - inset * 2);

		// Dimension newDimension = new Dimension(600, 700);
		setBounds(inset, inset, (int) width+100, (int) (width / 4)+100);
		//setBounds(inset, inset, (int) width * 5, (int) (width / 4) * 5);

		setResizable(true);
		Container contentPane = this.getContentPane();
		layout = new BorderLayout();
		getContentPane().setLayout(layout);
		 this.setPreferredSize(new Dimension(width, (width / 4)));
		//this.setPreferredSize(new Dimension(width * 5, (width / 4) * 5));
		createComponents();
		initComponents();
		addComponents();

		// then invoke the tree
//		Thread tree = new Thread(new Tree(this, width, (width / 4)));
//		tree.start();
		Server server = new Server(width, this);
		server.start();
	}

	public void createComponents() {
		pixelCanvas = new PixelCanvas(width);
		this.setPreferredSize(new Dimension(width, (int) (width / 4)));
		// JButton button = new JButton("Test");
		// button.setActionCommand("update");
		// button.addActionListener(this);
		// this.add(button, BorderLayout.SOUTH);
	}

	public void initComponents() {

	}

	public void addComponents() {
		this.add(pixelCanvas, BorderLayout.CENTER);
	}

	public void initLookAndFeel() {

	}

	// Quit the application.
	protected void quit() {
		System.exit(0);
	}

	private static void createAndShowGUI() {

		// Make sure we have nice window decorations.
		// JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		GUI frame = new GUI();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);
	}

	// public void invokeGUI() {
	// javax.swing.SwingUtilities.invokeLater(new Runnable() {
	//
	// public void run() {
	// createAndShowGUI();
	// }
	// });
	// }

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("update".equals(e.getActionCommand())) {
			// System.out.println("Calling button");
			// pixelCanvas.setAllPixelsRandom();
			// pixelCanvas.repaint();
			// pixelCanvas.paint(pixelCanvas.getGraphics());
			// pixelCanvas.repaint();
		}

	}

}
