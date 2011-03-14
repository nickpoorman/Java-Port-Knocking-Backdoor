package tes;

/*
 * CheckingAccount.java
 * 
 * Version: 
 *     $Id: CheckingAccount.java,v 1.2 2010/09/18 00:04:09 nep7569 Exp $
 * 
 * Revisions: 
 *     $Log: CheckingAccount.java,v $
 *     Revision 1.2  2010/09/18 00:04:09  nep7569
 *     untested
 *
 *     Revision 1.1  2003/03/17 14:37:28  cs2
 *     Initial revision
 *
 */

/**
 * The CheckingAccount class represents a checking account.
 * 
 * @author Lois Rixner
 * @author Paul Tymann
 */

public class CheckingAccount extends BankAccount {

	/**
	 * A constructor for a CheckingAccount object. It accepts the amount of
	 * money deposited when the account is created.
	 * 
	 * @param newMoney
	 *            The amount of money deposited when the account is opened.
	 */

	public CheckingAccount(int newMoney) {
		super(newMoney);
	}

	/**
	 * Earn interest on this account. Checking accounts do not earn interest.
	 */

	public void earnInterest() {
		//stub
	}

	/**
	 * What kind of account am I?
	 * 
	 * @return "CheckingAccount"
	 */

	public String toString() {
		return "CheckingAccount";
	}

} // CheckingAccount
