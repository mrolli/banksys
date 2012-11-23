/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_COUNTRYCODE;

import java.rmi.RemoteException;
import java.util.List;

import ch.sws.ds.banksys.backend.business.AccountManager;
import ch.sws.ds.banksys.backend.business.CustomerManager;
import ch.sws.ds.banksys.backend.business.TransactionManager;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;

/**
 * @author feuzl1
 * 
 */
public class EBankingServiceImpl implements EBankingService {

	private CustomerManager customerManager;
	private AccountManager accountManager;
	private TransactionManager transactionManager;

	public EBankingServiceImpl() {
		customerManager = CustomerManager.getInstance();
		accountManager = AccountManager.getInstance();
		transactionManager = TransactionManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#isValidCustomer
	 * (java.lang.Integer, int)
	 */
	@Override
	public boolean isValidCustomer(Integer customerNumber, int pin)
			throws InvalidCustomerException, RemoteException {
		return customerManager.isValidCustomer(customerNumber, pin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#getCustomer(java
	 * .lang.Integer)
	 */
	@Override
	public Customer getCustomer(Integer customerNumber) throws InvalidCustomerException, RemoteException {
		return customerManager.getCustomer(customerNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#getAccounts(java
	 * .lang.Integer)
	 */
	@Override
	public List<Account> getAccounts(Integer customerNumber)
			throws RemoteException {
		return accountManager.getAccounts(customerNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#getMoneyTransfers
	 * (java.lang.Integer)
	 */
	@Override
	public List<MoneyTransfer> getMoneyTransfers(Integer accountNumber)
			throws InvalidAccountException, RemoteException {
		return transactionManager.getMoneyTransfers(accountNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#executeMoneyTransfer
	 * (ch.sws.ds.banksys.common.MoneyTransfer)
	 */
	@Override
	public boolean executeMoneyTransfer(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException, UnknownClearingException, RemoteException {
		MoneyTransfer mt = transactionManager
				.executeMoneyTransfer(moneyTransfer);
		return mt != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#getClearingNbr()
	 */
	@Override
	public int getClearingNbr() throws RemoteException {
		return ClearingService.getInstance().getClearingNbr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.EBankingService#getCountryCode()
	 */
	@Override
	public String getCountryCode() throws RemoteException {
		return PropertyProvider.getProperty(BANKSYS_COUNTRYCODE);
	}

}
