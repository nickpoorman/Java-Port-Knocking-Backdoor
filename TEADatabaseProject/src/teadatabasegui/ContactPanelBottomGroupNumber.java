package teadatabasegui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author nick
 */
public class ContactPanelBottomGroupNumber extends JPanel implements ActionListener, KeyListener {

    private CompanyID companyID;
    JLabel groupNumbersLabel;
    SpringLayout layout;
    List<JTextField> groupNumberTextFields;
    List<String> data;
    List<String> groupNumberIDs;
    JButton newGroupNumber;

    public ContactPanelBottomGroupNumber(CompanyID companyID) {
        this.companyID = companyID;
        layout = new SpringLayout();
        this.setLayout(layout);
        if (!companyID.isNewCompany()) {
            updatePanel();
        }
    }

    private void updatePanel() {
        data = TeaDatabase.getCompanyGroupNumbers(companyID.getCompanyID());
        createComponents();
        addComponentsToCanvas();
        intiLayout();
        fillValues();
        setAndGuessSize();

    }

    public void save() {
        for (int i = 0; i < this.groupNumberTextFields.size(); i++) {
            String groupNumber = this.groupNumberTextFields.get(i).getText();
            int groupNumberID = Integer.parseInt(this.groupNumberIDs.get(i));
            TeaDatabase.updateCompanyGroupNumber(groupNumberID, groupNumber);
        }
    }

    private int getNumberOfGroupNumbers() {
        return (data.size() / 3);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    private void setAndGuessSize() {
        int size = 100;
        if (groupNumberTextFields.size() != 0) {
            JTextField f = groupNumberTextFields.get(0);
            size = (int) (20 + groupNumbersLabel.getPreferredSize().getHeight() + 10 + (10 * getNumberOfGroupNumbers()) + (getNumberOfGroupNumbers() * f.getPreferredSize().getHeight()));
        }
        this.setPreferredSize(new Dimension(100, size));
    }

    public void actionPerformed(ActionEvent e) {
        if ("newGroupNumber".equals(e.getActionCommand())) {
            this.save();
            //just add the new field to the database and reload the panel
            TeaDatabase.insertCompanyGroupNumber(companyID.getCompanyID(), "");
            //now reload
            this.removeAll();
            updatePanel();
            updateUI();
        }
    }

    public void createComponents() {
        groupNumbersLabel = new JLabel("Group Numbers");
        groupNumberTextFields = new LinkedList<JTextField>();
        groupNumberIDs = new LinkedList<String>();

        //create enough text boxes for the group numbers
        for (int i = 0; i < getNumberOfGroupNumbers(); i++) {
            JTextField tmp = new JTextField(40);
            tmp.addKeyListener(this);
            groupNumberTextFields.add(tmp);
        }

        newGroupNumber = new JButton("New Group Number");
        newGroupNumber.setActionCommand("newGroupNumber");
        newGroupNumber.addActionListener(this);
    }

    public void addComponentsToCanvas() {
        if (getNumberOfGroupNumbers() != 0) {
            this.add(groupNumbersLabel);
        }

        for (JTextField f : groupNumberTextFields) {
            this.add(f);
        }

        this.add(newGroupNumber);
    }

    public void intiLayout() {
        layout.putConstraint(SpringLayout.WEST, groupNumbersLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, groupNumbersLabel, 10, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, newGroupNumber, 60, SpringLayout.EAST, groupNumbersLabel);
        layout.putConstraint(SpringLayout.NORTH, newGroupNumber, 0, SpringLayout.NORTH, groupNumbersLabel);
        layout.putConstraint(SpringLayout.SOUTH, newGroupNumber, 0, SpringLayout.SOUTH, groupNumbersLabel);

        for (int i = 0; i < groupNumberTextFields.size(); i++) {
            if (i == 0) {
                //set the first one
                layout.putConstraint(SpringLayout.NORTH, groupNumberTextFields.get(i), 10, SpringLayout.SOUTH, groupNumbersLabel);
                layout.putConstraint(SpringLayout.WEST, groupNumberTextFields.get(i), 10, SpringLayout.WEST, this);
            } else {
                //put the rest after
                layout.putConstraint(SpringLayout.NORTH, groupNumberTextFields.get(i), 10, SpringLayout.SOUTH, groupNumberTextFields.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, groupNumberTextFields.get(i), 0, SpringLayout.WEST, groupNumberTextFields.get(i - 1));
            }
        }
    }

    public void fillValues() {
        //fill the group number text fields
        int textFieldPointer = 0;
        for (int i = 2; i < data.size(); i = i + 3) {
            groupNumberTextFields.get(textFieldPointer).setText(data.get(i));
            textFieldPointer++;
        }

        //fill the group number id's list
        for (int i = 1; i < data.size(); i = i + 3) {
            String tmp = new String(data.get(i));
            groupNumberIDs.add(tmp);
        }
    }
}
