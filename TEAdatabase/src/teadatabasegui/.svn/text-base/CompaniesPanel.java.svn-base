package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CompaniesPanel extends JPanel implements ActionListener, DocumentListener {

    List<JLabel> companyNameLabels;
    SpringLayout layout;
    int pad;
    JPanel companies;
    // JPanel company;
    JScrollPane companiesScrollPane;
    JTabbedPane openCompaniesTabPane;
    List<List> data;
    JButton newCompanyButton;
    JLabel searchLabel;
    JTextField searchField;

    public CompaniesPanel() {
        openCompaniesTabPane = new JTabbedPane();
        //data holds name at 0 and id at 1
        data = TeaDatabase.getAllCompanyIDandNames();

        // openCompaniesTabPane.setUI(new TestPlaf());
        //  openCompaniesTabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        pad = 20;
        layout = new SpringLayout();
        companies = new JPanel();
        companies.setLayout(layout);
        //company = new JPanel();
        companiesScrollPane = new JScrollPane(companies);
        companiesScrollPane.setSize(new Dimension(100, 100));
        companiesScrollPane.getVerticalScrollBar().setUnitIncrement(pad);

        //first display all the companies with buttons to go to their given page
        //will need a jscollpane to list companies
        //this.setBackground(Color.BLUE);

        this.setLayout(new BorderLayout());
        createComponents();
        addComponentsToCanvas();
        intiLayout();
        updatePreferedSize();
    }

    public void update() {
        for (JLabel l : companyNameLabels) {
            companies.remove(l);
        }
        data = TeaDatabase.getAllCompanyIDandNames();
        //data = TeaDatabase.getAllCompanyIDandNamesForRegex(searchField.getText());
        companyNameLabels = new LinkedList<JLabel>();
        for (int i = 0; i < getNumberOfCompanies(); i++) {
            final String companyName = new String((String) data.get(0).get(i));
            JLabel tmpLabel = new JLabel(companyName);
            final String companyID = new String(data.get(1).get(i).toString());
            tmpLabel.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
//                    if (e.isPopupTrigger()) {
//                        // do work here
//                    }
                    //DO WORK HERE
                    changeToGivenCompanyWindow(new CompanyID(companyID), companyName);
                }
//                public void mousePressed(MouseEvent e) {
//                    //make bold
//                    JLabel tmp = (JLabel) e.getSource();
//                    Font f = tmp.getFont();
//                    tmp.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
//                    tmp.updateUI();
//                }
//
//                public void mouseReleased(MouseEvent e) {
//                    //make normal
//                    JLabel tmp = (JLabel) e.getSource();
//                    Font f = tmp.getFont();
//                    tmp.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
//                    tmp.updateUI();
//                }
            });
            if (tmpLabel.getText().toUpperCase().contains(searchField.getText().toUpperCase())) {
                companyNameLabels.add(tmpLabel);
            }
        }
        for (JLabel l : companyNameLabels) {
            companies.add(l);
        }
        intiLayout();
        updatePreferedSize();
        this.updateUI();
    }

    public void actionPerformed(ActionEvent e) {
        if ("newCompany".equals(e.getActionCommand())) {
            //add the new company
            changeToGivenCompanyWindow(new CompanyID("-1"), "New Company");
        } else if ("SEARCH".equals(e.getActionCommand())) {
            update();
            System.out.println("DING 1");
        }
    }

    public void insertUpdate(DocumentEvent e) {
        update();
        System.out.println("DING 2");
    }

    public void removeUpdate(DocumentEvent e) {
        update();
        System.out.println("DING 3");
    }

    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }

    private void updatePreferedSize() {
        if (companyNameLabels.size() > 0) {
            int labelHeight = (int) companyNameLabels.get(0).getPreferredSize().getHeight();
            int total = pad + ((pad + labelHeight) * (companyNameLabels.size() + 1));
            companies.setPreferredSize(new Dimension(100, total));
        }else{
           companies.setPreferredSize(new Dimension(100, 100));
        }
    }

    private int getNumberOfCompanies() {
        return data.get(0).size();
    }

    private void changeToGivenCompanyWindow(CompanyID companyID, String companyName) {
        int selectedIndex = -1;
        //check to see if the tab is already opened
        for (int i = 0; i < openCompaniesTabPane.getTabCount(); i++) {
            try {
                JTabbedPane tmpPane = (JTabbedPane) openCompaniesTabPane.getComponentAt(i);
                ContactPanel tmpCP = (ContactPanel) tmpPane.getComponentAt(0);
//E                String tmpCompanyID = tmpCP.getCompanyID();
//E                if (tmpCompanyID.equals(companyID)) {
                if (tmpCP.getCompanyID().getCompanyID() == companyID.getCompanyID()) {
                    selectedIndex = i;
                    break;
                }
            } catch (Exception e) {
                //do nothing cause its not what we are looking for
            }

        }
        if (selectedIndex == -1) {
            // JPanel company = new JPanel();
            // company.setLayout(new BorderLayout());
            // company tabPane
            JTabbedPane companyTabPane = new JTabbedPane();
            //add the pages to the company tabPane
            ContactPanel contactPanel = new ContactPanel(openCompaniesTabPane, companyID, this);
            companyID.setContactPanel(contactPanel);
            RatesPanel ratesPanel = new RatesPanel(companyID);
            companyID.setRatesPanel(ratesPanel);
            JScrollPane ratesScrollPane = new JScrollPane(ratesPanel);
            ratesScrollPane.setSize(new Dimension(100, 100));
            ratesScrollPane.getVerticalScrollBar().setUnitIncrement(pad);
            //InfoPanel infoPanel = new InfoPanel();
            PlansPanel plansPanel = new PlansPanel(companyID);
            companyID.setPlansPanel(plansPanel);
            RatesArchivePanel ratesArchivePanel = new RatesArchivePanel(companyID, ratesPanel);
            companyID.setRatesArchivePanel(ratesArchivePanel);
            JScrollPane ratesArchiveScrollPane = new JScrollPane(ratesArchivePanel);
            ratesArchiveScrollPane.getVerticalScrollBar().setUnitIncrement(pad);
            ratesArchiveScrollPane.setSize(new Dimension(100, 100));

            companyTabPane.addTab("Contact", contactPanel);
            companyTabPane.addTab("Rates", ratesScrollPane);
            //companyTabPane.addTab("Info", infoPanel);
            companyTabPane.addTab("Plans", plansPanel);
            companyTabPane.addTab("Rates Archive", ratesArchiveScrollPane);

            // add the tabPane to the companyPanel
            // company.add(companyTabPane, BorderLayout.CENTER);
            // this.remove(companiesScrollPane);
            // this.add(company, BorderLayout.CENTER);
            openCompaniesTabPane.addTab(companyName, companyTabPane);
            openCompaniesTabPane.setSelectedComponent(companyTabPane);
            //refresh
            this.updateUI();
        } else {
            openCompaniesTabPane.setSelectedIndex(selectedIndex);
        }
    }

    private void createComponents() {
        newCompanyButton = new JButton("New Company");
        newCompanyButton.setActionCommand("newCompany");
        newCompanyButton.addActionListener(this);

        companyNameLabels = new LinkedList<JLabel>();
        for (int i = 0; i < getNumberOfCompanies(); i++) {
            final String companyName = new String((String) data.get(0).get(i));
            JLabel tmpLabel = new JLabel(companyName);
            final String companyID = new String(data.get(1).get(i).toString());
            tmpLabel.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
//                    if (e.isPopupTrigger()) {
//                        // do work here
//                    }
                    //DO WORK HERE
                    changeToGivenCompanyWindow(new CompanyID(companyID), companyName);
                }
            });
            companyNameLabels.add(tmpLabel);
        }

        //create the search field and label
        searchLabel = new JLabel("Search");
        searchField = new JTextField(40);
        searchLabel.setLabelFor(searchField);
        searchField.setActionCommand("SEARCH");
        searchField.getDocument().addDocumentListener(this);
        searchField.addActionListener(this);

    }

    private void addComponentsToCanvas() {
        companies.add(newCompanyButton);
        for (JLabel l : companyNameLabels) {
            companies.add(l);
        }
        if (openCompaniesTabPane.getTabCount() == 0) {
            openCompaniesTabPane.addTab("Companies List", companiesScrollPane);
        } else {
            openCompaniesTabPane.setTabComponentAt(0, companiesScrollPane);
        }
        this.add(openCompaniesTabPane, BorderLayout.CENTER);

        // this.add(companiesScrollPane, BorderLayout.CENTER);
//        this.add(company, BorderLayout.CENTER);
//        company.setVisible(false);

        companies.add(searchLabel);
        companies.add(searchField);

    }

    private void intiLayout() {
//        //set the layout for companies and company
//        layout.putConstraint(SpringLayout.WEST, companiesScrollPane, 0, SpringLayout.WEST, this);
//        layout.putConstraint(SpringLayout.NORTH, companiesScrollPane, 0, SpringLayout.NORTH, this);
//        layout.putConstraint(SpringLayout.EAST, companiesScrollPane, 0, SpringLayout.EAST, this);
//        layout.putConstraint(SpringLayout.SOUTH, companiesScrollPane, 0, SpringLayout.SOUTH, this);

//        layout.putConstraint(SpringLayout.WEST, companies, 0, SpringLayout.WEST, companiesScrollPane);
//        layout.putConstraint(SpringLayout.NORTH, companies, 0, SpringLayout.NORTH, companiesScrollPane);
//        layout.putConstraint(SpringLayout.EAST, companies, 0, SpringLayout.EAST, companiesScrollPane);
//        layout.putConstraint(SpringLayout.SOUTH, companies, 0, SpringLayout.SOUTH, companiesScrollPane);

//        layout.putConstraint(SpringLayout.WEST, company, 0, SpringLayout.WEST, this);
//        layout.putConstraint(SpringLayout.NORTH, company, 0, SpringLayout.NORTH, this);
//        layout.putConstraint(SpringLayout.EAST, company, 0, SpringLayout.EAST, this);
//        layout.putConstraint(SpringLayout.SOUTH, company, 0, SpringLayout.SOUTH, this);

        layout.putConstraint(SpringLayout.WEST, newCompanyButton, pad, SpringLayout.WEST, companies);
        layout.putConstraint(SpringLayout.NORTH, newCompanyButton, pad, SpringLayout.NORTH, companies);

        for (int i = 0; i < companyNameLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, companyNameLabels.get(i), pad * 2, SpringLayout.WEST, newCompanyButton);
                layout.putConstraint(SpringLayout.NORTH, companyNameLabels.get(i), pad * 2, SpringLayout.NORTH, newCompanyButton);
            } else {
                layout.putConstraint(SpringLayout.WEST, companyNameLabels.get(i), 0, SpringLayout.WEST, companyNameLabels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, companyNameLabels.get(i), pad, SpringLayout.SOUTH, companyNameLabels.get(i - 1));
            }
        }

        layout.putConstraint(SpringLayout.EAST, searchField, -20, SpringLayout.EAST, companies);
        layout.putConstraint(SpringLayout.SOUTH, searchField, 0, SpringLayout.SOUTH, newCompanyButton);
        layout.putConstraint(SpringLayout.EAST, searchLabel, -10, SpringLayout.WEST, searchField);
        layout.putConstraint(SpringLayout.SOUTH, searchLabel, 0, SpringLayout.SOUTH, newCompanyButton);

    }
}

