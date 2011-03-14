package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class RatesPanel extends JPanel implements ActionListener {

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
    RatesArchivePanel ratesArchivePanel;
    JButton save;
    JButton newRate;
    JPanel newPanel;
    JPanel savedPanel;
    List<JButton> medDetailsButtons;
    List<JButton> dentDetailsButtons;
    List<JButton> otherDetailsButtons;

    public RatesPanel(CompanyID companyID) {
        update(companyID);
    }

    public void callUpdate() {
        update(this.companyID);
    }

    private void update(CompanyID companyID) {
        this.removeAll();
        this.companyID = companyID;
        if (!companyID.isNewCompany()) {
            medicalData = TeaDatabase.getAllNonExpiredMedicalRates(companyID.getCompanyID());
            dentalData = TeaDatabase.getAllNonExpiredDentalRates(companyID.getCompanyID());
            otherData = TeaDatabase.getAllNonExpiredOtherRates(companyID.getCompanyID());
        } else {
            medicalData = new LinkedList();
            dentalData = new LinkedList();
            otherData = new LinkedList();
        }
        layout = new SpringLayout();
        this.setLayout(layout);
        createComponents();
        addComponentsToCanvas();
        updatePreferedSize();
        if (!companyID.isNewCompany()) {
            fillValues();
        }
        intiLayout();
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

    public static String changeDateUserToDatabase(String date) {
        if (date == null) {
            return null;
        } else if (date.equals("")) {
            return "";
        }
        try {
            String inputDateFormat = "MM/dd/yyyy";
            String[] dates = date.split("/");
            if (dates[2].length() == 2) {
                inputDateFormat = "MM/dd/yy";
            }
            SimpleDateFormat informat = new SimpleDateFormat(inputDateFormat);
            informat.setLenient(false);
            SimpleDateFormat outformat = new SimpleDateFormat("MM/dd/yyyy");
            outformat.setLenient(false);
            String newDate = outformat.format(informat.parse(date));
            inputDateFormat = "MM/dd/yyyy";
            String outputDateFormat = "yyyy-MM-dd";
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputDateFormat);
            inputFormatter.setLenient(false);
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputDateFormat);
            outputFormatter.setLenient(false);
            Date parsedDate = inputFormatter.parse(newDate);
            String output = outputFormatter.format(parsedDate);
            return output;
        } catch (ParseException pe) {
            return null;
        }
    }

    public static String changeDateDatabaseToUser(String date) {
        if (date == null) {
            return null;
        } else if (date.equals("")) {
            return "";
        }
        try {
            String inputDateFormat = "yyyy-MM-dd";
            String[] dates = date.split("-");
            if (dates[0].length() == 2) {
                inputDateFormat = "yy-MM-dd";
            }
            SimpleDateFormat informat = new SimpleDateFormat(inputDateFormat);
            informat.setLenient(false);
            SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");
            outformat.setLenient(false);
            String newDate = outformat.format(informat.parse(date));
            inputDateFormat = "yyyy-MM-dd";
            String outputDateFormat = "MM/dd/yyyy";
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputDateFormat);
            inputFormatter.setLenient(false);
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputDateFormat);
            outputFormatter.setLenient(false);
            Date parsedDate = inputFormatter.parse(newDate);
            String output = outputFormatter.format(parsedDate);
            return output;
        } catch (ParseException pe) {
            return null;
        }
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
                if (ratesArchivePanel != null) {
                    ratesArchivePanel.callUpdate();
                    ratesArchivePanel.updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "The Start Date and Renewal Date must be in MM/DD/YYYY format");
            }
        } else if ("newRate".equals(e.getActionCommand())) {
            newRate();
        }
    }

    private void newRate() {
        //create a new rate popup window and do what ever needs to be done to add a new rate        
        NewRatePanel nrp = new NewRatePanel(companyID, this);
        nrp.setVisible(true);
    }

    public static void openCompanyDetailsWindow(int companyID, int plancodeID) {
        List<String> companyName = TeaDatabase.getCompanyName(companyID + "");
        List<String> planCode = TeaDatabase.getPlanCodeFromPlanCodeID(companyID + "", plancodeID + "");
        String title = "Company: " + companyName.get(0) + " PlanCode: " + planCode.get(0);
        DetailsFrame fr = new DetailsFrame(companyID, plancodeID, title);
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
        //then get all the other fields
        List<String> otherPopFields = new LinkedList<String>();
        for (int i = 0; i < otherFields.size(); i++) {
            otherPopFields.add(otherFields.get(i).getText());
        }
        //then get all the id's in a list so we know what goes where
        List<String> otherIDs = new LinkedList<String>();
        for (int i = 0; i < otherData.size(); i = i + 12) {
            otherIDs.add(otherData.get(i));
        }
        //update the medical data
        for (int i = 0; i < medIDs.size(); i++) {
            List<String> tmp = medPopFields.subList(((numFields * (i + 1)) - numFields), (numFields * (i + 1)));
            TeaDatabase.updateRatesForCompany(companyID.getCompanyID(), Integer.parseInt(medIDs.get(i)), formatForOut(tmp));
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
        //change date for out to database
        tmp.add(RatesPanel.changeDateUserToDatabase(list.get(3)));

        //change date for out to database
        tmp.add(RatesPanel.changeDateUserToDatabase(list.get(2)));

        tmp.add(list.get(1));
        return tmp;
    }

    private void fillValues() {
        List<String> fmtMedData = formatData(medicalData);
        for (int i = 0; i < medFields.size(); i++) {
            medFields.get(i).setText(fmtMedData.get(i));
        }

        //fill in the dental information
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
        for (int i = 0; i < (data.size() / numColumns); i++) {
            tmp.add(data.get(2 + (i * numColumns)));
            tmp.add(data.get(11 + (i * numColumns)));

            //start date
            tmp.add(RatesPanel.changeDateDatabaseToUser(data.get(10 + (i * numColumns))));

            //end date
            tmp.add(RatesPanel.changeDateDatabaseToUser(data.get(9 + (i * numColumns))));

            tmp.add(data.get(3 + (i * numColumns)));
            tmp.add(data.get(4 + (i * numColumns)));
            tmp.add(data.get(5 + (i * numColumns)));
            tmp.add(data.get(6 + (i * numColumns)));
            tmp.add(data.get(7 + (i * numColumns)));
        }
        return tmp;
    }

    public static boolean verifyDateFormat(String date) {
        if(date.equals("")){
            return false;
        }
        //first check to make sure that all characters are numbers / or -
        for (int i = 0; i < date.length(); i++) {
            char c = date.charAt(i);
            if ((c < '0') || (c > '9')) {
                if ((c != '-') && (c != '/')) {
                    return false;
                }
            }
        }
        //check to see if its from database or from user
        if (date.contains("-")) {
            //from database
            String[] dates = date.split("-");
            if ((dates[0].length() != 2) && (dates[0].length() != 4)) {
                return false;
            }
        }
        if (date.contains("/")) {
            //from user
            String[] dates = date.split("/");
            if ((dates[2].length() != 2) && (dates[2].length() != 4)) {
                return false;
            }
        }
        if ((RatesPanel.changeDateDatabaseToUser(date) == null) && (RatesPanel.changeDateUserToDatabase(date) == null)) {
            return false;
        }
        return true;
    }

    private int getNumberOfMedicalRates() {
        int numRates = 0;
        // int numColumns = 12;
        if (medicalData != null) {
            numRates = medicalData.size() / numColumns;
        }
        return numRates;
    }

    private int getNumberOfDentalRates() {
        int numRates = 0;
        //int numColumns = 12;
        if (medicalData != null) {
            numRates = dentalData.size() / numColumns;
        }
        return numRates;
    }

    private int getNumberOfOtherRates() {
        int numRates = 0;
        //int numColumns = 12;
        if (otherData != null) {
            numRates = otherData.size() / numColumns;
        }
        return numRates;
    }

    private void createComponents() {
        //create the details button to open up the details window
        medDetailsButtons = new LinkedList<JButton>();
        dentDetailsButtons = new LinkedList<JButton>();
        otherDetailsButtons = new LinkedList<JButton>();

        medLabels = new LinkedList<JLabel>();
        medFields = new LinkedList<JTextField>();
        dentLabels = new LinkedList<JLabel>();
        dentFields = new LinkedList<JTextField>();
        otherLabels = new LinkedList<JLabel>();
        otherFields = new LinkedList<JTextField>();
        save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        newRate = new JButton("New Rate");
        newRate.setActionCommand("newRate");
        newRate.addActionListener(this);

        if (this.companyID.isNewCompany()) {
            save.setEnabled(false);
            newRate.setEnabled(false);
        }

        //create medical stuff
        medicalLabel = new JLabel("Medical");
        medicalLabel.setFont(medicalLabel.getFont().deriveFont((float) 16));
        for (int i = 0; i < getNumberOfMedicalRates(); i++) {
            //create the fields
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

        //CREATE ALL THE DETAILS BUTTONS
        //first get all the id's in a list so we know what goes where
        List<String> medIDs = new LinkedList<String>();
        for (int i = 0; i < medicalData.size(); i = i + 12) {
            medIDs.add(medicalData.get(i));
            medIDs.add(medicalData.get(i + 1));
        }
        //then get all the id's in a list so we know what goes where
        List<String> dentIDs = new LinkedList<String>();
        for (int i = 0; i < dentalData.size(); i = i + 12) {
            dentIDs.add(dentalData.get(i));
            dentIDs.add(dentalData.get(i + 1));
        }
        //then get all the id's in a list so we know what goes where
        List<String> otherIDs = new LinkedList<String>();
        for (int i = 0; i < otherData.size(); i = i + 12) {
            otherIDs.add(otherData.get(i));
            otherIDs.add(otherData.get(i + 1));
        }

        //for all the medRates create a details button for each
        for (int i = 0; i < medIDs.size(); i = i + 2) {
            final int tmpPlanCodeID = Integer.parseInt(medIDs.get(i));
            final int tmpCompanyID = Integer.parseInt(medIDs.get(i + 1));
            //get each medical id and create a details button for it
            JButton tmp = new JButton("Details");
            tmp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    RatesPanel.openCompanyDetailsWindow(tmpCompanyID, tmpPlanCodeID);
                }
            });
            medDetailsButtons.add(tmp);
        }

        //for all the dentalRates create a details button for each
        for (int i = 0; i < dentIDs.size(); i = i + 2) {
            final int tmpPlanCodeID = Integer.parseInt(dentIDs.get(i));
            final int tmpCompanyID = Integer.parseInt(dentIDs.get(i + 1));
            //get each dental id and create a details button for it
            JButton tmp = new JButton("Details");
            tmp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("tmpCompanyID: " + tmpCompanyID + " tmpPlanCodeID: " + tmpPlanCodeID);
                    RatesPanel.openCompanyDetailsWindow(tmpCompanyID, tmpPlanCodeID);
                }
            });
            dentDetailsButtons.add(tmp);
        }

        //for all the otherRates create a details button for each
        for (int i = 0; i < otherIDs.size(); i = i + 2) {
            final int tmpPlanCodeID = Integer.parseInt(otherIDs.get(i));
            final int tmpCompanyID = Integer.parseInt(otherIDs.get(i + 1));
            //get each other id and create a details button for it
            JButton tmp = new JButton("Details");
            tmp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    RatesPanel.openCompanyDetailsWindow(tmpCompanyID, tmpPlanCodeID);
                }
            });
            otherDetailsButtons.add(tmp);
        }
    }

    private void addComponentsToCanvas() {
        this.add(newRate);
        this.add(save);
        //add medical stuff
        this.add(medicalLabel);
        for (JLabel l : medLabels) {
            this.add(l);
            this.add(l.getLabelFor());
        }

        //add dental stuff
        this.add(dentalLabel);
        for (JLabel l : dentLabels) {
            this.add(l);
            this.add(l.getLabelFor());
        }

        //add other stuff
        this.add(otherLabel);
        for (JLabel l : otherLabels) {
            this.add(l);
            this.add(l.getLabelFor());
        }

        ////////////
        //add all med buttons to container
        for (JButton b : medDetailsButtons) {
            this.add(b);
        }
        //add all dental buttons to the container
        for (JButton b : dentDetailsButtons) {
            this.add(b);
        }
        //add all other buttons to the container
        for (JButton b : otherDetailsButtons) {
            this.add(b);
        }

    }

    private void intiLayout() {
        //set the pos of medical rates
        layout.putConstraint(SpringLayout.WEST, medicalLabel, 4, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, medicalLabel, 10, SpringLayout.NORTH, this);
        int pointer = 0;
        int buttonPointer = 0;
        for (int i = 0; i < medLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i), 5, SpringLayout.WEST, medicalLabel);
                layout.putConstraint(SpringLayout.NORTH, medLabels.get(i), 15, SpringLayout.SOUTH, medicalLabel);
                layout.putConstraint(SpringLayout.WEST, medLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, medLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, medLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, medLabels.get(i));
                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, medDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, medLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, medDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, medLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, medDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, medLabels.get(i).getLabelFor());
                buttonPointer++;
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
                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, medDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, medLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, medDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, medLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, medDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, medLabels.get(i).getLabelFor());
                buttonPointer++;
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
        buttonPointer = 0;
        for (int i = 0; i < dentLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i), 5, SpringLayout.WEST, dentalLabel);
                layout.putConstraint(SpringLayout.NORTH, dentLabels.get(i), 15, SpringLayout.SOUTH, dentalLabel);
                layout.putConstraint(SpringLayout.WEST, dentLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dentLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dentLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dentLabels.get(i));
                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, dentDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, dentLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, dentDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, dentLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, dentDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, dentLabels.get(i).getLabelFor());
                buttonPointer++;
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
                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, dentDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, dentLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, dentDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, dentLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, dentDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, dentLabels.get(i).getLabelFor());
                buttonPointer++;
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
        buttonPointer = 0;
        for (int i = 0; i < otherLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i), 5, SpringLayout.WEST, otherLabel);
                layout.putConstraint(SpringLayout.NORTH, otherLabels.get(i), 15, SpringLayout.SOUTH, otherLabel);
                layout.putConstraint(SpringLayout.WEST, otherLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, otherLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, otherLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, otherLabels.get(i));

                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, otherDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, otherLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, otherDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, otherLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, otherDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, otherLabels.get(i).getLabelFor());
                buttonPointer++;
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
                //set the pos for the medical details button
                layout.putConstraint(SpringLayout.NORTH, otherDetailsButtons.get(buttonPointer), 0, SpringLayout.NORTH, otherLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, otherDetailsButtons.get(buttonPointer), 0, SpringLayout.SOUTH, otherLabels.get(i).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, otherDetailsButtons.get(buttonPointer), 10, SpringLayout.EAST, otherLabels.get(i).getLabelFor());
                buttonPointer++;
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

        //pos save button
        layout.putConstraint(SpringLayout.EAST, save, -20, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, medicalLabel);
        //pos new rate button
        layout.putConstraint(SpringLayout.EAST, newRate, -20, SpringLayout.WEST, save);
        layout.putConstraint(SpringLayout.SOUTH, newRate, 0, SpringLayout.SOUTH, save);
    }

    /**
     * @param ratesArchivePanel the ratesArchivePanel to set
     */
    public void setRatesArchivePanel(RatesArchivePanel ratesArchivePanel) {
        this.ratesArchivePanel = ratesArchivePanel;
    }
}

class NewRatePanel extends JFrame implements ActionListener {

    List<JLabel> dataLabels;
    List<JTextField> dataFields;
    JLabel dataLabel;
    SpringLayout layout;
    int numFields = 9;
    JPanel desktop;
    JButton save;
    JButton cancel;
    CompanyID companyID;
    RatesPanel ratesPanel;

    public NewRatePanel(CompanyID companyID, RatesPanel ratesPanel) {
        this.ratesPanel = ratesPanel;
        this.companyID = companyID;
        int inset = 100;
        Dimension newDimension = new Dimension(1024, 170);
        setBounds(inset, inset, (int) newDimension.getWidth(), (int) newDimension.getHeight());
        setResizable(false);
        desktop = new JPanel();
        layout = new SpringLayout();
        desktop.setLayout(layout);
        this.setLayout(new BorderLayout());
        this.add(desktop, BorderLayout.CENTER);
        this.setContentPane(desktop);
        createComponents();
        addComponentsToCanvas();
        intiLayout();
    }

    private boolean verifyDateFormat(String date) {
        return RatesPanel.verifyDateFormat(date);
    }

    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            //before we save check to see if the two date fields have actual dates
            //dates are in 2 & 3
            boolean dateOneOK = verifyDateFormat(dataFields.get(2).getText());
            boolean dateTwoOK = verifyDateFormat(dataFields.get(3).getText());

            if (dateOneOK && dateTwoOK) {

                save();
                //update the rates and rates archive pages
                //refresh rates page
                ratesPanel.callUpdate();
                ratesPanel.updateUI();
                //refresh rates archive page
                if (ratesPanel.ratesArchivePanel != null) {
                    ratesPanel.ratesArchivePanel.callUpdate();
                    ratesPanel.ratesArchivePanel.updateUI();
                }
                close();
            } else {
                //if the dates were not right then popup a window saying the correct date format
                JOptionPane.showMessageDialog(null, "The Start Date and Renewal Date must be in valid MM/dd/yyyy format");
            }
        } else if ("cancel".equals(e.getActionCommand())) {
            //close the window but frist check to see if anything has changed
            boolean changed = false;
            for (JTextField f : dataFields) {
                if (!f.getText().equals("")) {
                    changed = true;
                }
            }
            if (changed) {
                int selection = JOptionPane.showConfirmDialog(null, "You have added information are you sure you want to close the window?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
                if (selection == JOptionPane.YES_OPTION) {
                    close();
                }
            } else {
                close();
            }

        }
    }

    private void close() {
        this.dispose();
    }

    private void save() {
        //get all the data from the fields and insert it into the database
        List<String> tmpData = new LinkedList<String>();
        for (JTextField f : dataFields) {
            tmpData.add(f.getText());
        }

        List<String> data = formatForOut(tmpData);
        TeaDatabase.insertCompanyRates(data);
    }

    private List<String> formatForOut(List<String> list) {
        List<String> tmp = new LinkedList<String>();
        //company_id, plan_code, s_field, two_p, e_ch, e_sp, f_field, contracts, renewal, filing, medical_or_dental
//        0 dataLabels.add(new JLabel("Plan:"));
//        1 dataLabels.add(new JLabel("Type:"));
//        2 dataLabels.add(new JLabel("Start Date:"));
//        3 dataLabels.add(new JLabel("Renewal Date:"));
//        4 dataLabels.add(new JLabel("S:"));
//        5 dataLabels.add(new JLabel("2P:"));
//        6 dataLabels.add(new JLabel("E/CH:"));
//        7 dataLabels.add(new JLabel("E/SP:"));
//        8 dataLabels.add(new JLabel("F:"));
        tmp.add(companyID.getCompanyIDString());
        tmp.add(list.get(0));
        tmp.add(list.get(4));
        tmp.add(list.get(5));
        tmp.add(list.get(6));
        tmp.add(list.get(7));
        tmp.add(list.get(8));
        tmp.add("none");
        //fix the date to go into the database
        tmp.add(RatesPanel.changeDateUserToDatabase(list.get(3)));
        //fix the date to go into the database
        tmp.add(RatesPanel.changeDateUserToDatabase(list.get(2)));
        tmp.add(list.get(1));
        return tmp;
    }

    public void createComponents() {
        save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);

        dataLabels = new LinkedList<JLabel>();
        dataFields = new LinkedList<JTextField>();
        dataLabel = new JLabel("Enter a new rate:");
        dataLabel.setFont(dataLabel.getFont().deriveFont((float) 16));

        dataLabels.add(new JLabel("Plan:"));
        dataLabels.add(new JLabel("Type:"));
        dataLabels.add(new JLabel("Start Date:"));
        dataLabels.add(new JLabel("Renewal Date:"));
        dataLabels.add(new JLabel("S:"));
        dataLabels.add(new JLabel("2P:"));
        dataLabels.add(new JLabel("E/CH:"));
        dataLabels.add(new JLabel("E/SP:"));
        dataLabels.add(new JLabel("F:"));

        for (JLabel l : dataLabels) {
            JTextField tmp = new JTextField(8);
            dataFields.add(tmp);
            l.setLabelFor(tmp);
        }

    }

    public void addComponentsToCanvas() {
        this.getContentPane().add(save);
        this.getContentPane().add(cancel);
        this.getContentPane().add(dataLabel);
        for (JLabel l : dataLabels) {
            this.getContentPane().add(l);
            this.getContentPane().add(l.getLabelFor());
        }
    }

    public void intiLayout() {
        //post the save and cancel buttons
        layout.putConstraint(SpringLayout.EAST, cancel, -10, SpringLayout.EAST, this.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, cancel, -10, SpringLayout.SOUTH, this.getContentPane());
        layout.putConstraint(SpringLayout.EAST, save, -10, SpringLayout.WEST, cancel);
        layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, cancel);

        //set the pos of the new rate
        layout.putConstraint(SpringLayout.WEST, dataLabel, 4, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, dataLabel, 10, SpringLayout.NORTH, this.getContentPane());

        int pointer = 0;
        for (int i = 0; i < dataLabels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i), 5, SpringLayout.WEST, dataLabel);
                layout.putConstraint(SpringLayout.NORTH, dataLabels.get(i), 15, SpringLayout.SOUTH, dataLabel);
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dataLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dataLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dataLabels.get(i));
            } else if (pointer == 1) {
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i), 0, SpringLayout.WEST, dataLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.NORTH, dataLabels.get(i), 10, SpringLayout.SOUTH, dataLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dataLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dataLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dataLabels.get(i));
                //make the east sides of the two fields flush
                layout.putConstraint(SpringLayout.EAST, dataLabels.get(i - 1).getLabelFor(), 0, SpringLayout.EAST, dataLabels.get(i).getLabelFor());
            } else if ((i % numFields) == 0) {
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i), 0, SpringLayout.WEST, dataLabels.get(i - numFields));
                layout.putConstraint(SpringLayout.NORTH, dataLabels.get(i), 30, SpringLayout.SOUTH, dataLabels.get(i - (numFields - 1)));
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dataLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dataLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dataLabels.get(i));
            } else {
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i), 13, SpringLayout.EAST, dataLabels.get(i - 1).getLabelFor());
                layout.putConstraint(SpringLayout.SOUTH, dataLabels.get(i), 0, SpringLayout.SOUTH, dataLabels.get(i - 1));
                layout.putConstraint(SpringLayout.WEST, dataLabels.get(i).getLabelFor(), 5, SpringLayout.EAST, dataLabels.get(i));
                layout.putConstraint(SpringLayout.SOUTH, dataLabels.get(i).getLabelFor(), 0, SpringLayout.SOUTH, dataLabels.get(i));
            }
            pointer++;
            if (pointer >= numFields) {
                pointer = 0;
            }
        }
    }
}
