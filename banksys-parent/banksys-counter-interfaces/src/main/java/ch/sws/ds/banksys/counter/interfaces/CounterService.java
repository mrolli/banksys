/**
 * 
 */
package ch.sws.ds.banksys.counter.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.exceptions.AccountNotEmptyException;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * @author feuzl1
 * 
 */
public interface CounterService extends Remote {

	/** RMI Service name */
	public static final String COUNTER_NAME = "Counter";

	/**
	 * Gibt den Kunden für das gegebene Konto zurück.
	 * 
	 * @param iban
	 *            iban des kontos
	 * @return Kunde
	 * @throws InvalidAccountException
	 * @throws InvalidCustomerException
	 * @throws RemoteException
	 */
	public Customer getCustomer(IBAN iban) throws InvalidAccountException,
			InvalidCustomerException, RemoteException;

	/**
	 * Erstellt einen neuen Kunden.
	 * 
	 * @param name
	 *            Name
	 * @param address
	 *            Addresse
	 * @param pin
	 *            pin
	 * @return Kundennummer
	 * @throws RemoteException
	 */
	public Integer createCustomer(Name name, Address address, int pin)
			throws RemoteException;

	/**
	 * Eröffnet einen neuen Account für den gegebenen kunden.
	 * 
	 * @param customerNumber
	 *            Kundennummer
	 * @param pin
	 *            pin des kunden
	 * @param description
	 *            beschreibung für das konto
	 * @return Accountnummer
	 * @throws InvalidCustomerException
	 * @throws RemoteException
	 */
	public Integer createAccount(Integer customerNumber, int pin,
			String description) throws InvalidCustomerException,
			RemoteException;

	/**
	 * Löscht das Konto.
	 * 
	 * @param accountNbr
	 *            account nummer
	 * @throws AccountNotEmptyException
	 * @throws InvalidAccountException
	 * @throws RemoteException
	 */
	public void deleteAccount(Integer accountNbr)
			throws AccountNotEmptyException, InvalidAccountException,
			RemoteException;

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
	 * @throws InvalidAccountException
	 * @throws RemoteException
	 */
	public Money getBalance(IBAN iban) throws InvalidAccountException,
			RemoteException;

	/**
	 * Geld auf dieses Konto einzahlen (transfer vom cash-Konto).
	 * 
	 * @param debit
	 *            Gutschriftkonto
	 * @param amount
	 *            Betrag
	 * @param text
	 *            Buchungstext
	 * @return Transaktionsnummer
	 * @throws InvalidAccountException
	 * @throws {@link UnknownClearingException}
	 * @throws RemoteException
	 */
	public Integer depositMoney(IBAN debit, Money amount, String text)
			throws InvalidAccountException, UnknownClearingException,
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
