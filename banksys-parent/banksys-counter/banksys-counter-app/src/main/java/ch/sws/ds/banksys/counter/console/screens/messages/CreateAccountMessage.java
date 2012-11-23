package ch.sws.ds.banksys.counter.console.screens.messages;

import java.util.ArrayList;

import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Anzeige nach erfolgreichem Erstellen eines Bankkontos.
 */
public class CreateAccountMessage extends MessageScreen {
	private static final String SCREEN_NAME = "Account Created";
	
	public CreateAccountMessage(Screen parentMenu, ArrayList<String> valueList) {
		super(SCREEN_NAME, parentMenu, valueList);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		addText("New account number:  " + getResult(ResultKeys.ACCOUNT_NUMBER));
		addText("Customer number:     " + getValue(0));
		addText("Account PIN:         " + getValue(2));
		addText("Description:         " + getValue(3));
	}
}
