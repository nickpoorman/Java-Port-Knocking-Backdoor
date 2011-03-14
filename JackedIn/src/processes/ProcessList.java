package processes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessList {

	public static List<String> listRunningProcesses() {
		List<String> processes = new ArrayList<String>();
		try {
			String line;
			Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (!line.trim().equals("")) {
					// we only need the process name
					line = line.substring(1);
					processes.add(line.substring(0, line.indexOf("\"")));
				}

			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return processes;
	}

	public static void main(String[] args) {
		List<String> processes = listRunningProcesses();
		for(String s : processes){
			System.out.println(s);
		}
	}
}