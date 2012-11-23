package ch.sws.ds.banksys.ebanking.weblogic;

/**
 * Enumeration der bekannten Actions.
 * 
 * Enumeration der bekannten Actions, die aufgrufen werden können.
 * 
 * @author mrolli
 */
public enum Action {
	/**
	 * Login screen.
	 */
	LOGIN,
	/**
	 * Logout screen
	 */
	LOGOUT,
	/**
	 * Kontoübersicht.
	 */
	ACCOUNTOVERVIEW,
	/**
	 * Kontoauszug.
	 */
	ACCOUNTSTATEMENT,
	/**
	 * Zahlung erfassen.
	 */
	PAYMENTTRANSACTION,
	/**
	 * Kontoübertrag ausführen
	 */
	PAYMENTTRANSFER,
}
