package ch.sws.ds.banksys.counter.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.Action;
import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.common.console.Session;
import ch.sws.ds.banksys.counter.console.actions.GetBalanceAction;
import ch.sws.ds.banksys.counter.console.screens.errors.GetBalanceError;
import ch.sws.ds.banksys.counter.console.screens.forms.DepositMoneyScreen;
import ch.sws.ds.banksys.counter.console.screens.forms.WithdrawMoneyScreen;
import ch.sws.ds.banksys.counter.console.screens.messages.GetBalanceMessage;

/**
 * @author kambl1
 *
 * Menü für alle Aktionen auf dem Konto (im eingeloggten Zustand).
 */
public class AccountMenu extends Screen {
	private static final String SCREEN_NAME = "Account Menu";
	private static final int GET_BALANCE_KEY = KeyEvent.VK_3;
	private Action getBalanceAction;
	
	public AccountMenu(Screen mainMenu) {
		super(SCREEN_NAME);
		getBalanceAction = new GetBalanceAction(new GetBalanceMessage(this), new GetBalanceError(this));
		addMenuItem(KeyEvent.VK_1, new MenuAction(new DepositMoneyScreen(this)));
		addMenuItem(KeyEvent.VK_2, new MenuAction(new WithdrawMoneyScreen(this)));
		setReturnAction(new MenuAction(new LogoutMenu(this, mainMenu)));
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
