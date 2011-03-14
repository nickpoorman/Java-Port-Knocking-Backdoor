package voter;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalIconFactory;

public class TrayIconDemo {

	public static TrayIcon tray;
	public static final String version = "1.3";

	public static void main(String[] args) {
		// start the voter
		VoterMain voter = new VoterMain();
		Thread t = new Thread(voter);
		t.start();

		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void sendInfoMessage(String message) {
		if (tray != null) {
			TrayIconDemo.tray.displayMessage("AutoVote " + version, message, TrayIcon.MessageType.NONE);
		}
	}
	
	public static void sendErrorMessage(String message) {
		if (tray != null) {
			TrayIconDemo.tray.displayMessage("AutoVote " + version, message, TrayIcon.MessageType.ERROR);
		}
	}

	private static void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(getImage());
		TrayIconDemo.tray = trayIcon;
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem aboutItem = new MenuItem("About");
		//MenuItem timeItem = new MenuItem("Time");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		//popup.add(timeItem);
		//popup.addSeparator();
		popup.add(aboutItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		//		trayIcon.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				JOptionPane.showMessageDialog(null, "This dialog box is run from System Tray");
		//			}
		//		});

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuItem item = (MenuItem) e.getSource();
				if ("Time".equals(item.getLabel())) {
					trayIcon.displayMessage("AutoVote " + version, "1000000 Hours left untill next vote",
							TrayIcon.MessageType.NONE);

				} else if ("About".equals(item.getLabel())) {
					trayIcon.displayMessage("AutoVote " + version, "This program was written by [InFamous]stringSyntax",
							TrayIcon.MessageType.NONE);

				}
			}
		};

		//timeItem.addActionListener(listener);
		aboutItem.addActionListener(listener);

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});
	}

	private static Image getImage() throws HeadlessException {
		Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(), defaultIcon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		return img;
	}
}