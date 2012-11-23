package ch.sws.ds.banksys.backend.persistence.provided;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.log4j.Logger;

/**
 * The class DAOFactory is used to configure the data source, to initialize the
 * database and to create DAO objects.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
public class DAOFactory {

	public static final String DB_DIRECTORY = "db";
	public static final String CONFIG_FILE = "/db.properties";

	private static final String CREATE_CUSTOMER_TABLE_QUERY = "CREATE TABLE APP.CUSTOMER ("
			+ "NR INTEGER PRIMARY KEY,"
			+ "NAME VARCHAR(50),"
			+ "ADDRESS VARCHAR(200),"
			+ "PIN VARCHAR(20),"
			+ "STATE VARCHAR(20))";
	private static final String CREATE_ACCOUNT_TABLE_QUERY = "CREATE TABLE APP.ACCOUNT ("
			+ "NR INTEGER PRIMARY KEY,"
			+ "CUSTOMER_NR INTEGER CONSTRAINT CUSTOMER_FK REFERENCES APP.CUSTOMER(NR),"
			+ "DESCRIPTION VARCHAR(50),"
			+ "BALANCE DECIMAL(12,2),"
			+ "STATE VARCHAR(20))";
	private static final String CREATE_TRANSACTION_TABLE_QUERY = "CREATE TABLE APP.TX ("
			+ "NR INTEGER PRIMARY KEY,"
			+ "DEBIT_ACCT_NR INTEGER CONSTRAINT DEBIT_ACCT_FK REFERENCES APP.ACCOUNT(NR),"
			+ "CREDIT_ACCT_NR INTEGER CONSTRAINT CREDIT_ACCT_FK REFERENCES APP.ACCOUNT(NR),"
			+ "VALUTA TIMESTAMP,"
			+ "AMOUNT DECIMAL(12,2),"
			+ "TEXT VARCHAR(50))";
	private static final String CREATE_SEQUENCE_TABLE_QUERY = "CREATE TABLE APP.SEQUENCE ("
			+ "NAME VARCHAR(50) PRIMARY KEY,"
			+ "VALUE INTEGER)";

	private static Logger logger = Logger.getLogger(DAOFactory.class);
	private static DAOFactory instance;
	private EmbeddedDataSource dataSource;
	private Properties props;

	public static DAOFactory getInstance() {
		if (instance == null)
			instance = new DAOFactory();
		return instance;
	}

	private DAOFactory() {
		try {
			props = new Properties();
			props.load(DAOFactory.class.getResourceAsStream(CONFIG_FILE));
			System.setProperty("derby.stream.error.file", "log/derby.log");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		createDataSource();
		createTables();
	}

	private void createDataSource() {
		logger.trace(null);
		dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName(DB_DIRECTORY + File.separator
				+ props.getProperty("dbName"));
		dataSource.setUser(props.getProperty("dbUser"));
		dataSource.setPassword(props.getProperty("dbPassword"));
		dataSource.setCreateDatabase("create");
	}

	private void createTables() {
		logger.trace(null);
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			Statement stmt = connection.createStatement();
			if (!dbmd.getTables(null, "APP", "CUSTOMER", null).next())
				stmt.execute(CREATE_CUSTOMER_TABLE_QUERY);
			if (!dbmd.getTables(null, "APP", "ACCOUNT", null).next())
				stmt.execute(CREATE_ACCOUNT_TABLE_QUERY);
			if (!dbmd.getTables(null, "APP", "TX", null).next())
				stmt.execute(CREATE_TRANSACTION_TABLE_QUERY);
			if (!dbmd.getTables(null, "APP", "SEQUENCE", null).next())
				stmt.execute(CREATE_SEQUENCE_TABLE_QUERY);
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public CustomerDAO createCustomerDAO() {
		logger.trace(null);
		return new CustomerDAO(dataSource);
	}

	public AccountDAO createAccountDAO() {
		logger.trace(null);
		return new AccountDAO(dataSource);
	}

	public TransactionDAO createTransactionDAO() {
		logger.trace(null);
		return new TransactionDAO(dataSource);
	}

	public SequenceDAO createSequenceDAO() {
		logger.trace(null);
		return new SequenceDAO(dataSource);
	}
}
