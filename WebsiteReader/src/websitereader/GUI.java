package websitereader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

public class GUI extends JFrame
        implements ActionListener {

    public GUI() {
        super("CSC365 - Web Page Categorization Program");

        initLookAndFeel();
        int inset = 0;
        setBounds(inset, inset,
                650,
                500);

        setResizable(true);
        Container contentPane = this.getContentPane();
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);

        JPanel scanning = new JPanel();
        wordScanner = new JLabel("Scanning...");

        JPanel searchPanel = new JPanel();
        searchField = new JTextField("Type in a new URL to be searched here.", 40);
        searchField.addMouseListener(new MouseAdapter() {

            int numClicks = 0;

            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (numClicks == 0) {
                    searchField.selectAll();
                }
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (numClicks == 0) {
                    searchField.setText("http://");
                }
                numClicks++;
            }
        });
        JButton goButton = new JButton("Go");
        goButton.addActionListener(this);
        goButton.setActionCommand("go");

        JButton spider = new JButton("Spider");
        spider.addActionListener(this);
        spider.setActionCommand("spider");

        JPanel historypanel = new JPanel();
        historypanel.setLayout(new BorderLayout());

        historypanel.setBackground(Color.red);
        JScrollPane scrollPane = new JScrollPane();
        historyBox = new JTextArea();
        historyBox.setEditable(false);
        scrollPane.setViewportView(historyBox);

        JPanel sitesParsedPanel = new JPanel();
        JPanel linksLeftPanel = new JPanel();
        JPanel pagesLeftPanel = new JPanel();
        JPanel configLoadingStatusPanel = new JPanel();
        sitesParsed = new JLabel("Sites Parsed: 0");
        linksLeft = new JLabel("Links left to process: 0");
        pagesLeft = new JLabel("Pages left to process: 0");
        configLoadingStatus = new JLabel("Config file loading status: LOADING...");

        sitesParsedPanel.add(sitesParsed);
        linksLeftPanel.add(linksLeft);
        pagesLeftPanel.add(pagesLeft);
        configLoadingStatusPanel.add(configLoadingStatus);
        historypanel.add(scrollPane, BorderLayout.CENTER);
        searchPanel.add(searchField);
        searchPanel.add(goButton);
        searchPanel.add(spider);
        scanning.add(wordScanner);
        contentPane.add(searchPanel);
        contentPane.add(scanning);
        contentPane.add(historypanel);
        contentPane.add(sitesParsedPanel);
        contentPane.add(linksLeftPanel);
        contentPane.add(pagesLeftPanel);
        contentPane.add(configLoadingStatusPanel);


        layout.putConstraint(SpringLayout.EAST, scanning, 1, SpringLayout.EAST, this.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, scanning, 1, SpringLayout.SOUTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WEST, searchPanel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, searchPanel, 10, SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WEST, searchField, 1, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, searchField, 1, SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WEST, goButton, 20, SpringLayout.WEST, searchField);
        layout.putConstraint(SpringLayout.NORTH, goButton, 0, SpringLayout.NORTH, searchField);
        layout.putConstraint(SpringLayout.WEST, spider, 20, SpringLayout.EAST, goButton);
        layout.putConstraint(SpringLayout.NORTH, spider, 0, SpringLayout.NORTH, goButton);
        layout.putConstraint(SpringLayout.WEST, historypanel, 0, SpringLayout.WEST, searchPanel);
        layout.putConstraint(SpringLayout.EAST, historypanel, 0, SpringLayout.EAST, searchPanel);
        layout.putConstraint(SpringLayout.NORTH, historypanel, 10, SpringLayout.SOUTH, searchPanel);
        layout.putConstraint(SpringLayout.SOUTH, historypanel, 200, SpringLayout.SOUTH, searchPanel);
        layout.putConstraint(SpringLayout.NORTH, sitesParsedPanel, 20, SpringLayout.SOUTH, historypanel);
        layout.putConstraint(SpringLayout.WEST, sitesParsedPanel, 0, SpringLayout.WEST, historypanel);
        layout.putConstraint(SpringLayout.WEST, sitesParsed, 10, SpringLayout.WEST, sitesParsedPanel);
        layout.putConstraint(SpringLayout.NORTH, sitesParsed, 10, SpringLayout.NORTH, sitesParsedPanel);
        layout.putConstraint(SpringLayout.WEST, linksLeftPanel, 25, SpringLayout.EAST, sitesParsedPanel);
        layout.putConstraint(SpringLayout.NORTH, linksLeftPanel, 0, SpringLayout.NORTH, sitesParsedPanel);
        layout.putConstraint(SpringLayout.WEST, linksLeft, 10, SpringLayout.WEST, linksLeftPanel);
        layout.putConstraint(SpringLayout.NORTH, linksLeft, 10, SpringLayout.NORTH, linksLeftPanel);
        layout.putConstraint(SpringLayout.WEST, pagesLeftPanel, 25, SpringLayout.EAST, linksLeftPanel);
        layout.putConstraint(SpringLayout.NORTH, pagesLeftPanel, 0, SpringLayout.NORTH, linksLeftPanel);
        layout.putConstraint(SpringLayout.WEST, pagesLeft, 10, SpringLayout.WEST, pagesLeftPanel);
        layout.putConstraint(SpringLayout.NORTH, pagesLeft, 10, SpringLayout.NORTH, pagesLeftPanel);
        layout.putConstraint(SpringLayout.NORTH, configLoadingStatusPanel, 0, SpringLayout.NORTH, pagesLeftPanel);
        layout.putConstraint(SpringLayout.WEST, configLoadingStatus, 10, SpringLayout.WEST, configLoadingStatusPanel);
        layout.putConstraint(SpringLayout.NORTH, configLoadingStatus, 10, SpringLayout.NORTH, configLoadingStatusPanel);
        layout.putConstraint(SpringLayout.EAST, configLoadingStatusPanel, 00, SpringLayout.EAST, historypanel);
        layout.putConstraint(SpringLayout.WEST, configLoadingStatusPanel, 25, SpringLayout.EAST, pagesLeftPanel);
    }

    public void actionPerformed(ActionEvent e) {
        if ("lolz.".equals(e.getActionCommand())) {
        } else if ("go".equals(e.getActionCommand())) {
            //add the link to the list to be searched
            if (this.getPageFetcherManager() != null) {
                String url = searchField.getText();
                this.getPageFetcherManager().addToPriorityLinksQueue(url);
            } else {
                while (this.getPageFetcherManager() == null) {
                    try{
                    Thread.sleep(300);
                    }catch(Exception ex){

                    }
                }
                actionPerformed(e);
            }
        } else if ("spider".equals(e.getActionCommand())) {
            JButton tmp = (JButton) e.getSource();
            if (tmp.getText().equals("Spider")) {
                tmp.setText("SpiderOff");
                this.getPageFetcherManager().setSpider(true);
            } else {
                tmp.setText("Spider");
                this.getPageFetcherManager().setSpider(false);
            }
        } else {
            quit();
        }
    }

    protected void quit() {
        System.exit(0);
    }

    public void updateWordScannerLabel(String word) {
        wordScanner.setText("Scanning... " + word);
    }

    public void updateWordScannerLabelDone() {       
        wordScanner.setText(wordScanner.getText() + " " + "Done!");
    }

    public void setPageFetcherManager(PageFetcherManager p) {
        this.pageFetcherManager = p;
    }

    /**
     * @return the pageFetcherManager
     */
    public PageFetcherManager getPageFetcherManager() {
        return pageFetcherManager;
    }

    public void addToHistory(String line) {
        historyBox.append(line + "\n");
        historyBox.setCaretPosition(historyBox.getText().length());
    }

    private static void initLookAndFeel() {       
        String lookAndFeel = "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * @return the sitesParsed
     */
    public JLabel getSitesParsed() {
        return sitesParsed;
    }

    public void updateSitesParsed() {
        this.sitesParsed.setText("Sites parsed: " + pageFetcherManager.getSitesParsed());
    }

    /**
     * @return the linksLeft
     */
    public JLabel getLinksLeft() {
        return linksLeft;
    }

    public void updateLinksLeft() {
        this.linksLeft.setText("Links left to process: " + pageFetcherManager.getLinks().size());
    }

    /**
     * @return the pagesLeft
     */
    public JLabel getPagesLeft() {
        return pagesLeft;
    }

    public void updatePagesLeft() {
        this.pagesLeft.setText("Pages left to process: " + pageFetcherManager.getQueue().size());
    }

    /**
     * @return the configLoadingStatus
     */
    public JLabel getConfigLoadingStatus() {
        return configLoadingStatus;
    }

    public void updateConfigLoadingStatus(boolean b) {
        if (b) {
            this.configLoadingStatus.setText("Config file loading status: DONE!");
        } else {
            this.configLoadingStatus.setText("Config file loading status: DONE!");
        }
    }
    public JLabel wordScanner;
    static volatile PageFetcherManager pageFetcherManager;
    private JTextField searchField;
    private JTextArea historyBox;
    private JLabel sitesParsed;
    private JLabel linksLeft;
    private JLabel pagesLeft;
    private JLabel configLoadingStatus;
}