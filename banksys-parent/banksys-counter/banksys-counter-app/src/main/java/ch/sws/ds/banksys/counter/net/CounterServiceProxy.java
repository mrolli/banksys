package ch.sws.ds.banksys.counter.net;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.utils.Settings;
import ch.sws.ds.banksys.common.exceptions.AccountNotEmptyException;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;
import ch.sws.ds.banksys.counter.interfaces.CounterService;

/**
 * @author kambl1
 * 
 * Proxy für die Schnittstelle zum Backend.
 */
public class CounterServiceProxy {
	private static final String ERROR_MESSAGE = "This action is not available because of technical issues!";
	
	private static CounterServiceProxy instance;
	private CounterService counterService;

	/**
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private CounterServiceProxy() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(Settings.getRmiHost(), Settings.getRmiPort());
		counterService = (CounterService)registry.lookup(CounterService.COUNTER_NAME);
	}

	/**
	 * Instanz des Service Proxys anfordern.
	 * @return Instanz des Service Proxys
	 * @throws CommunicationException
	 */
	public static synchronized CounterServiceProxy getInstance() throws CommunicationException {
		if (instance == null) {
			try {
				instance = new CounterServiceProxy();
			} catch (Exception e) {
				throw new CommunicationException(ERROR_MESSAGE);
			}
		}
		return instance;
	}

	/**
	 * CountryCode der eigenen Bank abfragen.
	 * @return CountryCode der eigenen Bank
	 * @throws CommunicationException
	 */
	public String getCountryCode() throws CommunicationException {
		try {
			return counterService.getCountryCode();
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * ClearingNumber der eigenen Bank abfragen.
	 * @return ClearingNumber der eigenen Bank
	 * @throws CommunicationException
	 */
	public int getClearingNbr() throws CommunicationException {
		try {
			return counterService.getClearingNbr();
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Gibt
	 * @param iban IBAN des Kundenkontos
	 * @return Kunde zur angegebenen IBAN
	 * @throws CommunicationException
	 * @throws InvalidCustomerException
	 * @throws InvalidAccountException
	 */
	public Customer getCustomer(IBAN iban) throws CommunicationException, InvalidCustomerException, InvalidAccountException {
		try {
			return counterService.getCustomer(iban);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Erstellt einen neuen Kunden.
	 * @param name Name des Kunden
	 * @param address Addresse des Kunden
	 * @param pin PIN des Kunden
	 * @return Kundennummer
	 * @throws CommunicationException
	 */
	public Integer createCustomer(Name name, Address address, int pin) throws CommunicationException {
		try {
			return counterService.createCustomer(name, address, pin);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Erstellt ein neues Bankkonto für den angegebenen Kunden.
	 * @param customerNumber Kundennummer
	 * @param pin PIN des Kunden
	 * @param description Beschreibung des Bankkontos
	 * @return Kontonummer
	 * @throws CommunicationException
	 * @throws InvalidCustomerException
	 */
	public Integer createAccount(Integer customerNumber, int pin, String description) throws CommunicationException, InvalidCustomerException {
		try {
			return counterService.createAccount(customerNumber, pin, description);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Löscht das angegebene Bankkonto.
	 * @param accountNumber Kontonummer
	 * @throws CommunicationException
	 * @throws AccountNotEmptyException
	 * @throws InvalidAccountException 
	 */
	public void deleteAccount(int accountNumber) throws CommunicationException, AccountNotEmptyException, InvalidAccountException {
		try {
			counterService.deleteAccount(Integer.valueOf(accountNumber));
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * ÜberprÜft ob das angegebene Bankkonto gÜltig ist.
	 * @param iban IBAN des zu validierenden Bankkontos.
	 * @return True falls gültiges Konto, sonst false.
	 * @throws CommunicationException
	 */
	public boolean isValidAccount(IBAN iban) throws CommunicationException {
		try {
			return counterService.isValidAccount(iban);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Überprüft ob die angegebene ClearingNumber gültig ist.
	 * @param clearingNumber Zu prüfende ClearingNumber.
	 * @return True falls gültige ClearingNumber, sonst false.
	 * @throws CommunicationException
	 */
	public boolean isValidClearingNumber(Integer clearingNumber) throws CommunicationException {
		try {
			return counterService.isValidClearingNbr(clearingNumber);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * ÜerprÜft ob das angegebene Bankkonto zur eigenen Bank gehört.
	 * @param iban IBAN des zu prüfenden Bankkontos.
	 * @return True falls eigener Kunde, sonst false.
	 * @throws CommunicationException
	 */
	public boolean isOwnCustomer(IBAN iban) throws CommunicationException {
		try {
			return counterService.isOwnCustomer(iban);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Aktueller Kontostand eines Bankkontos abfragen.
	 * @param iban IBAN des gewünschten Kontos.
	 * @return Kontostand
	 * @throws CommunicationException
	 * @throws InvalidAccountException
	 */
	public Money getBalance(IBAN iban) throws CommunicationException, InvalidAccountException {
		try {
			return counterService.getBalance(iban);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}
	
	/**
	 * Einzahlen eines bestimmten Betrags auf das Bankkonto.
	 * @param debit Zu begünstigendes Bankkonto
	 * @param amount Betrag
	 * @param text Buchungstext
	 * @return Transaktionsnummer
	 * @throws CommunicationException
	 * @throws UnknownClearingException 
	 * @throws InvalidAccountException 
	 */
	public Integer depositMoney(IBAN debit, Money amount, String text) throws CommunicationException, InvalidAccountException, UnknownClearingException {
		try {
			return counterService.depositMoney(debit, amount, text);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}
	
	/**
	 * Abheben eines bestimmten Betrags vom Bankkonto.
	 * @param credit Zu belastendes Bankkonto
	 * @param amount Betrag
	 * @param text Buchungstext
	 * @return Transaktionsnummer
	 * @throws CommunicationException
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException 
	 * @throws UnknownClearingException 
	 */
	public Integer withdrawMoney(IBAN credit, Money amount, String text) throws CommunicationException, AmountNotSufficientException, InvalidAccountException, UnknownClearingException {
		try {
			return counterService.withdrawMoney(credit, amount, text);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}
}
