package ch.sws.ds.banksys.counter.console.screens.errors;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Fehlermeldung falls beim Erstellen eines Kunden ein Fehler auftritt.
 */
public class CreateCustomerError extends ErrorScreen {
	private static final String SCREEN_NAME = "Create Customer Error";
	
	public CreateCustomerError(Screen parentScreen, Screen parentMenu) {
		super(SCREEN_NAME, parentMenu);
		addMenuItem(KeyEvent.VK_1, new MenuAction(parentScreen));
	}
}
