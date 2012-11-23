package ch.sws.ds.banksys.backend.persistence.provided;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * The class CustomerDAO implements the persistence of bank customers.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
public class CustomerDAO {

	private static final String INSERT_CUSTOMER_QUERY = "INSERT INTO APP.CUSTOMER "
			+ "(NR, NAME, ADDRESS, PIN, STATE) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_CUSTOMER_QUERY = "UPDATE APP.CUSTOMER "
			+ "SET NAME = ?, ADDRESS = ?, PIN = ?, STATE = ? WHERE NR = ?";
	private static final String FIND_CUSTOMER_QUERY = "SELECT * FROM APP.CUSTOMER WHERE NR = ?";

	private static Logger logger = Logger.getLogger(CustomerDAO.class);
	private DataSource dataSource;

	public CustomerDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Inserts a customer into the database.
	 */
	public void insertCustomer(Customer customer) {
		logger.trace(customer);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(INSERT_CUSTOMER_QUERY);
			stmt.setInt(1, customer.getNr());
			stmt.setString(2, customer.getName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getPin());
			stmt.setString(5, customer.getState().name());
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
	 * Updates a customer in the database.
	 * 
	 * @return true on success, false on failure
	 */
	public boolean updateCustomer(Customer customer) {
		logger.trace(customer);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(UPDATE_CUSTOMER_QUERY);
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getAddress());
			stmt.setString(3, customer.getPin());
			stmt.setString(4, customer.getState().name());
			stmt.setInt(5, customer.getNr());
			return stmt.executeUpdate() > 0;
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
	 * Finds a customer in the database.
	 * 
	 * @return the customer or null, if not found
	 */
	public Customer findCustomer(Integer customerNr) {
		logger.trace(customerNr);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(FIND_CUSTOMER_QUERY);
			stmt.setInt(1, customerNr);
			ResultSet result = stmt.executeQuery();
			if (!result.next())
				return null;
			String name = result.getString("NAME");
			String address = result.getString("ADDRESS");
			String pin = result.getString("PIN");
			Customer.State state = Customer.State.valueOf(result
					.getString("STATE"));
			return new Customer(customerNr, name, address, pin, state);
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
