package ch.sws.ds.banksys.common.console.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author kambl1
 *
 * Konfiugration der RMI-Schnittstelle und der zu erzeugenden Bankkarten.
 */
public class Settings {
	private static final String PROPERTIES_FILE = "config/banksys.properties";
	private static final String PROPERTIES_FILE_DEFAULT = "/banksys.properties";
	
	private static final String KEY_RMI_HOST = "rmi.host";
	private static final String KEY_RMI_PORT = "rmi.port";
	private static final String KEY_BANKCARD_DIRECTORY = "bankcard.dir";
	private static final String KEY_BANKCARD_WITHDRAW_LIMIT = "bankcard.withdrawLimit";

	private static Properties properties;

	private static Logger logger = Logger.getLogger(Settings.class);

	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE));
		} catch (Exception e) {
			logger.warn("Property file not found '" + PROPERTIES_FILE + "'! Use default..");
			try {
				properties.load(Settings.class.getResourceAsStream(PROPERTIES_FILE_DEFAULT));
			} catch (IOException e1) {
				logger.error("Property file not found! " + PROPERTIES_FILE_DEFAULT, e);
				System.exit(-1);
			}
		}
	}

	/**
	 * Propertie abfragen.
	 * @param key Key des zu suchenden Properties
	 * @return Propertie Value
	 */
	private static String getProperty(String key) {
		String property = properties.getProperty(key);
		if (property == null) {
			throw new RuntimeException("Properties file is incomplete! Propertie <" + key + "> is missing.");
		}
		return property;
	}

	/**
	 * RMI Host des Backends abfragen.
	 * @return RMI Host des Backends
	 */
	public static String getRmiHost() {
		return getProperty(KEY_RMI_HOST);
	}

	/**
	 * RMI Port des Backends abfragen.
	 * @return RMI Port des Backends
	 */
	public static int getRmiPort() {
		return Integer.parseInt(getProperty(KEY_RMI_PORT));
	}

	/**
	 * Ordner wo die Bankkarten abgelegt werden.
	 * @return Ordner der Bankkarten
	 */
	public static String getBankcardDirectory() {
		return getProperty(KEY_BANKCARD_DIRECTORY);
	}

	/**
	 * WithdrawLimit der Bankkarte abfragen.
	 * @return WithdrawLimit der Bankkarte
	 */
	public static int getBankcardWithdrawLimit() {
		return Integer.parseInt(getProperty(KEY_BANKCARD_WITHDRAW_LIMIT));
	}
}
