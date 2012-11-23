/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author feuzl1
 * 
 */
public class Customer implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 4887170439100668215L;

	private Integer number;

	private int pin;

	private Name name;

	private Address address;

	private List<Account> accounts;

	private CustomerState state;

	/**
	 * Default constructor.
	 */
	public Customer() {
		// nothing..
	}

	/**
	 * @param number
	 * @param pin
	 * @param name
	 * @param address
	 */
	public Customer(Integer number, int pin, Name name, Address address,
			CustomerState state) {
		super();
		this.number = number;
		this.pin = pin;
		this.name = name;
		this.address = address;
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
	 * @return the pin
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * @param pin
	 *            the pin to set
	 */
	public void setPin(int pin) {
		this.pin = pin;
	}

	/**
	 * @return the name
	 */
	public Name getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(Name name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the state
	 */
	public CustomerState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(CustomerState state) {
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [number=" + number + ", pin=" + pin + ", name=" + name
				+ ", address=" + address + ", accounts=" + accounts
				+ ", state=" + state + "]";
	}

}
