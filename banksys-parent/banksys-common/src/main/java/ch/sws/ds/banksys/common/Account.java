/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;

/**
 * @author feuzl1
 * 
 */
public class Account implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 4720183603444170820L;

	private Integer number;

	private Money balance;

	private String description;

	private Customer customer;

	private Integer customerNbr;

	private AccountState state;

	/**
	 * Default constructor.
	 */
	public Account() {
		// nothing..
	}

	/**
	 * @param number
	 * @param balance
	 * @param description
	 */
	public Account(Integer number, Integer customerNumber, Money balance,
			String description, AccountState state) {
		super();
		this.number = number;
		this.balance = balance;
		this.customerNbr = customerNumber;
		this.description = description;
		this.state = state;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the balance
	 */
	public Money getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(Money balance) {
		this.balance = balance;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the state
	 */
	public AccountState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(AccountState state) {
		this.state = state;
	}

	/**
	 * @return the customerNbr
	 */
	public Integer getCustomerNbr() {
		return customerNbr;
	}

	/**
	 * @param customerNbr
	 *            the customerNbr to set
	 */
	public void setCustomerNbr(Integer customerNbr) {
		this.customerNbr = customerNbr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [number=" + number + ", balance=" + balance
				+ ", description=" + description + ", customer=" + customer
				+ ", customerNbr=" + customerNbr + ", state=" + state + "]";
	}

}
