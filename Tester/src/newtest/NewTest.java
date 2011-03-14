package newtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NewTest {

	public static void main(String[] args) {
		File f = new File("bla");
		
		try {
			Scanner sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
