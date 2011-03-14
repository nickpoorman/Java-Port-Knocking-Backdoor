package teadatabasegui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author nick
 */
public class ConfigFile {

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static String DATABASE_USER_NAME;
    private static String DATABASE_PASSWORD;
    private static String JDBC_DRIVER;
    private static String DATABASE_CONNECTION_URL;
    private static String FTP_SERVER;
    private static String FTP_USER_NAME;
    private static String FTP_USER_PASSWORD;
    private static String FTP_UPLOAD_FOLDER;

    public ConfigFile() {
        //read in the properties file and get the info here
        getPropertiesFromFile();
    }

    private void getPropertiesFromFile() {
        try {
            File f = new File(CONFIG_FILE_NAME);
            if (f.exists()) {
                Properties pro = new Properties();
                FileInputStream in = new FileInputStream(f);
                pro.load(in);

                //set the variables to their properties
                DATABASE_USER_NAME = pro.getProperty("DATABASE_USER_NAME");
                DATABASE_PASSWORD = pro.getProperty("DATABASE_PASSWORD");
                JDBC_DRIVER = pro.getProperty("JDBC_DRIVER");
                DATABASE_CONNECTION_URL = pro.getProperty("DATABASE_CONNECTION_URL");
                FTP_SERVER = pro.getProperty("FTP_SERVER");
                FTP_USER_NAME = pro.getProperty("FTP_USER_NAME");
                FTP_USER_PASSWORD = pro.getProperty("FTP_USER_PASSWORD");
                FTP_UPLOAD_FOLDER = pro.getProperty("FTP_UPLOAD_FOLDER");

            } else {
                System.err.println("File not found!");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @return the DATABASE_USER_NAME
     */
    public String getDATABASE_USER_NAME() {
        return DATABASE_USER_NAME;
    }

    /**
     * @return the DATABASE_PASSWORD
     */
    public String getDATABASE_PASSWORD() {
        return DATABASE_PASSWORD;
    }

    /**
     * @return the JDBC_DRIVER
     */
    public String getJDBC_DRIVER() {
        return JDBC_DRIVER;
    }

    /**
     * @return the DATABASE_CONNECTION_URL
     */
    public String getDATABASE_CONNECTION_URL() {
        return DATABASE_CONNECTION_URL;
    }

    /**
     * @return the FTP_SERVER
     */
    public String getFTP_SERVER() {
        return FTP_SERVER;
    }

    /**
     * @return the FTP_USER_NAME
     */
    public String getFTP_USER_NAME() {
        return FTP_USER_NAME;
    }

    /**
     * @return the FTP_USER_PASSWORD
     */
    public String getFTP_USER_PASSWORD() {
        return FTP_USER_PASSWORD;
    }

    /**
     * @return the FTP_UPLOAD_FOLDER
     */
    public String getFTP_UPLOAD_FOLDER() {
        return FTP_UPLOAD_FOLDER;
    }
}
