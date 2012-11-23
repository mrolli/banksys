package ch.sws.ds.banksys.atm.console.screens.errors;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Fehlermeldung falls beim Abheben vom eigenen Bankkonto ein Fehler auftritt.
 */
public class WithdrawMoneyError extends ErrorScreen {
	private static final String SCREEN_NAME = "Withdraw Money Error";
	
	public WithdrawMoneyError(Screen parentScreen, Screen parentMenu) {
		super(SCREEN_NAME, parentMenu);
		addMenuItem(KeyEvent.VK_1, new MenuAction(parentScreen));
	}
}
