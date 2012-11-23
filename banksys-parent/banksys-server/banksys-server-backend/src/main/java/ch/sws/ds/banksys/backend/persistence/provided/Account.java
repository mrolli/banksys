package ch.sws.ds.banksys.backend.persistence.provided;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The class Account contains the data of a bank account.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
@SuppressWarnings("serial")
public class Account implements Serializable {

	public enum State {
		OPEN, CLOSED
	}

	private Integer nr;
	private Integer customerNr;
	private String description;
	private BigDecimal balance;
	private State state;

	public Account() {
	}

	public Account(Integer nr, Integer customerNr, String description,
			BigDecimal balance, State state) {
		this.nr = nr;
		this.customerNr = customerNr;
		this.description = description;
		this.balance = balance;
		this.state = state;
	}

	public Integer getNr() {
		return nr;
	}

	public Integer getCustomerNr() {
		return customerNr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String toString() {
		return nr + ":" + description + ":" + balance + ":" + state;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Account))
			return false;
		Account other = (Account) object;
		return nr.equals(other.nr) && description.equals(other.description)
				&& balance.compareTo(other.balance) == 0
				&& state.equals(other.state);
	}
}
