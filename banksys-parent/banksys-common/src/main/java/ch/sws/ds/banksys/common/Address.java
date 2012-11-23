/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;

/**
 * @author feuzl1
 * 
 */
public class Address implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 1L;

	private String streetName;

	private int houseNumber;

	private String city;

	private int postalCode;

	private String country;

	/**
	 * Defualt Constructor.
	 */
	public Address() {
		// nothing..
	}

	/**
	 * Constructor.
	 * 
	 * @param streetName
	 * @param houseNumber
	 * @param city
	 * @param postalCode
	 * @param country
	 */
	public Address(String streetName, int houseNumber, String city,
			int postalCode, String country) {
		super();
		this.streetName = streetName;
		this.houseNumber = houseNumber;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * @param streetName
	 *            the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	/**
	 * @return the houseNumber
	 */
	public int getHouseNumber() {
		return houseNumber;
	}

	/**
	 * @param houseNumber
	 *            the houseNumber to set
	 */
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	public int getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return streetName + " " + houseNumber + ", " + postalCode + " " + city
				+ ", " + country;
	}
}
