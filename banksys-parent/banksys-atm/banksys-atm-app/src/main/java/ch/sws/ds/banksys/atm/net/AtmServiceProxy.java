package ch.sws.ds.banksys.atm.net;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ch.sws.ds.banksys.atm.interfaces.AtmService;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.utils.Settings;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * @author kambl1
 * 
 * Proxy für die Schnittstelle zum Backend.
 */
public class AtmServiceProxy {
	private static final String ERROR_MESSAGE = "This action is not available because of technical issues!";
	
	private static AtmServiceProxy instance;
	private AtmService atmService;

	/**
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private AtmServiceProxy() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(Settings.getRmiHost(), Settings.getRmiPort());
		atmService = (AtmService)registry.lookup(AtmService.ATM_NAME);
	}

	/**
	 * Instanz des Service Proxys anfordern.
	 * @return Instanz des Service Proxys
	 * @throws CommunicationException
	 */
	public static synchronized AtmServiceProxy getInstance() throws CommunicationException {
		if (instance == null) {
			try {
				instance = new AtmServiceProxy();
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
			return atmService.getCountryCode();
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
			return atmService.getClearingNbr();
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Überprüft ob das angegebene Bankkonto gültig ist.
	 * @param iban IBAN des zu validierenden Bankkontos.
	 * @return True falls gültiges Konto, sonst false.
	 * @throws CommunicationException
	 */
	public boolean isValidAccount(IBAN iban) throws CommunicationException {
		try {
			return atmService.isValidAccount(iban);
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
			return atmService.isValidClearingNbr(clearingNumber);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * ÜberprÜft ob das angegebene Bankkonto zur eigenen Bank geHÖrt.
	 * @param iban IBAN des zu prÜfenden Bankkontos.
	 * @return True falls eigener Kunde, sonst false.
	 * @throws CommunicationException
	 */
	public boolean isOwnCustomer(IBAN iban) throws CommunicationException {
		try {
			return atmService.isOwnCustomer(iban);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}

	/**
	 * Aktueller Kontostand eines Bankkontos abfragen.
	 * @param iban IBAN des gewÜnschten Kontos.
	 * @return Kontostand
	 * @throws CommunicationException
	 * @throws InvalidAccountException
	 */
	public Money getBalance(IBAN iban) throws CommunicationException, InvalidAccountException {
		try {
			return atmService.getBalance(iban);
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
			return atmService.withdrawMoney(credit, amount, text);
		} catch (RemoteException e) {
			throw new CommunicationException(ERROR_MESSAGE);
		}
	}
}
