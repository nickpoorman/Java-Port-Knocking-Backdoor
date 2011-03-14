package tes;

/*
 * SavingsAccount.java
 * 
 * Version: 
 *     $Id: SavingsAccount.java,v 1.2 2010/09/18 00:04:09 nep7569 Exp $
 * 
 * Revisions: 
 *     $Log: SavingsAccount.java,v $
 *     Revision 1.2  2010/09/18 00:04:09  nep7569
 *     untested
 *
 *     Revision 1.1  2003/03/17 14:37:28  cs2
 *     Initial revision
 *
 */

/**
 * The SavingsAccount class represents a generic bank account.
 * 
 * @author Lois Rixner
 * @author Paul Tymann
 */

public class SavingsAccount extends BankAccount {

	/**
	 * The interest rate for a standard savings account is currently 3%.
	 */
	public static final int SAVINGS_INTEREST_RATE = 3;

	/**
	 * A constructor for a SavingsAccount object. It accepts the amount of money
	 * deposited when the account is created and sets the interest rate for the
	 * account to be the current rate for savings accounts.
	 * 
	 * @param newMoney
	 *            The amount of money deposited when the account is opened.
	 */

	public SavingsAccount(int newMoney) {
		super(newMoney);
		this.setInterestRate(SAVINGS_INTEREST_RATE);
	}

	/**
	 * Withdraw money from this account. If the amount wanted is more than the
	 * amount in the account, empty the account.
	 * 
	 * @param amountWanted
	 *            The amount of money to withdraw
	 */

	public void withdrawMoney(int amountWanted) {
		if (amountWanted > this.getCurrentBalance()) super.withdrawMoney(this.getCurrentBalance());
	}

	/**
	 * Earn interest on this account. Savings accounts earn interest on the
	 * entire current balance. Interest earned is considered to be a deposit.
	 */

	public void earnInterest() {
		int interest = this.getCurrentBalance() * (this.getInterestRate() / 100);

		//deposit the interest
		this.depositMoney(Math.round(interest));
		//new month
		this.newMonth();
	}

	/**
	 * What kind of account am I?
	 * 
	 * @return "SavingsAccount"
	 */

	public String toString() {
		return "SavingsAccount";
	}
} // SavingsAccount
