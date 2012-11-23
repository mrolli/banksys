/**
 * 
 */
package ch.sws.ds.banksys.backend.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author feuzl1
 * 
 */
public class PropertyProvider {

	public static final String BANKSYS_ACCOUNTS_CASH = "banksys.accounts.cash";
	public static final String BANKSYS_ACCOUNTS_TRANSFER = "banksys.accounts.transfer";
	public static final String BANKSYS_COUNTRYCODE = "banksys.countrycode";
	public static final String RMI_PORT = "rmi.port";
	public static final String HTTP_BIND_ADDRESS = "banksys.bind.address";
	public static final String BANKSYS_CUSTOMERS_THIS = "banksys.customers.thisbank";
	public static final String BANKSYS_CUSTOMER_NAME = "banksys.customers.thisbank.name";
	public static final String BANKSYS_CUSTOMER_ADDRESS = "banksys.customers.thisbank.address";
	public static final String BANKSYS_ACCOUNTS_TRANSFER_INIT = "banksys.accounts.transfer.balance";
	public static final String BANKSYS_ACCOUNTS_CASH_INIT = "banksys.accounts.cash.balance";
	public static final String BANKSYS_REGISTRATION_URL = "registration.url";
	public static final String CLEARING_URL = "clearing.url";
	public static final String BANKSYS_NAME = "banksys.name";

	private static Properties props;

	private static final String BANKSYS_PROPS = "config/banksys.properties";
	private static final String BANKSYS_PROPS_DEFAULT = "/banksys.properties";

	private static Logger logger = Logger.getLogger(PropertyProvider.class);

	static {
		props = new Properties();
		try {
			props.load(new FileInputStream(BANKSYS_PROPS));
		} catch (Exception e) {
			logger.warn("Property file not found '" + BANKSYS_PROPS
					+ "'! Use default..");
			try {
				props.load(PropertyProvider.class
						.getResourceAsStream(BANKSYS_PROPS_DEFAULT));
			} catch (IOException e1) {
				logger.error("Property file not found! "
						+ BANKSYS_PROPS_DEFAULT, e);
				System.exit(-1);
			}
		}
	}

	private PropertyProvider() {
		// nothing..
	}

	/**
	 * Liefert das property
	 * 
	 * @param key
	 *            property key
	 * @return property
	 */
	public static String getProperty(String key) {
		return props.getProperty(key);
	}

}
