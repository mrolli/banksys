/**
 * 
 */
package ch.sws.ds.banksys.common;

import java.io.Serializable;
import java.util.Date;

/**
 * @author feuzl1
 * 
 */
public class MoneyTransfer implements Serializable {

	/** UID. */
	private static final long serialVersionUID = -6489631336078693273L;

	private Integer number;

	private final IBAN credit;

	private final IBAN debit;

	private final Money amount;

	private final String text;

	private final Date valuta;

	/**
	 * @param number
	 * @param credit
	 * @param debit
	 * @param amount
	 * @param text
	 * @param valuta
	 */
	public MoneyTransfer(Integer number, IBAN credit, IBAN debit, Money amount,
			String text, Date valuta) {
		super();
		this.number = number;
		this.credit = credit;
		this.debit = debit;
		this.amount = amount;
		this.text = text;
		this.valuta = valuta;
	}

	/**
	 * @return the credit
	 */
	public IBAN getCredit() {
		return credit;
	}

	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
	}

	/**
	 * @return the debit
	 */
	public IBAN getDebit() {
		return debit;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the valuta
	 */
	public Date getValuta() {
		return valuta;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MoneyTransfer [number=" + number + ", credit=" + credit
				+ ", debit=" + debit + ", amount=" + amount + ", text=" + text
				+ ", valuta=" + valuta + "]";
	}

}
