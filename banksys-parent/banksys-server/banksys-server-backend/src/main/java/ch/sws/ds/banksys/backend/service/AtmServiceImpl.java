/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_COUNTRYCODE;

import java.rmi.RemoteException;
import java.util.Date;

import ch.sws.ds.banksys.atm.interfaces.AtmService;
import ch.sws.ds.banksys.backend.business.AccountManager;
import ch.sws.ds.banksys.backend.business.CustomerManager;
import ch.sws.ds.banksys.backend.business.TransactionManager;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * @author feuzl1
 * 
 */
public class AtmServiceImpl implements AtmService {

	private CustomerManager customerManager;
	private AccountManager accountManager;
	private TransactionManager transactionManager;

	public AtmServiceImpl() {
		customerManager = CustomerManager.getInstance();
		accountManager = AccountManager.getInstance();
		transactionManager = TransactionManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.ATMService#isValidAccount(ch.sws
	 * .ds.banksys.common.IBAN)
	 */
	@Override
	public boolean isValidAccount(IBAN iban) throws RemoteException {
		return accountManager.isValidAccount(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.ATMService#isOwnCustomer(ch.sws
	 * .ds.banksys.common.IBAN)
	 */
	@Override
	public boolean isOwnCustomer(IBAN iban) throws RemoteException {
		return customerManager.isOwnCustomer(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.ebanking.interfaces.ATMService#getBalance(ch.sws.ds
	 * .banksys.common.IBAN)
	 */
	@Override
	public Money getBalance(IBAN iban) throws InvalidAccountException,
			RemoteException {
		return accountManager.getBalance(iban);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sws.ds.banksys.counter.interfaces.AtmService#getClearingNbr()
	 */
	@Override
	public int getClearingNbr() throws RemoteException {
		return ClearingService.getInstance().getClearingNbr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sws.ds.banksys.counter.interfaces.AtmService#getCountryCode()
	 */
	@Override
	public String getCountryCode() throws RemoteException {
		return PropertyProvider.getProperty(BANKSYS_COUNTRYCODE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.sws.ds.banksys.counter.interfaces.AtmService#withdrawMoney(ch.sws.
	 * ds.banksys.common.IBAN, ch.sws.ds.banksys.common.Money, java.lang.String)
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

	@Override
	public boolean isValidClearingNbr(Integer clearing) throws RemoteException {
		return ClearingService.getInstance().isValidClearingNbr(clearing);
	}

}
