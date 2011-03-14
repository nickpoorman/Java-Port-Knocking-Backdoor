package test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println("Please type reload");
		QuickTest tmp = null;
		tmp = load();
		tmp.doAction();
		Scanner sc = new Scanner(System.in);
		String command = "";
		while (!(command = sc.next()).equals("quit")) {
			if (command.equals("reload")) {
				System.out.println("Reloading!");
				tmp = null;
				tmp = load();
				System.out.println("Done Reloading!");
				tmp.doAction();
//				QuickTest newTemp = load();
//				if(!newTemp.equals(tmp))
//					System.out.println("true");
				
			}
		}

	}

	public static QuickTest load() {
		try {
			URL[] urls = null;

			File dir = new File("bin" + File.separator + "test");
			URL url = dir.toURI().toURL();
			urls = new URL[] { url };

			ClassLoader cl = new URLClassLoader(urls);

			Class cls = cl.loadClass("test.QuickTest");

			QuickTest myObj = (QuickTest) cls.newInstance();
			return myObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
