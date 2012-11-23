/**
 * 
 */
package ch.sws.ds.banksys.backend.business;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.backend.persistence.AccountDao;
import ch.sws.ds.banksys.backend.persistence.CustomerDao;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.CustomerState;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;

/**
 * @author feuzl1
 * 
 */
public class CustomerManager {

	private static CustomerManager instance;

	private CustomerDao customerDao;
	private AccountDao accountDao;

	private static Logger logger = Logger.getLogger(CustomerManager.class);

	private CustomerManager() {
		customerDao = CustomerDao.getInstance();
		accountDao = AccountDao.getInstance();
	}

	public static synchronized CustomerManager getInstance() {
		if (instance == null) {
			instance = new CustomerManager();
		}
		return instance;
	}

	/**
	 * Liefert den Kunden zum entsprechenden Konto.
	 * 
	 * @param iban
	 *            iban
	 * @return kunde
	 * @throws InvalidAccountException
	 * @throws InvalidCustomerException
	 */
	public Customer getCustomer(IBAN iban) throws InvalidCustomerException,
			InvalidAccountException {
		Account account = accountDao.getAccount(iban);
		if (account == null)
			throw new InvalidAccountException(String.format(
					"Invalid account number '%d'", iban.getAccountNumber()));
		Customer customer = customerDao.getByNr(account.getCustomerNbr());
		if (customer == null)
			throw new InvalidCustomerException(String.format(
					"Customer not found for account number '%d'",
					iban.getAccountNumber()));
		return customer;
	}

	/**
	 * Liefert den Kunden anhand der Kundennummer.
	 * 
	 * @param customerNumber
	 *            Kundennummer
	 * @return Kunde
	 * @throws InvalidCustomerException
	 */
	public Customer getCustomer(Integer customerNumber)
			throws InvalidCustomerException {
		Customer customer = customerDao.getByNr(customerNumber);
		if (customer == null)
			throw new InvalidCustomerException(String.format(
					"Customer not found '%d'", customerNumber));
		return customer;
	}

	/**
	 * Erstellt einen neuen Kunden.
	 * 
	 * @param name
	 *            name
	 * @param address
	 *            adresse
	 * @param pin
	 *            pin
	 * @return Persistenter kunde
	 */
	public Customer createCustomer(Name name, Address address, int pin) {
		Customer customer = customerDao.save(new Customer(null, pin, name,
				address, CustomerState.ACTIVE));
		if (customer == null)
			throw new RuntimeException("Cannot create customer!");
		logger.debug("Created customer: " + customer.toString());
		return customer;
	}

	/**
	 * Prüft ob es sich um ein Konto dieser bank handelt.
	 * 
	 * @param iban
	 *            iban
	 * @return true wenn es ein konto dieser bank ist
	 */
	public boolean isOwnCustomer(IBAN iban) {
		try {
			getCustomer(iban);
		} catch (InvalidCustomerException | InvalidAccountException e) {
			return false;
		}
		return true;
	}

	/**
	 * Prüft ob Kontonummer und pin korrekt sind.
	 * 
	 * @param customerNumber
	 *            kontonummer
	 * @param pin
	 *            pin
	 * @return true wenn korrekt, false wenn pin false
	 * @throws InvalidCustomerException
	 */
	public boolean isValidCustomer(Integer customerNumber, int pin)
			throws InvalidCustomerException {
		Customer customer = getCustomer(customerNumber);
		if (customer == null)
			return false;
		else
			return customer.getPin() == pin;
	}

}
