package tes;

/*
 * AccountTester.java
 * 
 * Version: 
 *     $Id$
 * 
 * Revisions: 
 *     $Log$
 *
 */

/**
 * The AccountTester class is the test class for the accounts.
 * 
 * @author Nick Poorman
 */
public class AccountTester {

	/**
	 * The inital amout to put in each account.
	 */
	private static final int TEST_NEW_MONEY_AMOUNT = 2000;

	/**
	 * This value will be used to either increase or decrease the max to
	 * withdraw to test overdraft and underdraft.
	 */
	private static final int AMOUNT_MODIFIER = 10;

	public AccountTester() {
	}

	/**
	 * The main entry point of the program that runs the tests on the
	 * CheckingAccount, MoneyMarket Account, SavingsAccount
	 */

	public static void main(String[] args) {
		BankAccount checkingAccount = new CheckingAccount(TEST_NEW_MONEY_AMOUNT);
		BankAccount moneyMarket = new MoneyMarket(TEST_NEW_MONEY_AMOUNT);
		BankAccount savingsAccount = new SavingsAccount(TEST_NEW_MONEY_AMOUNT);

		//print out the type of account - checkingAccount 
		System.out.println(checkingAccount.toString());

		//test the CheckingAccount
		checkingAccount.earnInterest(); //this does nothing

		//print out the statement
		checkingAccount.printStatement();
		System.out.println("There should be " + TEST_NEW_MONEY_AMOUNT + " balance");

		//print out the type of account - moneyMarket
		System.out.println(moneyMarket.toString());

		System.out.println();
		//test the moneyMarket account
		//print statement for a basis
		moneyMarket.printStatement();

		//earn interest on the account
		moneyMarket.earnInterest();

		//print the statement to see the interest gained
		moneyMarket.printStatement();

		//try to withdraw more money then is in the account - minimum
		//get whats in the account
		int current = moneyMarket.getCurrentBalance();
		//subtract the minimum from that
		int max = current - MoneyMarket.MINIMUM_BALANCE;
		//withdraw more then the max - should produce an error
		moneyMarket.withdrawMoney(max + AMOUNT_MODIFIER);

		//now withdraw less then the max
		moneyMarket.withdrawMoney(max - AMOUNT_MODIFIER);

		//print out the statement
		moneyMarket.printStatement();

		//print out the type of account - savingsAccount
		System.out.println(savingsAccount.toString());

		//print out the balance for a basis
		savingsAccount.printStatement();

		//earn interest on the account
		savingsAccount.earnInterest();

		//print out the balance to see the interest gained
		savingsAccount.printStatement();

		//try removing some money - overdraft
		savingsAccount.withdrawMoney(savingsAccount.getCurrentBalance() + AMOUNT_MODIFIER);

		//there should now be no money left in the account
		savingsAccount.printStatement();

	}
}