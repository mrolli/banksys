/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_COUNTRYCODE;

import java.rmi.RemoteException;
import java.util.Date;

import ch.sws.ds.banksys.backend.business.AccountManager;
import ch.sws.ds.banksys.backend.business.CustomerManager;
import ch.sws.ds.banksys.backend.business.TransactionManager;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.exceptions.AccountNotEmptyException;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;
import ch.sws.ds.banksys.counter.interfaces.CounterService;

/**
 * @author feuzl1
 * 
 */
public class CounterServiceImpl implements CounterService {

	private CustomerManager customerManager;
	private AccountManager accountManager;
	private TransactionManager transactionManager;

	public CounterServiceImpl() {
		customerManager = CustomerManager.getInstance();
		accountManager = AccountManager.getInstance();
		transactionManager = TransactionManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#getCustomer(ch.sws
	 * .ds.banksys.common.IBAN)
	 */
	@Override
	public Customer getCustomer(IBAN iban) throws InvalidAccountException,
			InvalidCustomerException, RemoteException {
		return customerManager.getCustomer(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#createCustomer(ch
	 * .sws.ds.banksys.common.Name, ch.sws.ds.banksys.common.Address, int)
	 */
	@Override
	public Integer createCustomer(Name name, Address address, int pin)
			throws RemoteException {
		Customer customer = customerManager.createCustomer(name, address, pin);
		return customer.getNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#createAccount(java
	 * .lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public Integer createAccount(Integer customerNumber, int pin,
			String description) throws InvalidCustomerException,
			RemoteException {
		Account account = accountManager.createAccount(customerNumber, pin,
				description);
		return account.getNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#deleteAccount(java
	 * .lang.Integer)
	 */
	@Override
	public void deleteAccount(Integer accountNbr)
			throws AccountNotEmptyException, InvalidAccountException,
			RemoteException {
		accountManager.deleteAccount(new IBAN(getCountryCode(),
				getClearingNbr(), accountNbr));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#isValidAccount(ch
	 * .sws.ds.banksys.common.IBAN)
	 */
	@Override
	public boolean isValidAccount(IBAN iban) throws RemoteException {
		return accountManager.isValidAccount(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#isOwnCustomer(ch.
	 * sws.ds.banksys.common.IBAN)
	 */
	@Override
	public boolean isOwnCustomer(IBAN iban) throws RemoteException {
		return customerManager.isOwnCustomer(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#getBalance(ch.sws
	 * .ds.banksys.common.IBAN)
	 */
	@Override
	public Money getBalance(IBAN iban) throws InvalidAccountException,
			RemoteException {
		return accountManager.getBalance(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sws.ds.banksys.counter.interfaces.CounterService#getClearingNbr()
	 */
	@Override
	public int getClearingNbr() throws RemoteException {
		return ClearingService.getInstance().getClearingNbr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sws.ds.banksys.counter.interfaces.CounterService#getCountryCode()
	 */
	@Override
	public String getCountryCode() throws RemoteException {
		return PropertyProvider.getProperty(BANKSYS_COUNTRYCODE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#withdrawMoney(ch.
	 * sws.ds.banksys.common.IBAN, ch.sws.ds.banksys.common.Money,
	 * java.lang.String)
	 */
	@Override
	public Integer withdrawMoney(IBAN credit, Money amount, String text)
			throws AmountNotSufficientException, InvalidAccountException,
			UnknownClearingException, RemoteException {
		MoneyTransfer mt = new MoneyTransfer(null, credit, null, amount, text,
				new Date());
		mt = transactionManager.executeMoneyTransfer(mt);
		if (mt != null)
			return mt.getNumber();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.CounterService#depositMoney(ch.sws
	 * .ds.banksys.common.IBAN, ch.sws.ds.banksys.common.Money,
	 * java.lang.String)
	 */
	@Override
	public Integer depositMoney(IBAN debit, Money amount, String text)
			throws InvalidAccountException, UnknownClearingException,
			RemoteException {
		MoneyTransfer mt = new MoneyTransfer(null, null, debit, amount, text,
				new Date());
		try {
			mt = transactionManager.executeMoneyTransfer(mt);
		} catch (AmountNotSufficientException e) {
			e.printStackTrace();
		}
		if (mt != null)
			return mt.getNumber();
		return null;
	}

	@Override
	public boolean isValidClearingNbr(Integer clearing) throws RemoteException {
		return ClearingService.getInstance().isValidClearingNbr(clearing);
	}

}
