import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class DialogViewer {

	/**
	 * Main entry point of the program. Handles all the cases.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// the user did not enter any command line arguments
		if (args.length != 1) {
			System.out.println("You did not enter a"
					+ " command line argument.\n"
					+ "Please enter either:\n f\n c\n m\nas an argument.");
			// get out of the program cleanly
			return;
		}

		if (args[0].equals("f")) {
			// call the file chooser method
			doFileChooser();

		} else if (args[0].equals("c")) {
			// call the confirm dialog method
			doConfirmDialog();

		} else if (args[0].equals("m")) {
			// call the message dialog method
			doMessageDialog();

		} else {
			// the user didn't enter a proper command line argument
			System.out.println("You did not enter a"
					+ " valid command line argument.");
		}

		// this really isn't needed but the lab write-up says it is.
		System.exit(0);
	}

	/**
	 * Static method that prompts the user to select a file from their system,
	 * and then either prints out the name of the file selected or prints out
	 * that they canceled the selection.
	 */
	private static void doFileChooser() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		File selectedFile = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = fc.getSelectedFile();
			System.out.println("You selected the file: "
					+ selectedFile.getName());
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("You did not select a file.");
		}
	}

	/**
	 * Static method that displays a confirm dialog and prints out the option
	 * that was selected to the user, via stdout. (Yes, No, Cancel).
	 */
	private static void doConfirmDialog() {
		int response = JOptionPane.showConfirmDialog(null, "Are you sure?");
		if (response == JOptionPane.YES_OPTION) {
			System.out.println("You pressed the YES option.");
		} else if (response == JOptionPane.NO_OPTION) {
			System.out.println("You pressed the NO option.");
		} else if (response == JOptionPane.CANCEL_OPTION) {
			System.out.println("You pressed the CANCEL option.");
		}
	}

	/**
	 * Static method that displays my name in a dialog window.
	 */
	private static void doMessageDialog() {
		JOptionPane.showMessageDialog(null, "Nick Poorman");
	}

}
