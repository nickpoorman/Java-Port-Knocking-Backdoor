package teadatabasegui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Kendall
 *
 */
public class TeaFTP {

    private static String SERVER;
    private static String USER_NAME;
    private static String UPLOAD_FOLDER;
    private final FTPClient client = new FTPClient();
    private final String password;
    private CompanyID companyID;

    public TeaFTP(String password, CompanyID companyID) {
        this.password = password;
        this.companyID = companyID;
        SERVER = MainWindow.getConf().getFTP_SERVER();
        USER_NAME = MainWindow.getConf().getFTP_USER_NAME();
        UPLOAD_FOLDER = MainWindow.getConf().getFTP_UPLOAD_FOLDER();
        if (!UPLOAD_FOLDER.endsWith("/")) {
            UPLOAD_FOLDER = UPLOAD_FOLDER + "/";
        }
    }

    public boolean connect() {
        try {
            client.connect(SERVER);
            client.login(USER_NAME, password);
            client.setFileType(FTPClient.ASCII_FILE_TYPE);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean transferFile(File file) {
        try {
            System.out.println(file.getCanonicalPath());
            FileInputStream stream = openFileStream(file.getCanonicalPath());
            //put the file in the company id's folder
            boolean b = client.makeDirectory(UPLOAD_FOLDER + companyID.getCompanyIDString());
            boolean b2 = client.changeWorkingDirectory(UPLOAD_FOLDER + companyID.getCompanyIDString());
            client.storeFile(file.getName(), stream);
            stream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private FileInputStream openFileStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disconnect() {
        try {
            client.logout();
            client.disconnect();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

//    public static void main(String[] args) {
//        TeaFTP ftp = new TeaFTP("csarules1", new CompanyID(64));
//        boolean connected = ftp.connect();
//        try {
//            boolean mkdir = ftp.client.makeDirectory(UPLOAD_FOLDER + "testDirectory");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        boolean disconnected = ftp.disconnect();
//    }
}
