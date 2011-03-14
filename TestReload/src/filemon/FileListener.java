package filemon;

import java.io.File;

public abstract class FileListener implements Runnable {

	private File f;
	private long lastModified;

	public FileListener(File f) {
		this.f = f;
		lastModified = f.lastModified();
	}

	@Override
	public void run() {
		try {
			for (;;) {
				long newLastModified = f.lastModified();
				if (newLastModified != lastModified) {
					lastModified = newLastModified;
					fileChanged();					
				}
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	
	public abstract void fileChanged();
	
	public File getFile(){
		return f;
	}

}
