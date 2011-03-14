package tes;

/*
 * MoneyMarket.java
 * 
 * Version: 
 *     $Id: MoneyMarket.java,v 1.3 2010/09/18 00:07:59 nep7569 Exp nep7569 $
 * 
 * Revisions: 
 *     $Log: MoneyMarket.java,v $
 *     Revision 1.3  2010/09/18 00:07:59  nep7569
 *     compiled
 *
 *     Revision 1.2  2010/09/18 00:04:09  nep7569
 *     untested
 *
 *     Revision 1.1  2003/03/17 14:37:28  cs2
 *     Initial revision
 *
 */

/**
 * The MoneyMarket class represents a money market account.
 * 
 * @author Lois Rixner
 * @author Paul Tymann
 * @author Nick Poorman
 */

public class MoneyMarket extends BankAccount {

	/**
	 * The minimum amount that must be kept in the account at all times;
	 * interest is earned only on the amount over the minimum - currently $1000.
	 */
	public static final int MINIMUM_BALANCE = 1000;

	/**
	 * The interest rate for balances greater than the minimum - currently 5%.
	 */
	public static final int INTEREST_RATE = 5;

	/**
	 * A constructor for a MoneyMarket object. It accepts the amount of money
	 * deposited when the account is created. You are guaranteed that the
	 * parameter will always be greater than the minimum amount required.
	 * 
	 * @param newMoney
	 *            The amount of money deposited when the account is opened.
	 */

	public MoneyMarket(int newMoney) {
		super(newMoney);
		this.setInterestRate(INTEREST_RATE);
	}

	/**
	 * Money can only be withdrawn from this account if the minimum will remain.
	 * If the amount wanted will make the account go below the minimum, print
	 * the following message to standard error "Insufficient funds"
	 * 
	 * @param amountWanted
	 *            The amount of money to withdraw
	 */

	public void withdrawMoney(int amountWanted) {
		if (amountWanted > (this.getCurrentBalance() - MINIMUM_BALANCE)) {
			System.err.println("Insufficient funds");
			return;
		}

		super.withdrawMoney(amountWanted);
	}

	/**
	 * Earn interest on this account. With money market accounts no interest is
	 * earned on the minimum that must always be kept in the account. Interest
	 * is only earned on everything over that amount. Interest earned is
	 * considered to be a deposit.
	 */

	public void earnInterest() {
		int interestable = this.getCurrentBalance() - MINIMUM_BALANCE;
		if (interestable > 0) {
			this.depositMoney(Math.round(this.getCurrentBalance() * (INTEREST_RATE / 100)));
		}
		this.newMonth();
	}

	/**
	 * What kind of account am I?
	 * 
	 * @return "MoneyMarket"
	 */

	public String toString() {
		return "MoneyMarket";
	}
} // MoneyMarket