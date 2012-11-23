package ch.sws.ds.banksys.counter.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.screens.forms.CreateCustomerScreen;

/**
 * @author kambl1
 *
 * Menu um die Kunden zu verwalten.
 */
public class ManageCustomersMenu extends Screen {
	private static final String SCREEN_NAME = "Manage Customers Menu";
	
	public ManageCustomersMenu(Screen parentScreen) {
		super(SCREEN_NAME, parentScreen);
		addMenuItem(KeyEvent.VK_1, new MenuAction(new CreateCustomerScreen(this)));
	}
}
