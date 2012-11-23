/**
 * 
 */
package ch.sws.ds.banksys.backend.business;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.backend.locks.LockManager;
import ch.sws.ds.banksys.backend.persistence.AccountDao;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.AccountState;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.exceptions.AccountNotEmptyException;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;

/**
 * @author feuzl1
 * 
 */
public class AccountManager {

	private static final String BANKSYS_COUNTRYCODE = "banksys.countrycode";

	private static AccountManager instance;

	private CustomerManager customerManager;

	private AccountDao accountDao;

	private LockManager lockManager;

	private static Logger logger = Logger.getLogger(AccountManager.class);

	private AccountManager() {
		customerManager = CustomerManager.getInstance();
		accountDao = AccountDao.getInstance();
		lockManager = LockManager.getInstance();
	}

	public static synchronized AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}

	/**
	 * PRüft die ob customerNumber und pin korrekt sind und erstellt ein neues
	 * (leeres) Konto.
	 * 
	 * @param customerNumber
	 *            kundennummer
	 * @param pin
	 *            pin
	 * @param description
	 *            beschreibung
	 * @return Persistenten konto
	 * @throws InvalidCustomerException
	 */
	public Account createAccount(Integer customerNumber, int pin,
			String description) throws InvalidCustomerException {
		if (customerManager.isValidCustomer(customerNumber, pin)) {
			Account account = accountDao.save(new Account(null, customerNumber,
					new Money(new BigDecimal(0), Currency.CHF), description,
					AccountState.OPEN));
			logger.debug("Account created: " + account);
			return account;
		}
		throw new InvalidCustomerException(String.format(
				"Cannot create account, customer '%d' is not valid!",
				customerNumber));
	}

	/**
	 * Vordert ein Lock auf der iban an, prüft den kontostand und löscht das
	 * konto, sofern das konto leer ist. Am schluss wird der lock aufgelöst.
	 * 
	 * @param iban
	 *            iban
	 * @throws InvalidAccountException
	 * @throws AccountNotEmptyException
	 */
	public void deleteAccount(IBAN iban) throws InvalidAccountException,
			AccountNotEmptyException {
		try {
			lockManager.getLock(iban);
			Account account = accountDao.getAccount(iban);
			if (account == null)
				throw new InvalidAccountException("Invalid account!");
			if (account.getBalance().getMoney().doubleValue() != 0.0) {
				throw new AccountNotEmptyException(String.format(
						"Account '%d' is not empty!", account.getNumber()));
			}
			account.setState(AccountState.CLOSED);
			accountDao.save(account);
			logger.debug("Account deleted: " + iban.getAccountNumber());
		} catch (InterruptedException e) {
			logger.warn("Error locking account", e);
		} finally {
			lockManager.releaseLock(iban);
		}
	}

	/**
	 * Prüft die gültigkeit eines kontos. Countrycode und clearingnumber müssen
	 * mit denjenigen dieser bank übereinstimmen. Des weiteren muss das konto
	 * existieren und den Status OPEN haben.
	 * 
	 * @param iban
	 *            iban
	 * @return true wenn gültig
	 */
	public boolean isValidAccount(IBAN iban) {
		if (!iban.getCountryCode().equalsIgnoreCase(
				PropertyProvider.getProperty(BANKSYS_COUNTRYCODE))
				|| iban.getClearingNumber() != ClearingService.getInstance()
						.getClearingNbr())
			return false;
		Account account = accountDao.getAccount(iban);
		return account != null && account.getState().equals(AccountState.OPEN);
	}

	/**
	 * Konostand des Kontos abfragen.
	 * 
	 * @param iban
	 *            iban
	 * @throws InvalidAccountException
	 * @return aktueller Kontostand
	 */
	public Money getBalance(IBAN iban) throws InvalidAccountException {
		if (!isValidAccount(iban))
			throw new InvalidAccountException("Invalid account!");
		Account account = accountDao.getAccount(iban);
		return account.getBalance();
	}

	/**
	 * Liefert eine liste der Konti zur gegebenen Kundennummer
	 * 
	 * @param customerNumber
	 *            kundennummer
	 * @return Liste aller konti
	 */
	public List<Account> getAccounts(Integer customerNumber) {
		return accountDao.getCustomerAccounts(customerNumber);
	}

	/**
	 * Liefert ein konto anhand der kononummer
	 * 
	 * @param accountNumber
	 *            kontonunmmer
	 * @return konto (fully loaded)
	 */
	public Account getAccount(Integer accountNumber) {
		Account account = accountDao.getAccount(new IBAN(PropertyProvider
				.getProperty(BANKSYS_COUNTRYCODE), ClearingService
				.getInstance().getClearingNbr(), accountNumber));
		try {
			account.setCustomer(customerManager.getCustomer(account
					.getCustomerNbr()));
		} catch (InvalidCustomerException e) {
			logger.warn("Customer for account not found", e);
		}

		return account;
	}

	/**
	 * Addiert den Betrag zu diesem Konto. Für diese Operation wird das Konto
	 * gelockt.
	 * 
	 * @param iban
	 *            gutschriftkonto
	 * @param money
	 *            betrag
	 * @return erfolgreich
	 */
	public boolean addMoney(IBAN iban, Money money) {
		boolean success = false;
		try {
			lockManager.getLock(iban);
			Account account = accountDao.getAccount(iban);
			Money newMoney = new Money(account.getBalance().getMoney()
					.add(money.getMoney()), Currency.CHF);
			account.setBalance(newMoney);
			accountDao.save(account);
			success = true;
			logger.trace(String.format("Added '%f' to account '%s'!", money
					.getMoney().doubleValue(), iban));
		} catch (InterruptedException e) {
			logger.warn("Error locking account", e);
		} finally {
			lockManager.releaseLock(iban);
		}

		return success;
	}

	/**
	 * Addiert den Betrag zu diesem Konto. Für diese Operation wird das Konto
	 * gelockt.
	 * 
	 * @param iban
	 *            belastungskonto
	 * @param money
	 *            betrag
	 * @return erfolgreich
	 * @throws AmountNotSufficientException
	 */
	public boolean removeMoney(IBAN iban, Money money)
			throws AmountNotSufficientException {
		boolean success = false;
		try {
			lockManager.getLock(iban);
			Account account = accountDao.getAccount(iban);
			if (account.getBalance().getMoney().subtract(money.getMoney())
					.longValue() < 0)
				throw new AmountNotSufficientException("Insufficient money!");

			Money newMoney = new Money(account.getBalance().getMoney()
					.subtract(money.getMoney()), Currency.CHF);
			account.setBalance(newMoney);
			accountDao.save(account);
			success = true;
			logger.trace(String.format("Removed '%f' from account '%s'!", money
					.getMoney().doubleValue(), iban));
		} catch (InterruptedException e) {
			logger.warn("Error locking account", e);
		} finally {
			lockManager.releaseLock(iban);
		}

		return success;
	}
}
