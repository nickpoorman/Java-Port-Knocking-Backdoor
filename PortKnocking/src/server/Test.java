package server;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sw = "-i";
		String tmp = "-i012345";
		tmp = tmp.substring(sw.length());
		System.out.println(tmp);
		System.out.println("len " + sw.length());

	}

}
