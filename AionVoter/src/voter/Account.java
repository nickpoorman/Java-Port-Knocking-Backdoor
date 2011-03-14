package voter;

public class Account {
	
	String username;	
	long time;
	
	public Account(){
		this.username = "";		
		this.time = 0;
	}
	
	public Account(String username, long time){
		this.username = username;		
		this.time = time;	
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
