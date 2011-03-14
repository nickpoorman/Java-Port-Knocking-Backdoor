package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

	private static String runDirectory = "pluginTmp";
	private static AtomicInteger tmpFileNum = new AtomicInteger(0);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f = new File("test.txt");
		File copiedFile = createDirectoryCopyFile(f);
		File parent = copiedFile.getParentFile();
		
		System.out.println(parent.getCanonicalPath());
		try {
			FileReader in = new FileReader(copiedFile);

			int c;
			while ((c = in.read()) != -1)
				System.out.println((char) c);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Delete a directory
	 * 
	 * @param f
	 *            the {@link File} to be deleted
	 * @return true if and only if the file or directory is successfully
	 *         deleted; false otherwise
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * Looks to see if the directory contains a given directory that directory
	 * 
	 * @param folder
	 *            {@link File} is the current directory
	 * @param name
	 *            the file name to search for
	 * @return the new file if found; otherwise null
	 */
	public static File findDirectory(File folder, String name) {
		File[] list = folder.listFiles();
		for (File cF : list) {
			if (cF.isDirectory()) {
				if (cF.getName().equals(name)) {
					return cF;
				}
			}
		}
		return null;
	}

	/**
	 * Create a new folder to put the class into Then copy the given class into
	 * that directory
	 * 
	 * @param f
	 *            the {@link File} to be copied to the new directory
	 * @return the new file
	 */
	public static File createDirectoryCopyFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			//first check to see if the runDirectory is there		
			//if we don't find it then create it
			File tmpFolder = new File(runDirectory);
			if (!tmpFolder.exists()) tmpFolder.mkdir();
			tmpFolder.deleteOnExit();

			//copy the file
			//		FileReader in = new FileReader(f);
			FileChannel srcChannel = new FileInputStream(f).getChannel();
			//before we write to the output file we need to first 
			//check to see if the target folder already exists
			//increment the pointer until it doesn't
			File targetFolder = new File(runDirectory + File.separator + tmpFileNum.get());
			targetFolder.deleteOnExit();
			while (targetFolder.exists()) {
				tmpFileNum.incrementAndGet();
				targetFolder = new File(runDirectory + File.separator + tmpFileNum.get());				
			}
			targetFolder.mkdir();
			targetFolder.deleteOnExit();
			//now the pointer will point to a unique target folder
			//copy the file into the unique target folder			
			File outFile = new File(targetFolder.getCanonicalPath() + File.separator + f.getName());
			outFile.deleteOnExit();
			//		FileWriter out = new FileWriter(outFile);
			FileChannel dstChannel = new FileOutputStream(outFile).getChannel();

			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

			// Close the channels
			srcChannel.close();
			dstChannel.close();

			//		int c;
			//		while ((c = in.read()) != -1)
			//			out.write(c);
			//
			//		in.close();
			//		out.close();

			//return the copied file
			return outFile;
		} catch (IOException e) {
			return null;
		}

	}

}
