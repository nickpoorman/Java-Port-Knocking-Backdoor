package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;

/**
 *
 * @author Kendall Moore
 */
@SuppressWarnings("serial")
public class ToDoPanel extends JFrame {

    ToDoPanel() {
        super();
        createComponents();
    }

    private void createComponents() {
        setSize(1024, 720);
        setLayout(new BorderLayout());
        JScrollPane scroller = new JScrollPane(new ToDoList());
        scroller.setSize(new Dimension(100, 100));
        this.add(scroller, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ToDoPanel panel = new ToDoPanel();
                panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                panel.setVisible(true);
            }
        });
    }

    static final class ToDoList extends JPanel implements ActionListener {
    	
        private static final String DATE_TODAY;
        static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        static {
            final Date date = new Date();
            DATE_TODAY = dateFormat.format(date);
        }
        static volatile String dateToInsert = DATE_TODAY;
        
        private volatile JPanel bottom = new JPanel();
        
        private List<List<String>> results = TeaDatabase.getTasks(DATE_TODAY);

        private final SpringLayout layout;
        private JCalendar calendar;

        private List<JPanel> toDoTasks = new ArrayList<JPanel>();
        private List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
        private List<String> tasks;
        //private List<String> dates = results.get(1);
        private List<String> completed;
        
        private JButton addTaskButton = new JButton("Add a task");
        private JButton saveButton = new JButton("Save List");
        
        ToDoList() {
            layout = new SpringLayout();
            initTasks();
            createComponents();
            addComponents();
        }
        
        private void initTasks() {
        	tasks = results.get(0);
        	completed = results.get(2);
        }

        private void createComponents() {
            calendar = new JCalendar();
        }

        private void addComponents() {
            add(calendar);
            add(addTaskButton);
            add(saveButton);
            calendar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            	public void propertyChange(java.beans.PropertyChangeEvent evt) {
            		calendarDateChange(evt);
            	}
            });
            addTaskButton.addActionListener(this);
            saveButton.addActionListener(this);
        }
        
        private void calendarDateChange(java.beans.PropertyChangeEvent evt) {
        	Date selected = this.calendar.getDate();
        	String day = dateFormat.format(selected);
        	dateToInsert = day;
        	results = TeaDatabase.getTasks(day);
        	if (tasks != null) {
        		tasks.clear();
            	completed.clear();
        	}
        	initTasks();
        	renderLayout();
        	updateUI();
        }

        private void renderLayout() {
//        	removeAll();
//        	addComponents();
            setLayout(layout);
            // Align the calendar
            layout.putConstraint(SpringLayout.NORTH, calendar, 10, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, calendar, 0, SpringLayout.HORIZONTAL_CENTER, this);
            // Put in an add button
            layout.putConstraint(SpringLayout.WEST, addTaskButton, 0, SpringLayout.WEST, calendar);
            layout.putConstraint(SpringLayout.NORTH, addTaskButton, 10, SpringLayout.SOUTH, calendar);
            // Put in save button
            layout.putConstraint(SpringLayout.NORTH, saveButton, 0, SpringLayout.NORTH, addTaskButton);
            layout.putConstraint(SpringLayout.WEST, saveButton, 10, SpringLayout.EAST, addTaskButton);
            // Add the tasks from the ToDo list
            for (int i = 0; i < toDoTasks.size(); ++i)
            	toDoTasks.get(i).removeAll();
            for (int i = 0; i < tasks.size(); ++i) {
            	JPanel tmp = new JPanel();
                toDoTasks.add(tmp);
                JCheckBox box = new JCheckBox();
                box.setActionCommand(Integer.toString(i));
                box.addActionListener(this);
                checkBoxes.add(box);
                toDoTasks.get(i).add(BorderLayout.WEST, checkBoxes.get(i));
                toDoTasks.get(i).add(BorderLayout.CENTER, new JLabel("   "));
                JLabel task = new JLabel(tasks.get(i));
                toDoTasks.get(i).add(BorderLayout.EAST, task);
                JTextField date = null;
                if (completed.get(i).equals("0"))
                	date = new JTextField("Not Yet Completed");
                else
                	date = new JTextField("Completed");
                date.setBorder(null);
                JLabel datePanel = new JLabel("   ");
                toDoTasks.get(i).add(BorderLayout.EAST, datePanel);
//                datePanel.setLayout(new SpringLayout());
//                datePanel.add(BorderLayout.WEST, date);
//                datePanel.setBackground(Color.RED);
                toDoTasks.get(i).add(BorderLayout.EAST, date);
//                toDoTasks.get(i).setLayout(layout);
                add(toDoTasks.get(i));
                layout.putConstraint(SpringLayout.WEST, toDoTasks.get(i), 0, SpringLayout.WEST, calendar);
                //layout.putConstraint(SpringLayout.WEST, date, 50, SpringLayout.EAST, task);
                if (i == 0)
                    layout.putConstraint(SpringLayout.NORTH, toDoTasks.get(i), 20, SpringLayout.SOUTH, addTaskButton);
                else 
                    layout.putConstraint(SpringLayout.NORTH, toDoTasks.get(i), 5, SpringLayout.SOUTH, toDoTasks.get(i-1));
                bottom = tmp;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String source = e.getActionCommand();
            if (e.getActionCommand().equals("Add a task")) {
            	doAdd();
            	updateUI();
            }
            else if (e.getActionCommand().equals("Save List")) {
            	
            }
            else {
            	updateToDoList(source);
            }
        }
        
		private void doAdd() {
			createTaskChooser();
			JPanel newTask = new JPanel();
			JCheckBox box = new JCheckBox();
			checkBoxes.add(box);
			newTask.add(BorderLayout.WEST, box);
			newTask.add(BorderLayout.CENTER, new JLabel("   "));
			JLabel taskLabel = new JLabel();
			taskLabel.setBorder(null);
			tasks.add(taskLabel.getText());
			newTask.add(BorderLayout.EAST, taskLabel);
			newTask.add(BorderLayout.EAST, new JLabel("   "));
			JTextField date = new JTextField("Not Yet Completed");
			date.setBorder(null);
			newTask.add(BorderLayout.EAST, date);
			toDoTasks.add(newTask);
			box.setActionCommand(Integer.toString(toDoTasks.size()-1));
			box.addActionListener(this);
			add(newTask);
			if ((results.get(0).size()-1) > 1) {
				System.out.println("Here");
				layout.putConstraint(SpringLayout.NORTH, newTask, 5, SpringLayout.SOUTH, bottom);
				layout.putConstraint(SpringLayout.WEST, newTask, 0, SpringLayout.WEST, calendar);
			}
			else if (results.get(0).size()-1 == 1) {
				layout.putConstraint(SpringLayout.WEST, newTask, 0, SpringLayout.WEST, calendar);
				layout.putConstraint(SpringLayout.NORTH, newTask, 45, SpringLayout.SOUTH, addTaskButton);
			}
			else {
				layout.putConstraint(SpringLayout.WEST, newTask, 0, SpringLayout.WEST, calendar);
				layout.putConstraint(SpringLayout.NORTH, newTask, 20, SpringLayout.SOUTH, addTaskButton);
			}
			bottom = newTask;
		}
        
        private void createTaskChooser() {
        	TaskChooser chooser = new TaskChooser(this);
        	chooser.doPopup();
        }
        
        private void updateToDoList(String source) {
			try {
                int i = Integer.parseInt(source);
                Component[] components = toDoTasks.get(i).getComponents();
                for (int j = 0; j < components.length; ++j) {
                    if (components[j] instanceof JTextField) {
                        JTextField f = (JTextField)components[j];
                        if (f.getText().equals("Not Yet Completed")) {
                        	f.setText("Completed");
                        	TeaDatabase.updateToDoList(tasks.get(i), true);
                        }
                        else if (f.getText().equals("Completed")) {
                        	f.setText("Not Yet Completed");
                        	TeaDatabase.updateToDoList(tasks.get(i), false);
                        } 
                    }
                }
            }
            catch (NumberFormatException ex) { //Ignore this and just check the next case
                Logger.getLogger(ToDoList.class.getName()).log(Level.WARNING, null, ex);
            }
		}

    }
    
    static final class TaskChooser extends JFrame implements ActionListener, WindowListener {
    	private SpringLayout layout;
    	private JButton submitButton;
    	private JTextField taskField;
    	private ToDoList list;
    	
    	TaskChooser(ToDoList list) {
    		addWindowListener(this);
    		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    		this.list = list;
    		setSize(200,200);
    	}
    	
    	void doPopup() {
    		setSize(200,200);
    		setVisible(true);
    		layout = new SpringLayout();
    		submitButton = new JButton("Submit New Task");
    		submitButton.addActionListener(this);
    		taskField = new JTextField("Enter new task name here.");
    		taskField.setBorder(null);
    		taskField.setEditable(true);
    		add(taskField);
    		add(submitButton);
    		setLayout(layout);
    		render();
    	}
    	
    	private void render() {
    		layout.putConstraint(SpringLayout.WEST, taskField, 18, SpringLayout.WEST, this);
    		layout.putConstraint(SpringLayout.NORTH, taskField, 10, SpringLayout.NORTH, this);
    		layout.putConstraint(SpringLayout.NORTH, submitButton, 20, SpringLayout.SOUTH, taskField);
    		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submitButton, 0, SpringLayout.HORIZONTAL_CENTER, taskField);
    	}

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if (e.getActionCommand().equals("Submit New Task")) {
    			int i = (list.toDoTasks.size()-1);
    			if (taskField.getText().equals("Enter new task name here.")) {
    				updateParent(i);
    				return;
    			}
    			String update = taskField.getText();
    			JLabel label = (JLabel)list.toDoTasks.get(i).getComponent(2);
    			list.bottom = list.toDoTasks.get(i);
    			label.setText(update);
    			list.updateUI();
    			if (!label.getText().equals("Enter new task name here."))
    				TeaDatabase.insertIntoToDoList(label.getText(), ToDoList.dateToInsert);
//    			ToDoList.toDoTasks.get(i).getComponent(2);
    			dispose();
    		}
    	}

		private void updateParent(int i) {
			removeAll();
			int taskSize = list.results.get(0).size();
			list.results.get(0).remove(taskSize - 1);
			list.toDoTasks.get(i).removeAll();
			list.toDoTasks.remove(i);
			list.updateUI();
			dispose();
		}
		
		@Override public void windowClosed(WindowEvent e) {
			list.updateUI();
		}
		@Override public void windowActivated(WindowEvent e) {}
		@Override public void windowClosing(WindowEvent e) {
			int i = (list.toDoTasks.size()-1);
			updateParent(i);
		}
		@Override public void windowDeactivated(WindowEvent e) {}
		@Override public void windowDeiconified(WindowEvent e) {}
		@Override public void windowIconified(WindowEvent e) {}
		@Override public void windowOpened(WindowEvent e) {}
    	
    }
}

