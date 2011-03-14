package util;

import java.io.File;
import java.io.PrintWriter;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintWriter out = new PrintWriter(System.out);
		CommandController cc = new CommandController(out);
		
		//Command tmp = cc.getCommandLoader().loadCommand(new File("plugins\\"), "TesterCommand.class");		
		//cc.getCommand("test").doAction("", out, cc);
		//tmp.doAction();
		
		while (true) {
			//cc.getCommandLoader().loadCommand(new File("."), "TesterCommand.class").doAction("", null, null);
			cc.getCommandLoader().loadAllCommandsInDir(new File("."), null);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println("Done");
	}

}
