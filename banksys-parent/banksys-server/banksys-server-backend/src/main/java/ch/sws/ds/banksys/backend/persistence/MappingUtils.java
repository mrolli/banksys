/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_COUNTRYCODE;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.sws.ds.banksys.backend.persistence.provided.Transaction;
import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.AccountState;
import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.CustomerState;

/**
 * Klasse enthält sämtliche Mapping-Methoden die vom Auftraggeber gelieferten
 * Transferobjekte in die von uns implementierten Transferobjekte zu wanden (und
 * zurück).
 * 
 * @author feuzl1
 */
public class MappingUtils {

	private static final String COUNTRYCODE = getProperty(BANKSYS_COUNTRYCODE);
	private static final String IBAN_FORMAT = "%s-%05d-%010d";
	private static final String DELIMITER = ",";
	private static final String IBAN_DELIMITER = "-";
	private static final String NA = "N/A";

	private MappingUtils() {
		// nothing..
	}

	public static Customer convertTOCustomer(
			ch.sws.ds.banksys.backend.persistence.provided.Customer prov) {
		if (prov == null)
			return null;
		return new Customer(prov.getNr(), Integer.parseInt(prov.getPin()),
				convertToName(prov.getName()),
				convertToAddress(prov.getAddress()), CustomerState.valueOf(prov
						.getState().name()));
	}

	public static ch.sws.ds.banksys.backend.persistence.provided.Customer convertToProvCustomer(
			Customer customer) {
		ch.sws.ds.banksys.backend.persistence.provided.Customer prov = new ch.sws.ds.banksys.backend.persistence.provided.Customer(
				customer.getNumber(), convertToString(customer.getName()),
				convertToString(customer.getAddress()),
				Integer.toString(customer.getPin()),
				ch.sws.ds.banksys.backend.persistence.provided.Customer.State
						.valueOf(customer.getState().name()));
		return prov;
	}

	public static Name convertToName(String name) {
		String[] split = name.split(DELIMITER);
		if (split.length >= 2) {
			return new Name(split[1], split[0]);
		}
		return new Name(split[0], NA);
	}

	public static String convertToString(Name name) {
		StringBuffer sb = new StringBuffer();
		if (name.getLastName() != null)
			sb.append(name.getLastName());
		sb.append(DELIMITER);
		if (name.getFirstName() != null)
			sb.append(name.getFirstName());
		return sb.toString();
	}

	public static Address convertToAddress(String address) {
		if (address == null || address.isEmpty())
			return null;
		String[] split = address.split(DELIMITER);
		Address addr = new Address();
		addr.setHouseNumber(Integer.parseInt(split[1]));
		addr.setPostalCode(Integer.parseInt(split[2]));
		addr.setStreetName(split[0]);
		addr.setCity(split[3]);
		addr.setCountry(split[4]);
		return addr;
	}

	public static String convertToString(Address address) {
		if (address == null)
			return null;
		StringBuffer sb = new StringBuffer();
		if (address.getStreetName() != null)
			sb.append(address.getStreetName());
		else
			sb.append(NA);
		sb.append(DELIMITER);
		sb.append(address.getHouseNumber());
		sb.append(DELIMITER);
		sb.append(address.getPostalCode());
		sb.append(DELIMITER);
		if (address.getCity() != null)
			sb.append(address.getCity());
		else
			sb.append(NA);
		sb.append(DELIMITER);
		if (address.getCountry() != null)
			sb.append(address.getCountry());
		else
			sb.append(NA);
		return sb.toString();
	}

	public static MoneyTransfer convertToMoneyTransfer(Transaction transaction,
			Integer clearingNbr) {
		MoneyTransfer mt = new MoneyTransfer(transaction.getNr(), new IBAN(
				COUNTRYCODE, clearingNbr,
				transaction.getCreditAccountNr()), new IBAN(
				COUNTRYCODE, clearingNbr,
				transaction.getDebitAccountNr()), new Money(
				transaction.getAmount(), Currency.CHF), transaction.getText(),
				transaction.getValuta());
		return mt;
	}

	public static Transaction convertToProvTransaction(
			MoneyTransfer moneyTransfer) {
		return new Transaction(moneyTransfer.getNumber(), moneyTransfer
				.getDebit().getAccountNumber(), moneyTransfer.getCredit()
				.getAccountNumber(), moneyTransfer.getValuta(), moneyTransfer
				.getAmount().getMoney(), moneyTransfer.getText());
	}

	public static String convertToString(IBAN iban) {
		return String.format(IBAN_FORMAT, iban.getCountryCode(),
				iban.getClearingNumber(), iban.getAccountNumber());
	}

	public static IBAN convertToIBAN(String iban) {
		String[] split = iban.split(IBAN_DELIMITER);
		return new IBAN(split[0], Integer.parseInt(split[1]),
				Integer.parseInt(split[2]));
	}

	public static Account convertToAccount(
			ch.sws.ds.banksys.backend.persistence.provided.Account prov) {
		if (prov == null)
			return null;
		Account account = new Account();
		account.setBalance(new Money(prov.getBalance(), Currency.CHF));
		account.setDescription(prov.getDescription());
		account.setNumber(prov.getNr());
		account.setCustomerNbr(prov.getCustomerNr());
		account.setState(AccountState.valueOf(prov.getState().name()));
		return account;
	}

	public static ch.sws.ds.banksys.backend.persistence.provided.Account convertToProvAccount(
			Account account) {
		ch.sws.ds.banksys.backend.persistence.provided.Account acc = new ch.sws.ds.banksys.backend.persistence.provided.Account(
				account.getNumber(), account.getCustomerNbr(),
				account.getDescription(), account.getBalance().getMoney(),
				ch.sws.ds.banksys.backend.persistence.provided.Account.State
						.valueOf(account.getState().name()));
		return acc;
	}

	public static org.dibas.clearing.MoneyTransfer convertToProvMoneyTransfer(
			MoneyTransfer moneyTransfer) {
		org.dibas.clearing.MoneyTransfer prov = new org.dibas.clearing.MoneyTransfer();
		prov.setAmount(moneyTransfer.getAmount().getMoney());
		prov.setFromAccount(moneyTransfer.getCredit().getAccountNumber());
		prov.setFromBank(moneyTransfer.getCredit().getClearingNumber());
		prov.setText(moneyTransfer.getText());
		prov.setToAccount(moneyTransfer.getDebit().getAccountNumber());
		prov.setToBank(moneyTransfer.getDebit().getClearingNumber());

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(moneyTransfer.getValuta());
		XMLGregorianCalendar xmlDate;
		try {
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			prov.setValuta(xmlDate);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Illegal date format!", e);
		}

		return prov;
	}

	public static MoneyTransfer convertToMoneyTransfer(
			org.dibas.clearing.MoneyTransfer prov) {
		MoneyTransfer mt = new MoneyTransfer(null, new IBAN(
				COUNTRYCODE, prov.getFromBank(),
				prov.getFromAccount()), new IBAN(
				COUNTRYCODE, prov.getToBank(),
				prov.getToAccount()),
				new Money(prov.getAmount(), Currency.CHF), prov.getText(), prov
						.getValuta().toGregorianCalendar().getTime());
		return mt;
	}
}
