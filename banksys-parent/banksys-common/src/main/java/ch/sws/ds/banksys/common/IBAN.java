/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;

/**
 * @author feuzl1
 * 
 */
public class IBAN implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 3450020067081916990L;

	private static final String SEPARATOR = "-";

	private final String countryCode;

	private final int clearingNumber;

	private final int accountNumber;

	/**
	 * @param countryCode
	 * @param clearingNumber
	 * @param accountNumber
	 */
	public IBAN(String countryCode, int clearingNumber, int accountNumber) {
		super();
		this.countryCode = countryCode;
		this.clearingNumber = clearingNumber;
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @return the clearingNumber
	 */
	public int getClearingNumber() {
		return clearingNumber;
	}

	/**
	 * @return the accountNumber
	 */
	public int getAccountNumber() {
		return accountNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		result = prime * result + clearingNumber;
		result = prime * result
				+ ((countryCode == null) ? 0 : countryCode.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IBAN other = (IBAN) obj;
		if (accountNumber != other.accountNumber)
			return false;
		if (clearingNumber != other.clearingNumber)
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equalsIgnoreCase(other.countryCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return countryCode + SEPARATOR + clearingNumber + SEPARATOR
				+ accountNumber;
	}
}
