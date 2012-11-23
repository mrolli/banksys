package ch.sws.ds.banksys.counter.console.screens.menus;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.MenuAction;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.LogoutAction;

/**
 * @author kambl1
 *
 * Sicherheitsabfrage beim ausloggen.
 */
public class LogoutMenu extends Screen {
	private static final String SCREEN_NAME = "Logout";

	public LogoutMenu(Screen accountMenu, Screen mainMenu) {
		super(SCREEN_NAME);
		addText("Are you sure you wan't to logout?");
		addMenuItem(KeyEvent.VK_1, new MenuAction("No - Return to Account Menu", accountMenu));
		setReturnAction(new LogoutAction(mainMenu));
	}
}
