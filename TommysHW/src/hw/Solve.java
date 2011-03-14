package hw;

public class Solve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				for(int c = 0; c < 10; c++){
					if((a + b + c) == 11){
						if((a * b * c) == 16){
							if((b*4) == c){
								System.out.println("A is: " + a);
								System.out.println("B is: " + b);
								System.out.println("C is : " + c);
							}
						}
					}
				}
			}
		}

	}

}
