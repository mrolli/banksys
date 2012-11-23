package ch.sws.ds.banksys.backend.persistence.provided;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * The class TransactionDAO implements the persistence of bank transactions.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
public class TransactionDAO {

	private static final String INSERT_TRANSACTION_QUERY = "INSERT INTO APP.TX "
			+ "(NR, DEBIT_ACCT_NR, CREDIT_ACCT_NR, VALUTA, AMOUNT, TEXT) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String FIND_TRANSACTION_QUERY = "SELECT * FROM APP.TX WHERE NR = ?";
	private static final String FIND_TRANSACTIONS_OF_ACCOUNT_QUERY = "SELECT * FROM APP.TX "
			+ "WHERE DEBIT_ACCT_NR = ? OR CREDIT_ACCT_NR = ?";

	private static Logger logger = Logger.getLogger(TransactionDAO.class);
	private DataSource dataSource;

	public TransactionDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Inserts a transaction into the database.
	 */
	public void insertTransaction(Transaction transaction) {
		logger.trace(transaction);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(INSERT_TRANSACTION_QUERY);
			stmt.setInt(1, transaction.getNr());
			stmt.setInt(2, transaction.getDebitAccountNr());
			stmt.setInt(3, transaction.getCreditAccountNr());
			stmt.setTimestamp(4, new java.sql.Timestamp(transaction.getValuta()
					.getTime()));
			stmt.setBigDecimal(5, transaction.getAmount());
			stmt.setString(6, transaction.getText());
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
	 * Finds a transaction in the database.
	 * 
	 * @return the transaction or null, if not found
	 */
	public Transaction findTransaction(Integer transactionNr) {
		logger.trace(transactionNr);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(FIND_TRANSACTION_QUERY);
			stmt.setInt(1, transactionNr);
			ResultSet result = stmt.executeQuery();
			if (!result.next())
				return null;
			Integer debitAccountNr = result.getInt("DEBIT_ACCT_NR");
			Integer creditAccountNr = result.getInt("CREDIT_ACCT_NR");
			Date valuta = result.getTimestamp("VALUTA");
			BigDecimal amount = result.getBigDecimal("AMOUNT");
			String text = result.getString("TEXT");
			return new Transaction(transactionNr, debitAccountNr,
					creditAccountNr, valuta, amount, text);
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
	 * Finds the transactions of an account.
	 * 
	 * @return the list of transactions (may be empty)
	 */
	public List<Transaction> findAccountTransactions(Integer accountNr) {
		logger.trace(accountNr);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(FIND_TRANSACTIONS_OF_ACCOUNT_QUERY);
			stmt.setInt(1, accountNr);
			stmt.setInt(2, accountNr);
			ResultSet result = stmt.executeQuery();
			List<Transaction> transactions = new ArrayList<Transaction>();
			while (result.next()) {
				Integer transactionNr = result.getInt("NR");
				Integer debitAccountNr = result.getInt("DEBIT_ACCT_NR");
				Integer creditAccountNr = result.getInt("CREDIT_ACCT_NR");
				Date valuta = result.getTimestamp("VALUTA");
				BigDecimal amount = result.getBigDecimal("AMOUNT");
				String text = result.getString("TEXT");
				transactions.add(new Transaction(transactionNr, debitAccountNr,
						creditAccountNr, valuta, amount, text));
			}
			return transactions;
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
