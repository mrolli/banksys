package ch.sws.ds.banksys.backend.persistence.provided;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * The class AccountDAO implements the persistence of bank accounts.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
public class AccountDAO {

	private static final String INSERT_ACCOUNT_QUERY = "INSERT INTO APP.ACCOUNT "
			+ "(NR, CUSTOMER_NR, DESCRIPTION, BALANCE, STATE) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_ACCOUNT_QUERY = "UPDATE APP.ACCOUNT "
			+ "SET CUSTOMER_NR = ?, DESCRIPTION = ?, BALANCE = ?, STATE = ? WHERE NR = ?";
	private static final String FIND_ACCOUNT_QUERY = "SELECT * FROM APP.ACCOUNT WHERE NR = ?";
	private static final String FIND_ACCOUNTS_OF_CUSTOMER_QUERY = "SELECT * FROM APP.ACCOUNT WHERE CUSTOMER_NR = ?";

	private static Logger logger = Logger.getLogger(AccountDAO.class);
	private DataSource dataSource;

	protected AccountDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Inserts an account into the database.
	 */
	public void insertAccount(Account account) {
		logger.trace(account);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(INSERT_ACCOUNT_QUERY);
			stmt.setInt(1, account.getNr());
			stmt.setInt(2, account.getCustomerNr());
			stmt.setString(3, account.getDescription());
			stmt.setBigDecimal(4, account.getBalance());
			stmt.setString(5, account.getState().name());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Updates an account in the database.
	 * 
	 * @return true on success, false on failure
	 */
	public boolean updateAccount(Account account) {
		logger.trace(account);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(UPDATE_ACCOUNT_QUERY);
			stmt.setInt(1, account.getCustomerNr());
			stmt.setString(2, account.getDescription());
			stmt.setBigDecimal(3, account.getBalance());
			stmt.setString(4, account.getState().name());
			stmt.setInt(5, account.getNr());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Finds an account in the database.
	 * 
	 * @return the account or null, if not found
	 */
	public Account findAccount(Integer accountNr) {
		logger.trace(accountNr);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(FIND_ACCOUNT_QUERY);
			stmt.setInt(1, accountNr);
			ResultSet result = stmt.executeQuery();
			if (!result.next())
				return null;
			Integer customerNr = result.getInt("CUSTOMER_NR");
			String description = result.getString("DESCRIPTION");
			BigDecimal balance = result.getBigDecimal("BALANCE");
			Account.State state = Account.State.valueOf(result
					.getString("STATE"));
			return new Account(accountNr, customerNr, description, balance,
					state);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Find the accounts of a customer.
	 * 
	 * @return the list of accounts (may be empty)
	 */
	public List<Account> findCustomerAccounts(Integer customerNr) {
		logger.trace(customerNr);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(FIND_ACCOUNTS_OF_CUSTOMER_QUERY);
			stmt.setInt(1, customerNr);
			ResultSet result = stmt.executeQuery();
			List<Account> accounts = new ArrayList<Account>();
			while (result.next()) {
				Integer accountNr = result.getInt("NR");
				String description = result.getString("DESCRIPTION");
				BigDecimal balance = result.getBigDecimal("BALANCE");
				Account.State state = Account.State.valueOf(result
						.getString("STATE"));
				accounts.add(new Account(accountNr, customerNr, description,
						balance, state));
			}
			return accounts;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
