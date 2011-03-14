package hooverville.worlds.regions;

public class NoSuchRegionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchRegionException() { super(); }
	
	public NoSuchRegionException(String message) { super(message); }
	
	public NoSuchRegionException(String message, Throwable cause) { super(message, cause); }
	
	public NoSuchRegionException(Throwable cause) { super(cause); }

}
