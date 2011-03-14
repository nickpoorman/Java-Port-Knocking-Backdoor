package plugins.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

import plugins.api.Command;
import plugins.filemon.FileListener;

public class CommandLoader {

	// folder containing the package
	private static String packageName = "plugins";
	private CommandController cc;

	public CommandLoader(CommandController cc) {
		this.cc = cc;
	}

	// f is a directory, rootDir null to begin with
	public void loadAllCommandsInDir(File f, File rootDir) {
		if (rootDir == null)
			rootDir = f;
		if (f == null)
			return;
		if (!f.isDirectory()) {
			return;
		}
		File[] list = f.listFiles();
		for (File p : list) {
			if (p.isDirectory()) {
				loadAllCommandsInDir(p, rootDir);
			}
			if (p.getName().endsWith(".class")) {
				if (!p.getName().equals("Command.class")) {
					// for (File p : list) {
					// System.out.println("Loading file: " + p.getName());
					// load the command
					// System.out.println(p.getParentFile());

					Command c = loadCommand(rootDir, p.getName());
					//System.out.println("Tring to add " + p.getName());
					//EDIT: I removed this because it just adds the stuff in twice
//					if (c != null) {
//						if (c.getCommand() != null) {
//
//							cc.getPluginMap().remove(c.getCommand());
//							cc.putCommand(c.getCommand(), c);
//						}
//					}

				}
			}

		}

	}

	// File root directory, String className.class
	public Command loadCommand(File rootDirectory, String name) {
		//System.out.println("Reload point 1");
		if (rootDirectory == null) {
			//System.out.println("Reload point 2");
			return null;
		}
		URL[] urls = null;
		try {
			// Convert the file object to a URL
			// File dir = new File("plugins\\");
			URL url = rootDirectory.toURI().toURL(); // file:/c:/almanac1.4/examples/
			// System.out.println(url);
			// Runtime.getRuntime().exec("javac " +
			// rootDirectory.getAbsolutePath() + File.separator + "*.java");
			// Runtime.getRuntime().exec("javac plugins\\com\\nickpoorman\\command\\*.java");
			urls = new URL[] { url };
			//System.out.println("Reload point 3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ClassLoader cl = new URLClassLoader(urls);
			Class cls = cl.loadClass(packageName + "." + name.substring(0, name.length() - 6));
			// Class cls =
			// cl.loadClass("com.nickpoorman.command.TesterCommand");

			Command newCommand = (Command) cls.newInstance();

			// while(!cc.getCommand(newCommand.getCommand()).equals(newCommand)){
			// System.out.println("tring to put command");
			//System.out.println("Tring to add " + name);
			if (newCommand != null) {
				if (newCommand.getCommand() != null) {
					cc.getPluginMap().remove(newCommand.getCommand());
					cc.putCommand(newCommand.getCommand(), newCommand);
				}
			}
			// }

			// add it to the map of commands
			// cc.putCommand(newCommand.getCommand(), newCommand);
			// at this point we should create a FileListener for the file so it
			// will reload itself if it ever changes
			// String filePath = rootDirectory.getCanonicalPath() +
			// File.separator + packageName + File.separator + name;
			// System.out.println("Adding listener for: " + filePath);
			/* TODO: This needs to be fixed. It will create duplicates of the filelistener if its reloaded */
			new Thread(new FileListener(rootDirectory, packageName, name) {
				public void fileChanged() {
					// C:\Users\nick\workspace\csc490\commands\plugins\Whisper.class
					//System.out.println("Reloading now");
					loadCommand(super.getRootDirectory(), super.getFile().getName());					
					//System.out.println("Done Reloading: " + super.getFile().getName());
				}
			}).start();
			return newCommand;
		} catch (ClassCastException e) {
			//System.out.println("Tried a strange cast");
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			//System.out.println("Couldnt find the class");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
}

/**
 * Inner class that only accepts .class files
 */
class ClassFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return (name.endsWith(".class"));
	}
}

/**
 * Inner class that only accepts .java files
 */
class JavaFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return (name.endsWith(".java"));
	}
}

// class FileListenerImpl extends FileListener {
//
// private File f;
//	
// public FileListenerImpl(File f) {
// super(f);
// this.f = f;
// }
//
// @Override
// public void fileChanged() {
// loadCommand(f);
// }
//
// }
