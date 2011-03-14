package voter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
		if (DEBUG) logger.info("Running the VoterMain");

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
			if (DEBUG) logger.info("There was a problem opening the file");
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
				// read in the password line
				account.setPassword(next);
				line++;
			} else if (line == 2) {
				// read in the last time the account was used
				long time = Long.parseLong(next);
				if (DEBUG) logger.info("Time: " + time);
				account.setTime(time);
				//accounts.add(account);
				//line = 0;
				//TrayIconDemo.sendInfoMessage("Loaded account: " + account.getUsername());
				//logger.info("Loaded account: " + account.getUsername());
				//sleep so they see the message
				//try {
				//	Thread.sleep(1000);
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
				line++;
			}else if (line == 3) {
				// read in the last time the account was used
				long time2 = Long.parseLong(next);
				if (DEBUG) logger.info("Time2: " + time2);
				account.setTime2(time2);
				accounts.add(account);
				line = 0;
				TrayIconDemo.sendInfoMessage("Loaded account: " + account.getUsername());
				logger.info("Loaded account: " + account.getUsername());
				//sleep so they see the message
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (DEBUG) logger.info("Done reading in the file");
		Random random = new Random();
		//do this over and over until the program shuts down
		for (;;) {
			// now that all the accounts are stored in accounts go through them and
			// do the request for each one if its time
			for (Account a : accounts) {
				if(checkAccountPerfect(a, random, accounts)){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(checkAccountAion(a, random, accounts)){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			//now sleep for 10 min
			if (DEBUG) logger.info("Done checking ...Sleeping for 10 min");
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
	public static boolean checkAccountAion(Account a, Random random, List<Account> accounts){
		boolean success = false;
		boolean timeUp = false;
		// first check to see if the time is up on the account
		long time = a.getTime2();
		if (time == 0) { // 0 is default
			// time is up
			timeUp = true;
		} else if ((System.nanoTime() - time) > VoterMain.NANO_SECONDS_IN_4HOURS) {
			timeUp = true;
		}

		if (timeUp) {					
			if (DEBUG) logger.info("Time UP2! Fuzz Sleeping...");
			//sleep for a given number of seconds so that its not the same every time (fuzz it) 1 min
			try {
				int ran = random.nextInt(60000);
				if (DEBUG) logger.info("2 Sleeping for: " + (ran / 1000) + " seconds.");
				Thread.sleep(ran);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (DEBUG) logger.info("2 Done Fuzz Sleeping");
			
			success = doAccountAion(a.getUsername(), a.getPassword());
			//now we must write out the whole file again
			if (success) {
				TrayIconDemo.sendInfoMessage("Voted on account2: " + a.username);
			} else {
				TrayIconDemo.sendErrorMessage("Faild to vote on account2: " + a.username);
			}
			long newVoteTime = System.nanoTime();
			a.setTime2(newVoteTime);
			logger.info("Vote status2: " + success + " Account: " + a.username + " at: " + newVoteTime);
			updateFile(accounts);
		}
		return success;
	}
	
	/*
	 * checks the Perfect page accounts to see if its time to vote, if so then vote
	 */
	public static boolean checkAccountPerfect(Account a, Random random, List<Account> accounts){
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
			if (DEBUG) logger.info("Time UP! Fuzz Sleeping...");
			//sleep for a given number of seconds so that its not the same every time (fuzz it) 1 min
			try {
				int ran = random.nextInt(60000);
				if (DEBUG) logger.info("Sleeping for: " + (ran / 1000) + " seconds.");
				Thread.sleep(ran);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (DEBUG) logger.info("Done Fuzz Sleeping");
			
			success = doAccountPerfect(a.getUsername(), a.getPassword());
			//now we must write out the whole file again
			if (success) {
				TrayIconDemo.sendInfoMessage("Voted on account: " + a.username);
			} else {
				TrayIconDemo.sendErrorMessage("Faild to vote on account: " + a.username);
			}
			long newVoteTime = System.nanoTime();
			a.setTime(newVoteTime);
			logger.info("Vote status: " + success + " Account: " + a.username + " at: " + newVoteTime);
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
			if (DEBUG) logger.info("Writing over the files");
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))));

			//write the header
			printWriter.println("###########################################################");
			printWriter.println("# AutoVote 1.0 by [InFamous]stringSyntax 2010");
			printWriter.println("#");
			printWriter.println("# Account File works as follows:");
			printWriter.println("#  Any line that starts with a # is a comment line");
			printWriter.println("#   and the program will just ignore it.");
			printWriter.println("#");
			printWriter.println("#  Accounts should be listed as follows:");
			printWriter.println("#username");
			printWriter.println("#password");
			printWriter.println("#time <-NOTE: this its calculated by the program, if new account(perfect) default should be 0");
			printWriter.println("#time2 <-NOTE: this its calculated by the program, if new account(aion) default should be 0");
			printWriter.println("###########################################################");

			printWriter.println("#");
			//now print out all the accounts
			for (Account a : accounts) {
				printWriter.println(a.getUsername());
				printWriter.println(a.getPassword());
				printWriter.println(a.getTime());
				printWriter.println(a.getTime2());
				printWriter.println("#");
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean doAccountPerfect(String username, String password) {
		boolean success = false;
		// URL
		String textUrl = "http://66.45.229.98/Vendetta/Vote/";
		String scriptName = "checklogin.php";

		try {
			// Construct data
			String data = URLEncoder.encode("char", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
			data += "&" + URLEncoder.encode("vote1", "UTF-8") + "=" + URLEncoder.encode("Vote", "UTF-8");
			// Send data
			URL url = new URL(textUrl + scriptName);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				//System.out.println("Output: " + line);
				if (line.contains("XtremeTop100.com - Gaming top 100 list")) {
					success = true;
				}
			}
			wr.close();
			rd.close();

		} catch (Exception e) {
			return success;
		}
		return success;

	}
	
	public static boolean doAccountAion(String username, String password) {
		boolean success = false;
		// URL
		String textUrl = "http://nickpoorman.com/";
		String scriptName = "login.php";

		try {
			// Construct data
			String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
			data += "&" + URLEncoder.encode("secret", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
			// Send data
			URL url = new URL(textUrl + scriptName);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			logger.info("STARTING LOG FOR RETURN PAGE FROM AION ACCOUNT");
			while ((line = rd.readLine()) != null) {
				// Process line...
				//System.out.println("Output: " + line);
				//for debug output the resulting page:
				logger.info(line);
				
				if (line.contains("XtremeTop100.com - Gaming top 100 list")) {
					success = true;
				}else if (line.contains("XtremeTop100 - Gaming top 100 list")) {
					success = true;
				}
			}
			logger.info("FINISHING LOG FOR RETURN PAGE FROM AION ACCOUNT");
			wr.close();
			rd.close();

		} catch (Exception e) {
			return success;
		}
		return success;

	}
}
