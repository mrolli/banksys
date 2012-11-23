package ch.sws.ds.banksys.counter.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.screens.forms.LoginScreen;

/**
 * @author kambl1
 *
 * Hauptmenü welches nach dem Start angezeigt wird.
 */
public class CounterMenu extends Screen {
	private static final String SCREEN_NAME = "Counter Menu";
	
	public CounterMenu() {
		super(SCREEN_NAME);	
		addMenuItem(KeyEvent.VK_1, new MenuAction(new ManageCustomersMenu(this)));
		addMenuItem(KeyEvent.VK_2, new MenuAction(new ManageAccountsMenu(this)));
		addMenuItem(KeyEvent.VK_3, new MenuAction(new LoginScreen(this)));
	}
}
