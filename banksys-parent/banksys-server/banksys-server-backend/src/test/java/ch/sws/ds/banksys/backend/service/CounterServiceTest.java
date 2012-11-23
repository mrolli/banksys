/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.sws.ds.banksys.backend.persistence.TransactionDao;
import ch.sws.ds.banksys.backend.utils.TestUtils;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Currency;
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
@Ignore
public class CounterServiceTest {

	private CounterService cs = new CounterServiceImpl();

	private static Integer customer = null;
	private static IBAN account = null;

	@Test
	public void getClearingTest() throws RemoteException {
		int clearingNbr = cs.getClearingNbr();
		Assert.assertTrue(clearingNbr > 0);
	}

	@Test
	public void getCountryCodeTest() throws RemoteException {
		String cc = cs.getCountryCode();
		Assert.assertEquals("CH", cc);
	}

	@Before
	public void createAccountTest() throws RemoteException, InvalidCustomerException, InvalidAccountException {
		TestUtils.createTechnicalAccounts();
		
		customer = cs.createCustomer(new Name("Test", "Test"), new Address(
				"Teststr", 1, "Test", 1000, "CH"), 1234);
		Assert.assertNotNull(customer);

		Integer createAccount = cs.createAccount(customer, 1234, "Test");
		account = new IBAN(cs.getCountryCode(), cs.getClearingNbr(),
				createAccount);
		Assert.assertNotNull(account);

		Assert.assertTrue(cs.isOwnCustomer(account));
		Assert.assertTrue(cs.isValidAccount(account));

		Money balance = cs.getBalance(account);
		Assert.assertEquals(0, balance.getMoney().intValue());
		Assert.assertEquals(Currency.CHF, balance.getCurrency());
	}

	@Test
	public void depositTest() throws RemoteException, AmountNotSufficientException, InvalidAccountException, UnknownClearingException {
		Integer depositMoney = cs.depositMoney(account, new Money(
				new BigDecimal(100), Currency.CHF), "Add 100");
		Assert.assertNotNull(depositMoney);

		Money balance = cs.getBalance(account);
		Assert.assertEquals(100, balance.getMoney().intValue());

		Integer withdraw = cs.withdrawMoney(account, new Money(new BigDecimal(
				20), Currency.CHF), "remove 20");
		Assert.assertNotNull(withdraw);

		withdraw = cs.withdrawMoney(account, new Money(new BigDecimal(80),
				Currency.CHF), "remove 80");
		Assert.assertNotNull(withdraw);

		balance = cs.getBalance(account);
		Assert.assertEquals(0, balance.getMoney().intValue());

		List<MoneyTransfer> accountMoneyTransfers = TransactionDao
				.getInstance()
				.getAccountMoneyTransfers(account.getAccountNumber(),
						account.getClearingNumber());
		Assert.assertNotNull(accountMoneyTransfers);
		Assert.assertEquals(3, accountMoneyTransfers.size());
	}

	@After
	public void deleteAccountTest() throws RemoteException, AccountNotEmptyException, InvalidAccountException {
		cs.deleteAccount(account.getAccountNumber());

		Assert.assertFalse(cs.isValidAccount(account));
	}

}
