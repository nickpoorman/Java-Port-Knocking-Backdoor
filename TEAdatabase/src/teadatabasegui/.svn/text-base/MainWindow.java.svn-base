package teadatabasegui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

public class MainWindow extends JFrame
        implements ActionListener {

    SpringLayout layout;
    JTabbedPane mainTabPane;
    JTabbedPane companyTabPane;
    JPanel mainPanel;
    JPanel companiesPanel;
    JPanel contactPanel;
    JPanel ratesPanel;
    JPanel infoPanel;
    JPanel plansPanel;
    JPanel toDoListPanel;
    JPanel ratesArchivePanel;
    JScrollPane ratesArchiveScrollPane;
//    JPanel companiesPanelDesktop;

    public MainWindow() {
        super("TEA Database");
        initLookAndFeel();
        int inset = 100;
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setBounds(inset, inset,
//                screenSize.width - inset * 2,
//                screenSize.height - inset * 2);

        Dimension newDimension = new Dimension(1024, 768);
        setBounds(inset, inset, (int) newDimension.getWidth(), (int) newDimension.getHeight());

        setResizable(false);
        Container contentPane = this.getContentPane();
        layout = new SpringLayout();
        getContentPane().setLayout(layout);

        JMenuBar menu = createMenuBar();
        setJMenuBar(menu);

        initComponents();
        setLayout();
    }

    public void setLayout() {
        //set mainTabPane layout
        layout.putConstraint(SpringLayout.WEST, mainTabPane, 0, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, mainTabPane, 0, SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, mainTabPane, 0, SpringLayout.SOUTH, this.getContentPane());
        layout.putConstraint(SpringLayout.EAST, mainTabPane, 0, SpringLayout.EAST, this.getContentPane());

//        //set companyTabPane layout
//        layout.putConstraint(SpringLayout.WEST, companyTabPane, 0, SpringLayout.WEST, companiesPanel);
//        layout.putConstraint(SpringLayout.NORTH, companyTabPane, 0, SpringLayout.NORTH, companiesPanel);
//        layout.putConstraint(SpringLayout.SOUTH, companyTabPane, 0, SpringLayout.SOUTH, companiesPanel);
//        layout.putConstraint(SpringLayout.EAST, companyTabPane, 0, SpringLayout.EAST, companiesPanel);

//        //set companies layout
//        layout.putConstraint(SpringLayout.WEST, companiesPanelDesktop, 0, SpringLayout.WEST, companiesPanel);
//        layout.putConstraint(SpringLayout.NORTH, companiesPanelDesktop, 0, SpringLayout.NORTH, companiesPanel);
//        layout.putConstraint(SpringLayout.SOUTH, companiesPanelDesktop, 0, SpringLayout.SOUTH, companiesPanel);
//        layout.putConstraint(SpringLayout.EAST, companiesPanelDesktop, 0, SpringLayout.EAST, companiesPanel);
    }

    private void initComponents() {
        //add all the pages to the main panel here
        mainTabPane = new JTabbedPane();
//        companiesPanel = new JPanel();
        companiesPanel = new CompaniesPanel();
//        JScrollPane companiesScrollPane = new JScrollPane(companiesPanel);
//        companiesScrollPane.setSize(new Dimension(100,100));
        toDoListPanel = new ToDoListPanel();
       // companiesPanel.setLayout(layout);
      //  toDoListPanel.setLayout(layout);
        mainTabPane.addTab("Companies", companiesPanel);
        mainTabPane.addTab("To Do List", toDoListPanel);
        this.getContentPane().add(mainTabPane);

//        //companies panel
//        companiesPanelDesktop = new CompaniesPanel();
//        companiesPanel.add(companiesPanelDesktop);

//        //company tabPane
//        companyTabPane = new JTabbedPane();
//        //add the pages to the company tabPane
//        contactPanel = new ContactPanel();
//        ratesPanel = new RatesPanel();
//        infoPanel = new InfoPanel();
//        plansPanel = new PlansPanel();
//        ratesArchivePanel = new RatesArchivePanel();
//        ratesArchiveScrollPane = new JScrollPane(ratesArchivePanel);
//        ratesArchiveScrollPane.setSize(new Dimension(100,100));
//
//
//        companyTabPane.addTab("Contact", contactPanel);
//        companyTabPane.addTab("Rates", ratesPanel);
//        companyTabPane.addTab("Info", infoPanel);
//        companyTabPane.addTab("Plans", plansPanel);
//        companyTabPane.addTab("Rates Archive", ratesArchiveScrollPane);

    //add the tabPane to the companyPanel
//        companiesPanel.add(companyTabPane);
    }


    //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("config".equals(e.getActionCommand())) {
            // startConfig();
        } else if ("help".equals(e.getActionCommand())) {
            // open help window
            // Help h = new Help();
            // h.show();
        } else if ("save".equals(e.getActionCommand())) {
            // save all the settings
        } else { //quit
            quit();
        }
    }

    //Quit the application.
    protected void quit() {
        System.exit(0);
    }

    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        //   JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        MainWindow frame = new MainWindow();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void initLookAndFeel() {
        //  String lookAndFeel = "net.sourceforge.napkinlaf.NapkinLookAndFeel";
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

//        //Set up the second menu item.
//        menuItem = new JMenuItem("Help");
//        menuItem.setMnemonic(KeyEvent.VK_H);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_H, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("help");
//        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the third menu item.
//        menuItem = new JMenuItem("Save all settings");
//        menuItem.setMnemonic(KeyEvent.VK_S);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_S, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("save");
//        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the fourth menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
//        menuItem.addActionListener(this);
//        menu.add(menuItem);


        return menuBar;
    }
}
