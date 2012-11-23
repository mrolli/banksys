package ch.sws.ds.banksys.common.console.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.console.exceptions.BankcardException;

/**
 * @author kambl1
 *
 * Der BankcardManager ist zuständig für alle Interaktionen mit der Bankkarte.
 */
public class BankcardManager {
	private static final String BANKCARD_FILE = "bankcard.properties";
	private static final String DATE_PATTERN = "YYYY-MM-dd'T'HH:mm:ss";
	private static final long WITHDRAW_LIMIT_PERIOD = 1; // in Stunden
	
	private static final String KEY_BANK_NUMBER = "bankNr";
	private static final String KEY_ACCOUNT_NUMBER = "accountNr";
	private static final String KEY_PIN = "pin";
	private static final String KEY_WITHDRAW_LIMIT = "withdrawLimit";
	private static final String KEY_WITHDRAW_AMOUNT = "withdrawAmount";
	private static final String KEY_WITHDRAW_DATE = "withdrawDate";
	
	private static Properties properties;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
	private static Logger logger = Logger.getLogger(BankcardManager.class);

	/**
	 * Gibt den Pfad zu der einzuluesenden Bankkarte zurück.
	 * @return File Objekt mit dem Pfad zur Bankkarte
	 */
	private static File getBankcardFile() {
		return new File(Settings.getBankcardDirectory(), BANKCARD_FILE);
	}
	
	/**
	 * Gibt den Pfad zu einer Konto spezifischen Bankkarte zurück.
	 * @param dir Ordner in welchen die Bankkarte liegt
	 * @param accountNumber Bankkonto zu welchem die Bankkarte gehört
	 * @return File Objekt mit dem Pfad zur Bankkarte
	 */
	private static File getBankcardFile(File dir, Integer accountNumber) {
		return new File(dir, accountNumber.toString() + BANKCARD_FILE);
	}
	
	/**
	 * Gibt den Dateinmane der Bankkarten Datei zurück.
	 * @return Dateiname der Bankkarten Datei
	 */
	public static String getBankcardFileName() {
		return BANKCARD_FILE;
	}
	
	/**
	 * Gibt den Ordner in welchen die Bankkarten liegen zurück.
	 * @return Ordner in welchem die Bankkarten liegen.
	 */
	public static String getBankcardFolder() {
		return Settings.getBankcardDirectory();
	}
	
	/**
	 * Lädt die bereitgestellte Bankkarte ein und aktualisiert das Limit, falls nötig.
	 * @throws BankcardException
	 */
	public static void loadCard() throws BankcardException {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(getBankcardFile()));
			updateWithdrawLimit();
		} catch (IOException e) {
			String errorMessage = "Bankcard file not found '" + BANKCARD_FILE + "'!";
			logger.warn(errorMessage);
			throw new BankcardException(errorMessage);
		}
	}
	
	/**
	 * Erstellt eine Bankarte.
	 * @param clearingNumber ClearingNumber des zugehörigen Bankkontos
	 * @param accountNumber AccountNumber des zugehörigen Bankkontos
	 * @param pin PIN des zugehörigen Bankkontos
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws BankcardException
	 */
	public static void createCard(int clearingNumber, Integer accountNumber, int pin) throws BankcardException {
		try {
			Properties newProperties = new Properties();
			newProperties.setProperty(KEY_BANK_NUMBER, String.valueOf(clearingNumber));
			newProperties.setProperty(KEY_ACCOUNT_NUMBER, accountNumber.toString());
			newProperties.setProperty(KEY_PIN, String.valueOf(pin));
			newProperties.setProperty(KEY_WITHDRAW_LIMIT, (new BigDecimal(Settings.getBankcardWithdrawLimit())).toString());
			newProperties.setProperty(KEY_WITHDRAW_AMOUNT, (new BigDecimal(0)).toString());
			newProperties.setProperty(KEY_WITHDRAW_DATE, simpleDateFormat.format(new Date()));
			File dir = new File(Settings.getBankcardDirectory());
			if (dir.mkdirs()) {
				newProperties.store(new FileOutputStream(getBankcardFile(dir, accountNumber)), null);
			} else {
				String errorMessage = "Could not create bankcard folder '" + Settings.getBankcardDirectory() + "'!";
				logger.warn(errorMessage);
				throw new BankcardException(errorMessage);
			}
		} catch (IOException e) {
			String errorMessage = "Could not create bankcard file '" + BANKCARD_FILE + "'!";
			logger.warn(errorMessage);
			throw new BankcardException(errorMessage);
		}
	}

	/**
	 * Propertie aus der Bankkarte auslesen.
	 * @param key Key des Properties
	 * @return Inhalt des Properties
	 * @throws BankcardException
	 */
	private static String getProperty(String key) throws BankcardException {
		String property = properties.getProperty(key);
		if (property == null) {
			throw new BankcardException("Bankcard file is incomplete! Propertie '" + key + "' is missing.");
		}
		return property;
	}

	/**
	 * Propertie in der Bankkarte abspeichern.
	 * @param key Key des Properties
	 * @param value Inhalt des Properties
	 * @throws BankcardException
	 */
	private static void setProperty(String key, String value) throws BankcardException {
		Object property = properties.setProperty(key, value);
		if (property == null) {
			throw new BankcardException("Bankcard file is corrupt! Can't set propertie '" + key + "'.");
		}
		try {
			properties.store(new FileOutputStream(getBankcardFile()), null);
		} catch (IOException e) {
			throw new BankcardException("Could not save to bankcard file '" + BANKCARD_FILE + "'.");
		}
	}

	/**
	 * Überprüft das aktuelle Abhebelimit und setzt es zurück, falls notwendig.
	 * @throws BankcardException
	 * @throws DatatypeConfigurationException 
	 */
	private static void updateWithdrawLimit() throws BankcardException {
		try {
			String rawDate = getProperty(KEY_WITHDRAW_DATE);
			XMLGregorianCalendar lastWithdrawDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(rawDate);
			GregorianCalendar actualDate = new GregorianCalendar();
			actualDate.setTime(new Date());
			int elapsedHours = actualDate.get(Calendar.HOUR_OF_DAY) - lastWithdrawDate.getHour();
			if (Math.abs(elapsedHours) >= WITHDRAW_LIMIT_PERIOD) {
				resetWithdrawAmount();
			}
		} catch (DatatypeConfigurationException e) {
			throw new BankcardException("Could not convert withdraw date string!");
		}
	}
	
	/**
	 * Setzt das Abhebelimit zurück.
	 * @throws BankcardException
	 */
	private static void resetWithdrawAmount() throws BankcardException {
		setProperty(KEY_WITHDRAW_AMOUNT, "0");
		updateWithdrawDate();
	}
	
	/**
	 * Aktualisiert das letzte Abhebedatum.
	 * @throws BankcardException
	 */
	private static void updateWithdrawDate() throws BankcardException {
		setProperty(KEY_WITHDRAW_DATE, simpleDateFormat.format(new Date()));
	}

	/**
	 * Gibt die ClearingNumber der eingelegten Bankkarte zurück.
	 * @return CleringNumber des Bankkontos
	 * @throws BankcardException
	 */
	public static int getBankNumber() throws BankcardException {
		try {
			return Integer.parseInt(getProperty(KEY_BANK_NUMBER));
		} catch (NumberFormatException e) {
			throw new BankcardException("Could not convert propertie '" + KEY_BANK_NUMBER + "' to int!");
		}
	}

	/**
	 * Gibt die AccountNumber der eingelegten Bankkarte zurück.
	 * @return AccountNumber des Bankkontos
	 * @throws BankcardException
	 */
	public static int getAccountNumber() throws BankcardException {
		try {
			return Integer.parseInt(getProperty(KEY_ACCOUNT_NUMBER));
		} catch (NumberFormatException e) {
			throw new BankcardException("Could not convert propertie '" + KEY_ACCOUNT_NUMBER + "' to int!");
		}
	}

	/**
	 * Gibt die PIN der eingelegten Bankkarte zurück.
	 * @return PIN des Bankkontos
	 * @throws BankcardException
	 */
	public static String getPin() throws BankcardException {
		return getProperty(KEY_PIN);
	}

	/**
	 * Gibt das Abhebelimit pro Periode zurück.
	 * @return Abhebelimite des Bankkontos
	 * @throws BankcardException
	 */
	public static BigDecimal getWithdrawLimit() throws BankcardException {
		return new BigDecimal(getProperty(KEY_WITHDRAW_LIMIT));
	}

	/**
	 * Gibt das bereits bezogenen Geld in dieser Periode zurück.
	 * @return Bereits bezogenes Geld
	 * @throws BankcardException
	 */
	public static BigDecimal getWithdrawAmount() throws BankcardException {
		return new BigDecimal(getProperty(KEY_WITHDRAW_AMOUNT));
	}

	/**
	 * Überprüft ob die Bankkarte noch über genügend Limite verfügt.
	 * @param amount Gewünschter abzuhebender Betrag
	 * @return True wenn die Limit genügt, sonst false
	 * @throws BankcardException
	 */
	public static boolean checkWithdrawLimit(Money amount) throws BankcardException {
		BigDecimal remainingLimit = getWithdrawLimit().subtract(getWithdrawAmount());
		return (remainingLimit.subtract(amount.getMoney()).intValue() >= 0);
	}

	/**
	 * Fügt dem bereits bezogenen Geld den neuen Betrag hinzu.
	 * @param amount Betrag der abgehoben wurde
	 * @throws BankcardException
	 */
	public static void increaseWithdrawAmount(Money amount) throws BankcardException {
		BigDecimal newLimit = getWithdrawAmount().add(amount.getMoney());
		setProperty(KEY_WITHDRAW_AMOUNT, newLimit.toString());
		updateWithdrawDate();
	}
}
