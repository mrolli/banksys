package ch.sws.ds.banksys.counter.console.screens.errors;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.Screen;

/**
 * @author kambl1
 *
 * Fehlermeldung falls beim Überprüfen des Saldos ein Fehler auftritt.
 */
public class GetBalanceError extends ErrorScreen {
	private static final String SCREEN_NAME = "Get Balance Error";
	
	public GetBalanceError(Screen parentMenu) {
		super(SCREEN_NAME, parentMenu);
	}
}
