package ch.sws.ds.banksys.ebanking.weblogic;

import java.util.Date;

import ch.sws.ds.banksys.common.Money;

/**
 * ViewModel-Klasse für eine einzelne Transaktion.
 * 
 * Die Klasse repräsentiert die von der View benötigten Daten zur
 * Anzeige eine Buchung inklusive dem nach der Buchung aktuellen 
 * Kontostand.
 * 
 * <b>Ein Objekt vom Typ TransferViewBean ist immutable.</b>
 * 
 * Die Klasse wird vom übergeordneten ViewModel {@link TransferListViewBean}
 * verwendet.
 *
 * @author mrolli
 */
public class TransferViewBean {
	private Date valuta;
	private String text;
	private Money debit;
	private Money credit;
	private Money saldo;
	
	/**
	 * Klassenkonstruktor zur Erzeugung eines TransferViewBean.
	 * 
	 * TransferViewBeans sind immutable.
	 * 
	 * @param valuta Valuta der Buchung
	 * @param text Buchungstext
	 * @param debit Gutschriftsbetrag
	 * @param credit Belastungsbetrag
	 * @param saldo Saldo
	 */
	public TransferViewBean(Date valuta, String text, Money debit, Money credit, Money saldo) {
		this.valuta = valuta;
		this.text = text;
		this.debit = debit != null ? new Money(debit.getMoney().abs(), debit.getCurrency()) : null;
		this.credit = credit != null ? new Money(credit.getMoney().abs(), credit.getCurrency()) : null;
		this.saldo = saldo;
	}
	
	/**
	 * Gibt das Valuta der Buchung zurück.
	 * 
	 * @return the valuta
	 */
	public Date getValuta() {
		return valuta;
	}

	/**
	 * Gibt den Buchungstext der Buchung zurück.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gibt den Gutschriftsbetrag der Buchung zurück.
	 * 
	 * Falls es sich um eine Belastung handelt, wird null zurückgegeben.
	 * 
	 * @return the debit
	 */
	public Money getDebit() {
		return debit;
	}

	/**
	 * Gibt den Belastungsbetrag der Buchung zurück.
	 * 
	 * Falls es sich um eine Gutschrift handelt, wird null zurückgegeben.
	 * 
	 * @return the credit
	 */
	public Money getCredit() {
		return credit;
	}

	/**
	 * Gibt den aktuellen Saldo nach der Buchung zurück.
	 * 
	 * @return the saldo
	 */
	public Money getSaldo() {
		return saldo;
	}
}
