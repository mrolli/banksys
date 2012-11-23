package ch.sws.ds.banksys.atm.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.atm.console.actions.AbortAction;
import ch.sws.ds.banksys.atm.console.actions.GetBalanceAction;
import ch.sws.ds.banksys.atm.console.screens.errors.GetBalanceError;
import ch.sws.ds.banksys.atm.console.screens.forms.WithdrawMoneyScreen;
import ch.sws.ds.banksys.atm.console.screens.messages.GetBalanceMessage;
import ch.sws.ds.banksys.common.console.Action;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.common.console.Session;

/**
 * @author kambl1
 *
 * Hauptmen�üwelches nach dem Start angezeigt wird.
 */
public class AtmMenu extends Screen {
	private static final String SCREEN_NAME = "ATM Menu";
	private static final int GET_BALANCE_KEY = KeyEvent.VK_2;
	private Action getBalanceAction;
	
	public AtmMenu(Screen parentScreen) {
		super(SCREEN_NAME);
		getBalanceAction = new GetBalanceAction(new GetBalanceMessage(this), new GetBalanceError(this));
		addMenuItem(KeyEvent.VK_1, new MenuAction(new WithdrawMoneyScreen(this)));
		setReturnAction(new AbortAction(parentScreen));
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		if (Session.getInstance().isOwnCustomer()) {
			addMenuItem(GET_BALANCE_KEY, getBalanceAction);
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onHide()
	 */
	@Override
	public void onHide() {
		removeMenuItem(GET_BALANCE_KEY);
	}
}
