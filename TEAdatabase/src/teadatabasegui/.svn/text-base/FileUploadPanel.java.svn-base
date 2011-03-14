package teadatabasegui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author Kendall Moore
 */
@SuppressWarnings("serial")
public class FileUploadPanel extends JFrame implements ActionListener {
	
	private static CompanyID id;

    FileUploadPanel(CompanyID id) {
        super();
        FileUploadPanel.id = id;
        createComponents();
    }

    private void createComponents() {
        setSize(1024, 720);
        this.setLayout(new BorderLayout());
        JScrollPane scroller = new JScrollPane(new UploadPanel());
        scroller.setSize(new Dimension(100, 100));
        this.add(scroller, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FileUploadPanel panel = new FileUploadPanel(new CompanyID(1));
                panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                panel.setVisible(true);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {

    }

    static final class UploadPanel extends JPanel implements ActionListener {

        private SpringLayout layout;
        private JButton uploadButton;
        private JLabel fileName;
        private JLabel label;
        private JButton save;
        final JFileChooser fileChooser = new JFileChooser();
        private URI uri;

        private List<JLabel> uploadedFiles = new ArrayList<JLabel>();
        private List<JTextField> labels = new ArrayList<JTextField>();
        private List<JLabel> originalFiles = new ArrayList<JLabel>();
        private List<JTextField> originalLabels = new ArrayList<JTextField>();
        private List<List<String>> labelsAndFiles;
        
        private boolean filesRemoved;

        private volatile JLabel bottomFile;

		UploadPanel() {
        	labelsAndFiles = TeaDatabase.getCompanyFilesWithLabels(FileUploadPanel.id.getCompanyID());
            layout = new SpringLayout();
            createAndInitComponents();
            addComponents();
            setLayout();
        }

        private void createAndInitComponents() {
            uploadButton = new JButton("Upload");
            uploadButton.setActionCommand("upload");
            uploadButton.addActionListener(this);
            fileName = new JLabel("File Name");
            fileName.setFont(fileName.getFont().deriveFont((float) 14));
            label = new JLabel("Label");
            label.setFont(label.getFont().deriveFont((float) 14));
            save = new JButton("Save Files");
            save.setActionCommand("save");
            save.addActionListener(this);
            List<String> label = null;
            List<String> file = null;
            if (labelsAndFiles.size() > 0 && labelsAndFiles.get(0).size() != 0) {
            	label = labelsAndFiles.get(0);
            	file = labelsAndFiles.get(1);
            
            	// Add the files to the screen
            	for (String s : file)
            		setupFileLabels(s);
            	// Add the labels to the screen
            	for (String s : label)
            		setupLabelFields(s);
            }
            else {
            	uploadedFiles.add(new JLabel(""));
            	//labels.add(new JTextField("No Labels To Display"));
            }
            keepOriginalCopies();
        }

		private void setupLabelFields(String s) {
			JTextField tmp = new JTextField(s);
			tmp.setEditable(true);
			tmp.setBorder(null);
			labels.add(tmp);
		}

		private void setupFileLabels(String s) {
			final JLabel tmp = new JLabel(s);
			addListenerToLabel(tmp);
			uploadedFiles.add(tmp);
		}
		
		private void addListenerToLabel(final JLabel tmp) {
			tmp.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					open(tmp.getText());
				}
			});
		}
		
		private void open(String append) {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					uri = new URI("http://moxie.oswego.edu/~csa/ftp_test/"+append);
					desktop.browse(uri);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				catch (URISyntaxException e) {
					popupErrorWindow();
					e.printStackTrace();
				}
			}
		}
		
		private void popupErrorWindow() {
			new ErrorWindow();
		}


		private void keepOriginalCopies() {
			// Keep a copy of the original lists
            for (JLabel lab : uploadedFiles)
            	originalFiles.add(lab);
            for (JTextField jt : labels)
            	originalLabels.add(jt);
		}
		
        private void addComponents() {
            this.add(uploadButton);
            this.add(fileName);
            this.add(label);
            this.add(save);
        }

        private void setLayout() {
        	removeAll();
        	
        	addComponents();
        	bottomFile = uploadedFiles.get(uploadedFiles.size() - 1);
            this.setLayout(layout);
            //Upload button
            layout.putConstraint(SpringLayout.NORTH, uploadButton, 10, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, uploadButton, 10, SpringLayout.WEST, this);
            //File Name
            layout.putConstraint(SpringLayout.NORTH, fileName, 40, SpringLayout.SOUTH, uploadButton);
            layout.putConstraint(SpringLayout.WEST, fileName, 65, SpringLayout.WEST, this);
            //Label
            layout.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.SOUTH, uploadButton);
            layout.putConstraint(SpringLayout.WEST, label, 100, SpringLayout.EAST, fileName);
            //The previously uploaded files
            for (int i = 0; i < uploadedFiles.size(); ++i) {
                this.add(uploadedFiles.get(i));
                JLabel removeLabel = new JLabel("Remove");
                if (!bottomFile.getText().equals(""))
                	add(removeLabel);
                addRemoveListener(removeLabel, uploadedFiles.get(i), labels.get(i));
                if (i == 0) {
                	layout.putConstraint(SpringLayout.NORTH, removeLabel, 10, SpringLayout.SOUTH, fileName);
                	layout.putConstraint(SpringLayout.WEST, removeLabel, 10, SpringLayout.WEST, this);
                    layout.putConstraint(SpringLayout.NORTH, uploadedFiles.get(i), 10, SpringLayout.SOUTH, fileName);
                    layout.putConstraint(SpringLayout.WEST, uploadedFiles.get(i), 10, SpringLayout.EAST, removeLabel);
                }
                else {
                	layout.putConstraint(SpringLayout.NORTH, removeLabel, 10, SpringLayout.SOUTH, uploadedFiles.get(i-1));
                	layout.putConstraint(SpringLayout.WEST, removeLabel, 10, SpringLayout.WEST, this);
                    layout.putConstraint(SpringLayout.NORTH, uploadedFiles.get(i), 10, SpringLayout.SOUTH, uploadedFiles.get(i-1));
                    layout.putConstraint(SpringLayout.WEST, uploadedFiles.get(i), 10, SpringLayout.EAST, removeLabel);
                }
            }
            //The labels for the uploaded files
            for (int i = 0; i < labels.size(); ++i) {
                this.add(labels.get(i));
                if (i == 0) {
                    layout.putConstraint(SpringLayout.SOUTH, labels.get(i), 0, SpringLayout.SOUTH, uploadedFiles.get(i));
                    layout.putConstraint(SpringLayout.WEST, labels.get(i), 0, SpringLayout.WEST, label);
                }
                else {
                    layout.putConstraint(SpringLayout.NORTH, labels.get(i), 10, SpringLayout.SOUTH, labels.get(i-1));
                    layout.putConstraint(SpringLayout.SOUTH, labels.get(i), 0, SpringLayout.SOUTH, uploadedFiles.get(i));
                    layout.putConstraint(SpringLayout.WEST, labels.get(i), 0, SpringLayout.WEST, label);
                }
            }
            positionSaveButton();
        }
        
        private void addRemoveListener(final JLabel removeLabel, final JLabel fileLabel, final JTextField labelField) {
        	removeLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					uploadedFiles.remove(fileLabel);
					labels.remove(labelField);
					setLayout();
					TeaDatabase.removeFileFromDatabase(fileLabel.getText());
					filesRemoved = true;
					updateUI();
				}
			});
        }

        private void positionSaveButton() {
            //The save button
            layout.putConstraint(SpringLayout.WEST, save, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, save, 40, SpringLayout.SOUTH, bottomFile);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("upload")) {
                popupFileChooser();
            }
            if(e.getActionCommand().equals("save")){
                save();
                originalLabels = labels;
            }
        }

        private void popupFileChooser() {
            fileChooser.addActionListener(this);
            int val = fileChooser.showOpenDialog(this);
            File file = null;
            if (val == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            }
            if (file != null) {
                appendFileToUploadedFiles(file);
                saveFileToFTPServer(file);
            }
        }

        private void appendFileToUploadedFiles(File f) {
            //Init the new file name field
            JLabel newFileName = new JLabel(f.getName());
            addListenerToLabel(newFileName);
            this.add(newFileName);
            // Init the new file label field
            JTextField newFileLabel = new JTextField("Enter a label");
            newFileLabel.setBorder(null);
            this.add(newFileLabel);
            JLabel removeLabel = new JLabel("Remove");
            add(removeLabel);
            addRemoveListener(removeLabel, newFileName, newFileLabel);
            if (bottomFile.getText().equals("")) {
            	layout.putConstraint(SpringLayout.NORTH, removeLabel, 10, SpringLayout.SOUTH, fileName);
            	layout.putConstraint(SpringLayout.WEST, removeLabel, 10, SpringLayout.WEST, this);
            	// Arrange the file name
            	layout.putConstraint(SpringLayout.NORTH, newFileName, 0, SpringLayout.NORTH, removeLabel);
                layout.putConstraint(SpringLayout.WEST, newFileName, 10, SpringLayout.EAST, removeLabel);
                // Arrange the label
                layout.putConstraint(SpringLayout.SOUTH, newFileLabel, 0, SpringLayout.SOUTH, newFileName);
            	layout.putConstraint(SpringLayout.WEST, newFileLabel, 0, SpringLayout.WEST, label);
            	labels.add(newFileLabel);
            	// Change the bottom file
                bottomFile = newFileName;
                uploadedFiles.add(newFileName);
            }
            else {
            	layout.putConstraint(SpringLayout.NORTH, removeLabel, 10, SpringLayout.SOUTH, bottomFile);
            	layout.putConstraint(SpringLayout.WEST, removeLabel, 10, SpringLayout.WEST, this);
            	// Arrange the file name
            	layout.putConstraint(SpringLayout.NORTH, newFileName, 0, SpringLayout.NORTH, removeLabel);
            	layout.putConstraint(SpringLayout.WEST, newFileName, 10, SpringLayout.EAST, removeLabel);
            	// Arrange the label
            	layout.putConstraint(SpringLayout.SOUTH, newFileLabel, 0, SpringLayout.SOUTH, newFileName);
            	layout.putConstraint(SpringLayout.WEST, newFileLabel, 0, SpringLayout.WEST, labels.get(labels.size() - 1));
            	labels.add(newFileLabel);
            	// Change the bottom file
            	bottomFile = newFileName;
            	uploadedFiles.add(newFileName);
            }
            updateUI();
            positionSaveButton();
            
        }

        private void saveFileToFTPServer(File f) {
            TeaFTP ftp = new TeaFTP("csarules1");
            System.out.println("Connected: " + ftp.connect());
            System.out.println("File Transfered: " + ftp.transferFile(f));
            System.out.println("Disconnected: " + ftp.disconnect());
        }

        private void save() {
            // Insert or update the database with files and labels
            if (labelsChanged()) {
            	System.out.println("Label changed");
            	insertOrUpdateLabelsInDatabase();
//            	TeaDatabase.insertLabelsIntoDatabase(FileUploadPanel.id.getCompanyID(), getNewLabels());
            }
            if (newFilesAdded()) {
            	List<String> updated = getNewFiles();
            	for (int i = 0; i < updated.size(); ++i)
            		TeaDatabase.insertFilesIntoDatabase(labels.get(labels.size()-1 + i).getText(), updated.get(i));
            }	
        }
        
        private boolean newFilesAdded() {
        	for (JLabel l : uploadedFiles)
        		if (!originalFiles.contains(l))
        			return true;
        	return false;
        }
        
        private List<String> getNewFiles() {
        	List<String> filesToAdd = new ArrayList<String>();
        	for (JLabel l : uploadedFiles)
        		if (!originalFiles.contains(l))
        			filesToAdd.add(l.getText());
        	return filesToAdd;
        }
        
        private boolean labelsChanged() {
        	for (int i = 0; i < labels.size(); ++i) {
        		if (!originalLabels.get(i).getText().equals(labels.get(i).getText()))
        			return true;
        	}
        	return false;
        }
        
        private void insertOrUpdateLabelsInDatabase() {
        	if (originalLabels.size() == labels.size()) {
        		if (!filesRemoved) {
        			for (int i = 0; i < labels.size(); ++i) {
            			if (!labels.get(i).getText().equals(originalLabels.get(i).getText()))
            				TeaDatabase.updateLabelsIntoDatabase(originalLabels.get(i).getText(), labels.get(i).getText());
            		}
        		}
        	}
        	else {
        		System.out.println("Inserting labels into DB");
        		for (int i = 0; i < getNewFiles().size(); ++i)
        			TeaDatabase.insertLabelsIntoDatabase(FileUploadPanel.id.getCompanyID(), labels.get(labels.size()-1 + i).getText());
        	}
        }

    }
    
    static final class ErrorWindow extends JFrame implements ActionListener {
    	
    	private JButton close = new JButton("Close");
    	private JLabel errorLabel = new JLabel("Error Occured");
    	private JLabel description = new JLabel("Something unexpected occured when trying to open the contents of the file. ");
    	private JLabel description2 = new JLabel("Please contact your system administrator to get more information on the error.");
    	private SpringLayout layout = new SpringLayout();
    	
    	ErrorWindow() {
    		setSize(500,500);
    		setVisible(true);
    		addListeners();
    		addComponents();
    		render();
    	}
    	
    	private void addListeners() {
    		close.addActionListener(this);
    	}
    	
    	private void addComponents() {
    		add(close);
    		errorLabel.setFont(errorLabel.getFont().deriveFont((float) 20));
    		add(errorLabel);
    		add(description);
    		add(description2);
    	}
    	
    	private void render() {
    		setLayout(layout);
    		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, errorLabel, 220, SpringLayout.HORIZONTAL_CENTER, this);
    		layout.putConstraint(SpringLayout.VERTICAL_CENTER, errorLabel, 0, SpringLayout.VERTICAL_CENTER, this);
    		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, close, 220, SpringLayout.HORIZONTAL_CENTER, this);
    		layout.putConstraint(SpringLayout.NORTH, close, 100, SpringLayout.SOUTH, errorLabel);
    		layout.putConstraint(SpringLayout.NORTH, description, 20, SpringLayout.SOUTH, errorLabel);
    		layout.putConstraint(SpringLayout.NORTH, description2, 5, SpringLayout.SOUTH, description);
    	}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Close"))
				dispose();
		}
    	
    }

}
