/**
 * 
 */
package ch.sws.ds.banksys.backend.utils;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToAddress;
import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToName;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_CASH;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_TRANSFER;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMERS_THIS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMER_ADDRESS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMER_NAME;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;

import java.math.BigDecimal;

import ch.sws.ds.banksys.backend.persistence.MappingUtils;
import ch.sws.ds.banksys.backend.persistence.provided.Account;
import ch.sws.ds.banksys.backend.persistence.provided.AccountDAO;
import ch.sws.ds.banksys.backend.persistence.provided.Customer;
import ch.sws.ds.banksys.backend.persistence.provided.CustomerDAO;
import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.common.AccountState;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.CustomerState;
import ch.sws.ds.banksys.common.Money;

/**
 * @author feuzl1
 * 
 */
public class TestUtils {

	public static final int INITIAL_MONEY = 10000;

	public static void createTechnicalAccounts() {
		Integer bankCustomer = Integer
				.parseInt(getProperty(BANKSYS_CUSTOMERS_THIS));
		Integer transferNbr = Integer
				.parseInt(getProperty(BANKSYS_ACCOUNTS_TRANSFER));
		Integer cashNbr = Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_CASH));
		CustomerDAO customerDAO = DAOFactory.getInstance().createCustomerDAO();
		Customer findCustomer = customerDAO.findCustomer(bankCustomer);
		if (findCustomer == null) {
			customerDAO
					.insertCustomer(MappingUtils
							.convertToProvCustomer(new ch.sws.ds.banksys.common.Customer(
									bankCustomer,
									1234,
									convertToName(getProperty(BANKSYS_CUSTOMER_NAME)),
									convertToAddress(getProperty(BANKSYS_CUSTOMER_ADDRESS)),
									CustomerState.ACTIVE)));
		}

		AccountDAO accountDAO = DAOFactory.getInstance().createAccountDAO();
		Account transfer = accountDAO.findAccount(transferNbr);
		if (transfer == null) {
			accountDAO.insertAccount(MappingUtils
					.convertToProvAccount(new ch.sws.ds.banksys.common.Account(
							transferNbr, bankCustomer, new Money(
									new BigDecimal(INITIAL_MONEY), Currency.CHF),
							"Transfer", AccountState.OPEN)));
		} else {
			transfer.setBalance(new BigDecimal(INITIAL_MONEY));
			accountDAO.updateAccount(transfer);
		}
		Account cash = accountDAO.findAccount(cashNbr);
		if (cash == null) {
			accountDAO.insertAccount(MappingUtils
					.convertToProvAccount(new ch.sws.ds.banksys.common.Account(
							cashNbr, bankCustomer, new Money(new BigDecimal(
									INITIAL_MONEY), Currency.CHF), "Cash",
							AccountState.OPEN)));
		} else {
			cash.setBalance(new BigDecimal(INITIAL_MONEY));
			accountDAO.updateAccount(cash);
		}

	}
}
