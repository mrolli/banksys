/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author feuzl1
 * 
 */
public class Money implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 6744770034721947080L;

	private final BigDecimal money;

	private final Currency currency;

	/**
	 * @param money
	 * @param currency
	 */
	public Money(BigDecimal money, Currency currency) {
		super();
		this.money = money;
		this.currency = currency;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @return the money
	 */
	public BigDecimal getMoney() {
		return money;
	}
}
