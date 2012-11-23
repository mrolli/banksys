/**
 * 
 */
package ch.sws.ds.banksys.backend.business;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.backend.persistence.TransactionDao;
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
public class TransactionManager {

	private static final String BANKSYS_COUNTRYCODE = "banksys.countrycode";

	private static final String BANKSYS_ACCOUNTS_TRANSFER = "banksys.accounts.transfer";
	private static final String BANKSYS_ACCOUNTS_CASH = "banksys.accounts.cash";

	private static TransactionManager instance;

	private AccountManager accountManager;

	private TransactionDao transactionDao;

	private static Logger logger = Logger.getLogger(TransactionManager.class);

	private final IBAN transfer;

	private final IBAN cash;

	ClearingService cs = ClearingService.getInstance();

	private TransactionManager() {
		transactionDao = TransactionDao.getInstance();
		accountManager = AccountManager.getInstance();
		transfer = new IBAN(getProperty(BANKSYS_COUNTRYCODE), ClearingService
				.getInstance().getClearingNbr(),
				Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_TRANSFER)));
		cash = new IBAN(getProperty(BANKSYS_COUNTRYCODE), ClearingService
				.getInstance().getClearingNbr(),
				Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_CASH)));
	}

	public static synchronized TransactionManager getInstance() {
		if (instance == null) {
			instance = new TransactionManager();
		}
		return instance;
	}

	/**
	 * Führt eine eingehende clearing transaktion aus.
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 */
	public MoneyTransfer simpleClearingTransfer(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException {
		if (!accountManager.isValidAccount(moneyTransfer.getDebit()))
			throw new InvalidAccountException(String.format(
					"Account (debit) not valid '%s'", moneyTransfer.getDebit()));
		MoneyTransfer localMoneyTransfer = executeLocalMoneyTransfer(new MoneyTransfer(
				null, transfer, moneyTransfer.getDebit(),
				moneyTransfer.getAmount(), getForeignCreditText(
						moneyTransfer.getText(), moneyTransfer.getCredit()),
				moneyTransfer.getValuta()));
		logger.debug("Incoming clearing moneyTransfer executed. "
				+ localMoneyTransfer);
		return localMoneyTransfer;
	}

	/**
	 * Ausführen eines MoneyTransfers
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 * @throws UnknownClearingException
	 */
	public MoneyTransfer executeMoneyTransfer(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException,
			UnknownClearingException {
		if (moneyTransfer.getCredit() == null
				|| moneyTransfer.getDebit() == null)
			return executeCashOperation(moneyTransfer);
		else
			return executeEBankingMoneyTransfer(moneyTransfer);
	}

	/**
	 * EBaniking moneyTransfer.
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws InvalidAccountException
	 * @throws AmountNotSufficientException
	 * @throws UnknownClearingException
	 */
	private MoneyTransfer executeEBankingMoneyTransfer(
			MoneyTransfer moneyTransfer) throws InvalidAccountException,
			AmountNotSufficientException, UnknownClearingException {
		if (accountManager.isValidAccount(moneyTransfer.getCredit())
				&& accountManager.isValidAccount(moneyTransfer.getDebit()))
			return simpleTransfer(moneyTransfer);
		else
			return foreignEBankingTransfer(moneyTransfer);
	}

	/**
	 * Credit: own, Debit: own. EBanking only.
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 */
	private MoneyTransfer simpleTransfer(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException {
		IBAN credit = moneyTransfer.getCredit();
		if (!accountManager.isValidAccount(credit))
			throw new InvalidAccountException(String.format(
					"Account (credit) not valid '%s'", credit));
		IBAN debit = moneyTransfer.getDebit();
		if (!accountManager.isValidAccount(debit))
			throw new InvalidAccountException(String.format(
					"Account (debit) not valid '%s'", debit));
		MoneyTransfer localMoneyTransfer = executeLocalMoneyTransfer(moneyTransfer);
		logger.debug("Local transaction executed. " + localMoneyTransfer);
		return localMoneyTransfer;
	}

	/**
	 * Führt eine Cash Operation aus (ein konto ist immer Cash)
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 * @throws UnknownClearingException
	 */
	private MoneyTransfer executeCashOperation(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException,
			UnknownClearingException {
		IBAN credit = moneyTransfer.getCredit();
		boolean creditForeign = credit != null ? !accountManager
				.isValidAccount(credit) : false;
		IBAN debit = moneyTransfer.getDebit();
		boolean debitForeign = debit != null ? !accountManager
				.isValidAccount(debit) : false;

		if (!creditForeign && !debitForeign) {
			return simpleCashOperation(moneyTransfer);
		} else
			return foreignCashOperation(moneyTransfer);
	}

	/**
	 * Einfache cash operation für ein konto bei dieser bank (das zweite konto
	 * ist immer cash).
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 */
	private MoneyTransfer simpleCashOperation(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException {
		IBAN credit = moneyTransfer.getCredit();
		IBAN debit = moneyTransfer.getDebit();
		if (credit == null) {// deposit
			credit = cash;
			if (!accountManager.isValidAccount(debit))
				throw new InvalidAccountException(String.format(
						"Account (debit) not valid '%s'", debit));
		} else if (debit == null) {// withdraw
			debit = cash;
			if (!accountManager.isValidAccount(credit))
				throw new InvalidAccountException(String.format(
						"Account (credit) not valid '%s'", credit));
		} else
			throw new RuntimeException("No account specified!");
		MoneyTransfer localMoneyTransfer = executeLocalMoneyTransfer(new MoneyTransfer(
				null, credit, debit, moneyTransfer.getAmount(),
				moneyTransfer.getText(), new Date()));
		logger.debug("Local transaction executed. " + localMoneyTransfer);
		return localMoneyTransfer;
	}

	/**
	 * EBanking überweisung von lokalem Konto zu Fremdkonto.
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws InvalidAccountException
	 * @throws AmountNotSufficientException
	 * @throws UnknownClearingException
	 */
	private MoneyTransfer foreignEBankingTransfer(MoneyTransfer moneyTransfer)
			throws InvalidAccountException, AmountNotSufficientException,
			UnknownClearingException {
		IBAN credit = moneyTransfer.getCredit();
		IBAN debit = moneyTransfer.getDebit();
		if (!accountManager.isValidAccount(credit))
			throw new InvalidAccountException(String.format(
					"Account (credit) not valid '%s'", credit));
		if (accountManager.isValidAccount(debit))
			throw new InvalidAccountException(String.format(
					"Account (debit) not foreign '%s'", debit));
		else if (!cs.isValidClearingNbr(debit.getClearingNumber()))
			throw new UnknownClearingException(String.format(
					"Clearing number '%d' not valid!",
					debit.getClearingNumber()));

		MoneyTransfer localTransfer = executeLocalMoneyTransfer(new MoneyTransfer(
				null, credit, transfer, moneyTransfer.getAmount(),
				getForeignDebitText(moneyTransfer.getText(), debit),
				moneyTransfer.getValuta()));
		logger.debug("Local transaction executed. " + localTransfer);

		MoneyTransfer foreignMoneyTransfer = new MoneyTransfer(
				localTransfer.getNumber(), credit,
				debit, // or credit = transfer?
				moneyTransfer.getAmount(), moneyTransfer.getText(),
				moneyTransfer.getValuta());
		cs.process(foreignMoneyTransfer);
		return localTransfer;
	}

	private String getForeignCreditText(String text, IBAN credit) {
		StringBuffer sb = new StringBuffer();
		if (text != null)
			sb.append(text);
		sb.append(" (von " + credit.toString() + ")");
		return sb.toString();
	}

	private String getForeignDebitText(String text, IBAN debit) {
		StringBuffer sb = new StringBuffer();
		if (text != null)
			sb.append(text);
		sb.append(" (an " + debit.toString() + ")");
		return sb.toString();
	}

	/**
	 * Abheben und Einzahlen von Fremdkonti am Atm oder Counter.
	 * 
	 * @param moneyTransfer
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 * @throws UnknownClearingException
	 */
	private MoneyTransfer foreignCashOperation(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException, InvalidAccountException,
			UnknownClearingException {
		IBAN credit = moneyTransfer.getCredit();
		IBAN debit = moneyTransfer.getDebit();
		Money amount = moneyTransfer.getAmount();
		String text = moneyTransfer.getText();
		if (credit == null) {// deposit
			if (accountManager.isValidAccount(debit))
				throw new InvalidAccountException(String.format(
						"Account (debit) not valid '%s'", debit));
			else if (!cs.isValidClearingNbr(debit.getClearingNumber()))
				throw new UnknownClearingException(String.format(
						"Clearing number '%d' not valid!",
						debit.getClearingNumber()));
			return foreignDeposit(debit, debit, amount, text);
		} else if (debit == null) {// withdraw
			if (accountManager.isValidAccount(credit))
				throw new InvalidAccountException(String.format(
						"Account (credit) not valid '%s'", credit));
			else if (!cs.isValidClearingNbr(credit.getClearingNumber()))
				throw new UnknownClearingException(String.format(
						"Clearing number '%d' not valid!",
						credit.getClearingNumber()));
			return foreignWithdraw(credit, credit, amount, text);
		} else
			throw new RuntimeException("No account specified!");
	}

	/**
	 * Abheben von einem Fremdkonto
	 * 
	 * @param credit
	 * @param debit
	 * @param amount
	 * @param text
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 */
	private MoneyTransfer foreignWithdraw(IBAN credit, IBAN debit,
			Money amount, String text) throws AmountNotSufficientException,
			InvalidAccountException {
		Date valuta = new Date();
		MoneyTransfer localTransfer = executeLocalMoneyTransfer(new MoneyTransfer(
				null, transfer, cash, amount,
				getForeignCreditText(text, credit), valuta));
		logger.debug("Local transaction executed. " + localTransfer);

		MoneyTransfer foreignMoneyTransfer = new MoneyTransfer(
				localTransfer.getNumber(), debit, credit, new Money(amount
						.getMoney().negate(), amount.getCurrency()), text,
				valuta);
		cs.process(foreignMoneyTransfer);
		return localTransfer;
	}

	/**
	 * Einzahlen auf ein Fremdkonto
	 * 
	 * @param credit
	 * @param debit
	 * @param amount
	 * @param text
	 * @return
	 * @throws AmountNotSufficientException
	 * @throws InvalidAccountException
	 */
	private MoneyTransfer foreignDeposit(IBAN credit, IBAN debit, Money amount,
			String text) throws AmountNotSufficientException,
			InvalidAccountException {
		Date valuta = new Date();
		MoneyTransfer localTransfer = executeLocalMoneyTransfer(new MoneyTransfer(
				null, cash, transfer, amount, getForeignDebitText(text, debit),
				valuta));
		logger.debug("Local transaction executed. " + localTransfer);

		MoneyTransfer foreignMoneyTransfer = new MoneyTransfer(
				localTransfer.getNumber(), credit, debit, new Money(
						amount.getMoney(), amount.getCurrency()), text, valuta);
		cs.process(foreignMoneyTransfer);

		return localTransfer;
	}

	/**
	 * Führt einen MoneyTransfer von einem lokalen Konto (credit) auf ein
	 * anderes lokales Konto (debit) durch.
	 * 
	 * @param moneyTransfer
	 *            moneyTransfer
	 * @return Persistenter MoneyTransfer
	 * @throws AmountNotSufficientException
	 */
	private MoneyTransfer executeLocalMoneyTransfer(MoneyTransfer moneyTransfer)
			throws AmountNotSufficientException {
		accountManager.removeMoney(moneyTransfer.getCredit(),
				moneyTransfer.getAmount());
		accountManager.addMoney(moneyTransfer.getDebit(),
				moneyTransfer.getAmount());
		MoneyTransfer mt = transactionDao.save(moneyTransfer);
		return mt;
	}

	/**
	 * Liefert eine liste aller MoneyTransfers dieses Kontos.
	 * 
	 * @param accountNumber
	 *            kontonummer
	 * @return liste der MoneyTransfers
	 * @throws InvalidAccountException
	 */
	public List<MoneyTransfer> getMoneyTransfers(Integer accountNumber)
			throws InvalidAccountException {
		if (accountManager.isValidAccount(new IBAN(PropertyProvider
				.getProperty(BANKSYS_COUNTRYCODE), ClearingService
				.getInstance().getClearingNbr(), accountNumber)))
			return transactionDao.getAccountMoneyTransfers(accountNumber,
					cs.getClearingNbr());
		throw new InvalidAccountException("Invalid account number!");
	}
}
