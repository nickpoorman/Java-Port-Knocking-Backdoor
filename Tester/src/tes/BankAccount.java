package tes;

/*
 * BankAccount.java
 * 
 * Version: 
 *     $Id: BankAccount.java,v 1.1 2003/03/17 14:37:28 cs2 Exp $
 * 
 * Revisions: 
 *     $Log: BankAccount.java,v $
 *     Revision 1.1  2003/03/17 14:37:28  cs2
 *     Initial revision
 *
 */

/**
 * The BankAccount class represents a generic bank account. This BankAccount
 * deviates from the bank account that you may actually have in that the amount
 * it contains is always an integer value. When interest is computed, the amount
 * is converted to an integer by rounding up for all values greater or equal to
 * .5 and rounding down for values less than .5
 * 
 * @author Lois Rixner
 * @author Paul Tymann
 */

public abstract class BankAccount {

	/**
	 * the balance at the beginning of this month
	 */
	private int initialBalance;

	/**
	 * the total amount of money deposited this month
	 */
	private int totalDeposits;

	/**
	 * the total amount of money withdrawn this month
	 */
	private int totalWithdrawn;

	/**
	 * the actual amount of interest earned this month
	 */
	private int interestEarned;

	/**
	 * the interest rate on this account.
	 */
	private int interestRate;

	/**
	 * the current balance on this account
	 */
	private int currentBalance;

	/**
	 * A constructor for a BankAccount object. It accepts the amount of money
	 * deposited when the account is created.
	 * 
	 * @param newMoney
	 *            The amount of money deposited when the account is opened.
	 */

	public BankAccount(int newMoney) {
		initialBalance = newMoney;
		currentBalance = newMoney;
		totalWithdrawn = 0;
		totalDeposits = newMoney;
		interestRate = 0;
	}

	/**
	 * Withdraw money from this account.
	 * 
	 * @param amountWanted
	 *            The amount of money to withdraw
	 */

	public void withdrawMoney(int amountWanted) {
		if (amountWanted <= currentBalance) {
			currentBalance = currentBalance - amountWanted;
			totalWithdrawn += amountWanted;
		}
	}

	/**
	 * Deposit money into this account
	 * 
	 * @param newMoney
	 *            The amount to deposit
	 */

	public void depositMoney(int newMoney) {
		currentBalance += newMoney;
		totalDeposits += newMoney;
	}

	/**
	 * Earn interest on this account. The rules for earning interest are
	 * different for every kind of account. Interest earned is considered to be
	 * a deposit.
	 */

	public abstract void earnInterest();

	/**
	 * Print a statement for this month.
	 */

	public void printStatement() {
		System.out.println("Initial Balance: " + initialBalance);
		System.out.println("Interest Earned: " + interestEarned);
		System.out.println("Total Deposits: " + totalDeposits);
		System.out.println("Total Withdrawn: " + totalWithdrawn);
		System.out.println("Current Balance: " + currentBalance);
		System.out.println();
	}

	/**
	 * Clear the totals and set the initial balance to the current balance to
	 * indicate the start of a new month.
	 */

	public void newMonth() {
		totalDeposits = 0;
		totalWithdrawn = 0;
		interestEarned = 0;
		initialBalance = currentBalance;
	}

	/**
	 * Return the total amount of money withdrawn during this month.
	 * 
	 * @return the total amount withdrawn since the start of the month.
	 */

	public int getTotalWithdrawn() {
		return totalWithdrawn;
	}

	/**
	 * Return the total amount of money deposited this month.
	 * 
	 * @return the total amount of money deposited this month
	 */

	public int getTotalDeposits() {
		return totalDeposits;
	}

	/**
	 * Return the current balance in the account
	 * 
	 * @return the current balance in the account.
	 */

	public int getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * Return the current interest rate
	 * 
	 * @return the current interest rate.
	 */

	public int getInterestRate() {
		return interestRate;
	}

	/**
	 * Change the interest rate
	 * 
	 * @param newRate
	 *            the new interest rate
	 */

	public void setInterestRate(int newRate) {
		interestRate = newRate;
	}

	/**
	 * Set the amount of interest earned. Since interest can only be earned once
	 * during each month, a running total is not kept.
	 * 
	 * @param the
	 *            total amount of interest earned.
	 */

	public void setInterestEarned(int newInterestEarned) {
		interestEarned = newInterestEarned;
	}

	/**
	 * What kind of account am I?
	 * 
	 * @return "GenericAccount"
	 */

	public String toString() {
		return "GenericAccount";
	}
} // BankAccount