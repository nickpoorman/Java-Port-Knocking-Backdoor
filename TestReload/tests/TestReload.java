package com;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestReload {
	private TestClass reload() {
		URL[] urls = null;
		try {
			// Convert the file object to a URL
			File dir = new File("plugins\\");
			URL url = dir.toURI().toURL(); // file:/c:/almanac1.4/examples/
			System.out.println(url);
			Runtime.getRuntime().exec("javac plugins\\com\\*.java");
			urls = new URL[] { url };
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ClassLoader cl = new URLClassLoader(urls);
			Class cls = cl.loadClass("com.TestClassImpl");
			return (TestClass) cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] argsv) {
		System.out.println("sys out");
		TestReload testReload = new TestReload();
		while (true) {
			System.out.println(testReload.reload().getMessage());
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}