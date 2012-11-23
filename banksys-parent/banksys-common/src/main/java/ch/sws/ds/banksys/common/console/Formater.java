package ch.sws.ds.banksys.common.console;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;

/**
 * @author kambl1
 *
 * Formatiert verschieden Datensätze für die Ausgabe auf den Screens.
 */
public class Formater {
	private static final String FORMAT = "+ #,###,##0.00;- #,###,##0.00";
	private static final NumberFormat numberFormat = new DecimalFormat(FORMAT);

	/**
	 * Formatiert eine IBAN für die Ausgabe.
	 * @param iban Zu formatierende IBAN
	 * @return Formatierte IBAN
	 */
	public static String formatIBAN(IBAN iban) {
		return iban.getCountryCode() + "-" + iban.getClearingNumber() + "-" + iban.getAccountNumber();
	}
	
	/**
	 * Formatiert einen Geldbetrag für die Ausgabe.
	 * @param money Zu formatierenden Geldbetrag
	 * @return Formatierter Geldbetrag
	 */
	public static String formatMoney(Money money) {
		return numberFormat.format(money.getMoney()) + " " + money.getCurrency();
	}

	/**
	 * Formatiert die erste Zeile einer Adresse für die Ausgabe.
	 * @param address Zu formatierende Adresse
	 * @return Formatierte Adresszeile
	 */
	public static String formatAddressLine1(Address address) {
		return address.getStreetName() + " " + address.getHouseNumber();
	}

	/**
	 * Formatiert die zweite Zeile einer Adresse für die Ausgabe.
	 * @param address Zu formatierende Adresse
	 * @return Formatierte Adresszeile
	 */
	public static String formatAddressLine2(Address address) {
		return address.getPostalCode() + " " + address.getCity();
	}

	/**
	 * Formatiert die dritte Zeile einer Adresse für die Ausgabe.
	 * @param address Zu formatierende Adresse
	 * @return Formatierte Adresszeile
	 */
	public static String formatAddressLine3(Address address) {
		return address.getCountry();
	}
}
