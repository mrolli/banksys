/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence;

import java.util.ArrayList;
import java.util.List;

import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.backend.persistence.provided.Transaction;
import ch.sws.ds.banksys.backend.persistence.provided.TransactionDAO;
import ch.sws.ds.banksys.common.MoneyTransfer;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToMoneyTransfer;
import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToProvTransaction;

/**
 * @author feuzl1
 * 
 */
public class TransactionDao {

	private static final String MONEY_TRANSFER_SEQ = "MoneyTransferSeq";

	private static TransactionDao instance;

	private TransactionDAO provDAO;

	private TransactionDao() {
		provDAO = DAOFactory.getInstance().createTransactionDAO();
	}

	public static synchronized TransactionDao getInstance() {
		if (instance == null) {
			instance = new TransactionDao();
		}
		return instance;
	}

	public MoneyTransfer getMoneyTransfer(Integer number, Integer clearingNbr) {
		Transaction findTransaction = provDAO.findTransaction(number);
		return convertToMoneyTransfer(findTransaction, clearingNbr);
	}

	public List<MoneyTransfer> getAccountMoneyTransfers(Integer accountNumber,
			Integer clearingNbr) {
		List<Transaction> findAccountTransactions = provDAO
				.findAccountTransactions(accountNumber);
		List<MoneyTransfer> result = new ArrayList<>();
		for (Transaction t : findAccountTransactions) {
			result.add(convertToMoneyTransfer(t, clearingNbr));
		}
		return result;
	}

	/**
	 * Speichert einen neuen MoneyTransfer. Es wird eine neue nummer generiert
	 * und dann dem MoneyTransfer zugeordnet. Dies geschieht in einem
	 * synchronisierten block.
	 * 
	 * @param moneyTransfer
	 *            MoneyTransfer
	 * @return persistenter MoneyTransfer
	 */
	public synchronized MoneyTransfer save(MoneyTransfer moneyTransfer) {
		Integer newNumber = SequenceDao.getInstance().getValue(
				MONEY_TRANSFER_SEQ);
		moneyTransfer.setNumber(newNumber);
		provDAO.insertTransaction(convertToProvTransaction(moneyTransfer));
		return moneyTransfer;
	}
}
