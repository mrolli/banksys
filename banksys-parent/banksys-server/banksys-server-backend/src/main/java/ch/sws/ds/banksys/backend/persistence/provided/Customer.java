package ch.sws.ds.banksys.backend.persistence.provided;

import java.io.Serializable;

/**
 * The class Customer contains the data of a bank customer.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
@SuppressWarnings("serial")
public class Customer implements Serializable {

	public enum State {
		ACTIVE, INACTIVE
	}

	private Integer nr;
	private String name;
	private String address;
	private String pin;
	private State state;

	public Customer() {
	}

	public Customer(Integer nr, String name, String address, String pin,
			State state) {
		this.nr = nr;
		this.name = name;
		this.address = address;
		this.pin = pin;
		this.state = state;
	}

	public Integer getNr() {
		return nr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String toString() {
		return nr + ":" + name + ":" + address + ":" + pin + ":" + state;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Customer))
			return false;
		Customer other = (Customer) object;
		return nr.equals(other.nr) && name.equals(other.name)
				&& address.equals(other.address) && pin.equals(other.pin)
				&& state.equals(other.state);
	}
}
