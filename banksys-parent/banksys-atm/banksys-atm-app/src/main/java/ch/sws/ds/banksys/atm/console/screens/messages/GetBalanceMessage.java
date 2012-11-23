package ch.sws.ds.banksys.atm.console.screens.messages;

import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Anzeige nach erfolgreichem Abfragen des Saldos.
 */
public class GetBalanceMessage extends MessageScreen {
	private static final String SCREEN_NAME = "Get Balance";
	
	public GetBalanceMessage(Screen parentMenu) {
		super(SCREEN_NAME, parentMenu, null);
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		addText("Account: " + getResult(ResultKeys.IBAN));
		addText("Balance: " + getResult(ResultKeys.BALANCE));
	}
}
