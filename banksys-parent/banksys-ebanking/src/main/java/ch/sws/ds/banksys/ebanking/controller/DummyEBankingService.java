package ch.sws.ds.banksys.ebanking.controller;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.AccountState;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.CustomerState;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;

/**
 * EBankingService Implementation für Testzwecke.
 * 
 * Die Dummyimplementation des {@link EBankingService} Interface liefert
 * statischen Content zurück und wir zu Testzwecken verwendet.
 * 
 * @author mrolli
 */
public class DummyEBankingService implements EBankingService {
	@Override
	public boolean isValidCustomer(Integer customerNumber, int pin)
			throws RemoteException {
		return customerNumber == 4321 && pin == 1234;
	}

	@Override
	public Customer getCustomer(Integer customerNumber) throws RemoteException {
		if (customerNumber == 4321) {
			return new Customer(new Integer(4321), 1234, new Name("Wurst", "Hans"),
					new Address("Im Glück", 13, "Bern", 3010, "Switzerland"),
					CustomerState.ACTIVE);
		}
		return null;
	}

	@Override
	public List<Account> getAccounts(Integer customerNumber)
			throws RemoteException {
		List<Account> accounts = new ArrayList<Account>();

		if (customerNumber == 4321) {
			Account account1 = new Account(123456, new Integer(162534), new Money(
					new BigDecimal(1238.15), Currency.CHF), "Lohnkonto",
					AccountState.OPEN);
			accounts.add(account1);

			Account account2 = new Account(654321, new Integer(162534), new Money(
					new BigDecimal(5235), Currency.CHF), "Sparkonto",
					AccountState.OPEN);
			accounts.add(account2);
		}

		return accounts;
	}

	@Override
	public List<MoneyTransfer> getMoneyTransfers(Integer accountNumber)
			throws RemoteException {

		List<MoneyTransfer> transfers = new ArrayList<MoneyTransfer>();

		if (accountNumber == 123456) {
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 999, 123456),
					new IBAN("ch", 10, 123553) , new Money(new BigDecimal(1775.25),
							Currency.CHF), "Miete", new Date(1311781583573L)));
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 999, 654321),
					new IBAN("ch", 999, 123456), new Money(new BigDecimal(500),
							Currency.CHF), "Übertrag", new Date(311781573573L)));
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 999, 123456),
					new IBAN("ch", 12, 12345132), new Money(new BigDecimal(
							45.25), Currency.CHF), "Rechnung X", new Date(811781563573L)));
		}

		if (accountNumber == 654321) {
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 12, 12345132),
					new IBAN("ch", 999, 654321), new Money(new BigDecimal(120),
							Currency.CHF), "Gutschrift", new Date(1311781583573L)));
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 999, 654321),
					new IBAN("ch", 10, 123456), new Money(new BigDecimal(500),
							Currency.CHF), "Übertrag", new Date(311781573573L)));
			transfers.add(new MoneyTransfer(null, new IBAN("ch", 999, 654321),
					new IBAN("ch", 12, 12345132), new Money(new BigDecimal(
							15.15), Currency.CHF), "Rechnung X", new Date(1311781583373L)));
		}

		return transfers;
	}

	@Override
	public boolean executeMoneyTransfer(MoneyTransfer moneyTransfer)
            throws AmountNotSufficientException, RemoteException {
		System.out.println("Transfer of "
				+ moneyTransfer.getAmount().toString() + " from "
				+ moneyTransfer.getCredit().getAccountNumber() + " to "
				+ moneyTransfer.getDebit().getAccountNumber());
		return true;
	}

	@Override
	public int getClearingNbr() throws RemoteException {
		return 999;
	}

	@Override
	public String getCountryCode() throws RemoteException {
		return "ch";
	}
}
