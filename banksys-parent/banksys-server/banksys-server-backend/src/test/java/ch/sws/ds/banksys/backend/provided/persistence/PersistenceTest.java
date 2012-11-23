package ch.sws.ds.banksys.backend.provided.persistence;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.sws.ds.banksys.backend.persistence.provided.Account;
import ch.sws.ds.banksys.backend.persistence.provided.AccountDAO;
import ch.sws.ds.banksys.backend.persistence.provided.Customer;
import ch.sws.ds.banksys.backend.persistence.provided.CustomerDAO;
import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.backend.persistence.provided.SequenceDAO;
import ch.sws.ds.banksys.backend.persistence.provided.Transaction;
import ch.sws.ds.banksys.backend.persistence.provided.TransactionDAO;

/**
 * The class PersistenceTest implements a unit test of the persistence classes.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
@Ignore
public class PersistenceTest {

	public static final String CUSTOMER_SEQ_NAME = "CUSTOMER";
	public static final String ACCOUNT_SEQ_NAME = "ACCOUNT";
	public static final String TRANSACTION_SEQ_NAME = "TRANSACTION";

	public static final int INIT_CUSTOMER_NR = 200;
	public static final int INIT_ACCOUNT_NR = 5000;
	public static final int INIT_TRANSACTION_NR = 10000;

	private static DAOFactory factory;
	private CustomerDAO customerDAO;
	private AccountDAO accountDAO;
	private TransactionDAO transactionDAO;
	private SequenceDAO sequenceDAO;

	private Customer customer;
	private Account personalAccount;
	private Account savingsAccount;

	@BeforeClass
	public static void setup() {
		factory = DAOFactory.getInstance();
		SequenceDAO sequenceDAO = factory.createSequenceDAO();
		if (sequenceDAO.getValue(CUSTOMER_SEQ_NAME) == null)
			sequenceDAO.createSequence(CUSTOMER_SEQ_NAME, INIT_CUSTOMER_NR);
		if (sequenceDAO.getValue(ACCOUNT_SEQ_NAME) == null)
			sequenceDAO.createSequence(ACCOUNT_SEQ_NAME, INIT_ACCOUNT_NR);
		if (sequenceDAO.getValue(TRANSACTION_SEQ_NAME) == null)
			sequenceDAO.createSequence(TRANSACTION_SEQ_NAME,
					INIT_TRANSACTION_NR);
	}

	@Before
	public void init() {
		customerDAO = factory.createCustomerDAO();
		accountDAO = factory.createAccountDAO();
		transactionDAO = factory.createTransactionDAO();
		sequenceDAO = factory.createSequenceDAO();
	}

	@Test
	public void test() {
		createCustomer();
		createAccounts();
		createTransaction();
	}

	private void createCustomer() {
		sequenceDAO.incrementValue(CUSTOMER_SEQ_NAME);
		customer = new Customer(sequenceDAO.getValue(CUSTOMER_SEQ_NAME),
				"Alice Smith", "123 Maple Street, Mill Valley, CA 90952",
				"12345678", Customer.State.ACTIVE);
		customerDAO.insertCustomer(customer);
		Assert.assertEquals(customer,
				customerDAO.findCustomer(customer.getNr()));

		String address = "8 Oak Avenue, Old Town, PA 95819";
		customer.setAddress(address);
		customerDAO.updateCustomer(customer);
		Assert.assertEquals(address, customerDAO.findCustomer(customer.getNr())
				.getAddress());
	}

	private void createAccounts() {
		sequenceDAO.incrementValue(ACCOUNT_SEQ_NAME);
		personalAccount = new Account(sequenceDAO.getValue(ACCOUNT_SEQ_NAME),
				customer.getNr(), "Personal Account", BigDecimal.ZERO,
				Account.State.OPEN);
		accountDAO.insertAccount(personalAccount);
		Assert.assertEquals(personalAccount,
				accountDAO.findAccount(personalAccount.getNr()));

		sequenceDAO.incrementValue(ACCOUNT_SEQ_NAME);
		savingsAccount = new Account(sequenceDAO.getValue(ACCOUNT_SEQ_NAME),
				customer.getNr(), "Savings Account", BigDecimal.ZERO,
				Account.State.OPEN);
		accountDAO.insertAccount(savingsAccount);
		Assert.assertEquals(savingsAccount,
				accountDAO.findAccount(savingsAccount.getNr()));

		Assert.assertEquals(2, accountDAO
				.findCustomerAccounts(customer.getNr()).size());
	}

	private void createTransaction() {
		BigDecimal balance = new BigDecimal("132.50");
		personalAccount.setBalance(balance);
		accountDAO.updateAccount(personalAccount);
		Assert.assertEquals(0, accountDAO.findAccount(personalAccount.getNr())
				.getBalance().compareTo(balance));

		sequenceDAO.incrementValue(TRANSACTION_SEQ_NAME);
		BigDecimal amount = new BigDecimal("95.80");
		Transaction transaction = new Transaction(
				sequenceDAO.getValue(TRANSACTION_SEQ_NAME),
				personalAccount.getNr(), savingsAccount.getNr(), new Date(),
				amount, "Money Transfer");
		transactionDAO.insertTransaction(transaction);
		Assert.assertEquals(transaction,
				transactionDAO.findTransaction(transaction.getNr()));

		Assert.assertEquals(1,
				transactionDAO.findAccountTransactions(personalAccount.getNr())
						.size());
		Assert.assertEquals(1,
				transactionDAO.findAccountTransactions(savingsAccount.getNr())
						.size());
	}
}
