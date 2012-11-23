package ch.sws.ds.banksys.common.console;

import ch.sws.ds.banksys.common.IBAN;

/**
 * @author kambl1
 *
 * Die Session speichert Informationen zu dem eingeloggten Bankkonto.
 */
public class Session {
	private static Session instance = new Session();
	
	private IBAN iban;
	private boolean isOwnCustomer;
	
	private Session() {
		iban = null;
		isOwnCustomer = false;
	}
	
	/**
	 * Instanz der Session anfordern.
	 * @return Instanz der Session
	 */
	public static Session getInstance() {
		return instance;
	}
	
	/**
	 * IBAN des eingeloggten Bankkontos setzen.
	 * @param iban IBAN des eingeloggten Bankkontos
	 */
	public void setIBAN(IBAN iban) {
		this.iban = iban;
	}
	
	/**
	 * IBAN des eingeloggten Bankkontos abfragen.
	 * @return IBAN des eingeloggten Bankkontos
	 */
	public IBAN getIBAN() {
		return iban;
	}
	
	/**
	 * Prüfen ob der Kunde (sein Bankkonto) zur eigenen Bank gehört.
	 * @return True wenn Kunde zu eigener Bank gehört, sonst false
	 */
	public boolean isOwnCustomer() {
		return isOwnCustomer;
	}

	/**
	 * Kunde (sein Bankkonto) als zur eigenen Bank gehörend definieren.
	 * @param isOwnCustomer True wenn Kunde zu eigener Bank gehört, sonst false
	 */
	public void setIsOwnCustomer(boolean isOwnCustomer) {
		this.isOwnCustomer = isOwnCustomer;
	}
	
	/**
	 * Invalidiert die Session Daten zurück.
	 */
	public void clearSession() {
		isOwnCustomer = false;
		iban = null;
	}
}
