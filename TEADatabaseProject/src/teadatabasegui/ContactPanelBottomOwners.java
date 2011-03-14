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
public class ContactPanelBottomOwners extends JPanel implements ActionListener, KeyListener {

    private CompanyID companyID;
    JLabel ownersLabel;
    JLabel percentOwnershipLabel;
    SpringLayout layout;
    List<JTextField> ownersTextFields;
    List<JTextField> percentOwnershipTextFields;
    List<String> ownerIDs;
    List<String> data;
    int numberOfColums = 4;
    JButton newOwner;

    public ContactPanelBottomOwners(CompanyID companyID) {
        this.companyID = companyID;
        layout = new SpringLayout();
        this.setLayout(layout);

        //get the values from the database and create that many text fields
        if (!companyID.isNewCompany()) {
            updatePanel();
        }
    }

    private void updatePanel() {
        data = TeaDatabase.getCompanyOwners(companyID.getCompanyID());
        createComponents();
        addComponentsToCanvas();
        intiLayout();
        fillValues();
        setAndGuessSize();
    }

    private int getNumberOfOwners() {
        return (data.size() / 4);
    }

    private void setAndGuessSize() {
        int size = 100;
        if (ownersTextFields.size() != 0) {
            JTextField f = ownersTextFields.get(0);
            size = (int) (20 + ownersLabel.getPreferredSize().getHeight() + 10 + (10 * getNumberOfOwners()) + (getNumberOfOwners() * f.getPreferredSize().getHeight()));
        }
        this.setPreferredSize(new Dimension(10, size));
    }

    public void actionPerformed(ActionEvent e) {
        if ("newOwner".equals(e.getActionCommand())) {
            this.save();
            //just add the new field to the database and reload the panel
            TeaDatabase.insertCompanyOwner(companyID.getCompanyID(), "", "");
            //now reload
            this.removeAll();
            updatePanel();
            updateUI();
        }
    }

    public void save() {
        for (int i = 0; i < this.ownersTextFields.size(); i++) {
            String ownerName = this.ownersTextFields.get(i).getText();
            String percentOwnership = this.percentOwnershipTextFields.get(i).getText();
            int ownerID = Integer.parseInt(this.ownerIDs.get(i));
            TeaDatabase.updateCompanyOwner(ownerID, ownerName, percentOwnership);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void createComponents() {
        ownersLabel = new JLabel("Owners");
        percentOwnershipLabel = new JLabel("% of ownership");

        //create the lists
        ownersTextFields = new LinkedList<JTextField>();
        percentOwnershipTextFields = new LinkedList<JTextField>();
        ownerIDs = new LinkedList<String>();

        //create the owner text fields
        for (int i = 0; i < getNumberOfOwners(); i++) {
            JTextField tmp = new JTextField(40);
            tmp.addKeyListener(this);
            ownersTextFields.add(tmp);
        }

        //create the percent ownership text fields
        for (int i = 0; i < getNumberOfOwners(); i++) {
            JTextField tmp = new JTextField(5);
            percentOwnershipTextFields.add(tmp);
        }

        newOwner = new JButton("New Owner");
        newOwner.setActionCommand("newOwner");
        newOwner.addActionListener(this);
    }

    public void addComponentsToCanvas() {
        if (getNumberOfOwners() != 0) {
            this.add(ownersLabel);
            this.add(percentOwnershipLabel);
        }

        //put all the owner text fields on the canvas
        for (JTextField f : ownersTextFields) {
            this.add(f);
        }

        //put all the percent ownership text fields on the canvas
        for (JTextField f : percentOwnershipTextFields) {
            this.add(f);
        }
        this.add(newOwner);
    }

    public void intiLayout() {
        //add the new owner button        
        layout.putConstraint(SpringLayout.WEST, ownersLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, ownersLabel, 10, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, newOwner, 60, SpringLayout.EAST, ownersLabel);
        layout.putConstraint(SpringLayout.NORTH, newOwner, 0, SpringLayout.NORTH, ownersLabel);
        layout.putConstraint(SpringLayout.SOUTH, newOwner, 0, SpringLayout.SOUTH, ownersLabel);

        layout.putConstraint(SpringLayout.NORTH, percentOwnershipLabel, 0, SpringLayout.NORTH, ownersLabel);
        layout.putConstraint(SpringLayout.SOUTH, percentOwnershipLabel, 0, SpringLayout.SOUTH, ownersLabel);

        if (percentOwnershipTextFields.size() > 0) {
            layout.putConstraint(SpringLayout.WEST, percentOwnershipLabel, 0, SpringLayout.WEST, percentOwnershipTextFields.get(0));
        }

        if (ownersTextFields.size() > 0) {
            layout.putConstraint(SpringLayout.WEST, ownersLabel, 0, SpringLayout.WEST, ownersTextFields.get(0));
        }

        //make the owner text fields go into a list
        for (int i = 0; i < ownersTextFields.size(); i++) {
            if (i == 0) {
                //set the first one
                layout.putConstraint(SpringLayout.NORTH, ownersTextFields.get(i), 10, SpringLayout.SOUTH, ownersLabel);
                layout.putConstraint(SpringLayout.WEST, ownersTextFields.get(i), 10, SpringLayout.WEST, this);
            } else {
                //put the rest after
                layout.putConstraint(SpringLayout.NORTH, ownersTextFields.get(i), 10, SpringLayout.SOUTH, ownersTextFields.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, ownersTextFields.get(i), 0, SpringLayout.WEST, ownersTextFields.get(i - 1));
            }
        }

        //put each of the percent ownership text fields next to their owner
        for (int i = 0; i < percentOwnershipTextFields.size(); i++) {
            if (i == 0) {
                //set the first one
                layout.putConstraint(SpringLayout.NORTH, percentOwnershipTextFields.get(i), 0, SpringLayout.NORTH, ownersTextFields.get(i));
                layout.putConstraint(SpringLayout.SOUTH, percentOwnershipTextFields.get(i), 0, SpringLayout.SOUTH, ownersTextFields.get(i));
                layout.putConstraint(SpringLayout.WEST, percentOwnershipTextFields.get(i), 30, SpringLayout.EAST, ownersTextFields.get(i));
                layout.putConstraint(SpringLayout.EAST, percentOwnershipTextFields.get(i), -10, SpringLayout.EAST, this);

            } else {
                //put the rest after
                layout.putConstraint(SpringLayout.NORTH, percentOwnershipTextFields.get(i), 10, SpringLayout.SOUTH, percentOwnershipTextFields.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, percentOwnershipTextFields.get(i), 0, SpringLayout.WEST, percentOwnershipTextFields.get(i - 1));
                layout.putConstraint(SpringLayout.EAST, percentOwnershipTextFields.get(i), 0, SpringLayout.EAST, percentOwnershipTextFields.get(i - 1));
            }
        }

    }

    public void fillValues() {
        //fill the values of the owners text fields
        int textFieldPointer = 0;
        for (int i = 2; i < data.size(); i = i + 4) {
            ownersTextFields.get(textFieldPointer).setText(data.get(i));
            textFieldPointer++;
        }
        //fill the values of the percent ownership text fields
        textFieldPointer = 0;
        for (int i = 3; i < data.size(); i = i + 4) {
            percentOwnershipTextFields.get(textFieldPointer).setText(data.get(i));
            textFieldPointer++;
        }
        //fill the ownerIDs list
        for (int i = 1; i < data.size(); i = i + 4) {
            String tmp = new String(data.get(i));
            ownerIDs.add(tmp);
        }
    }
}
