package ch.sws.ds.banksys.ebanking.weblogic;

import java.math.BigDecimal;
import java.util.List;

import ch.sws.ds.banksys.common.Account;

/**
 * View Modelklasse der Accountliste.
 * 
 * Das AccontListViewBean-Objekt wird von JSPX-Views für den Zugriff auf
 * eine Liste von Konten verwendet.
 * 
 * @author mrolli
 */
public class AccountListViewBean {
	/**
	 * Liste mit Konten.
	 */
	private final List<Account> accounts;

	/**
	 * Klassenkonstruktor.
	 * 
	 * @param accounts Liste der Accounts
	 */
	public AccountListViewBean(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	/**
	 * Gibt die Kontoliste zurück.
	 * 
	 * @return Liste der Accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}
	
	/**
	 * Gibt die Anzahl der Konten zurück.
	 * @return Anzahl Konti
	 */
	public int getCount() {
		return accounts.size();
	}
	
	/**
	 * Gibt die Summe der Kontostände aller Konten der
	 * Liste zurück.
	 * 
	 * @return Summe der Kontostände
	 */
	public BigDecimal getTotal() {
		BigDecimal sum = new BigDecimal(0);
		for (Account account : accounts) {
			sum = sum.add(account.getBalance().getMoney());
		}
		return sum;
	}
}
