package ch.sws.ds.banksys.backend.persistence.provided;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * The class SequenceDAO implements the persistence of sequences.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
public class SequenceDAO {

	private static final String GET_VALUE_QUERY = "SELECT * FROM APP.SEQUENCE WHERE NAME = ?";
	private static final String CREATE_SEQUENCE_QUERY = "INSERT INTO APP.SEQUENCE (NAME, VALUE) VALUES (?, ?)";
	private static final String INCREMENT_VALUE_QUERY = "UPDATE APP.SEQUENCE SET VALUE = VALUE + 1 WHERE NAME = ?";

	private static Logger logger = Logger.getLogger(SequenceDAO.class);
	private DataSource dataSource;

	public SequenceDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the value of a sequence.
	 * 
	 * @return the value or null, if not found
	 */
	public Integer getValue(String name) {
		logger.trace(name);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(GET_VALUE_QUERY);
			stmt.setString(1, name);
			ResultSet result = stmt.executeQuery();
			if (!result.next())
				return null;
			return result.getInt("VALUE");
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
	 * Creates a sequence.
	 */
	public void createSequence(String name, int initialValue) {
		logger.trace(name + ":" + initialValue);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(CREATE_SEQUENCE_QUERY);
			stmt.setString(1, name);
			stmt.setInt(2, initialValue);
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
	 * Increments the value of a sequence.
	 * 
	 * @return true on success, false on failure
	 */
	public boolean incrementValue(String name) {
		logger.trace(name);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement(INCREMENT_VALUE_QUERY);
			stmt.setString(1, name);
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
}
