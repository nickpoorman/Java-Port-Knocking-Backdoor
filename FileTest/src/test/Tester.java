package test;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Tester {
	
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
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	}



	public static void main(String[] args) {
//		try {
//			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File("test.txt"))));
//
//			//write the header
//			//printWriter.println("###########################################################\n# Account File works as follows:\n#  Any line that starts with a # is a comment line\n#   and the program will just ignore it.\n#\n#  Accounts should be listed as follows:\n#username\n#password\n#time <-NOTE: this its calculated by the program, if new account default should be 0\n###########################################################\n");
//
//			printWriter.println("###########################################################");
//			printWriter.println("# Account File works as follows:");
//			printWriter.println("#  Any line that starts with a # is a comment line");
//			printWriter.println("#   and the program will just ignore it.");
//			printWriter.println("#");
//			printWriter.println("#  Accounts should be listed as follows:");
//			printWriter.println("#username");
//			printWriter.println("#password");
//			printWriter.println("#time <-NOTE: this its calculated by the program, if new account default should be 0");
//			printWriter.println("###########################################################");
//
//			printWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(System.nanoTime());
		//logger.info("this is a test");
		//logger.info("this is a test two");
		
		
	}

}
