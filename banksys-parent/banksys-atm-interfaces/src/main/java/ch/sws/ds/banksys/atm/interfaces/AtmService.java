/**
 * 
 */
package ch.sws.ds.banksys.atm.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * @author feuzl1
 * 
 */
public interface AtmService extends Remote {

	/** RMI Service name */
	public static final String ATM_NAME = "ATM";

	/**
	 * Prüft ob die IBAN in dieser Bank registriert ist (CountryCode und
	 * Clearing) und ob ein aktives Konto mit der gegebenen Nummer vorahanden
	 * ist.
	 * 
	 * @param iban
	 *            die iban des kontos
	 * @return true wenn gültig
	 * @throws RemoteException
	 */
	public boolean isValidAccount(IBAN iban) throws RemoteException;

	/**
	 * Prüft ob dieses Konto bei dieser Bank registriert ist.
	 * 
	 * @param iban
	 *            iban des Kontos
	 * @return true wenn konto (und kunde) bei dieser bank registriert.
	 * @throws RemoteException
	 */
	public boolean isOwnCustomer(IBAN iban) throws RemoteException;

	/**
	 * Gibt den Kontostand dieses Kontos zürück.
	 * 
	 * @param iban
	 *            iban des Kontos
	 * @return Kontostand
	 * @throws RemoteException
	 * @throws InvalidAccountException
	 */
	public Money getBalance(IBAN iban) throws InvalidAccountException,
			RemoteException;

	/**
	 * Hebt geld von diesem Konto ab (und transferiert dieses auf das
	 * cash-Konto).
	 * 
	 * @param credit
	 *            Bezugskonto
	 * @param amount
	 *            Betrag
	 * @param text
	 *            Buchungstext
	 * @return Transaktionsnummer
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 * @throws UnknownClearingException
	 * @throws RemoteException
	 */
	public Integer withdrawMoney(IBAN credit, Money amount, String text)
			throws AmountNotSufficientException, InvalidAccountException,
			UnknownClearingException, RemoteException;

	/**
	 * Gibt die Clearingnummer der Bank zurück
	 * 
	 * @return clearingnummer
	 * @throws RemoteException
	 */
	public int getClearingNbr() throws RemoteException;

	/**
	 * Gibt den Countrycode der Bank zurück.
	 * 
	 * @return Countrycode
	 * @throws RemoteException
	 */
	public String getCountryCode() throws RemoteException;

	/**
	 * Püft die clearing nummber gegen die bankliste der clearingstelle.
	 * 
	 * @param clearing
	 * @return
	 * @throws RemoteException
	 */
	public boolean isValidClearingNbr(Integer clearing) throws RemoteException;

}
