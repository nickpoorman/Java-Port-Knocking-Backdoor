package teadatabasegui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ContactPanel extends JPanel implements ActionListener {

    private CompanyID companyID;
    List<JLabel> labels;
    List<JTextField> fields;
    List<String> companyInfo;
    SpringLayout layout;
    List<JLabel> firstColumnLabels;
    List<JTextField> firstColumnFields;
    List<JLabel> secondColumnLabels;
    List<JTextField> secondColumnFields;
    List<JLabel> thirdColumnLabels;
    List<JTextField> thirdColumnFields;
    List<JLabel> fourthColumnLabels;
    List<JTextField> fourthtColumnFields;
    JTabbedPane openCompaniesTabPane;
    CompaniesPanel companiesPanel;
    JButton close;
    JButton save;
    int statusDropDownID = 1;
    JComboBox statusDropDown;
    String statusDropDownName;
    List<String> statusDropDownOptions;
    JLabel statusLabel;
    int statusSavedOption = 1;
    int groupSizeDropDownID = 2;
    JComboBox groupSizeDropDown;
    String groupSizeName;
    List<String> groupSizeDropDownOptions;
    JLabel groupSizeLabel;
    int groupSizeSavedOption;
    JPanel bottomPanel;
    ContactPanelBottomOwners contactPanelBottomOwners;
    ContactPanelBottomGroupNumber contactPanelBottomGroupNumber;
    JScrollPane bottomScrollPaneOwners;
    JScrollPane bottomScrollPaneGroupNumber;
    JButton groupSizeDropDownEditButton;
    JButton statusDropDownEditButton;

    public ContactPanel(JTabbedPane openCompaniesTabPane, CompanyID companyID, CompaniesPanel companiesPanel) {
        this.companiesPanel = companiesPanel;
        this.openCompaniesTabPane = openCompaniesTabPane;
        this.companyID = companyID;
        labels = new LinkedList<JLabel>();
        fields = new LinkedList<JTextField>();

        firstColumnLabels = new LinkedList<JLabel>();
        firstColumnFields = new LinkedList<JTextField>();
        secondColumnLabels = new LinkedList<JLabel>();
        secondColumnFields = new LinkedList<JTextField>();
        thirdColumnLabels = new LinkedList<JLabel>();
        thirdColumnFields = new LinkedList<JTextField>();
        fourthColumnLabels = new LinkedList<JLabel>();
        fourthtColumnFields = new LinkedList<JTextField>();

        //querry for the drop down information
        //get the drop down name
        statusDropDownName = TeaDatabase.getDropDownName(statusDropDownID);
        //get the drop down options for the given drop down
        statusDropDownOptions = TeaDatabase.getDropDownOptions(statusDropDownID);
        //querry for the group size drop down
        groupSizeName = TeaDatabase.getDropDownName(groupSizeDropDownID);
        //get the drop down options for the given drop down
        groupSizeDropDownOptions = TeaDatabase.getDropDownOptions(groupSizeDropDownID);

        if (!companyID.isNewCompany()) {
            companyInfo = TeaDatabase.getCompanyInfo(companyID.getCompanyIDString());
            companyInfo.remove(0);
        }
        layout = new SpringLayout();
        this.setLayout(layout);
        createComponents();
        setFieldSizes();
        addComponentsToCanvas();
        intiLayout();
        if (!companyID.isNewCompany()) {
            fillValues();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ("close".equals(e.getActionCommand())) {
            int index = openCompaniesTabPane.getSelectedIndex();
            openCompaniesTabPane.remove(index);
        } else if ("save".equals(e.getActionCommand())) {
            save();
            companiesPanel.update();
        } else if ("editGroupSizeDropDown".equals(e.getActionCommand())) {
            //open a new window for the drop down editor
            DropDownEditor.showDropDownEditorWindow(groupSizeDropDownID, this.getCompanyID());
            //openNewDropDownEditorWindow(groupSizeDropDownID);
        } else if ("editStatusDropDown".equals(e.getActionCommand())) {
            //open a new window for the drop down editor
            DropDownEditor.showDropDownEditorWindow(statusDropDownID, this.getCompanyID());
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    private void save() {
        boolean newCompany = this.companyID.isNewCompany();
        boolean notChanged = true;
        //first check to see if the name has changed:
        if (!fields.get(0).getText().equals("")) {
            if (!openCompaniesTabPane.getTitleAt(openCompaniesTabPane.getSelectedIndex()).equals(
                    fields.get(0).getText())) {
                //open a popup to continue
                int selection = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you would you like to change the company name? (No will cancel save on all fields)",
                        "Confirm Change", JOptionPane.YES_NO_OPTION);
                if (selection != JOptionPane.YES_OPTION) {
                    notChanged = false;
                }
            }
            if (notChanged != false) {
                //check to see if the status has changed
                if (statusSavedOption != (statusDropDown.getSelectedIndex() + 1)) {
                    int selection = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you would you like to change the status drop down? (No will cancel save on all fields)",
                            "Confirm Change", JOptionPane.YES_NO_OPTION);
                    if (selection != JOptionPane.YES_OPTION) {
                        notChanged = false;
                    }
                }
            }

            if (notChanged) {
                statusSavedOption = (statusDropDown.getSelectedIndex() + 1);
                //do all the stuff for saving the information here
                //first get all the fields into a list
                List<String> list = new LinkedList<String>();
                for (int i = 0; i < fields.size(); i++) {
                    list.add(fields.get(i).getText());
                }
                if (this.companyID.isNewCompany()) {
                    //insert the new information
                    //then pass the list into the insert
                    companyID.setCompanyID(TeaDatabase.insertCompanyInfo(list));
                    openCompaniesTabPane.setTitleAt(openCompaniesTabPane.getSelectedIndex(), fields.get(0).getText());
                    //save the status for insert
                    TeaDatabase.insertSelectedDropDownsOption(statusDropDown.getSelectedIndex() + 1, statusDropDownID,
                            companyID.getCompanyID());
                    TeaDatabase.insertSelectedDropDownsOption(groupSizeDropDown.getSelectedIndex() + 1,
                            groupSizeDropDownID, companyID.getCompanyID());
                    //make the buttons on the rates and archive rates pages ena
                    for (int i = 1; i < fields.size(); i++) {
                        fields.get(i).setEnabled(true);
                    }
                    statusDropDown.setEnabled(true);
                    groupSizeDropDown.setEnabled(true);

                } else {
                    //update the new information                   
                    TeaDatabase.updateForCompanyDetails(this.getCompanyID().getCompanyID(), list);
                    openCompaniesTabPane.setTitleAt(openCompaniesTabPane.getSelectedIndex(), fields.get(0).getText());
                    //save the status for update
                    TeaDatabase.updateSavedDropDowns(statusDropDown.getSelectedIndex() + 1, statusDropDownID, companyID.getCompanyID());
                    TeaDatabase.updateSavedDropDowns(groupSizeDropDown.getSelectedIndex() + 1, groupSizeDropDownID,
                            companyID.getCompanyID());
                }

                if (!newCompany) {
                    this.contactPanelBottomOwners.save();
                    this.contactPanelBottomGroupNumber.save();

                } else {
                    //update the bottom two panels because they never got created
                    //first remove the bottom panels then create them and add them
                    this.remove(bottomPanel);
                    createBottomPanels();
                    this.add(bottomPanel);
                    bottomPanel.add(bottomScrollPaneOwners);
                    bottomPanel.add(bottomScrollPaneGroupNumber);
                    intiLayout();
                    this.updateUI();
                    //update the rest of the panels as well
                    //init the rates panel
                    companyID.getRatesPanel().callUpdate();
                    //init the rates archive panel
                    companyID.getRatesArchivePanel().callUpdate();
                    //insert the new information for the plans panel blank
                    companyID.getPlansPanel().insertNewPanelIntoDB();
                    companyID.getPlansPanel().callUpdate();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must enter a name for the company.");
        }
    }

    private void fillValues() {
        for (int i = 0; i < labels.size(); i++) {
            fields.get(i).setText(companyInfo.get(i));
        }

        //change the selected option for the status drop down
        //get the selected index
        statusSavedOption = TeaDatabase.getSavedOption(statusDropDownID, companyID.getCompanyID());
        statusDropDown.setSelectedIndex(statusSavedOption - 1);

        //change the select option for the group size drop down
        groupSizeSavedOption = TeaDatabase.getSavedOption(groupSizeDropDownID, companyID.getCompanyID());
        groupSizeDropDown.setSelectedIndex(groupSizeSavedOption - 1);
    }

    private void createComponents() {
        //create the status drop down
        statusLabel = new JLabel("Status:");
        //fill the values for the status drop down
        statusDropDown = new JComboBox(statusDropDownOptions.toArray());
        this.getCompanyID().addDropDown(new Integer(statusDropDownID), statusDropDown);
        //create the group size drop down
        groupSizeLabel = new JLabel("Group Size:");
        //fill the values for the group size drop down
        groupSizeDropDown = new JComboBox(groupSizeDropDownOptions.toArray());
        this.getCompanyID().addDropDown(new Integer(groupSizeDropDownID), groupSizeDropDown);

        close = new JButton("Close");
        close.setActionCommand("close");
        close.addActionListener(this);
        save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);

        /*
         * LEFT SIDE: 0, 1, 3, 6, 10, 14, 18
         *
         * SECOND COLUMN: 2, 4, 7, 11, 15, 19
         *
         * THIRD COLUMN: 5, 8, 12, 16, 20
         *
         * FOURTH COLUMN: 9, 13, 17
         */

        labels.add(new JLabel("Company Name:"));
        labels.add(new JLabel("Contact First Name:"));
        labels.add(new JLabel("Last Name:"));
        labels.add(new JLabel("Contact Phone:"));
        labels.add(new JLabel("Contact Cell:"));
        labels.add(new JLabel("Contact Email:"));
        labels.add(new JLabel("Street:"));
        labels.add(new JLabel("City:"));
        labels.add(new JLabel("State:"));
        labels.add(new JLabel("Zip:"));
        labels.add(new JLabel("Alt Mailing Address:"));
        labels.add(new JLabel("City:"));
        labels.add(new JLabel("State:"));
        labels.add(new JLabel("Zip:"));
        labels.add(new JLabel("Billing Address:"));
        labels.add(new JLabel("City:"));
        labels.add(new JLabel("State:"));
        labels.add(new JLabel("Zip:"));
        labels.add(new JLabel("Billing Contact:"));
        labels.add(new JLabel("Phone:"));
        labels.add(new JLabel("Email:"));
        for (JLabel l : labels) {
            JTextField tmp = new JTextField(20);
            if (l.getText().equals("State:")) {
                tmp.setColumns(10);
            }
            fields.add(tmp);
            l.setLabelFor(tmp);
        }

        for (int i = 0; i < labels.size(); i++) {
            if (i == 0 || i == 1 || i == 3 || i == 6 || i == 10 || i == 14 || i == 18) {
                firstColumnLabels.add(labels.get(i));
                firstColumnFields.add((JTextField) labels.get(i).getLabelFor());
            } else if (i == 2 || i == 4 || i == 7 || i == 11 || i == 15 || i == 19) {
                secondColumnLabels.add(labels.get(i));
                secondColumnFields.add((JTextField) labels.get(i).getLabelFor());
            } else if (i == 5 || i == 8 || i == 12 || i == 16 || i == 20) {
                thirdColumnLabels.add(labels.get(i));
                thirdColumnFields.add((JTextField) labels.get(i).getLabelFor());
            } else if (i == 9 || i == 13 || i == 17) {
                fourthColumnLabels.add(labels.get(i));
                fourthtColumnFields.add((JTextField) labels.get(i).getLabelFor());
            }
        }

        //create the bottom scroll panes
        createBottomPanels();

        //if it is a new company then disable everything (except the name field) on the panel
        if (this.companyID.isNewCompany()) {
            for (int i = 1; i < fields.size(); i++) {
                fields.get(i).setEnabled(false);
            }
            statusDropDown.setEnabled(false);
            groupSizeDropDown.setEnabled(false);
        }

        //the edit button for the groupSizeDropDown
        groupSizeDropDownEditButton = new JButton("+");
        groupSizeDropDownEditButton.setFont(groupSizeDropDownEditButton.getFont().deriveFont(9f));
        groupSizeDropDownEditButton.setPreferredSize(new Dimension(20, 100));
        groupSizeDropDownEditButton.setActionCommand("editGroupSizeDropDown");
        groupSizeDropDownEditButton.addActionListener(this);

        //the edit button for the statusDropDown
        statusDropDownEditButton = new JButton("+");
        statusDropDownEditButton.setFont(statusDropDownEditButton.getFont().deriveFont(9f));
        statusDropDownEditButton.setPreferredSize(new Dimension(20, 100));
        statusDropDownEditButton.setActionCommand("editStatusDropDown");
        statusDropDownEditButton.addActionListener(this);

    }

    private void createBottomPanels() {
        //create the bottom jpannel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2));
        //create the bottom scroll pane
        contactPanelBottomOwners = new ContactPanelBottomOwners(this.companyID);
        bottomScrollPaneOwners = new JScrollPane(contactPanelBottomOwners);
        bottomScrollPaneOwners.setSize(new Dimension(100, 100));
        bottomScrollPaneOwners.getVerticalScrollBar().setUnitIncrement(16);

        contactPanelBottomGroupNumber = new ContactPanelBottomGroupNumber(this.companyID);
        bottomScrollPaneGroupNumber = new JScrollPane(contactPanelBottomGroupNumber);
        bottomScrollPaneGroupNumber.setSize(new Dimension(100, 100));
        bottomScrollPaneGroupNumber.getVerticalScrollBar().setUnitIncrement(16);

    }

    private void addComponentsToCanvas() {
        this.add(statusLabel);
        this.add(statusDropDown);
        this.add(groupSizeLabel);
        this.add(groupSizeDropDown);
        this.add(close);
        this.add(save);
        for (JLabel l : labels) {
            this.add(l);
            this.add(l.getLabelFor());
        }
        this.add(bottomPanel);
        bottomPanel.add(bottomScrollPaneOwners);
        bottomPanel.add(bottomScrollPaneGroupNumber);
        this.add(groupSizeDropDownEditButton);
        this.add(statusDropDownEditButton);

    }

    private int findLongestLabel(List<JLabel> list) {
        int longest = 0;
        int pos = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().length() > longest) {
                longest = list.get(i).getText().length();
                pos = i;
            }
        }
        return pos;
    }

    private void setFieldSizes() {
        for (JTextField t : firstColumnFields) {
            t.setColumns(20);
        }
        for (JTextField t : secondColumnFields) {
            t.setColumns(15);
        }
        for (JTextField t : thirdColumnFields) {
            t.setColumns(2);
        }
        for (JTextField t : fourthtColumnFields) {
            t.setColumns(5);
        }
        thirdColumnFields.get(0).setColumns(30);
        thirdColumnFields.get(thirdColumnFields.size() - 1).setColumns(30);
    }

    private void intiLayout() {
        int firstColumnLongestLabel = findLongestLabel(firstColumnLabels);
        int secondColumnLongestLabel = findLongestLabel(secondColumnLabels);
        int thirdColumnLongestLabel = findLongestLabel(thirdColumnLabels);
        int fourthColumnLongestLabel = findLongestLabel(fourthColumnLabels);
        for (int i = 0; i < firstColumnLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, firstColumnLabels.get(i), 60, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, firstColumnLabels.get(i), 50, SpringLayout.NORTH, this);
                layout.putConstraint(SpringLayout.WEST, firstColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        firstColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, firstColumnLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH,
                        firstColumnLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, firstColumnLabels.get(i), 0, SpringLayout.WEST,
                        firstColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, firstColumnLabels.get(i), 20, SpringLayout.SOUTH,
                        firstColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, firstColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        firstColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, firstColumnLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH,
                        firstColumnLabels.get(i));
            }
        }

        for (int i = 0; i < secondColumnLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, secondColumnLabels.get(i), 30, SpringLayout.EAST,
                        firstColumnLabels.get(1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, secondColumnLabels.get(i), 0, SpringLayout.NORTH,
                        firstColumnLabels.get(1));
                layout.putConstraint(SpringLayout.WEST, secondColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        secondColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, secondColumnLabels.get(i).getLabelFor(), 0,
                        SpringLayout.SOUTH, secondColumnLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, secondColumnLabels.get(i), 0, SpringLayout.WEST,
                        secondColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, secondColumnLabels.get(i), 20, SpringLayout.SOUTH,
                        secondColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, secondColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        secondColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, secondColumnLabels.get(i).getLabelFor(), 0,
                        SpringLayout.SOUTH, secondColumnLabels.get(i));
            }
        }
        for (int i = 0; i < thirdColumnLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, thirdColumnLabels.get(i), 30, SpringLayout.EAST,
                        secondColumnLabels.get(1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, thirdColumnLabels.get(i), 0, SpringLayout.NORTH,
                        secondColumnLabels.get(1));
                layout.putConstraint(SpringLayout.WEST, thirdColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        thirdColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, thirdColumnLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH,
                        thirdColumnLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, thirdColumnLabels.get(i), 0, SpringLayout.WEST,
                        thirdColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, thirdColumnLabels.get(i), 20, SpringLayout.SOUTH,
                        thirdColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, thirdColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        thirdColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, thirdColumnLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH,
                        thirdColumnLabels.get(i));
            }
        }
        for (int i = 0; i < fourthColumnLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.EAST, fourthColumnLabels.get(i).getLabelFor(), 0, SpringLayout.EAST,
                        thirdColumnLabels.get(0).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, fourthColumnLabels.get(i), 0, SpringLayout.NORTH,
                        thirdColumnLabels.get(1));
                layout.putConstraint(SpringLayout.WEST, fourthColumnLabels.get(i), 80, SpringLayout.EAST,
                        thirdColumnLabels.get(1).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, fourthColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        fourthColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, fourthColumnLabels.get(i).getLabelFor(), 0,
                        SpringLayout.SOUTH, fourthColumnLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, fourthColumnLabels.get(i), 0, SpringLayout.WEST,
                        fourthColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, fourthColumnLabels.get(i), 20, SpringLayout.SOUTH,
                        fourthColumnLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, fourthColumnLabels.get(i).getLabelFor(), 10, SpringLayout.EAST,
                        fourthColumnLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, fourthColumnLabels.get(i).getLabelFor(), 0,
                        SpringLayout.SOUTH, fourthColumnLabels.get(i));
            }
        }

        //set all the fields to line up
        for (int i = 0; i < firstColumnLabels.size(); i++) {
            if (i != firstColumnLongestLabel) {
                layout.putConstraint(SpringLayout.WEST, firstColumnLabels.get(i).getLabelFor(), 0, SpringLayout.WEST,
                        firstColumnLabels.get(firstColumnLongestLabel).getLabelFor());
                layout.putConstraint(SpringLayout.EAST, firstColumnLabels.get(i).getLabelFor(), 0, SpringLayout.EAST,
                        firstColumnLabels.get(firstColumnLongestLabel).getLabelFor());
            }
        }

        for (int i = 0; i < secondColumnLabels.size(); i++) {
            if (i != secondColumnLongestLabel) {
                layout.putConstraint(SpringLayout.WEST, secondColumnLabels.get(i).getLabelFor(), 0, SpringLayout.WEST,
                        secondColumnLabels.get(secondColumnLongestLabel).getLabelFor());
                layout.putConstraint(SpringLayout.EAST, secondColumnLabels.get(i).getLabelFor(), 0, SpringLayout.EAST,
                        secondColumnLabels.get(secondColumnLongestLabel).getLabelFor());
            }
        }

        for (int i = 0; i < thirdColumnLabels.size(); i++) {
            if (i != thirdColumnLongestLabel) {
                layout.putConstraint(SpringLayout.WEST, thirdColumnLabels.get(i).getLabelFor(), 0, SpringLayout.WEST,
                        thirdColumnLabels.get(thirdColumnLongestLabel).getLabelFor());
                if (!thirdColumnLabels.get(i).getText().equals("State:")) {
                    layout.putConstraint(SpringLayout.EAST, thirdColumnLabels.get(i).getLabelFor(), 0,
                            SpringLayout.EAST, thirdColumnLabels.get(thirdColumnLongestLabel).getLabelFor());
                }
            }
        }

        for (int i = 0; i < fourthColumnLabels.size(); i++) {
            if (i != fourthColumnLongestLabel) {
                layout.putConstraint(SpringLayout.WEST, fourthColumnLabels.get(i).getLabelFor(), 0, SpringLayout.WEST,
                        fourthColumnLabels.get(fourthColumnLongestLabel).getLabelFor());
                layout.putConstraint(SpringLayout.EAST, fourthColumnLabels.get(i).getLabelFor(), 0, SpringLayout.EAST,
                        fourthColumnLabels.get(fourthColumnLongestLabel).getLabelFor());
            }
        }

        //pos close button
        layout.putConstraint(SpringLayout.EAST, close, -20, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, close, 0, SpringLayout.NORTH, firstColumnLabels.get(0));
        layout.putConstraint(SpringLayout.EAST, save, -10, SpringLayout.WEST, close);
        layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, close);

        //pos the status drop down
        layout.putConstraint(SpringLayout.EAST, statusDropDown, -40, SpringLayout.WEST, save);
        layout.putConstraint(SpringLayout.SOUTH, statusDropDown, 0, SpringLayout.SOUTH, save);
        layout.putConstraint(SpringLayout.EAST, statusLabel, -10, SpringLayout.WEST, statusDropDown);
        layout.putConstraint(SpringLayout.SOUTH, statusLabel, 0, SpringLayout.SOUTH, statusDropDown);

        //pos the group size drop down
        layout.putConstraint(SpringLayout.WEST, groupSizeLabel, 0, SpringLayout.WEST, firstColumnLabels.get(firstColumnLabels.size() - 1));
        layout.putConstraint(SpringLayout.NORTH, groupSizeLabel, 30, SpringLayout.SOUTH, firstColumnLabels.get(firstColumnLabels.size() - 1));
        layout.putConstraint(SpringLayout.WEST, groupSizeDropDown, 10, SpringLayout.EAST, groupSizeLabel);
        layout.putConstraint(SpringLayout.NORTH, groupSizeDropDown, 0, SpringLayout.NORTH, groupSizeLabel);

        layout.putConstraint(SpringLayout.NORTH, bottomPanel, 10, SpringLayout.SOUTH, groupSizeDropDown);
        layout.putConstraint(SpringLayout.SOUTH, bottomPanel, 0, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.EAST, bottomPanel, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, bottomPanel, 0, SpringLayout.WEST, this);

        //put the groupSizeDropDownEditButton next to the groupSizeDropDown
        layout.putConstraint(SpringLayout.WEST, groupSizeDropDownEditButton, 5, SpringLayout.EAST, groupSizeDropDown);
        layout.putConstraint(SpringLayout.SOUTH, groupSizeDropDownEditButton, 0, SpringLayout.SOUTH, groupSizeDropDown);
        layout.putConstraint(SpringLayout.NORTH, groupSizeDropDownEditButton, 0, SpringLayout.NORTH, groupSizeDropDown);

        //statusDropDownEditButton
        //put the statusDropDownEditButton next to the statusDropDown
        layout.putConstraint(SpringLayout.WEST, statusDropDownEditButton, 5, SpringLayout.EAST, statusDropDown);
        layout.putConstraint(SpringLayout.SOUTH, statusDropDownEditButton, 0, SpringLayout.SOUTH, statusDropDown);
        layout.putConstraint(SpringLayout.NORTH, statusDropDownEditButton, 0, SpringLayout.NORTH, statusDropDown);

    }

    /**
     * @return the companyID
     */
    public CompanyID getCompanyID() {
        return companyID;
    }

    private boolean isNewCompany() {
        return companyID.isNewCompany();
    }
}

