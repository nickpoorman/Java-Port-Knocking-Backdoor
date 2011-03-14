package teadatabasegui;

import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author nick
 */
public class Calendar extends JPanel implements TableModelListener, ListSelectionListener, ActionListener, DocumentListener, ItemListener {

    JCalendar calendar;
    SpringLayout layout;
    static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    JButton searchButton;
    JTextField searchField;
    JTable table;
    JScrollPane tablePane;
    DefaultTableModel model;
    JPanel calendarPanel;
    JPanel dataPanel;
    List<CalendarTask> tasks;
    boolean done = false;
    JTextField taskField;
    JTextField companyNameField;
    JTextField startDateField;
    JTextField completedDateField;
    JLabel taskLabel;
    JLabel companyNameLabel;
    JLabel startDateLabel;
    JLabel completedDatelabel;
    CalendarTask currentTask;
    JButton updateButton;
    JButton insertButton;
    JButton completeButton;
    JButton clearButton;
    static final int GET_ALL_TASKS = 0;
    static final int GET_TASKS_FOR_DAY = 1;
    private String currentSearchString = "";
    JCheckBox searchWhileIType;

    public Calendar() {
        layout = new SpringLayout();
        this.setLayout(new BorderLayout());
        createComponents();
        addComponentsToCanvas();
        initLayout();
        done = true;
    }

    public void createComponents() {
        searchWhileIType = new JCheckBox("Search while typing (slow)");
        searchWhileIType.addItemListener(this);
        searchWhileIType.setSelected(true);

        calendarPanel = new JPanel();
        calendarPanel.setPreferredSize(new Dimension(230, 200));
        calendarPanel.setLayout(layout);
        dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout());

        calendar = new JCalendar();
        calendar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarDateChange(evt);
            }
        });
        searchButton = new JButton("Search");
        searchButton.setActionCommand("search");
        searchButton.addActionListener(this);
        searchField = new JTextField(20);
        searchField.setActionCommand("search");
        searchField.getDocument().addDocumentListener(this);
        searchField.addActionListener(this);
        model = new NonEditableTableModel();
        model.addTableModelListener(this);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(this);
        table.getColumnModel().getSelectionModel().addListSelectionListener(this);


        model.addColumn("Task");
        model.addColumn("Company");
        model.addColumn("Start Date");
        model.addColumn("Completed Date");
        model.addColumn("Task ID");
        table.getColumnModel().getColumn(0).setPreferredWidth(470);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.removeColumn(table.getColumnModel().getColumn(4));

        tablePane = new JScrollPane(table);

        taskField = new JTextField();
        companyNameField = new JTextField();
        startDateField = new JTextField();
        startDateField.setEditable(false);
        completedDateField = new JTextField();
        completedDateField.setEditable(false);
        taskLabel = new JLabel("Task: ");
        companyNameLabel = new JLabel("Company Name: ");
        startDateLabel = new JLabel("Start Date: ");
        completedDatelabel = new JLabel("Completed Date: ");

        updateButton = new JButton("Update");
        updateButton.setActionCommand("update");
        updateButton.addActionListener(this);
        updateButton.setEnabled(false);
        insertButton = new JButton("Add New");
        insertButton.setActionCommand("add");
        insertButton.addActionListener(this);
        completeButton = new JButton("Complete");
        completeButton.setActionCommand("complete");
        completeButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if ("add".equals(e.getActionCommand())) {
            insertTask();
        } else if ("update".equals(e.getActionCommand())) {
            updateTask();
        } else if ("complete".equals(e.getActionCommand())) {
            completeTask();
        } else if ("clear".equals(e.getActionCommand())) {
            refreshTable();
        } else if ("search".equals(e.getActionCommand())) {
            search();
        }
    }

    public void insertUpdate(DocumentEvent e) {
        if (searchWhileIType.isSelected()) {
            search();
        }
    }

    public void removeUpdate(DocumentEvent e) {
        if (searchWhileIType.isSelected()) {
            search();
        }
    }

    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }

    public void itemStateChanged(ItemEvent e) {
    }

    private void search() {
        this.currentSearchString = searchField.getText();
        this.refreshTable(Calendar.GET_ALL_TASKS);
    }

    private void insertTask() {
        if (taskField.getText().length() > 300) {
            JOptionPane.showMessageDialog(null, "The task description must be less than 300 characters.", "Input To Long", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //get the Task and pass it to the TeaDatabase connection
        Date selected = this.calendar.getDate();
        String day = dateFormat.format(selected);
        TeaDatabase.insertCalendarTask(taskField.getText(), companyNameField.getText(), day);
        //update the gui
        refreshTable();
    }

    private void updateTask() {
        if (taskField.getText().length() > 300) {
            JOptionPane.showMessageDialog(null, "The task description must be less than 300 characters.", "Input To Long", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //get the current task and update it in the database
        //refresh the table       
        if (currentTask != null) {
            TeaDatabase.updateCalendarTask(currentTask.getTask_id(), taskField.getText(), companyNameField.getText(), currentTask.getStart_date(), currentTask.getCompleted_date());
            refreshTable();
        }
    }

    private void completeTask() {
        if (taskField.getText().length() > 300) {
            JOptionPane.showMessageDialog(null, "The task description must be less than 300 characters.", "Input To Long", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentTask != null) {
            Date date = new Date();
            String day = this.dateFormat.format(date);
            //set the currentTasks completed date
            this.currentTask.setCompleted_date(day);
            updateTask();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) {
            // Column selection changed
            int first = e.getFirstIndex();
            int last = e.getLastIndex();
            //get the current row and set it to be the currentTask           
            if (table.getSelectedRow() > -1) {
                //get all the elements in that row
                Vector row = (Vector) model.getDataVector().elementAt(table.getSelectedRow());
                this.currentTask = getTask((Integer) row.get(4));
                taskField.setText((String) row.get(0));
                companyNameField.setText((String) row.get(1));
                startDateField.setText((String) row.get(2));
                completedDateField.setText((String) row.get(3));
                if (!completedDateField.getText().equals("") && completedDateField.getText() != null) {
                    taskField.setEditable(false);
                    companyNameField.setEditable(false);
                    this.updateButton.setEnabled(false);
                } else {
                    taskField.setEditable(true);
                    companyNameField.setEditable(true);
                    this.updateButton.setEnabled(true);
                }

            }
        }
    }

    public void tableChanged(TableModelEvent e) {
        //if it was just an update then update the row in the database
        if (done) {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel) e.getSource();
                String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);
                //now just update the field in the database
            }
        }
    }

    public void addComponentsToCanvas() {
        calendarPanel.add(searchButton);
        calendarPanel.add(searchField);
        calendarPanel.add(searchWhileIType);
        calendarPanel.add(calendar);
        dataPanel.add(tablePane, BorderLayout.CENTER);

        calendarPanel.add(taskField);
        calendarPanel.add(companyNameField);
        calendarPanel.add(startDateField);
        calendarPanel.add(completedDateField);

        calendarPanel.add(taskLabel);
        calendarPanel.add(companyNameLabel);
        calendarPanel.add(startDateLabel);
        calendarPanel.add(completedDatelabel);

        calendarPanel.add(updateButton);
        calendarPanel.add(insertButton);
        calendarPanel.add(completeButton);
        calendarPanel.add(clearButton);

        this.add(calendarPanel, BorderLayout.NORTH);
        this.add(dataPanel, BorderLayout.CENTER);
    }

    public void initLayout() {
        //pos the calendar
        layout.putConstraint(SpringLayout.NORTH, calendar, 10, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, calendar, 10, SpringLayout.WEST, this);

//        //put the search field to the right of the calendar
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, searchField, 0, SpringLayout.VERTICAL_CENTER, calendar);
        layout.putConstraint(SpringLayout.WEST, searchField, 30, SpringLayout.EAST, calendar);

        //pos the search while i type check box
        layout.putConstraint(SpringLayout.NORTH, searchWhileIType, 5, SpringLayout.SOUTH, searchField);
        layout.putConstraint(SpringLayout.WEST, searchWhileIType, 0, SpringLayout.WEST, searchField);
//
//        //pos the search button
        layout.putConstraint(SpringLayout.WEST, searchButton, 5, SpringLayout.EAST, searchField);
        layout.putConstraint(SpringLayout.NORTH, searchButton, 0, SpringLayout.NORTH, searchField);
        layout.putConstraint(SpringLayout.SOUTH, searchButton, 0, SpringLayout.SOUTH, searchField);

        //pos the edit text fields labels
        layout.putConstraint(SpringLayout.WEST, taskLabel, 40, SpringLayout.EAST, searchButton);
        layout.putConstraint(SpringLayout.NORTH, taskLabel, 0, SpringLayout.NORTH, calendar);

        layout.putConstraint(SpringLayout.WEST, companyNameLabel, 0, SpringLayout.WEST, taskLabel);
        layout.putConstraint(SpringLayout.NORTH, companyNameLabel, 15, SpringLayout.SOUTH, taskLabel);

        layout.putConstraint(SpringLayout.WEST, startDateLabel, 0, SpringLayout.WEST, taskLabel);
        layout.putConstraint(SpringLayout.NORTH, startDateLabel, 15, SpringLayout.SOUTH, companyNameLabel);

        layout.putConstraint(SpringLayout.WEST, completedDatelabel, 0, SpringLayout.WEST, taskLabel);
        layout.putConstraint(SpringLayout.NORTH, completedDatelabel, 15, SpringLayout.SOUTH, startDateLabel);

        //pos the edit text fields
        layout.putConstraint(SpringLayout.WEST, taskField, 10, SpringLayout.EAST, taskLabel);
        layout.putConstraint(SpringLayout.SOUTH, taskField, 0, SpringLayout.SOUTH, taskLabel);
        layout.putConstraint(SpringLayout.EAST, taskField, -10, SpringLayout.EAST, calendarPanel);

        layout.putConstraint(SpringLayout.WEST, companyNameField, 10, SpringLayout.EAST, companyNameLabel);
        layout.putConstraint(SpringLayout.SOUTH, companyNameField, 0, SpringLayout.SOUTH, companyNameLabel);
        layout.putConstraint(SpringLayout.EAST, companyNameField, -10, SpringLayout.EAST, calendarPanel);

        layout.putConstraint(SpringLayout.WEST, startDateField, 10, SpringLayout.EAST, startDateLabel);
        layout.putConstraint(SpringLayout.SOUTH, startDateField, 0, SpringLayout.SOUTH, startDateLabel);
        layout.putConstraint(SpringLayout.EAST, startDateField, -10, SpringLayout.EAST, calendarPanel);

        layout.putConstraint(SpringLayout.WEST, completedDateField, 10, SpringLayout.EAST, completedDatelabel);
        layout.putConstraint(SpringLayout.SOUTH, completedDateField, 0, SpringLayout.SOUTH, completedDatelabel);
        layout.putConstraint(SpringLayout.EAST, completedDateField, -10, SpringLayout.EAST, calendarPanel);

        //set all the west edges of the test fields to completed date
        layout.putConstraint(SpringLayout.WEST, taskField, 0, SpringLayout.WEST, completedDateField);
        layout.putConstraint(SpringLayout.WEST, companyNameField, 0, SpringLayout.WEST, completedDateField);
        layout.putConstraint(SpringLayout.WEST, startDateField, 0, SpringLayout.WEST, completedDateField);

        //pos the update and insert buttons
        layout.putConstraint(SpringLayout.NORTH, updateButton, 10, SpringLayout.SOUTH, completedDateField);
        layout.putConstraint(SpringLayout.EAST, updateButton, 0, SpringLayout.EAST, completedDateField);

        layout.putConstraint(SpringLayout.SOUTH, insertButton, 0, SpringLayout.SOUTH, updateButton);
        layout.putConstraint(SpringLayout.NORTH, insertButton, 0, SpringLayout.NORTH, updateButton);
        layout.putConstraint(SpringLayout.EAST, insertButton, -15, SpringLayout.WEST, updateButton);

        layout.putConstraint(SpringLayout.SOUTH, clearButton, 0, SpringLayout.SOUTH, insertButton);
        layout.putConstraint(SpringLayout.NORTH, clearButton, 0, SpringLayout.NORTH, insertButton);
        layout.putConstraint(SpringLayout.EAST, clearButton, -15, SpringLayout.WEST, insertButton);

        layout.putConstraint(SpringLayout.SOUTH, completeButton, 0, SpringLayout.SOUTH, insertButton);
        layout.putConstraint(SpringLayout.NORTH, completeButton, 0, SpringLayout.NORTH, insertButton);
        layout.putConstraint(SpringLayout.WEST, completeButton, 0, SpringLayout.WEST, completedDateField);
        layout.putConstraint(SpringLayout.EAST, completeButton, -15, SpringLayout.WEST, clearButton);
    }

    private CalendarTask getTask(int taskID) {
        for (CalendarTask t : tasks) {
            if (t.getTask_id() == taskID) {
                return t;
            }
        }
        return null;
    }

    private void refreshTable() {
        refreshTable(Calendar.GET_TASKS_FOR_DAY);
    }

    private void refreshTable(int taskType) {
        taskField.setEditable(true);
        companyNameField.setEditable(true);
        //clear the edit fields
        taskField.setText("");
        companyNameField.setText("");
        startDateField.setText("");
        completedDateField.setText("");
        //clear the current task
        this.currentTask = null;
        this.updateButton.setEnabled(false);
        model.setRowCount(0);
        if (taskType == Calendar.GET_TASKS_FOR_DAY) {
            Date selected = this.calendar.getDate();
            String day = dateFormat.format(selected);
            //get the results from the database
            tasks = TeaDatabase.getToDoList(day);
            for (CalendarTask t : tasks) {
                //for every task create a new row for the table
                model.addRow(t.getRow());
            }
        } else if (taskType == Calendar.GET_ALL_TASKS) {
            //get all the tasks
            tasks = TeaDatabase.getAllTasksFromCalendar();
            for (CalendarTask t : tasks) {
                if (taskContains(this.currentSearchString, t)) {
                    //for every task create a new row for the table
                    model.addRow(t.getRow());
                }
            }
        }
    }

    private boolean taskContains(String s, CalendarTask t) {
        //check each thing in the task and see if it contains it
        if (t.getTask().toUpperCase().contains(s.toUpperCase())) {
            return true;
        } else if (t.getCompany_name().toUpperCase().contains(s.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    private void calendarDateChange(java.beans.PropertyChangeEvent evt) {
        refreshTable();
    }
}

class CalendarTask {

    private int task_id;
    private String task;
    private String company_name;
    private String start_date;
    private String completed_date;

    public CalendarTask() {
    }

    public CalendarTask(int task_id, String task, String company_name, String start_date, String completed_date) {
        this.task_id = task_id;
        this.task = task;
        this.company_name = company_name;
        this.start_date = start_date;
        this.completed_date = completed_date;
    }

    public Vector getRow() {
        Vector v = new Vector();
        v.add(this.task);
        v.add(this.company_name);
        v.add(this.start_date);
        v.add(this.completed_date);
        v.add(task_id);
        return v;
    }

    /**
     * @return the task_id
     */
    public int getTask_id() {
        return task_id;
    }

    /**
     * @param task_id the task_id to set
     */
    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    /**
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @return the company_name
     */
    public String getCompany_name() {
        return company_name;
    }

    /**
     * @param company_name the company_name to set
     */
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    /**
     * @return the start_date
     */
    public String getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * @return the completed_date
     */
    public String getCompleted_date() {
        return completed_date;
    }

    /**
     * @param completed_date the completed_date to set
     */
    public void setCompleted_date(String completed_date) {
        this.completed_date = completed_date;
    }
}

class NonEditableTableModel extends DefaultTableModel {

    public NonEditableTableModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
