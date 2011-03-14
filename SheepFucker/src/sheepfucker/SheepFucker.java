package sheepfucker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SheepFucker implements Runnable {

	public void run() {
		for (;;) {
			doAccountAion("POORMANS_SHEEP_FUCKER", "passwordfuck");
			//TrayIconMain.sendInfoMessage("Sent login.");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean doAccountAion(String username, String password) {
		boolean success = false;
		// URL
		String textUrl = "http://google.com/";
		String scriptName = "login.py";

		try {
			// Construct data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
			data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
			// Send data
			URL url = new URL(textUrl + scriptName);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//conn.setRequestProperty("Content-Type", "text/html");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;			
			while ((line = rd.readLine()) != null) {				
			}			
			wr.close();
			rd.close();
		} catch (Exception e) {
			return success;
		}
		return success;

	}

}
