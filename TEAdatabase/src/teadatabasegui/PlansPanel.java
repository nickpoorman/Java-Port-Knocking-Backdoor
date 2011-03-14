package teadatabasegui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class PlansPanel extends JPanel implements ActionListener {

	private CompanyID companyID;
	SpringLayout layout;
	JButton save;
	JLabel classDropDownLabel;
	JLabel ratingDropDownLabel;
	JLabel waitingPeriodLabel;
	JLabel unionLabel;
	JLabel eligiblesLabel;
	JLabel submittedDateLabel;
	JLabel initialsLabel;
	JLabel approvalDateLabel;
	JLabel welcomeLetterLabel;
	JLabel effectiveDateLabel;
	JComboBox classesDropDown;
	JComboBox ratingDropDown;
	JTextField waitingPeriod;
	JTextField union;
	JTextField eligibles;
	JTextField submittedDate;
	JTextField initials;
	JTextField approvalDate;
	JTextField welcomeLetter;
	JTextField effectiveDate;
	List<String> data;
	int ratingDropDownMenuID = 3;
	List<String> ratingDropDownOptions;
	int ratingSavedOption;
	int classesDropDownMenuID = 4;
	List<String> classesDropDownOptions;
	List<String> classesDropDownOptionsIDs;
	int classesSavedOption;
	boolean currentClassExists = false;
	JButton ratingDropDownEditButton;
	JButton classesDropDownEditButton;

	public PlansPanel(CompanyID companyID) {
		update(companyID);
	}

	public void callUpdate() {
		this.update(this.companyID);
	}

	public CompanyID getCompanyID() {
		return this.companyID;
	}

	private void update(CompanyID companyID) {
		this.removeAll();
		this.companyID = companyID;
		layout = new SpringLayout();
		this.setLayout(layout);

		data = TeaDatabase.getPlansForCompany(companyID.getCompanyID());
		ratingDropDownOptions = TeaDatabase.getDropDownOptions(ratingDropDownMenuID);
		List<List> tmp = TeaDatabase.getDropDownOptionsWithID(classesDropDownMenuID);
		classesDropDownOptionsIDs = tmp.get(0);
		classesDropDownOptions = tmp.get(1);
		// classesDropDownOptions = TeaDatabase.getDropDownOptions(classesDropDownMenuID);

		createComponents();
		addComponentsToCanvas();
		//        updatePreferedSize();
		if (!companyID.isNewCompany()) {
			fillValues();
		}
		intiLayout();
	}

	public void actionPerformed(ActionEvent e) {
		if ("save".equals(e.getActionCommand())) {
			if (checkInput()) {
				save();
				//update the panel
				this.removeAll();
				this.callUpdate();
				this.updateUI();
			}
		} else if ("classes".equals(e.getActionCommand())) {
			System.out.println("CHANGED!");
			reloadClassesDependendFields();

		} else if ("editRatingDropDown".equals(e.getActionCommand())) {
			DropDownEditor.showDropDownEditorWindow(ratingDropDownMenuID, this.getCompanyID());
		} else if ("editClassesDropDown".equals(e.getActionCommand())) {
			DropDownEditor.showDropDownEditorWindow(classesDropDownMenuID, this.getCompanyID());
		}
	}

	public void reloadClassesDependendFields() {
		//        waitingPeriod
		//        eligibles
		//  TeaDatabase.getPlansClassInfoForCompany(this.companyID.getCompanyID(), this.classesDropDown.getSelectedIndex());
		// System.out.println("Selected INDEX IS: " + this.classesDropDown.getSelectedIndex());
		//get the id for the selected index
		//        int selectedIndex = this.classesDropDown.getSelectedIndex();
		//        int selectedClassID = Integer.parseInt(classesDropDownOptionsIDs.get(selectedIndex));
		List<String> classInfo = TeaDatabase.getPlansClassInfoForCompany(this.companyID.getCompanyID(),
				getSelectedClassID());
		//fill the field
		if (classInfo.size() == 4) {
			waitingPeriod.setText(classInfo.get(2));
			eligibles.setText(classInfo.get(3));
			this.currentClassExists = true;
		} else {
			waitingPeriod.setText("");
			eligibles.setText("");
			this.currentClassExists = false;
			System.out.println("the class info size is: " + classInfo.size());
		}
	}

	private int getSelectedClassID() {
		int selectedIndex = this.classesDropDown.getSelectedIndex();
		return Integer.parseInt(classesDropDownOptionsIDs.get(selectedIndex));
	}

	public void createComponents() {
		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		classDropDownLabel = new JLabel("Classes: ");
		classDropDownLabel.setFont(classDropDownLabel.getFont().deriveFont((float) 16));
		ratingDropDownLabel = new JLabel("Rating: ");
		waitingPeriodLabel = new JLabel("*Waiting Period: ");
		unionLabel = new JLabel("Union: ");
		eligiblesLabel = new JLabel("*Eligibles: ");
		submittedDateLabel = new JLabel("Submitted: ");
		initialsLabel = new JLabel("Initials: ");
		approvalDateLabel = new JLabel("Approval Date: ");
		welcomeLetterLabel = new JLabel("Welcome Letter: ");
		effectiveDateLabel = new JLabel("Effective Date: ");

		classesDropDown = new JComboBox(classesDropDownOptions.toArray());
		classesDropDown.setActionCommand("classes");
		classesDropDown.addActionListener(this);

		ratingDropDown = new JComboBox(ratingDropDownOptions.toArray());
		waitingPeriod = new JTextField(20);
		union = new JTextField(20);
		eligibles = new JTextField(20);
		submittedDate = new JTextField(20);
		initials = new JTextField(20);
		approvalDate = new JTextField(20);
		welcomeLetter = new JTextField(20);
		effectiveDate = new JTextField(20);

		if (this.companyID.isNewCompany()) {
			save.setEnabled(false);
			classesDropDown.setEnabled(false);
			ratingDropDown.setEnabled(false);
			waitingPeriod.setEnabled(false);
			union.setEnabled(false);
			eligibles.setEnabled(false);
			submittedDate.setEnabled(false);
			initials.setEnabled(false);
			approvalDate.setEnabled(false);
			welcomeLetter.setEnabled(false);
			effectiveDate.setEnabled(false);
		}
		//the edit button for the ratingDropDown
		ratingDropDownEditButton = new JButton("+");
		ratingDropDownEditButton.setFont(ratingDropDownEditButton.getFont().deriveFont(9f));
		ratingDropDownEditButton.setPreferredSize(new Dimension(20, 100));
		ratingDropDownEditButton.setActionCommand("editRatingDropDown");
		ratingDropDownEditButton.addActionListener(this);

		//the edit button for the classesDropDown
		classesDropDownEditButton = new JButton("+");
		classesDropDownEditButton.setFont(classesDropDownEditButton.getFont().deriveFont(9f));
		classesDropDownEditButton.setPreferredSize(new Dimension(20, 100));
		classesDropDownEditButton.setActionCommand("editClassesDropDown");
		classesDropDownEditButton.addActionListener(this);
	}

	public void addComponentsToCanvas() {
		this.add(save);

		this.add(classDropDownLabel);
		this.add(ratingDropDownLabel);
		this.add(waitingPeriodLabel);
		this.add(unionLabel);
		this.add(eligiblesLabel);
		this.add(submittedDateLabel);
		this.add(initialsLabel);
		this.add(approvalDateLabel);
		this.add(welcomeLetterLabel);
		this.add(effectiveDateLabel);

		this.add(classesDropDown);
		this.add(ratingDropDown);
		this.add(waitingPeriod);
		this.add(union);
		this.add(eligibles);
		this.add(submittedDate);
		this.add(initials);
		this.add(approvalDate);
		this.add(welcomeLetter);
		this.add(effectiveDate);

		//add the drop down edit buttons
		this.add(ratingDropDownEditButton);
		this.add(classesDropDownEditButton);

	}

	public void updatePreferedSize() {
	}

	public boolean checkInput() {
		boolean ok = true;
		//check the eligibles field for a valid number
		//        if (!eligibles.getText().equals("")) {
		//            try {
		//                Integer.parseInt(eligibles.getText());
		//            } catch (NumberFormatException nfe) {
		//                ok = false;
		//                JOptionPane.showMessageDialog(null, "Eligibles must be a number!");
		//            }
		//        }

		//check the submittedDate, approvalDate, welcomeLetter, effectiveDate for a valid date
		////////////////
		if (!verifyDateFormat(submittedDate.getText())) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Submitted Date' must be in YYYY-MM-DD format");
		}
		////////////////
		if (!verifyDateFormat(approvalDate.getText())) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Approval Date' must be in YYYY-MM-DD format");
		}
		////////////////
		if (!verifyDateFormat(welcomeLetter.getText())) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Welcome Letter Date' must be in YYYY-MM-DD format");
		}
		////////////////
		if (!verifyDateFormat(effectiveDate.getText())) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Effective Date' must be in YYYY-MM-DD format");
		}
		////////////////

		//check to see if the lengths of the fields are not longer then they are supposed to be
		if (waitingPeriod.getText().length() > 50) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Waiting Period' must be no more then 50 characters!");
		}
		/////////
		if (union.getText().length() > 20) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Union' must be no more then 20 characters!");
		}
		/////////
		if (initials.getText().length() > 20) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Initials' must be no more then 20 characters!");
		}
		/////////
		if (initials.getText().length() > 50) {
			ok = false;
			JOptionPane.showMessageDialog(null, "'Eligibles' must be no more then 50 characters!");
		}
		/////////

		return ok;
	}

	private boolean verifyDateFormat(String date) {
		try {
			if (date.equals("")) {
				return true;
			}

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

	public void save() {
		if (!this.companyID.isNewCompany()) {
			//get all the values for the company's plan page and save them
			TeaDatabase.updatePlansForCompany(this.companyID.getCompanyID(), union.getText(), submittedDate.getText(),
					initials.getText(), approvalDate.getText(), welcomeLetter.getText(), effectiveDate.getText());

			//save the rating drop down selected option
			TeaDatabase.updateSavedDropDowns(ratingDropDown.getSelectedIndex() + 1, ratingDropDownMenuID, companyID
					.getCompanyID());

			//save the classes drop down selected option           
			TeaDatabase.updateSavedDropDowns(classesDropDown.getSelectedIndex() + 1, classesDropDownMenuID, companyID
					.getCompanyID());

			//save the class dependent fields

			if (this.currentClassExists) {
				TeaDatabase.updatePlansClassInfoForCompany(this.companyID.getCompanyID(), getSelectedClassID(),
						waitingPeriod.getText(), eligibles.getText());

			} else {
				TeaDatabase.insertPlansClassInfoForCompany(this.companyID.getCompanyID(), getSelectedClassID(),
						waitingPeriod.getText(), eligibles.getText());
			}
			System.out.println("SAVED");
		} else {
			System.out.println("Cannot save a new company");
		}

	}

	public void fillValues() {
		//                    /*New Stuff -->*/
		//                CREATE TABLE IF NOT EXISTS plans(
		//           0    company_id          INT NOT NULL,
		//           1    union_yes_no        INT(1),
		//           2    submitted           DATE,
		//           3    initials            VARCHAR(10),
		//           4    approval_date       DATE,
		//           5    welcome_letter      DATE,
		//           6    effective_date      DATE,
		//                FOREIGN KEY(company_id) REFERENCES companies(company_id)
		//                );
		//
		//                CREATE TABLE IF NOT EXISTS plans_class_info(
		//                company_id          INT NOT NULL,
		//                class_id            INT NOT NULL,
		//                waiting_period      VARCHAR(50),
		//                eligibles           INT,
		//                FOREIGN KEY(company_id) REFERENCES companies(company_id),
		//                FOREIGN KEY(class_id) REFERENCES drop_down_options(option_id)
		//                );

		union.setText(data.get(1));
		submittedDate.setText(data.get(2));
		initials.setText(data.get(3));
		approvalDate.setText(data.get(4));
		welcomeLetter.setText(data.get(5));
		effectiveDate.setText(data.get(6));
		//set the rating drop down menu
		//get the selected index
		ratingSavedOption = TeaDatabase.getSavedOption(ratingDropDownMenuID, companyID.getCompanyID());
		if (ratingSavedOption > -1) {
			ratingDropDown.setSelectedIndex(ratingSavedOption - 1);
		}

		classesSavedOption = TeaDatabase.getSavedOption(classesDropDownMenuID, companyID.getCompanyID());
		if (classesSavedOption > -1) {
			classesDropDown.setSelectedIndex(classesSavedOption - 1);
		}

	}

	public void insertNewPanelIntoDB() {
		TeaDatabase.insertPlansForNewCompany(companyID.getCompanyID());
		//set the default for the rating drop down
		TeaDatabase.insertSelectedDropDownsOption(0, ratingDropDownMenuID, companyID.getCompanyID());
		TeaDatabase.insertSelectedDropDownsOption(0, classesDropDownMenuID, companyID.getCompanyID());
	}

	public void intiLayout() {
		//classes drop down
		layout.putConstraint(SpringLayout.WEST, classDropDownLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, classDropDownLabel, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, classesDropDown, 10, SpringLayout.EAST, classDropDownLabel);
		layout.putConstraint(SpringLayout.SOUTH, classesDropDown, 0, SpringLayout.SOUTH, classDropDownLabel);

		//waiting period
		layout.putConstraint(SpringLayout.WEST, waitingPeriodLabel, 15, SpringLayout.WEST, classDropDownLabel);
		layout.putConstraint(SpringLayout.NORTH, waitingPeriodLabel, 20, SpringLayout.SOUTH, classDropDownLabel);
		layout.putConstraint(SpringLayout.WEST, waitingPeriod, 10, SpringLayout.EAST, waitingPeriodLabel);
		layout.putConstraint(SpringLayout.SOUTH, waitingPeriod, 0, SpringLayout.SOUTH, waitingPeriodLabel);

		//eligibles
		layout.putConstraint(SpringLayout.WEST, eligiblesLabel, 0, SpringLayout.WEST, waitingPeriodLabel);
		layout.putConstraint(SpringLayout.NORTH, eligiblesLabel, 20, SpringLayout.SOUTH, waitingPeriodLabel);
		layout.putConstraint(SpringLayout.WEST, eligibles, 10, SpringLayout.EAST, eligiblesLabel);
		layout.putConstraint(SpringLayout.SOUTH, eligibles, 0, SpringLayout.SOUTH, eligiblesLabel);

		//union
		layout.putConstraint(SpringLayout.WEST, unionLabel, 0, SpringLayout.WEST, eligiblesLabel);
		layout.putConstraint(SpringLayout.NORTH, unionLabel, 20, SpringLayout.SOUTH, eligiblesLabel);
		layout.putConstraint(SpringLayout.WEST, union, 10, SpringLayout.EAST, unionLabel);
		layout.putConstraint(SpringLayout.SOUTH, union, 0, SpringLayout.SOUTH, unionLabel);

		//rating
		layout.putConstraint(SpringLayout.WEST, ratingDropDownLabel, 0, SpringLayout.WEST, unionLabel);
		layout.putConstraint(SpringLayout.NORTH, ratingDropDownLabel, 20, SpringLayout.SOUTH, unionLabel);
		layout.putConstraint(SpringLayout.WEST, ratingDropDown, 10, SpringLayout.EAST, ratingDropDownLabel);
		layout.putConstraint(SpringLayout.SOUTH, ratingDropDown, 0, SpringLayout.SOUTH, ratingDropDownLabel);

		//submitted
		layout.putConstraint(SpringLayout.WEST, submittedDateLabel, 0, SpringLayout.WEST, ratingDropDownLabel);
		layout.putConstraint(SpringLayout.NORTH, submittedDateLabel, 20, SpringLayout.SOUTH, ratingDropDownLabel);
		layout.putConstraint(SpringLayout.WEST, submittedDate, 10, SpringLayout.EAST, submittedDateLabel);
		layout.putConstraint(SpringLayout.SOUTH, submittedDate, 0, SpringLayout.SOUTH, submittedDateLabel);

		//initials
		layout.putConstraint(SpringLayout.WEST, initialsLabel, 0, SpringLayout.WEST, submittedDateLabel);
		layout.putConstraint(SpringLayout.NORTH, initialsLabel, 20, SpringLayout.SOUTH, submittedDateLabel);
		layout.putConstraint(SpringLayout.WEST, initials, 10, SpringLayout.EAST, initialsLabel);
		layout.putConstraint(SpringLayout.SOUTH, initials, 0, SpringLayout.SOUTH, initialsLabel);

		//approval date
		layout.putConstraint(SpringLayout.WEST, approvalDateLabel, 0, SpringLayout.WEST, initialsLabel);
		layout.putConstraint(SpringLayout.NORTH, approvalDateLabel, 20, SpringLayout.SOUTH, initialsLabel);
		layout.putConstraint(SpringLayout.WEST, approvalDate, 10, SpringLayout.EAST, approvalDateLabel);
		layout.putConstraint(SpringLayout.SOUTH, approvalDate, 0, SpringLayout.SOUTH, approvalDateLabel);

		//welcome letter
		layout.putConstraint(SpringLayout.WEST, welcomeLetterLabel, 0, SpringLayout.WEST, approvalDateLabel);
		layout.putConstraint(SpringLayout.NORTH, welcomeLetterLabel, 20, SpringLayout.SOUTH, approvalDateLabel);
		layout.putConstraint(SpringLayout.WEST, welcomeLetter, 10, SpringLayout.EAST, welcomeLetterLabel);
		layout.putConstraint(SpringLayout.SOUTH, welcomeLetter, 0, SpringLayout.SOUTH, welcomeLetterLabel);

		//effective date
		layout.putConstraint(SpringLayout.WEST, effectiveDateLabel, 0, SpringLayout.WEST, welcomeLetterLabel);
		layout.putConstraint(SpringLayout.NORTH, effectiveDateLabel, 20, SpringLayout.SOUTH, welcomeLetterLabel);
		layout.putConstraint(SpringLayout.WEST, effectiveDate, 10, SpringLayout.EAST, effectiveDateLabel);
		layout.putConstraint(SpringLayout.SOUTH, effectiveDate, 0, SpringLayout.SOUTH, effectiveDateLabel);

		//need to make them all even
		layout.putConstraint(SpringLayout.WEST, ratingDropDown, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, ratingDropDown, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, waitingPeriod, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, waitingPeriod, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, union, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, union, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, eligibles, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, eligibles, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, submittedDate, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, submittedDate, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, initials, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, initials, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, approvalDate, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, approvalDate, 0, SpringLayout.EAST, welcomeLetter);
		layout.putConstraint(SpringLayout.WEST, effectiveDate, 0, SpringLayout.WEST, welcomeLetter);
		layout.putConstraint(SpringLayout.EAST, effectiveDate, 0, SpringLayout.EAST, welcomeLetter);

		layout.putConstraint(SpringLayout.EAST, save, -20, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, classDropDownLabel);
		
		//constraint for the drop down edit buttons
		//put the groupSizeDropDownEditButton next to the groupSizeDropDown
		layout.putConstraint(SpringLayout.WEST, ratingDropDownEditButton, 5, SpringLayout.EAST, ratingDropDown);
		layout.putConstraint(SpringLayout.SOUTH, ratingDropDownEditButton, 0, SpringLayout.SOUTH, ratingDropDown);
		layout.putConstraint(SpringLayout.NORTH, ratingDropDownEditButton, 0, SpringLayout.NORTH, ratingDropDown);

		//statusDropDownEditButton
		//put the statusDropDownEditButton next to the statusDropDown
		layout.putConstraint(SpringLayout.WEST, classesDropDownEditButton, 5, SpringLayout.EAST, classesDropDown);
		layout.putConstraint(SpringLayout.SOUTH, classesDropDownEditButton, 0, SpringLayout.SOUTH, classesDropDown);
		layout.putConstraint(SpringLayout.NORTH, classesDropDownEditButton, 0, SpringLayout.NORTH, classesDropDown);
	}
}
