package ch.sws.ds.banksys.counter.console.screens.messages;

import java.util.ArrayList;

import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Anzeige nach erfolgreichem Löschen eines Bankkontos.
 */
public class DeleteAccountMessage extends MessageScreen {
	private static final String SCREEN_NAME = "Account Deleted";
		
	public DeleteAccountMessage(Screen parentMenu, ArrayList<String> valueList) {
		super(SCREEN_NAME, parentMenu, valueList);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		addText("Account " + getResult(ResultKeys.IBAN) + " deleted!");
	}
}
