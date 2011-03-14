package teadatabasegui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class RatesArchivePanel extends JPanel implements ActionListener {

    CompanyID companyID;
    List<JLabel> medLabels;
    List<JTextField> medFields;
    List<JLabel> dentLabels;
    List<JTextField> dentFields;
    List<JLabel> otherLabels;
    List<JTextField> otherFields;
    SpringLayout layout;
    JLabel medicalLabel;
    JLabel dentalLabel;
    JLabel otherLabel;
    List<String> medicalData;
    List<String> dentalData;
    List<String> otherData;
    int numColumns = 12;
    int numFields = 9;
    JButton save;
    RatesPanel ratesPanel;

    public RatesArchivePanel(CompanyID companyID, RatesPanel ratesPanel) {
        this.ratesPanel = ratesPanel;
        ratesPanel.setRatesArchivePanel(this);
        update(companyID);
    }

    public void callUpdate() {
        update(this.companyID);
    }

    private void update(CompanyID companyID) {
        this.removeAll();
        this.companyID = companyID;
        if (!companyID.isNewCompany()) {
            //change the query to the new one that gets rates for renewal dates previous to the current date (expired plans)
            medicalData = TeaDatabase.getAllExpiredMedicalRates(companyID.getCompanyID());
            dentalData = TeaDatabase.getAllExpiredDentalRates(companyID.getCompanyID());
            otherData = TeaDatabase.getAllExpiredOtherRates(companyID.getCompanyID());
        } else {
            medicalData = new LinkedList();
            dentalData = new LinkedList();
            otherData = new LinkedList();
        }
        layout = new SpringLayout();
        this.setLayout(layout);
        createComponents();
        addComponentsToCanvas();
        intiLayout();
        updatePreferedSize();
        if (!companyID.isNewCompany()) {
            fillValues();
        }

    }

    private boolean checkAllDateFields() {
        //the date fields are under 2 & 3 for each section (med, dent, other)
        //first get all the medical fields
        List<String> medPopFields = new LinkedList<String>();
        for (int i = 2; i < medFields.size(); i = i + 9) {
            //start date
            medPopFields.add(medFields.get(i).getText());
            //renewal date
            medPopFields.add(medFields.get(i + 1).getText());
        }

        //then get all the dental fields
        List<String> dentPopFields = new LinkedList<String>();
        for (int i = 2; i < dentFields.size(); i = i + 9) {
            dentPopFields.add(dentFields.get(i).getText());
            dentPopFields.add(dentFields.get(i + 1).getText());
        }

        //then get all the other fields
        List<String> otherPopFields = new LinkedList<String>();
        for (int i = 2; i < otherFields.size(); i = i + 9) {
            otherPopFields.add(otherFields.get(i).getText());
            otherPopFields.add(otherFields.get(i + 1).getText());
        }

        //then check all the dates to make sure none of them are wrong
        for (String s : medPopFields) {
            if (!verifyDateFormat(s)) {
                return false;
            }
        }

        for (String s : dentPopFields) {
            if (!verifyDateFormat(s)) {
                return false;
            }
        }

        for (String s : otherPopFields) {
            if (!verifyDateFormat(s)) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyDateFormat(String date) {
        try {
            //2009-09-14
            String[] dates = date.split("-");
            //check for year, month, day
            if (dates.length != 3) {
                return false;
            }

            if (dates[0].length() != 4) {
                return false;
            }

            if (dates[1].length() != 2) {
                return false;
            }

            if (dates[2].length() != 2) {
                return false;
            }

            if (Integer.parseInt(dates[0]) < 0) {
                return false;
            }

            if ((Integer.parseInt(dates[1]) > 12) || Integer.parseInt(dates[1]) < 1) {
                return false;
            }

            if ((Integer.parseInt(dates[2]) > 31) || Integer.parseInt(dates[2]) < 1) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            //before we save check to see if the two date fields have actual dates
            //need to check every date field
            boolean datesOK = checkAllDateFields();

            if (datesOK) {

                save();
                update(companyID);
                this.updateUI();
                if (ratesPanel != null) {
                    ratesPanel.callUpdate();
                    ratesPanel.updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "The Start Date and Renewal Date must be in YYYY-MM-DD format");
            }
        }
    }

    private void save() {
        //first get all the medical fields
        List<String> medPopFields = new LinkedList<String>();
        for (int i = 0; i < medFields.size(); i++) {
            medPopFields.add(medFields.get(i).getText());
        }
        //then get all the id's in a list so we know what goes where
        List<String> medIDs = new LinkedList<String>();
        for (int i = 0; i < medicalData.size(); i = i + 12) {
            medIDs.add(medicalData.get(i));
        }
        //then get all the dental fields
        List<String> dentPopFields = new LinkedList<String>();
        for (int i = 0; i < dentFields.size(); i++) {
            dentPopFields.add(dentFields.get(i).getText());
        }
        //then get all the id's in a list so we know what goes where
        List<String> dentIDs = new LinkedList<String>();
        for (int i = 0; i < dentalData.size(); i = i + 12) {
            dentIDs.add(dentalData.get(i));
        }
        //first get all the medical fields
        List<String> otherPopFields = new LinkedList<String>();
        for (int i = 0; i < otherFields.size(); i++) {
            medPopFields.add(otherFields.get(i).getText());
        }
        //then get all the id's in a list so we know what goes where
        List<String> otherIDs = new LinkedList<String>();
        for (int i = 0; i < otherData.size(); i = i + 12) {
            medIDs.add(otherData.get(i));
        }
        //update the medical data
        for (int i = 0; i < medIDs.size(); i++) {
            List<String> tmp = medPopFields.subList(((numFields * (i + 1)) - numFields), (numFields * (i + 1)));
            TeaDatabase.updateRatesForCompany(companyID.getCompanyID(), Integer.parseInt(medIDs.get(i)), formatForOut(tmp));
            System.out.println("updating med data for: " + medIDs.get(i));
        }

        //update the dental data
        for (int i = 0; i < dentIDs.size(); i++) {
            List<String> tmp = dentPopFields.subList(((numFields * (i + 1)) - numFields), (numFields * (i + 1)));
            TeaDatabase.updateRatesForCompany(companyID.getCompanyID(), Integer.parseInt(dentIDs.get(i)), formatForOut(tmp));
        }

        //update the other data
        for (int i = 0; i < otherIDs.size(); i++) {
            List<String> tmp = otherPopFields.subList(((numFields * (i + 1)) - numFields), (numFields * (i + 1)));
            TeaDatabase.updateRatesForCompany(companyID.getCompanyID(), Integer.parseInt(otherIDs.get(i)), formatForOut(tmp));
        }
    }

    private List<String> formatForOut(List<String> list) {
        List<String> tmp = new LinkedList<String>();
        //0 plan code
        //1 plan type
        //2 filing date
        //3 renewal date
        //4 s
        //5 2p
        //6 e/ch
        //7 e/sp
        //8 f

        //0 plan code
        //1 filing date
        //2 renewal date
        //3 s
        //4 2p
        //5 e/ch
        //6 e/sp
        //7 f
        tmp.add(list.get(0));
        tmp.add(list.get(4));
        tmp.add(list.get(5));
        tmp.add(list.get(6));
        tmp.add(list.get(7));
        tmp.add(list.get(8));
        tmp.add(list.get(3));
        tmp.add(list.get(2));
        tmp.add(list.get(1));
        return tmp;
    }

    private void updatePreferedSize() {
        int txtBoxSize = 0;
        if (medFields.size() > 0) {
            txtBoxSize = (int) medFields.get(0).getPreferredSize().getHeight();
        }
        int medicalLabeSize = (int) medicalLabel.getPreferredSize().getHeight();
        int dentalLabelSize = (int) dentalLabel.getPreferredSize().getHeight();
        int spacing = 15;
        int pad = 15;
        int total = (pad + medicalLabeSize + dentalLabelSize + ((txtBoxSize + spacing) * getNumberOfMedicalRates()) + ((txtBoxSize + spacing) * getNumberOfDentalRates()) + ((txtBoxSize + spacing) * getNumberOfOtherRates()));
        this.setPreferredSize(new Dimension(100, total * 2));
    }

    private void fillValues() {
        //fill in the medical information
        List<String> fmtMedData = formatData(medicalData);
        for (int i = 0; i < medFields.size(); i++) {
            medFields.get(i).setText(fmtMedData.get(i));
        }

        List<String> fmtDentData = formatData(dentalData);
        for (int i = 0; i < dentFields.size(); i++) {
            dentFields.get(i).setText(fmtDentData.get(i));
        }
        //fill in the other information
        List<String> fmtOtherData = formatData(otherData);
        for (int i = 0; i < otherFields.size(); i++) {
            otherFields.get(i).setText(fmtOtherData.get(i));
        }
    }

    private List<String> formatData(List<String> data) {
        List<String> tmp = new LinkedList<String>();
        //int numColumns = 13;
        for (int i = 0; i < (data.size() / numColumns); i++) {
            tmp.add(data.get(2 + (i * numColumns)));
            tmp.add(data.get(11 + (i * numColumns)));
            tmp.add(data.get(10 + (i * numColumns)));
            tmp.add(data.get(9 + (i * numColumns)));
            tmp.add(data.get(3 + (i * numColumns)));
            tmp.add(data.get(4 + (i * numColumns)));
            tmp.add(data.get(5 + (i * numColumns)));
            tmp.add(data.get(6 + (i * numColumns)));
            tmp.add(data.get(7 + (i * numColumns)));
        }


        return tmp;
    }

    private int getNumberOfMedicalRates() {
        int numRates = 0;
        //int numColumns = 13;
        if (medicalData != null) {
            numRates = medicalData.size() / numColumns;
            if ((medicalData.size() % numColumns) != 0) {
                System.out.println("Something went wrong with rates getNumberOfMedicalRates()");
            }
        }
        return numRates;
    }

    private int getNumberOfDentalRates() {
        int numRates = 0;
        //  int numColumns = 13;
        if (medicalData != null) {
            numRates = dentalData.size() / numColumns;
            if ((dentalData.size() % numColumns) != 0) {
                System.out.println("Something went wrong with rates getNumberOfMedicalRates()");
            }
        }
        return numRates;
    }

    private int getNumberOfOtherRates() {
        int numRates = 0;
        //int numColumns = 12;
        if (otherData != null) {
            numRates = otherData.size() / numColumns;
            if ((otherData.size() % numColumns) != 0) {
                System.out.println("Something went wrong with rates getNumberOfOtherRates()");
            }
        }
        return numRates;
    }

    private void createComponents() {
        medLabels = new LinkedList<JLabel>();
        medFields = new LinkedList<JTextField>();
        dentLabels = new LinkedList<JLabel>();
        dentFields = new LinkedList<JTextField>();
        otherLabels = new LinkedList<JLabel>();
        otherFields = new LinkedList<JTextField>();
        save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        if (this.companyID.isNewCompany()) {
            save.setEnabled(false);
        }
        //create medical stuff
        medicalLabel = new JLabel("Medical");
        medicalLabel.setFont(medicalLabel.getFont().deriveFont((float) 16));
        for (int i = 0; i < getNumberOfMedicalRates(); i++) {
            medLabels.add(new JLabel("Plan:"));
            medLabels.add(new JLabel("Type:"));
            medLabels.add(new JLabel("Start Date:"));
            medLabels.add(new JLabel("Renewal Date:"));
            medLabels.add(new JLabel("S:"));
            medLabels.add(new JLabel("2P:"));
            medLabels.add(new JLabel("E/CH:"));
            medLabels.add(new JLabel("E/SP:"));
            medLabels.add(new JLabel("F:"));
        }
        for (JLabel l : medLabels) {
            JTextField tmp = new JTextField(8);
            medFields.add(tmp);
            l.setLabelFor(tmp);
        }

        //create dental stuff
        dentalLabel = new JLabel("Dental");
        dentalLabel.setFont(dentalLabel.getFont().deriveFont((float) 16));

        for (int i = 0; i < getNumberOfDentalRates(); i++) {
            dentLabels.add(new JLabel("Plan:"));
            dentLabels.add(new JLabel("Type:"));
            dentLabels.add(new JLabel("Start Date:"));
            dentLabels.add(new JLabel("Renewal Date:"));
            dentLabels.add(new JLabel("S:"));
            dentLabels.add(new JLabel("2P:"));
            dentLabels.add(new JLabel("E/CH:"));
            dentLabels.add(new JLabel("E/SP:"));
            dentLabels.add(new JLabel("F:"));
        }
        for (JLabel l : dentLabels) {
            JTextField tmp = new JTextField(8);
            dentFields.add(tmp);
            l.setLabelFor(tmp);
        }
        //create other sutff
        otherLabel = new JLabel("Other");
        otherLabel.setFont(otherLabel.getFont().deriveFont((float) 16));
        for (int i = 0; i < getNumberOfOtherRates(); i++) {
            otherLabels.add(new JLabel("Plan:"));
            otherLabels.add(new JLabel("Type:"));
            otherLabels.add(new JLabel("Start Date:"));
            otherLabels.add(new JLabel("Renewal Date:"));
            otherLabels.add(new JLabel("S:"));
            otherLabels.add(new JLabel("2P:"));
            otherLabels.add(new JLabel("E/CH:"));
            otherLabels.add(new JLabel("E/SP:"));
            otherLabels.add(new JLabel("F:"));
        }
        for (JLabel l : otherLabels) {
            JTextField tmp = new JTextField(8);
            otherFields.add(tmp);
            l.setLabelFor(tmp);
        }
    }

    private void addComponentsToCanvas() {
        this.add(save);
        //add medical stuff
        this.add(medicalLabel);
//        desktop.add(medicalLabel);
        for (JLabel l : medLabels) {
            this.add(l);
            this.add(l.getLabelFor());
//            desktop.add(l);
//            desktop.add(l.getLabelFor());
        }

        //add dental stuff
        this.add(dentalLabel);
//        desktop.add(dentalLabel);
        for (JLabel l : dentLabels) {
            this.add(l);
            this.add(l.getLabelFor());
//            desktop.add(l);
//            desktop.add(l.getLabelFor());
        }

        //add other stuff
        this.add(otherLabel);
        for (JLabel l : otherLabels) {
            this.add(l);
            this.add(l.getLabelFor());
        }
    }

    private void intiLayout() {
        //set the pos of medical rates
        layout.putConstraint(SpringLayout.WEST, medicalLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, medicalLabel, 10, SpringLayout.NORTH, this);
        int pointer = 0;
        for (int i = 0; i < medLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i), 5, SpringLayout.WEST, medicalLabel);
                layout.putConstraint(SpringLayout.NORTH, medLabels.get(i), 15, SpringLayout.SOUTH, medicalLabel);
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, medLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, medLabels.get(i));
//            } else if (i == 1 || (i % (numFields + 1)) == 0) {
            } else if (pointer == 1) {
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i), 0, SpringLayout.WEST, medLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, medLabels.get(i), 10, SpringLayout.SOUTH, medLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, medLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, medLabels.get(i));
                //make the east sides of the two fields flush
                layout.putConstraint(SpringLayout.EAST, medLabels.get(i - 1).getLabelFor(), 0, SpringLayout.EAST, medLabels.get(i).getLabelFor());
            } else if ((i % numFields) == 0) {
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i), 0, SpringLayout.WEST, medLabels.get(i - numFields));
                layout.putConstraint(SpringLayout.NORTH, medLabels.get(i), 30, SpringLayout.SOUTH, medLabels.get(i - (numFields - 1)));
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, medLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, medLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i), 13, SpringLayout.EAST, medLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i), 0, SpringLayout.SOUTH, medLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, medLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, medLabels.get(i));
            }
            pointer++;
            if (pointer >= numFields) {
                pointer = 0;
            }
        }

        //set the pos of dental rates
        if (medLabels.size() != 0) {
            layout.putConstraint(SpringLayout.WEST, dentalLabel, 0, SpringLayout.WEST, medicalLabel);
            layout.putConstraint(SpringLayout.NORTH, dentalLabel, 20, SpringLayout.SOUTH, medLabels.get(medLabels.size() - (numFields - 1)));
        } else {
            layout.putConstraint(SpringLayout.WEST, dentalLabel, 0, SpringLayout.WEST, medicalLabel);
            layout.putConstraint(SpringLayout.NORTH, dentalLabel, 20, SpringLayout.SOUTH, medicalLabel);
        }
        pointer = 0;
        for (int i = 0; i < dentLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i), 5, SpringLayout.WEST, dentalLabel);
                layout.putConstraint(SpringLayout.NORTH, dentLabels.get(i), 15, SpringLayout.SOUTH, dentalLabel);
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dentLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dentLabels.get(i));
            } else if (pointer == 1) {
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i), 0, SpringLayout.WEST, dentLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, dentLabels.get(i), 10, SpringLayout.SOUTH, dentLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dentLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dentLabels.get(i));
                //make the east sides of the two fields flush
                layout.putConstraint(SpringLayout.EAST, dentLabels.get(i - 1).getLabelFor(), 0, SpringLayout.EAST, dentLabels.get(i).getLabelFor());
            } else if ((i % numFields) == 0) {
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i), 0, SpringLayout.WEST, dentLabels.get(i - numFields));
                layout.putConstraint(SpringLayout.NORTH, dentLabels.get(i), 30, SpringLayout.SOUTH, dentLabels.get(i - (numFields - 1)));
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dentLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dentLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i), 13, SpringLayout.EAST, dentLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i), 0, SpringLayout.SOUTH, dentLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dentLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dentLabels.get(i));
            }
            pointer++;
            if (pointer >= numFields) {
                pointer = 0;
            }
        }

        //set the pos of other rates
        if (dentLabels.size() != 0) {
            layout.putConstraint(SpringLayout.WEST, otherLabel, 0, SpringLayout.WEST, dentalLabel);
            layout.putConstraint(SpringLayout.NORTH, otherLabel, 20, SpringLayout.SOUTH, dentLabels.get(dentLabels.size() - (numFields - 1)));
        } else {
            layout.putConstraint(SpringLayout.WEST, otherLabel, 0, SpringLayout.WEST, dentalLabel);
            layout.putConstraint(SpringLayout.NORTH, otherLabel, 20, SpringLayout.SOUTH, dentalLabel);
        }
        pointer = 0;
        for (int i = 0; i < otherLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i), 5, SpringLayout.WEST, otherLabel);
                layout.putConstraint(SpringLayout.NORTH, otherLabels.get(i), 15, SpringLayout.SOUTH, otherLabel);
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, otherLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, otherLabels.get(i));
            } else if (pointer == 1) {
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i), 0, SpringLayout.WEST, otherLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, otherLabels.get(i), 10, SpringLayout.SOUTH, otherLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, otherLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, otherLabels.get(i));
                //make the east sides of the two fields flush
                layout.putConstraint(SpringLayout.EAST, otherLabels.get(i - 1).getLabelFor(), 0, SpringLayout.EAST, otherLabels.get(i).getLabelFor());
            } else if ((i % numFields) == 0) {
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i), 0, SpringLayout.WEST, otherLabels.get(i - numFields));
                layout.putConstraint(SpringLayout.NORTH, otherLabels.get(i), 30, SpringLayout.SOUTH, otherLabels.get(i - (numFields - 1)));
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, otherLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, otherLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i), 13, SpringLayout.EAST, otherLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i), 0, SpringLayout.SOUTH, otherLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, otherLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, otherLabels.get(i));
            }
            pointer++;
            if (pointer >= numFields) {
                pointer = 0;
            }
        }
        layout.putConstraint(SpringLayout.EAST, save, -20, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, medicalLabel);
    }
}
