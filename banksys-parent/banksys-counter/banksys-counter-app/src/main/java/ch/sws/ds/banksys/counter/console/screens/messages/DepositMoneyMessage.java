package ch.sws.ds.banksys.counter.console.screens.messages;

import java.util.ArrayList;

import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Anzeige nach erfolgreichem Einzahlen auf das eigene Bankkonto.
 */
public class DepositMoneyMessage extends MessageScreen {
	private static final String SCREEN_NAME = "Deposit Money Completed";
	
	public DepositMoneyMessage(Screen parentMenu, ArrayList<String> valueList) {
		super(SCREEN_NAME, parentMenu, valueList);
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		addText("Transaction number: " + getResult(ResultKeys.TRANSACTION_NUMBER));
		addText("Account:            " + getResult(ResultKeys.IBAN));
		addText("Amount:             " + getResult(ResultKeys.AMOUNT));
		String balance = getResult(ResultKeys.BALANCE);
		if (balance != null) {
			addText("New balance:        " + balance);
		}
	}
}
