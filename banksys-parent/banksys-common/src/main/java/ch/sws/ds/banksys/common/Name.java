/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;

/**
 * @author feuzl1
 * 
 */
public class Name implements Serializable {

	/** UID. */
	private static final long serialVersionUID = -6241585894353143942L;
	private String lastName;
	private String firstName;

	public Name() {
		// nothing..
	}

	public Name(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return firstName + lastName;
	}

}
