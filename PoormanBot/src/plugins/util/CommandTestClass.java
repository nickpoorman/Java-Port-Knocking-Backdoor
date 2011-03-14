package plugins.util;

import java.io.File;


public class CommandTestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CommandController cc = new CommandController();

		//Command tmp = cc.getCommandLoader().loadCommand(new File("plugins\\"), "TesterCommand.class");		
		//cc.getCommand("test").doAction("", out, cc);
		//tmp.doAction();

		//while (true) {
		cc.getCommandLoader().loadCommand(new File("commands\\"), "TesterCommand.class").doAction(null, null, null);
		//cc.getCommandLoader().loadAllCommandsInDir(new File("plugins\\"), null);
		//			try {
		//				Thread.sleep(5000);
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}
		while (true) {
			cc.getCommand("testcommand").doAction(null, null, null);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//System.out.println("Done");
	}

}
