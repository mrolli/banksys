package ch.sws.ds.banksys.counter.console.screens.messages;

import java.util.ArrayList;

import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Anzeige nach erfolgreichem Erstellen eines Kunden.
 */
public class CreateCustomerMessage extends MessageScreen {
	private static final String SCREEN_NAME = "Customer Created";
	
	public CreateCustomerMessage(Screen parentMenu, ArrayList<String> valueList) {
		super(SCREEN_NAME, parentMenu, valueList);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		addText("Customer number: " + getResult(ResultKeys.CUSTOMER_NUMBER));
		addText("Firstname:       " + getValue(0));
		addText("Lastname:        " + getValue(1));
		addText("PIN:             " + getValue(2));
		addText("Address:         " + getResult(ResultKeys.ADDRESS_LINE_1));
		addText("                 " + getResult(ResultKeys.ADDRESS_LINE_2));
		addText("                 " + getResult(ResultKeys.ADDRESS_LINE_3));
	}
}
