/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

/**
 *
 * @author chintan
 */
public class detailsGUI extends JFrame implements ActionListener {

    /**
     * @param args the command line arguments
     */
    JButton getDetails;

    public detailsGUI() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setVisible(true);
        initComponents();
        setLayout(this.getContentPane());
        addActionListener();

    }

//    public static void main(String[] args) {
//
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                new detailsGUI();
//            }
//        });
//
//    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("SHOW DETAILS")) {
            new mypanel(1, 1);
        }
    }

    private void addActionListener() {
        getDetails.addActionListener(this);
    }

    private void initComponents() {
        getDetails = new JButton("SHOW DETAILS");
    }

    private void setLayout(Container contentPane) {
        SpringLayout mylayout = new SpringLayout();
        contentPane.setLayout(mylayout);
        contentPane.add(getDetails);
        mylayout.putConstraint(SpringLayout.WEST, getDetails, 50, SpringLayout.WEST, contentPane);
        mylayout.putConstraint(SpringLayout.NORTH, getDetails, 50, SpringLayout.NORTH, contentPane);

    }

    class mypanel extends JFrame implements ActionListener {

        int company, plancode;
        List<List> details = new ArrayList<List>();
        List<JLabel> nameslabellist;
        List<JTextField> valuefieldlist;
        JScrollPane scrollpane;
        JPanel panel;
        JPanel panel2;
        JButton add;
        JButton save;

        mypanel(int companyid, int plancodeid) {
            this.setSize(600, 600);
            this.setVisible(true);
            this.setName("Company panel");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.company = companyid;
            this.plancode = plancodeid;
            InitComponents();
            addComponents();
            addActionListeners();
        }

        public void removeComponents() {
            this.getContentPane().remove(panel2);
            this.getContentPane().remove(scrollpane);
        }

        public void InitComponents() {
            nameslabellist = new ArrayList<JLabel>();
            valuefieldlist = new ArrayList<JTextField>();
            panel = new JPanel();
            panel2 = new JPanel();

            panel.setPreferredSize(new Dimension(200, 500));
            scrollpane = new JScrollPane(panel);
            scrollpane.setSize(new Dimension(100, 100));

            scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            add = new JButton("ADD NEW FIELD");
            save = new JButton("SAVE");
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("ADD NEW FIELD")) {
                new popup(plancode, this);
            }

            if (command.equals("SAVE")) {
                for (int i = 0; i < nameslabellist.size(); i++) {
                    TeaDatabase.updateFieldValues(i + 1, valuefieldlist.get(i).getText().trim(), plancode);
                }
                this.dispose();
            }
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

            if (names.size() != 0) {
                SpringLayout mylayout = new SpringLayout();
                panel.setLayout(mylayout);

                for (int i = 0; i < names.size(); i++) {
                    nameslabellist.add(new JLabel());
                    nameslabellist.get(i).setText(names.get(i));
                    nameslabellist.get(i).setPreferredSize(new Dimension(150, 20));
                    valuefieldlist.add(new JTextField());
                    valuefieldlist.get(i).setText(values.get(i));
                    valuefieldlist.get(i).setPreferredSize(new Dimension(150, 20));
                }

                for (int i = 0; i < names.size(); i++) {
                    panel.add(nameslabellist.get(i));
                    panel.add(valuefieldlist.get(i));
                }

                for (int i = 0; i < names.size(); i++) {

                    if (i == 0) {
                        mylayout.putConstraint(SpringLayout.WEST, nameslabellist.get(i), 10, SpringLayout.WEST, panel);
                        mylayout.putConstraint(SpringLayout.NORTH, nameslabellist.get(i), 10, SpringLayout.NORTH, panel);
                        mylayout.putConstraint(SpringLayout.WEST, valuefieldlist.get(i), 50, SpringLayout.EAST, nameslabellist.get(i));
                        mylayout.putConstraint(SpringLayout.NORTH, valuefieldlist.get(i), 10, SpringLayout.NORTH, panel);
                    } else {
                        mylayout.putConstraint(SpringLayout.WEST, nameslabellist.get(i), 10, SpringLayout.WEST, panel);
                        mylayout.putConstraint(SpringLayout.NORTH, nameslabellist.get(i), 30, SpringLayout.NORTH, nameslabellist.get(i - 1));
                        mylayout.putConstraint(SpringLayout.WEST, valuefieldlist.get(i), 50, SpringLayout.EAST, nameslabellist.get(0));
                        mylayout.putConstraint(SpringLayout.NORTH, valuefieldlist.get(i), 30, SpringLayout.NORTH, valuefieldlist.get(i - 1));
                    }
                }
            }
        }

        private void addActionListeners() {
            add.addActionListener(this);
            save.addActionListener(this);
        }
    }

    class popup extends JFrame implements ActionListener {

        String fieldName = null;
        String fieldValue = null;
        JTextField field;
        JLabel name;
        JTextField value;
        JLabel fvalue;
        JButton save;
        JButton delete;
        JComboBox dropdown;
        int plancodeid;
        mypanel test;

        popup(int plancodeid, mypanel panel) {
            test = panel;
            this.plancodeid = plancodeid;
            this.setSize(400, 175);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addComponents(this.getContentPane());
            addActionListeners();
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();


            if (command.equals("OK")) {
                fieldName = field.getText().trim();
                fieldValue = value.getText().trim();
                TeaDatabase.insertField(fieldName);
                TeaDatabase.insertFieldValues(plancodeid, TeaDatabase.getLatestSavedFieldID());
                test.removeComponents();
                test.addComponents();
                test.repaint();
                this.dispose();
            }

            if (command.equals("CANCEL")) {
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
            name.setText("Name");
            fvalue = new JLabel();
            fvalue.setText("value :-");
            name.setPreferredSize(new Dimension(200, 20));
            fvalue.setPreferredSize(new Dimension(200, 20));

            field = new JTextField();
            field.setPreferredSize(new Dimension(150, 20));


            value = new JTextField();
            value.setPreferredSize(new Dimension(150, 20));
            value.setText("NONE");


            save = new JButton("OK");
            name.setPreferredSize(new Dimension(40, 20));
            delete = new JButton("CANCEL");
            name.setPreferredSize(new Dimension(40, 20));

            dropdown = new JComboBox(TeaDatabase.getFieldNames(plancodeid));
            dropdown.setPreferredSize(new Dimension(200, 20));

            SpringLayout layout = new SpringLayout();
            contentPane.setLayout(layout);
            contentPane.add(name);
            contentPane.add(field);
            contentPane.add(save);
            contentPane.add(delete);
            contentPane.add(dropdown);

            layout.putConstraint(SpringLayout.WEST, name, 20, SpringLayout.WEST, contentPane);
            layout.putConstraint(SpringLayout.NORTH, name, 20, SpringLayout.NORTH, contentPane);

            layout.putConstraint(SpringLayout.WEST, dropdown, 20, SpringLayout.WEST, contentPane);
            layout.putConstraint(SpringLayout.NORTH, dropdown, 40, SpringLayout.NORTH, name);

            layout.putConstraint(SpringLayout.WEST, field, 100, SpringLayout.EAST, name);
            layout.putConstraint(SpringLayout.NORTH, field, 20, SpringLayout.NORTH, contentPane);

            layout.putConstraint(SpringLayout.WEST, save, 80, SpringLayout.WEST, contentPane);
            layout.putConstraint(SpringLayout.NORTH, save, 90, SpringLayout.NORTH, name);

            layout.putConstraint(SpringLayout.WEST, delete, 50, SpringLayout.EAST, save);
            layout.putConstraint(SpringLayout.NORTH, delete, 90, SpringLayout.NORTH, name);

        }
    }
}
