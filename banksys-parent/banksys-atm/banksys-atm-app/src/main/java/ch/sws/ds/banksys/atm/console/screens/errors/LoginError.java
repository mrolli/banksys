package ch.sws.ds.banksys.atm.console.screens.errors;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Fehlermeldung falls beim Einloggen ein Fehler auftritt.
 */
public class LoginError extends ErrorScreen {
	private static final String SCREEN_NAME = "Login Error";
	
	public LoginError(Screen parentScreen) {
		super(SCREEN_NAME);
		setReturnAction(new MenuAction("Abort", parentScreen));
	}
}
