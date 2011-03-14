/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teadatabasegui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author nick
 */
public class FileUpload extends JPanel implements ActionListener {

    private CompanyID companyID;
    private static String FTP_USERNAME = MainWindow.getConf().getFTP_USER_NAME();
    private static String FTP_PASSWORD = MainWindow.getConf().getFTP_USER_PASSWORD();
    private static String FTP_SERVER = MainWindow.getConf().getFTP_SERVER();
    private static String FTP_FOLDER = MainWindow.getConf().getFTP_UPLOAD_FOLDER();
    JButton upload;
    JButton save;
    SpringLayout layout;
    List<JLabel> files;
    List<JTextField> labels;
    List<List<String>> data;
    List<JButton> removeButtons;
    JLabel filesLabel;
    JLabel labelsLabel;
    JFileChooser fileChooser;
    JLabel loading;
    private URI uri;

    public FileUpload(CompanyID companyID) {
        update(companyID);
    }

    private void update(CompanyID companyID) {
        this.removeAll();
        this.companyID = companyID;
        layout = new SpringLayout();
        this.setLayout(layout);
        data = TeaDatabase.getCompanyFilesWithLabels(this.companyID.getCompanyID());

        createComponents();
        addComponentsToCanvas();
        fillValues();
        initLayout();

        if (companyID.isNewCompany()) {
            upload.setEnabled(false);
            save.setEnabled(false);
        }
        this.updateUI();
    }

    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            save();
        } else if ("upload".equals(e.getActionCommand())) {
            loading.setVisible(true);
            updateUI();
            popupFileChooser();
            loading.setVisible(false);
            updateUI();

        } else if (e.getActionCommand().startsWith("REMOVE")) {
            String command = e.getActionCommand();
            command = command.replace("REMOVE", "");
            command = command.trim();
            int ptr = Integer.parseInt(command);
            //get the file name
            String fileName = files.get(ptr).getText();
            removeFile(fileName);
        } else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
        }
    }

    public int getLongestFileNameWidth() {
        int largeLen = 0;
        for (int i = 0; i < files.size(); i++) {
            if (largeLen < files.get(i).getPreferredSize().getWidth()) {
                largeLen = (int) files.get(i).getPreferredSize().getWidth();
            }
        }
        return largeLen;
    }

    public int getLongestLabel() {
        int largeLen = 0;
        int pointer = 0;
        for (int i = 0; i < labels.size(); i++) {
            if (largeLen < labels.get(i).getPreferredSize().getWidth()) {
                largeLen = (int) labels.get(i).getPreferredSize().getWidth();
                pointer = i;
            }
        }
        return pointer;
    }

    public void createComponents() {
        upload = new JButton("Upload");
        upload.addActionListener(this);
        upload.setActionCommand("upload");
        save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        filesLabel = new JLabel("Files");
        filesLabel.setFont(filesLabel.getFont().deriveFont(18f));
        labelsLabel = new JLabel("Labels");
        labelsLabel.setFont(labelsLabel.getFont().deriveFont(18f));

        fileChooser = new JFileChooser();
        loading = new JLabel("Loading...");
        loading.setForeground(Color.BLUE);
        loading.setVisible(false);

        files = new ArrayList<JLabel>();
        labels = new ArrayList<JTextField>();
        removeButtons = new ArrayList<JButton>();

        //load the file data
        for (int i = 0; i < data.get(1).size(); i++) {
            JLabel l = new JLabel();
            l.setFont(l.getFont().deriveFont(13f));
            files.add(l);
        }

        //load the label data
        for (int i = 0; i < data.get(0).size(); i++) {
            JTextField l = new JTextField();
            l.setBorder(null);
            labels.add(l);
        }

        //load the label data
        for (int i = 0; i < data.get(0).size(); i++) {
            removeButtons.add(new JButton("Remove"));
        }
    }

    public void addComponentsToCanvas() {
        this.add(upload);
        this.add(save);
        this.add(filesLabel);
        this.add(labelsLabel);
        this.add(loading);

        for (JLabel l : files) {
            this.add(l);
        }

        for (JTextField l : labels) {
            this.add(l);
        }

        for (JButton l : removeButtons) {
            this.add(l);
        }
    }

    public void save() {
        //first check to see if any of the labels are over 200
        boolean ok = true;
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).getText().length() > 50) {
                ok = false;
                JOptionPane.showMessageDialog(null, "The label must be less then 50 characters.");
                labels.get(i).requestFocus();
                labels.get(i).selectAll();
            }
        }
        if (ok) {
            //save the files and labels into the database
            for (int i = 0; i < files.size(); i++) {
                TeaDatabase.insertFilesIntoDatabase(this.companyID.getCompanyID(), labels.get(i).getText(), files.get(i).getText());
            }

            //then update the gui
            update(this.companyID);
        }

    }

    public void fillValues() {
        for (int i = 0; i < data.get(0).size(); i++) {
            files.get(i).setText(data.get(1).get(i));
            addListenerToLabel(files.get(i));
        }
        for (int i = 0; i < data.get(0).size(); i++) {
            labels.get(i).setText(data.get(0).get(i));
        }
        //set the remove button to be linked to a number
        for (int i = 0; i < data.get(0).size(); i++) {
            removeButtons.get(i).addActionListener(this);
            removeButtons.get(i).setActionCommand("REMOVE " + i);
        }
    }

    public void initLayout() {
        //place the progress bar down in the bottom
        layout.putConstraint(SpringLayout.WEST, loading, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, loading, 35, SpringLayout.NORTH, this);

        //place the upload and save buttons
        layout.putConstraint(SpringLayout.WEST, upload, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, upload, 10, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, save, 20, SpringLayout.EAST, upload);
        layout.putConstraint(SpringLayout.SOUTH, save, 0, SpringLayout.SOUTH, upload);
        layout.putConstraint(SpringLayout.NORTH, save, 0, SpringLayout.NORTH, upload);

        //place the file and label labels
        layout.putConstraint(SpringLayout.WEST, filesLabel, 10, SpringLayout.WEST, upload);
        layout.putConstraint(SpringLayout.NORTH, filesLabel, 25, SpringLayout.SOUTH, upload);
        layout.putConstraint(SpringLayout.WEST, labelsLabel, (20 + getLongestFileNameWidth()), SpringLayout.EAST, filesLabel);
        layout.putConstraint(SpringLayout.SOUTH, labelsLabel, 0, SpringLayout.SOUTH, filesLabel);

        //place all the labels
        for (int i = 0; i < labels.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, labels.get(i), 0, SpringLayout.WEST, labelsLabel);
                layout.putConstraint(SpringLayout.NORTH, labels.get(i), 10, SpringLayout.SOUTH, labelsLabel);
            } else {
                layout.putConstraint(SpringLayout.WEST, labels.get(i), 0, SpringLayout.WEST, labels.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, labels.get(i), 10, SpringLayout.SOUTH, labels.get(i - 1));
            }
        }

        //place all the files
        for (int i = 0; i < files.size(); i++) {
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, files.get(i), 0, SpringLayout.WEST, filesLabel);
                layout.putConstraint(SpringLayout.NORTH, files.get(i), 10, SpringLayout.SOUTH, filesLabel);
            } else {
                layout.putConstraint(SpringLayout.WEST, files.get(i), 0, SpringLayout.WEST, files.get(i - 1));
                layout.putConstraint(SpringLayout.NORTH, files.get(i), 10, SpringLayout.SOUTH, files.get(i - 1));
            }
            layout.putConstraint(SpringLayout.NORTH, files.get(i), 0, SpringLayout.NORTH, labels.get(i));
            layout.putConstraint(SpringLayout.SOUTH, files.get(i), 0, SpringLayout.SOUTH, labels.get(i));
        }

        //align all the right sides of the labels
        int logestLabel = getLongestLabel();
        for (int i = 0; i < labels.size(); i++) {
            if (i != logestLabel) {
                layout.putConstraint(SpringLayout.EAST, labels.get(i), 0, SpringLayout.EAST, labels.get(logestLabel));
            }
        }

        //place the remove button next to each label
        for (int i = 0; i < removeButtons.size(); i++) {
            layout.putConstraint(SpringLayout.WEST, removeButtons.get(i), 20, SpringLayout.EAST, labels.get(i));
            layout.putConstraint(SpringLayout.NORTH, removeButtons.get(i), 0, SpringLayout.NORTH, labels.get(i));
            layout.putConstraint(SpringLayout.SOUTH, removeButtons.get(i), 0, SpringLayout.SOUTH, labels.get(i));
        }
    }

    //the upload button needs to work the ftp
    //the save button needs to save the files
    //the remove buttons should remove the file from the database
    private void saveFileToFTPServer(File f) {
        TeaFTP ftp = new TeaFTP(FTP_PASSWORD, this.companyID);
        ftp.connect();
        ftp.transferFile(f);
        ftp.disconnect();
    }

    private void popupFileChooser() {
        fileChooser.addActionListener(this);
        int val = fileChooser.showOpenDialog(this);
        File file = null;
        if (val == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        if (file != null) {
            //upload the file to the server
            saveFileToFTPServer(file);
            //save the file and label into the database
            TeaDatabase.insertFilesIntoDatabase(this.companyID.getCompanyID(), "Enter A Label", file.getName());
            //update the gui
            update(companyID);
        }
    }

    public void removeFile(String name) {
        //first remove it from the database
        TeaDatabase.removeFileFromDatabase(this.companyID.getCompanyID(), name);
        //then update the gui
        update(companyID);
    }

    private void addListenerToLabel(final JLabel tmp) {
        tmp.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                open(tmp.getText());
            }
        });
    }

    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String getExtension(String s) {
        String ext = null;
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private void open(String fileName) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            fileName = fileName.replaceAll("\\s", "%20");
            //get the file type
            String fixFolder = FTP_FOLDER;
            if (!FTP_FOLDER.endsWith("/")) {
                fixFolder = fixFolder + "/";
            }
            if (!FTP_FOLDER.startsWith("/")) {
                fixFolder = "/" + fixFolder;
            }
            try {
                //instead of opening apache lets have it open a file cooser and save the file there
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(this);
                if (result != JFileChooser.CANCEL_OPTION) {
                    File selFile = fileChooser.getSelectedFile();
                    //check to make sure the extension is correct
                    String selFileExtension = getExtension(selFile);
                    String uploadedFileExtension = getExtension(fileName);
                    if (uploadedFileExtension != null) {
                        if (selFileExtension != null) {

                            if (!selFileExtension.equals(uploadedFileExtension)) {
                                //set the new extension
                                String path = selFile.getCanonicalPath();
                                selFile = new File(path + "." + uploadedFileExtension);
                            }
                        } else {
                            String path = selFile.getCanonicalPath();
                            selFile = new File(path + "." + uploadedFileExtension);
                        }
                    }
                    if (selFile != null) {
                        uri = new URI("ftp://" + FTP_USERNAME + ":" + FTP_PASSWORD + "@" + FTP_SERVER + fixFolder + this.companyID.getCompanyIDString() + "/" + fileName);

                        URLConnection con = uri.toURL().openConnection();
                        BufferedInputStream in =
                                new BufferedInputStream(con.getInputStream());
                        FileOutputStream out =
                                new FileOutputStream(selFile);

                        int i = 0;
                        byte[] bytesIn = new byte[1024];
                        while ((i = in.read(bytesIn)) >= 0) {
                            out.write(bytesIn, 0, i);
                        }
                        out.close();
                        in.close();
                        desktop.open(selFile);
                    }
                }
            } catch (IOException e) {
            } catch (URISyntaxException e) {
                popupErrorWindow();
                e.printStackTrace();
            }

        }
    }

    private void popupErrorWindow() {
        new ErrorWindow();
    }

    static final class ErrorWindow extends JFrame implements ActionListener {

        private JButton close = new JButton("Close");
        private JLabel errorLabel = new JLabel("Error Occured");
        private JLabel description = new JLabel("Something unexpected occured when trying to open the contents of the file. ");
        private JLabel description2 = new JLabel("Please contact your system administrator to get more information on the error.");
        private SpringLayout layout = new SpringLayout();

        ErrorWindow() {
            setSize(500, 500);
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
            if (e.getActionCommand().equals("Close")) {
                dispose();
            }
        }
    }
}
