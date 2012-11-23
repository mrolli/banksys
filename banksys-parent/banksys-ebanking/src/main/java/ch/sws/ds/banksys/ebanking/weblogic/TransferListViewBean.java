package ch.sws.ds.banksys.ebanking.weblogic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;

/**
 * ViewModel zur Kapselung der Buchungen eines Kontos.
 * 
 * Diese Klasse übernimmt die Weblogik zur Sortierung von Buchungen und
 * anschliessender Berechnung des Kontostandes.
 * 
 * Nach der Sortierung nach Datum absteigend wird aus dem aktuellen Saldo für
 * jede Buchung, chronolgisch in die Vergangenheit, der nach der
 * jeweiligenBuchung aktuellen Saldo berechnet. Die Buchungsdetails sowie der
 * Saldo werden dann für jede Buchung in TransferViewBeans verpackt und in einer
 * Liste gespeichert.
 * 
 * @author mrolli
 */
public class TransferListViewBean {
	private final List<TransferViewBean> transfers = new ArrayList<TransferViewBean>();
	private final IBAN accountIban;

	/**
	 * Klassenkonstruktor zur Erzeugung der Buchungliste für die Vew.
	 * 
	 * Details siehe Klassenbeschreibung.
	 * 
	 * @param transfers
	 *            Liste der Buchungen
	 * @param account
	 *            Konto, auf das sich die Buchungen beziehen
	 * @param clearingNumber
	 *            Clearingnummer der Bank
	 */
	public TransferListViewBean(List<MoneyTransfer> transfers, Account account,
			int clearingNumber) {
		Money saldo = account.getBalance();
		accountIban = new IBAN("ch", clearingNumber, account.getNumber());

		// first sort the incoming transfers by date desc to compute the correct
		// saldo
		Collections.sort(transfers, new Comparator<MoneyTransfer>() {
			public int compare(MoneyTransfer o1, MoneyTransfer o2) {
				return -1 * o1.getValuta().compareTo(o2.getValuta());
			}
		});

		// the pump in to view model
		for (MoneyTransfer t : transfers) {
			try {
				TransferViewBean tvb = new TransferViewBean(t.getValuta(),
						t.getText(), isDebitTransfer(t) == true ? t.getAmount()
								: null,
						isDebitTransfer(t) == false ? t.getAmount() : null,
						saldo);
				this.transfers.add(tvb);
				BigDecimal amount = null;
				if (isDebitTransfer(t)) {
					amount = saldo.getMoney()
							.subtract(t.getAmount().getMoney().abs());
				} else {
					amount = saldo.getMoney().add(t.getAmount().getMoney().abs());
				}
				saldo = new Money(amount, t.getAmount().getCurrency());
			} catch (Exception e) {
				Logger logger = Logger.getLogger(TransferListViewBean.class);
				logger.error(e);
			}
		}
	}

	/**
	 * Gibt die Liste der Buchungen an die View zurück.
	 * 
	 * @return List der Buchungen
	 */
	public List<TransferViewBean> getTransfers() {
		return transfers;
	}

	/**
	 * Gibt die Anzahl der Buchungen des Kontos zurück.
	 * 
	 * @return Anzahl der Buchungen
	 */
	public int getCount() {
		return transfers.size();
	}

	/**
	 * Hilfsmethode zur Ermittlung, ob es sich bei einer Buchung um eine
	 * Gutschrift oder eine Belastung handelt.
	 * 
	 * @param t
	 *            Buchung
	 * @return true falls es sich um eine Gutschrift handelt, false falls es
	 *         sich um eine Belastung handelt
	 * @throws Falls
	 *             eine Buchung nicht zum betrachteten Konto gehört
	 */
	private boolean isDebitTransfer(MoneyTransfer t) {
		if (t.getDebit().equals(accountIban)
				&& t.getAmount().getMoney().doubleValue() >= 0) {
			return true;
		} else if (t.getCredit().equals(accountIban)
				|| (t.getDebit().equals(accountIban) && t.getAmount()
						.getMoney().doubleValue() < 0)) {
			return false;
		} else {
			throw new IllegalArgumentException(
					"Unrelated transfer encountered!");
		}
	}
}
