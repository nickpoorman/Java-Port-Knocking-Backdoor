package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nick
 */
public class DropDownEditor extends JFrame {

    private int dropDownID;
    private CompanyID companyID;

    public DropDownEditor(int dropDownID, CompanyID companyID) {
        this.setTitle("Drop Down Editor: " + TeaDatabase.getDropDownName(dropDownID));
        setSize(500, 275);
        this.setResizable(false);
        this.dropDownID = dropDownID;
        this.companyID = companyID;
        this.setLayout(new BorderLayout());
        DropDownEditorPanel ddep = new DropDownEditorPanel(dropDownID, companyID);
        ddep.setVisible(true);
        this.add(ddep, BorderLayout.CENTER);
    }

    public static void showDropDownEditorWindow(int dropDownID, CompanyID companyID) {
        DropDownEditor dropDownEditor = new DropDownEditor(dropDownID, companyID);
        dropDownEditor.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                DropDownEditor dropDownEditor = new DropDownEditor(2, new CompanyID(22));
                dropDownEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dropDownEditor.setVisible(true);
            }
        });
    }
}

class DropDownEditorPanel extends JPanel implements ListSelectionListener, ActionListener {

    private int dropDownID;
    private CompanyID companyID;
    JList options;
    String[] columnTitle = {"Options"};
    List<String> dropDownOptions;
    List<String> dropDownOptionIDs;
    //HashMap<String, String> ddOptions;
    String dropDownName;
    SpringLayout layout;
    JScrollPane optionsScrollPane;
    JButton add;
    JButton save;
    JTextField editTextField;
    //JPanel desktop;

    public DropDownEditorPanel(int dropDownID, CompanyID companyID) {
        this.dropDownID = dropDownID;
        this.companyID = companyID;
        layout = new SpringLayout();
        this.setLayout(layout);
        //desktop = new JPanel();
        // this.setContentPane(desktop);
        //desktop.setLayout(layout);
        //get all the data for the table
        //get the title of the column
        // columnTitle = new String[]{"Options"};
        //get the drop down name
        dropDownName = TeaDatabase.getDropDownName(dropDownID);
        //get the drop down options for the given drop down
        //dropDownOptions = TeaDatabase.getDropDownOptions(dropDownID);
        List<List> data = TeaDatabase.getDropDownOptionsWithID(dropDownID);
        dropDownOptionIDs = data.get(0);
        dropDownOptions = data.get(1);
//        for (int i = 0; i < data.get(0).size(); i++) {
//            ddOptions.put((String) data.get(0).get(i), (String) data.get(1).get(i));
//        }

        createComponents();
        addComponentsToCanvas();
//        updatePreferedSize();
        // if (!companyID.isNewCompany()) {
        fillValues();
        // }
        initLayout();
    }

    private void update() {
        this.remove(optionsScrollPane);
        //get the drop down name
        dropDownName = TeaDatabase.getDropDownName(dropDownID);
        //get the drop down options for the given drop down
        //dropDownOptions = TeaDatabase.getDropDownOptions(dropDownID);
        List<List> data = TeaDatabase.getDropDownOptionsWithID(dropDownID);
        dropDownOptionIDs = data.get(0);
        dropDownOptions = data.get(1);
        options = new JList(dropDownOptions.toArray());
        options.addListSelectionListener(this);
        options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionsScrollPane = new JScrollPane(options);
        this.add(optionsScrollPane);
        initLayout();
        this.updateUI();
    }

    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            //get the currently selected index
            //Object selection = options.getSelectedValue();
            // intSelection = (Integer) selection;
            updateOption();
        } else if ("add".equals(e.getActionCommand())) {
            saveOption();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        JList jListSource = (JList) e.getSource();

        // Object selection = jListSource.getSelectedValues();
        if (!e.getValueIsAdjusting()) {
            try {
                int selectionIndex = jListSource.getSelectedIndex();
                if (selectionIndex != -1) {
                    String selection = (String) jListSource.getModel().getElementAt(selectionIndex);
                    editTextField.setText(selection);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                //do nothing
            }
//            System.out.println("----");
//            for (int i = 0; i < selection.length; i++) {
//                System.out.println("selection = " + selection[i]);
//            }
        }

    }

    public void createComponents() {
        options = new JList(dropDownOptions.toArray());
        options.addListSelectionListener(this);
        options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionsScrollPane = new JScrollPane(options);

        add = new JButton("Add");
        add.setActionCommand("add");
        add.addActionListener(this);

        save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);

        editTextField = new JTextField(50);
    }

    public void addComponentsToCanvas() {
        // this.add(options);

        this.add(optionsScrollPane);
        this.add(editTextField);
        this.add(save);
        this.add(add);

    }

    public void fillValues() {
    }

    public void initLayout() {

        //pos the list
        layout.putConstraint(SpringLayout.WEST, optionsScrollPane, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, optionsScrollPane, 10, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, optionsScrollPane, -10, SpringLayout.EAST, this);

        //pos the text field
        layout.putConstraint(SpringLayout.WEST, editTextField, 0, SpringLayout.WEST, optionsScrollPane);
        layout.putConstraint(SpringLayout.NORTH, editTextField, 10, SpringLayout.SOUTH, optionsScrollPane);
        layout.putConstraint(SpringLayout.EAST, editTextField, 0, SpringLayout.EAST, optionsScrollPane);

        //pos the add button
        layout.putConstraint(SpringLayout.WEST, save, 0, SpringLayout.WEST, editTextField);
        layout.putConstraint(SpringLayout.NORTH, save, 10, SpringLayout.SOUTH, editTextField);

        //pos the save button
        layout.putConstraint(SpringLayout.WEST, add, 10, SpringLayout.EAST, save);
        layout.putConstraint(SpringLayout.SOUTH, add, 0, SpringLayout.SOUTH, save);


    }

    public void updateCallerWindow() {
        JComboBox jcb = this.companyID.getDropDownByID(this.dropDownID);
        //get the current selected index
        int selectedIndex = jcb.getSelectedIndex();
        jcb.removeAllItems();
        List<List> data = TeaDatabase.getDropDownOptionsWithID(dropDownID);
        for (int i = 0; i < data.get(1).size(); i++) {
            jcb.addItem(data.get(1).get(i));
        }
        jcb.setSelectedIndex(selectedIndex);
        jcb.updateUI();

    }

    public void saveOption() {
        //first check to see if the current selection is already in the list
//        int selectionIndex = options.getSelectedIndex();
//        String selection = (String) options.getModel().getElementAt(selectionIndex);
        String selection = editTextField.getText();
        for (int i = 0; i < dropDownOptions.size(); i++) {
            if (selection.equals(dropDownOptions.get(i))) {
                //popup a window that says the option already exists with that name and return
                JOptionPane.showMessageDialog(null, "Option already exists!", "Duplicate Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        //if it doesnt find it just add it to the list and then update
        TeaDatabase.insertDropDownOption(dropDownID, selection);
        update();
        updateCallerWindow();
        return;
    }

    public void updateOption() {
        System.out.println("CALLING!");
        //check the list for its id
        //dropDownOptions
        //dropDownOptionIDs
        //dropDownID
        try {
            int selectionIndex = options.getSelectedIndex();
            if (selectionIndex != -1) {
                String selection = (String) options.getModel().getElementAt(selectionIndex);
                //for (int i = 0; i < dropDownOptions.size(); i++) {
                //first check to see if the strings match
                if (dropDownOptions.get(selectionIndex).equals(selection)) {
                    //if it does get the id for the given selection
                    int optionID = Integer.parseInt(dropDownOptionIDs.get(selectionIndex));
                    //now that we have the selected value lets update it in the database
                    TeaDatabase.updateDropDownsOptionName(dropDownID, optionID, editTextField.getText());
                    update();
                    updateCallerWindow();
                    return;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            //do nothing
        }

        //}
    }
    //should take in drop down id
    //list all the current options of the drop down in a jtable
    //when an option is selected have it populate the field below
    //you can either update a selected field or you can create a new field (cannot delete a field)
}
