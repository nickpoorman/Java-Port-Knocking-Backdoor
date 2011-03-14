package voter;

public class Account {
	
	String username;
	String password;
	long time;
	long time2;
	
	public Account(){
		this.username = "";
		this.password = "";
		this.time = 0;
		this.time2 = 0;
	}
	
	public Account(String username, String password, long time, long time2){
		this.username = username;
		this.password = password;
		this.time = time;
		this.time2 = time2;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public long getTime2() {
		return time2;
	}

	public void setTime2(long time2) {
		this.time2 = time2;
	}

}
