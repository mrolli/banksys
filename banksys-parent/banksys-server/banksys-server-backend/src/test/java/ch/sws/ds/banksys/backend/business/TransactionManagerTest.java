/**
 * 
 */
package ch.sws.ds.banksys.backend.business;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;
import static ch.sws.ds.banksys.backend.utils.TestUtils.INITIAL_MONEY;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;

import org.dibas.clearing.Clearing;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.sws.ds.banksys.backend.persistence.MappingUtils;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.service.CounterServiceImpl;
import ch.sws.ds.banksys.backend.service.clearing.ClearingImpl;
import ch.sws.ds.banksys.backend.utils.TestUtils;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;
import ch.sws.ds.banksys.counter.interfaces.CounterService;

/**
 * @author feuzl1
 * 
 */
public class TransactionManagerTest {

	private static final String BANKSYS_ACCOUNTS_TRANSFER = "banksys.accounts.transfer";
	private static final String BANKSYS_ACCOUNTS_CASH = "banksys.accounts.cash";
	private static final String BANKSYS_COUNTRYCODE = "banksys.countrycode";

	private CounterService cs = new CounterServiceImpl();

	private Clearing c = new ClearingImpl();

	private TransactionManager tm = TransactionManager.getInstance();

	private static Integer customer = null;
	private static IBAN account1 = null;
	private static IBAN account2 = null;

	private final int AMOUNT = 100;
	private final int START_MONEY = 1000;
	private Money amount = new Money(new BigDecimal(AMOUNT), Currency.CHF);

	private IBAN transfer = new IBAN(getProperty(BANKSYS_COUNTRYCODE),
			ClearingService.getInstance().getClearingNbr(),
			Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_TRANSFER)));
	private IBAN cash = new IBAN(getProperty(BANKSYS_COUNTRYCODE),
			ClearingService.getInstance().getClearingNbr(),
			Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_CASH)));

	@Before
	public void createAccountTest() throws RemoteException,
			InvalidCustomerException, InvalidAccountException,
			UnknownClearingException {
		TestUtils.createTechnicalAccounts();

		customer = cs.createCustomer(new Name("Test", "Test"), new Address(
				"Teststr", 1, "Test", 1000, "CH"), 1234);
		Assert.assertNotNull(customer);

		Integer createAccount = cs.createAccount(customer, 1234, "Test");
		account1 = new IBAN(cs.getCountryCode(), cs.getClearingNbr(),
				createAccount);
		Assert.assertNotNull(account1);

		createAccount = cs.createAccount(customer, 1234, "Test");
		account2 = new IBAN(cs.getCountryCode(), cs.getClearingNbr(),
				createAccount);
		Assert.assertNotNull(account2);

		Assert.assertTrue(cs.isOwnCustomer(account1));
		Assert.assertTrue(cs.isValidAccount(account1));

		Assert.assertTrue(cs.isOwnCustomer(account2));
		Assert.assertTrue(cs.isValidAccount(account2));

		Money balance = cs.getBalance(account1);
		Assert.assertEquals(0, balance.getMoney().intValue());
		Assert.assertEquals(Currency.CHF, balance.getCurrency());

		cs.depositMoney(account1, new Money(new BigDecimal(START_MONEY),
				Currency.CHF), "test");
	}

	@Test
	public void executeSimpleTransaction() throws AmountNotSufficientException,
			InvalidAccountException, RemoteException, UnknownClearingException {
		MoneyTransfer simpleMoneyTransfer = tm
				.executeMoneyTransfer(new MoneyTransfer(null, account1,
						account2, amount, "Simple Transaction", new Date()));
		Assert.assertNotNull(simpleMoneyTransfer.getNumber());
		Assert.assertEquals(START_MONEY - AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
		Assert.assertEquals(AMOUNT, cs.getBalance(account2).getMoney()
				.intValue());
		Assert.assertEquals(INITIAL_MONEY - START_MONEY, cs.getBalance(cash)
				.getMoney().intValue());
	}

	@Test
	public void deposit() throws AmountNotSufficientException,
			InvalidAccountException, RemoteException, UnknownClearingException {
		cs.depositMoney(account1, amount, "deposit");
		Assert.assertEquals(START_MONEY + AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
		Assert.assertEquals(INITIAL_MONEY - START_MONEY - AMOUNT, cs
				.getBalance(cash).getMoney().intValue());
	}

	@Test
	public void withdraw() throws AmountNotSufficientException,
			InvalidAccountException, RemoteException, UnknownClearingException {
		cs.withdrawMoney(account1, amount, "deposit");
		Assert.assertEquals(START_MONEY - AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
		Assert.assertEquals(INITIAL_MONEY - START_MONEY + AMOUNT, cs
				.getBalance(cash).getMoney().intValue());
	}

	@Test
	public void executeForeignTransaction()
			throws AmountNotSufficientException, InvalidAccountException,
			RemoteException, UnknownClearingException {
		IBAN foreign = new IBAN("CH", 26, 123);
		MoneyTransfer foreignTransaction = tm
				.executeMoneyTransfer(new MoneyTransfer(null, account1,
						foreign, amount, "foreignTransaction", new Date()));
		Assert.assertNotNull(foreignTransaction.getNumber());
		Assert.assertEquals(START_MONEY - AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
		Assert.assertEquals(INITIAL_MONEY + AMOUNT, cs.getBalance(transfer)
				.getMoney().intValue());
	}

	@Test
	public void executeForeignCashOperation()
			throws AmountNotSufficientException, InvalidAccountException,
			RemoteException, UnknownClearingException {
		IBAN foreign = new IBAN("CH", 26, 123);
		cs.withdrawMoney(foreign, amount, "foreignWithdraw");
		Assert.assertEquals(INITIAL_MONEY - START_MONEY + AMOUNT, cs
				.getBalance(cash).getMoney().intValue());
		Assert.assertEquals(INITIAL_MONEY - AMOUNT, cs.getBalance(transfer)
				.getMoney().intValue());
	}

	@Test
	public void clearingTransaction() throws RemoteException,
			InvalidAccountException, InterruptedException {
		IBAN foreign = new IBAN("CH", 26, 123);
		MoneyTransfer foreignTransfer = new MoneyTransfer(26, foreign,
				account1, amount, "clearingSimple", new Date());
		c.process(Arrays.asList(MappingUtils
				.convertToProvMoneyTransfer(foreignTransfer)));
		Thread.sleep(500); // wait for async execution
		Assert.assertEquals(INITIAL_MONEY - AMOUNT, cs.getBalance(transfer)
				.getMoney().intValue());
		Assert.assertEquals(START_MONEY + AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
	}

	@Test
	public void clearingWithdraw() throws RemoteException,
			InvalidAccountException, InterruptedException {
		IBAN foreign = new IBAN("CH", 26, 123);
		MoneyTransfer foreignTransfer = new MoneyTransfer(26, foreign,
				account1, new Money(new BigDecimal(-100), Currency.CHF),
				"clearingSimple", new Date());
		c.process(Arrays.asList(MappingUtils
				.convertToProvMoneyTransfer(foreignTransfer)));
		Thread.sleep(500); // wait for async execution
		Assert.assertEquals(INITIAL_MONEY + AMOUNT, cs.getBalance(transfer)
				.getMoney().intValue());
		Assert.assertEquals(START_MONEY - AMOUNT, cs.getBalance(account1)
				.getMoney().intValue());
	}

}
