package voter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class VoterMain implements Runnable {
	public final static long NANO_SECONDS_IN_HOUR = 3600000000000l;
	public final static long NANO_SECONDS_IN_4HOURS = 14400000000000l;
	public static String fileName = "accounts.txt";

	public static final boolean DEBUG = true;

	public static Logger logger;

	static {
		try {
			boolean append = true;
			FileHandler fh = new FileHandler("accounts.log", append);
			fh.setFormatter(new Formatter() {
				public String format(LogRecord rec) {
					StringBuffer buf = new StringBuffer(1000);
					buf.append(new java.util.Date());
					buf.append(' ');
					buf.append(rec.getLevel());
					buf.append(' ');
					buf.append(formatMessage(rec));
					buf.append('\n');
					return buf.toString();
				}
			});
			logger = Logger.getLogger("TestLog");
			logger.addHandler(fh);

			Logger rootLogger = Logger.getLogger("");
			Handler[] handlers = rootLogger.getHandlers();
			if (handlers[0] instanceof ConsoleHandler) {
				rootLogger.removeHandler(handlers[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (DEBUG) logger.info("Running the VoterMain" + "\n");

		// open the file scan in all the accounts
		File accountsFile = new File(fileName);
		Scanner sc = null;
		try {
			sc = new Scanner(accountsFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (sc == null) {
			// there was a problem opening the file
			if (DEBUG) logger.info("There was a problem opening the file" + "\n");
			System.exit(0);
			return;
		}

		Account account = null;
		int line = 0;
		List<Account> accounts = new LinkedList<Account>();
		while (sc.hasNext()) {
			String next = sc.nextLine();
			if (next.equals("")) {
				continue;
			} else if (next.startsWith("#")) {
				continue;
			}

			if (line == 0) {
				account = new Account();
				// read in the user line
				account.setUsername(next);
				line++;
			} else if (line == 1) {
				// read in the last time the account was used
				long time = Long.parseLong(next);
				if (DEBUG) logger.info("Time: " + time + "\n");
				account.setTime(time);
				accounts.add(account);
				line = 0;
				TrayIconDemo.sendInfoMessage("Loaded account: " + account.getUsername());
				logger.info("Loaded account: " + account.getUsername() + "\n");
				//sleep so they see the message
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (DEBUG) logger.info("Done reading in the file" + "\n");
		Random random = new Random();
		//do this over and over until the program shuts down
		for (;;) {
			// now that all the accounts are stored in accounts go through them and
			// do the request for each one if its time
			for (Account a : accounts) {
				if (checkAccountAion(a, random, accounts)) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			//now sleep for 10 min
			if (DEBUG) logger.info("Done checking ...Sleeping for 10 min" + "\n");
			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * checks the Aion page accounts to see if its time to vote, if so then vote
	 */
	public static boolean checkAccountAion(Account a, Random random, List<Account> accounts) {
		boolean success = false;
		boolean timeUp = false;
		// first check to see if the time is up on the account
		long time = a.getTime();
		if (time == 0) { // 0 is default
			// time is up
			timeUp = true;
		} else if ((System.nanoTime() - time) > VoterMain.NANO_SECONDS_IN_4HOURS) {
			timeUp = true;
		}

		if (timeUp) {
			if (DEBUG) logger.info("Time UP! Fuzz Sleeping..." + "\n");
			//sleep for a given number of seconds so that its not the same every time (fuzz it) 1 min
			try {
				int ran = random.nextInt(60000);
				if (DEBUG) logger.info("Sleeping for: " + (ran / 1000) + " seconds." + "\n");
				Thread.sleep(ran);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (DEBUG) logger.info("Done Fuzz Sleeping" + "\n");

			for (int voteFieldNum = 1; voteFieldNum < 6; voteFieldNum++) {
				try {
					success = doAccountAion(a.getUsername(), voteFieldNum);
					Thread.sleep(random.nextInt(20000));
					if (!success) {
						// try it again
						success = doAccountAion(a.getUsername(), voteFieldNum);
						Thread.sleep(random.nextInt(25000));
					}
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			}
			//now we must write out the whole file again
			if (success) {
				TrayIconDemo.sendInfoMessage("Voted on account: " + a.username);
			} else {
				TrayIconDemo.sendErrorMessage("Faild to vote on account: " + a.username);
			}
			long newVoteTime = System.nanoTime();
			a.setTime(newVoteTime);
			logger.info("Vote status: " + success + " Account: " + a.username + " at: " + newVoteTime + "\n");
			updateFile(accounts);
		}
		return success;
	}

	/*
	 * updateFile will write out all the accounts overwriting the old account
	 * file
	 */
	public static void updateFile(List<Account> accounts) {
		try {
			if (DEBUG) logger.info("Writing over the files" + "\n");
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))));

			//write the header
			printWriter.println("###########################################################");
			printWriter.println("# AutoVote 1.0 by [InFamous]stringSyntax 2011");
			printWriter.println("#");
			printWriter.println("# Account File works as follows:");
			printWriter.println("#  Any line that starts with a # is a comment line");
			printWriter.println("#   and the program will just ignore it.");
			printWriter.println("#");
			printWriter.println("#  Accounts should be listed as follows:");
			printWriter.println("#username");
			printWriter.println("#time <-NOTE: this its calculated by the program, if new account(aion) default should be 0");
			printWriter.println("###########################################################");

			printWriter.println("#");
			//now print out all the accounts
			for (Account a : accounts) {
				printWriter.println(a.getUsername());
				printWriter.println(a.getTime());
				printWriter.println("#");
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean doAccountAion(String username, int num) throws Exception {
		boolean success = false;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet("http://gamezaion.com/index.php?page=vote");

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());
			EntityUtils.consume(entity);

			System.out.println("Initial set of cookies:");
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

			HttpPost httpost = new HttpPost("http://gamezaion.com/index.php?page=vote");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("char", username));
			nvps.add(new BasicNameValuePair("vote" + num, "Vote"));

			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			response = httpclient.execute(httpost);
			entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());

			// retrieve the output and display it in console
			String s = convertInputStreamToString(response.getEntity().getContent());
			System.out.print(s);
			if (s.contains("Your IP already voted!")) {
				success = true;
			}

			EntityUtils.consume(entity);

			System.out.println("Post logon cookies:");
			cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return success;
	}

	/**
	 * method to convert an InputStream to a string using the
	 * BufferedReader.readLine() method this methods reads the InputStream line
	 * by line until the null line is encountered it appends each line to a
	 * StringBuilder object for optimal performance
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}

			return stringBuilder.toString();
		} else {
			return null;
		}
	}

}
