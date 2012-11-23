/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToAccount;
import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToProvAccount;

import java.util.ArrayList;
import java.util.List;

import ch.sws.ds.banksys.backend.persistence.provided.AccountDAO;
import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.IBAN;

/**
 * @author feuzl1
 * 
 */
public class AccountDao {

	private static final String ACCOUNT_SEQ = "AccountSeq";

	private static AccountDao instance;

	private AccountDAO provDAO;

	private AccountDao() {
		provDAO = DAOFactory.getInstance().createAccountDAO();
	}

	public static synchronized AccountDao getInstance() {
		if (instance == null) {
			instance = new AccountDao();
		}
		return instance;
	}

	public List<Account> getCustomerAccounts(Integer customerNumber) {
		List<ch.sws.ds.banksys.backend.persistence.provided.Account> findCustomerAccounts = provDAO
				.findCustomerAccounts(customerNumber);
		List<Account> result = new ArrayList<>();
		for (ch.sws.ds.banksys.backend.persistence.provided.Account prov : findCustomerAccounts) {
			result.add(convertToAccount(prov));
		}
		return result;
	}

	public Account getAccount(IBAN iban) {
		return convertToAccount(provDAO.findAccount(iban.getAccountNumber()));
	}

	/**
	 * Speichert einen Account. Anhand der nummer wird entschieden ob neu oder
	 * update. Wenn neu, wird eine neue nummer generiert und dann dem Account
	 * zugeordnet. Dies geschieht in einem synchronisierten block.
	 * 
	 * @param account
	 *            account
	 * @return persistenter account.
	 */
	public Account save(Account account) {
		if (account.getNumber() == null) { // new
			synchronized (this) {
				Integer newNumber = SequenceDao.getInstance().getValue(
						ACCOUNT_SEQ);
				account.setNumber(newNumber);
				provDAO.insertAccount(convertToProvAccount(account));
				return account;
			}
		} else { // update
			provDAO.updateAccount(convertToProvAccount(account));
			return account;
		}
	}

}
