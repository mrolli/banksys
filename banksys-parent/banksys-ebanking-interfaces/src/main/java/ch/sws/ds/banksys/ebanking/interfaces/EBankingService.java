/**
 * 
 */
package ch.sws.ds.banksys.ebanking.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * Remote Interface für die EBanking Applikation.
 * 
 * @author feuzl1
 */
public interface EBankingService extends Remote {

	/** RMI Service name */
	public static final String EBANKING_NAME = "EBanking";

	/**
	 * Prüft ob der kunde vorhanden ist und ob der pin korrekt ist.
	 * 
	 * @param customerNumber
	 *            Kundennummer
	 * @param pin
	 *            pin
	 * @return true wenn beides korrekt
	 * @throws RemoteException
	 * @throws InvalidCustomerException
	 */
	public boolean isValidCustomer(Integer customerNumber, int pin)
			throws InvalidCustomerException, RemoteException;

	/**
	 * Gibt den Kunden für das gegebene Konto zurück.
	 * 
	 * @param customerNumber
	 *            kundennummer
	 * @return Kunde
	 * @throws InvalidCustomerException
	 * @throws RemoteException
	 * @throws InvalidCustomerException
	 */
	public Customer getCustomer(Integer customerNumber)
			throws InvalidCustomerException, RemoteException;

	/**
	 * Liefert eine Liste der Konti dieses Kunden.
	 * 
	 * @param customerNumber
	 *            kundennummer
	 * @return Kontoliste
	 * @throws InvalidCustomerException
	 * @throws RemoteException
	 */
	public List<Account> getAccounts(Integer customerNumber)
			throws RemoteException;

	/**
	 * Liefert eine Liste der {@link MoneyTransfer}s dieses Kontos.
	 * 
	 * @param accountNumber
	 *            Kontonummer
	 * @return Liste der {@link MoneyTransfer}
	 * @throws InvalidAccountException
	 * @throws RemoteException
	 */
	public List<MoneyTransfer> getMoneyTransfers(Integer accountNumber)
			throws InvalidAccountException, RemoteException;

	/**
	 * Führt einen MoneyTransfer aus.
	 * 
	 * @param moneyTransfer
	 *            {@link MoneyTransfer}
	 * @return erfolgreich
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 * @throws UnknownClearingException
	 * @throws RemoteException
	 */
	public boolean executeMoneyTransfer(MoneyTransfer moneyTransfer)
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
}
