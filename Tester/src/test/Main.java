package test;

import java.util.Scanner;

public class Main {


	public static void main(String[] args) {
		//We will declare our constant values first. Usually constant values are in all CAPS (just good programming practice).
		//we will hold whole numbers in integers here
		int FEET_PER_MILE = 5280;
		int INCHES_PER_FOOT = 12;
		//this value has a decimal point so it has to be held in a floating point number using either float, or double. double holds larger decimals like 13.135414525264564534 kinda thing
		//we will use double
		double CENTIMETERS_PER_INCH = 2.54;
		int CENTIMETERS_PER_KILOMETER = 100000;
		
		
		//create a new scanner object, when its capitalized like Scanner that means its an object from a class. 
		//Someone made the Scanner object for us. A class is like blueprints to make an object. 
		//So where it first says Scanner in the beginning we are just saying that we are going to make a Scanner object.
		// sc is the variable that we use to reference the Scanner object in memory. So we can access it later.
		// the part that says "new Scanner" is what takes the blueprints called a "class" (which is just compiled source code) and makes the new Object for us.
		//Think of it as a chair. If I wanted to make a chair I would do something like this:
		//Chair c = new Chair();
		//The part that says "(System.in)" just means we are calling the constructor of the blueprints aka the Scanner class and
		//telling it that we just want to read in input from the standard input stream aka your keyboard.
		Scanner sc = new Scanner(System.in);		
		
		//print out the prompt for the user to enter something
		System.out.println("Please enter a distance in miles: ");
		
		//get the next input from the user and store it in a String that we call token.		
		String token = sc.next();
		
		//now do the rest of the code over and over again until the user enters the text "quit" into the program
		while(!token.equals("quit")){
			//this is where all the cool stuff happens
			//REMEMBER token is holding the value they just entered!
			//lets turn this from a string into an integer first so we can do math on it as a number
			//Integer.parseInt just looks at the string and turns it from a "literal string" into an integer data type
			int miles = Integer.parseInt(token);
			
			//first we have to do all the conversions
			
			//convert miles to feet
			int milesToFeet = miles * FEET_PER_MILE;
			
			//convert feet to inches
			int feetToInches = milesToFeet * INCHES_PER_FOOT;
			
			//convert inches to centimeters
			//REMEMBER you have to use double here because we are holding a decimal number aka a floating point number
			double inchesToCentimeters = feetToInches * CENTIMETERS_PER_INCH;
			
			//convert centimeters to kilometers
			//Now we are going from a smaller unit to a larger one so we will have to divide
			// which means we will most likely get a decimal number so use double again
			double centimetersToKilometers = inchesToCentimeters / CENTIMETERS_PER_KILOMETER;
			
			//Finally we have all our values so we can print them out
			System.out.println(miles + " miles is: " + milesToFeet + " feet");
			System.out.println(milesToFeet + " feet is: " + feetToInches + " inches");
			System.out.println(feetToInches + " inches is: " + inchesToCentimeters + " centimeters");
			System.out.println(inchesToCentimeters + " centimeters is: " + centimetersToKilometers + " kilometers");
			
			
			//now print out a prompt again so the user knows to enter another value
			System.out.println("Please enter a distance in miles: ");
			
			//get the value they enter and store it in token. Now run the while loop again.
			token = sc.next();
		}

	}

}
