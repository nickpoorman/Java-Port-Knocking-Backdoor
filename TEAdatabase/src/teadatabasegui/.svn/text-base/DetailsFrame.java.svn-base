package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

/**
 *
 * @author nick
 */
public class DetailsFrame extends JFrame implements ActionListener, PropertyChangeListener {

    int company, plancode;
    List<List> details = new ArrayList<List>();
    List<JLabel> nameslabellist;
    List<JFormattedTextField> valuefieldlist;
    Map<Integer, JCheckBox> removeCheckBox;
    JScrollPane scrollpane;
    JPanel panel;
    JPanel panel2;
    JButton add;
    JButton save;
    boolean propertyChanged;

    DetailsFrame(int companyid, int plancodeid, String title) {
        super(title);
        this.setSize(600, 600);
        this.setVisible(true);
        this.setName("Details");
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.company = companyid;
        this.plancode = plancodeid;
        InitComponents();
        addComponents();
        addActionListeners();
        propertyChanged = false;
    }

    public void removeComponents() {
        this.getContentPane().remove(panel2);
        this.getContentPane().remove(scrollpane);
        panel.removeAll();
        nameslabellist.clear();
        valuefieldlist.clear();
        removeCheckBox.clear();
    }

    public void InitComponents() {
        nameslabellist = new ArrayList<JLabel>();
        valuefieldlist = new ArrayList<JFormattedTextField>();
        removeCheckBox = new HashMap<Integer, JCheckBox>();
        panel = new JPanel();
        panel2 = new JPanel();

        panel.setPreferredSize(new Dimension(200, 500));
        scrollpane = new JScrollPane(panel);
        scrollpane.setSize(new Dimension(100, 100));

        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        add = new JButton("Add New Field");
        save = new JButton("Save");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Add New Field")) {
            if (this.propertyChanged) {
                int selection = JOptionPane.showConfirmDialog(null, "You have may have changed information. Would you like to save first?", "Confirm Save", JOptionPane.YES_NO_OPTION);
                if (selection == JOptionPane.YES_OPTION) {
                    saveIt();
                }
                this.propertyChanged = false;
            }
            new Popup(plancode, this);
        } else if (command.equals("Save")) {
            saveIt();
        }
    }

    private void saveIt() {
        // the field_ids are here = details.get(0)
        List<Integer> fieldID = details.get(0);
        for (Integer i : fieldID) {
            System.out.println("filedID: " + i.intValue());
        }
        System.out.println("NAMESLABLELIST size: " + nameslabellist.size());
        System.out.println("VALUEFIELDLIST size: " + valuefieldlist.size());
        for (int i = 0; i < nameslabellist.size(); i++) {

            //need to fix this to update the right field_id
            System.out.println("filedIDINLOOP: " + fieldID.get(i));
            TeaDatabase.updateFieldValues(fieldID.get(i), valuefieldlist.get(i).getText().trim(), plancode);
        }
        Set<Integer> keys = removeCheckBox.keySet();
        for (int i : keys) {
            if (removeCheckBox.get(i).isSelected()) {
                TeaDatabase.deleteFieldNameValue(i, plancode);
            }
        }

        //do some sort of update here
        this.removeComponents();
        this.addComponents();
        this.repaint();
        this.panel.updateUI();
        this.panel2.updateUI();
        //this.dispose();

    }

    public void propertyChange(PropertyChangeEvent evt) {
        //set the change boolean
        System.out.println("FIRED!!");
        propertyChanged = true;

    }

    public void addComponents() {
        panel2.add(add, BorderLayout.WEST);
        panel2.add(save, BorderLayout.EAST);
        this.getContentPane().add(panel2, BorderLayout.NORTH);
        this.getContentPane().add(scrollpane, BorderLayout.CENTER);
        ManageJPanelLayout();
    }

    private void ManageJPanelLayout() {
        details = TeaDatabase.getFieldNamesandValues(company, plancode);


        List<String> names = details.get(1);
        List<String> values = details.get(2);
        List<Integer> fieldid = details.get(0);

        populateCheckBox(fieldid);

        if (names.size() != 0) {
            SpringLayout mylayout = new SpringLayout();
            panel.setLayout(mylayout);

            for (int i = 0; i < names.size(); i++) {
                nameslabellist.add(new JLabel());
                nameslabellist.get(i).setText(names.get(i));
                nameslabellist.get(i).setPreferredSize(new Dimension(150, 20));
                JFormattedTextField tmp = new JFormattedTextField();
                tmp.addPropertyChangeListener("value", this);
                valuefieldlist.add(tmp);
                valuefieldlist.get(i).setText(values.get(i));
                valuefieldlist.get(i).setPreferredSize(new Dimension(150, 20));

            }

            for (int i = 0; i < names.size(); i++) {
                panel.add(nameslabellist.get(i));
                panel.add(valuefieldlist.get(i));
                panel.add(removeCheckBox.get(fieldid.get(i)));
            }

            for (int i = 0; i < names.size(); i++) {

                if (i == 0) {
                    mylayout.putConstraint(SpringLayout.WEST, nameslabellist.get(i), 10, SpringLayout.WEST, panel);
                    mylayout.putConstraint(SpringLayout.NORTH, nameslabellist.get(i), 10, SpringLayout.NORTH, panel);
                    mylayout.putConstraint(SpringLayout.WEST, valuefieldlist.get(i), 50, SpringLayout.EAST, nameslabellist.get(i));
                    mylayout.putConstraint(SpringLayout.NORTH, valuefieldlist.get(i), 10, SpringLayout.NORTH, panel);
                    mylayout.putConstraint(SpringLayout.WEST, removeCheckBox.get(fieldid.get(i)), 50, SpringLayout.EAST, valuefieldlist.get(i));
                    mylayout.putConstraint(SpringLayout.NORTH, removeCheckBox.get(fieldid.get(i)), 10, SpringLayout.NORTH, panel);
                } else {
                    mylayout.putConstraint(SpringLayout.WEST, nameslabellist.get(i), 10, SpringLayout.WEST, panel);
                    mylayout.putConstraint(SpringLayout.NORTH, nameslabellist.get(i), 30, SpringLayout.NORTH, nameslabellist.get(i - 1));
                    mylayout.putConstraint(SpringLayout.WEST, valuefieldlist.get(i), 50, SpringLayout.EAST, nameslabellist.get(0));
                    mylayout.putConstraint(SpringLayout.NORTH, valuefieldlist.get(i), 30, SpringLayout.NORTH, valuefieldlist.get(i - 1));
                    mylayout.putConstraint(SpringLayout.WEST, removeCheckBox.get(fieldid.get(i)), 50, SpringLayout.EAST, valuefieldlist.get(0));
                    mylayout.putConstraint(SpringLayout.NORTH, removeCheckBox.get(fieldid.get(i)), 30, SpringLayout.NORTH, removeCheckBox.get(fieldid.get(i - 1)));
                }
            }
        } else {
            System.out.println("ok");
        }
    }

    private void addActionListeners() {
        add.addActionListener(this);
        save.addActionListener(this);
    }

    private void populateCheckBox(List<Integer> fieldid) {
        for (int i = 0; i < fieldid.size(); i++) {
            JCheckBox tmp = new JCheckBox("Remove");
            // tmp.addPropertyChangeListener(this);
            removeCheckBox.put(fieldid.get(i), tmp);
        }
        System.out.println("CheckBox key:-" + removeCheckBox.keySet().toString());
        System.out.println("fieldid  :- " + fieldid.toString());
    }
}

class Popup extends JFrame implements ActionListener {

    String fieldName = null;
    String fieldValue = null;
    JFormattedTextField field;
    JLabel name;
    JFormattedTextField value;
    JLabel fvalue;
    JButton save;
    JButton delete;
    JComboBox dropdown;
    int plancodeid;
    DetailsFrame test;

    Popup(int plancodeid, DetailsFrame panel) {
        test = panel;
        this.plancodeid = plancodeid;
        this.setSize(400, 175);
        this.setVisible(true);
        //  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(this.getContentPane());
        addActionListeners();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();


        if (command.equals("OK")) {


            fieldName = field.getText().trim();
            if (!fieldName.equals("")) {
                //if(fieldName!=null && fieldName.length()!=0)
                int id = TeaDatabase.insertField(fieldName);
                if (Integer.parseInt(TeaDatabase.getQueryResults("Select count(*) as count from field_values where field_id = " + id + " AND rates_id = " + plancodeid + " ;").get(0)) == 0) {
                    TeaDatabase.insertFieldValues(plancodeid, id);
                }

                test.removeComponents();
                test.addComponents();
                test.repaint();
                test.panel.updateUI();
                test.panel2.updateUI();
                this.dispose();
            }
        }

        if (command.equals("Cancel")) {
            this.dispose();
        }

        if (e.getSource().equals(dropdown)) {
            field.setText(dropdown.getSelectedItem().toString());

        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    private void addActionListeners() {
        save.addActionListener(this);
        delete.addActionListener(this);
        dropdown.addActionListener(this);

    }

    private void addComponents(Container contentPane) {

        name = new JLabel();
        name.setText("Type a new field name here or select one from below.");
        fvalue = new JLabel();
        fvalue.setText("value :-");
        // name.setPreferredSize(new Dimension(200, 20));
        fvalue.setPreferredSize(new Dimension(200, 20));

        field = new JFormattedTextField();
        field.setPreferredSize(new Dimension(150, 20));


        value = new JFormattedTextField();
        value.setPreferredSize(new Dimension(150, 20));
        value.setText("NONE");


        save = new JButton("OK");
        // name.setPreferredSize(new Dimension(40, 20));
        delete = new JButton("Cancel");
        // name.setPreferredSize(new Dimension(40, 20));

        dropdown = new JComboBox(TeaDatabase.getFieldNames(plancodeid));
        //System.out.println(TeaDatabase.getFieldNamesNotIn(plancodeid));
        dropdown.setPreferredSize(new Dimension(200, 20));


        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        contentPane.add(name);
        contentPane.add(field);
        contentPane.add(save);
        contentPane.add(delete);
        contentPane.add(dropdown);

        layout.putConstraint(SpringLayout.WEST, name, 20, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, name, 10, SpringLayout.NORTH, contentPane);

        layout.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, name);
        layout.putConstraint(SpringLayout.NORTH, field, 10, SpringLayout.SOUTH, name);

        layout.putConstraint(SpringLayout.WEST, dropdown, 20, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, dropdown, 10, SpringLayout.SOUTH, field);

        layout.putConstraint(SpringLayout.WEST, save, 80, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, save, 90, SpringLayout.NORTH, name);

        layout.putConstraint(SpringLayout.WEST, delete, 50, SpringLayout.EAST, save);
        layout.putConstraint(SpringLayout.NORTH, delete, 90, SpringLayout.NORTH, name);

    }
}


