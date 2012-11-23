package ch.sws.ds.banksys.counter.console.screens.errors;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Fehlermeldung falls beim Löschen eines Bankkontos ein Fehler auftritt.
 */
public class DeleteAccountError extends ErrorScreen {
	private static final String SCREEN_NAME = "Delete Account Error";
	
	public DeleteAccountError(Screen parentScreen, Screen parentMenu) {
		super(SCREEN_NAME, parentMenu);
		addMenuItem(KeyEvent.VK_1, new MenuAction(parentScreen));
	}
}
