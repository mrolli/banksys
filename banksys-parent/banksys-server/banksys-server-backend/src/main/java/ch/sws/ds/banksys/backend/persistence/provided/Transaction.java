package ch.sws.ds.banksys.backend.persistence.provided;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The class Transaction contains the data of a bank transaction.
 * 
 * @author Stephan Fischli
 * @version 2.0
 */
@SuppressWarnings("serial")
public class Transaction implements Serializable {

	private Integer nr;
	private Integer debitAccountNr;
	private Integer creditAccountNr;
	private Date valuta;
	private BigDecimal amount;
	private String text;

	public Transaction() {
	}

	public Transaction(Integer nr, Integer debitAccountNr,
			Integer creditAccountNr, Date valuta, BigDecimal amount, String text) {
		this.nr = nr;
		this.debitAccountNr = debitAccountNr;
		this.creditAccountNr = creditAccountNr;
		this.valuta = valuta;
		this.amount = amount;
		this.text = text;
	}

	public Integer getNr() {
		return nr;
	}

	public Integer getDebitAccountNr() {
		return debitAccountNr;
	}

	public Integer getCreditAccountNr() {
		return creditAccountNr;
	}

	public Date getValuta() {
		return valuta;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return nr + ":" + debitAccountNr + ":" + creditAccountNr + ":" + valuta
				+ ":" + amount + ":" + text;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Transaction))
			return false;
		Transaction other = (Transaction) object;
		return nr.equals(other.nr)
				&& debitAccountNr.equals(other.debitAccountNr)
				&& creditAccountNr.equals(other.creditAccountNr)
				&& valuta.equals(other.valuta)
				&& amount.compareTo(other.amount) == 0
				&& text.equals(other.text);
	}
}
