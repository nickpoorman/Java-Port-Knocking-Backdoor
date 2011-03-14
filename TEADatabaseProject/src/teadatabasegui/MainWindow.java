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
    JPanel calendar;
    private static ConfigFile conf;

    public MainWindow() {
        super("TEA Database");
        initLookAndFeel();
        int inset = 100;

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
    }

    private void initComponents() {
        //add all the pages to the main panel here
        mainTabPane = new JTabbedPane();
        companiesPanel = new CompaniesPanel();
        toDoListPanel = new ToDoListPanel();
        mainTabPane.addTab("Companies", companiesPanel);
        mainTabPane.addTab("To Do List", toDoListPanel);
        this.getContentPane().add(mainTabPane);
    }

    public void actionPerformed(ActionEvent e) {
        if ("config".equals(e.getActionCommand())) {
        } else if ("help".equals(e.getActionCommand())) {
        } else if ("save".equals(e.getActionCommand())) {
        } else {
            quit();
        }
    }

    //Quit the application.
    protected void quit() {
        System.exit(0);
    }

    private static void createAndShowGUI() {
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

    public static synchronized ConfigFile getConf() {
        if (conf == null) {
            conf = new ConfigFile();
        }
        return conf;
    }

    private static void initLookAndFeel() {
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
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

        return menuBar;
    }
}
