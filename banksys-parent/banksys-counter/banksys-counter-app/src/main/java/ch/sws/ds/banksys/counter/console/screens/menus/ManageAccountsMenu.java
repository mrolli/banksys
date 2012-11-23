package ch.sws.ds.banksys.counter.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.screens.forms.CreateAccountScreen;
import ch.sws.ds.banksys.counter.console.screens.forms.DeleteAccountScreen;

/**
 * @author kambl1
 *
 * Menü um die Bankkontos zu verwalten.
 */
public class ManageAccountsMenu extends Screen {
	private static final String SCREEN_NAME = "Manage Accounts Menu";
	
	public ManageAccountsMenu(Screen parentScreen) {
		super(SCREEN_NAME, parentScreen);
		addMenuItem(KeyEvent.VK_1, new MenuAction(new CreateAccountScreen(this)));
		addMenuItem(KeyEvent.VK_2, new MenuAction(new DeleteAccountScreen(this)));
	}
}
