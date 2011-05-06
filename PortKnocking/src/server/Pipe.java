package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pipe implements Runnable {

	InputStream inputStream;
	OutputStream outputStream;

	// read ==> write
	public Pipe(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		
	}

	/**
	 * Connect the output to the input
	 */
	public void run() {
		for (;;) {
			try {
				byte[] buffer = new byte[1024];
				int len;
				len = inputStream.read(buffer);
				while (len != -1) {
					System.out.println("Writing to output: " + new String(buffer));
					outputStream.write(buffer, 0, len);
					outputStream.flush();
					len = inputStream.read(buffer);
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
				}
			} catch (IOException e) {
				return;//?
			} catch (InterruptedException e) {
				return; //why not?
			}
		}
	}

}
