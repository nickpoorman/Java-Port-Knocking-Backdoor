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

	private static final String SERVER = "moxie.oswego.edu";
	private static final String USER_NAME = "csa";

	private final FTPClient client = new FTPClient();

	private final String password;

	public TeaFTP(String password) {
		this.password = password;
	}

	public boolean connect() {
		try {
			client.connect(SERVER);
			client.login(USER_NAME, password);
			client.setFileType(FTPClient.ASCII_FILE_TYPE);
		}
		catch (IOException e) { return false; }
		return true;
	}

	public boolean transferFile(File file) {
		try {
            System.out.println(file.getCanonicalPath());
            FileInputStream stream = openFileStream(file.getCanonicalPath());
			client.makeDirectory("~/public_html/ftp_test");
			client.changeWorkingDirectory("~/public_html/ftp_test");
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
		}
		catch (IOException e) { return false; }
		return true;
	}

	public static void main(String[] args) {
		TeaFTP ftp = new TeaFTP("csarules1");
		boolean connected = ftp.connect();
		System.out.println("Connected: " + connected);
		boolean transfer = ftp.transferFile(new File("C:\\Users\\Kendall\\Temp.txt.txt"));
		System.out.println("Transfering file: " + (transfer ? "Success" : "Failure"));
		boolean disconnected = ftp.disconnect();
		System.out.println("Disconnected: " + disconnected);
	}

}
