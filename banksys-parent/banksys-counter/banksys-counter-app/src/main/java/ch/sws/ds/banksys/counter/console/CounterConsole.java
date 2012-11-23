package ch.sws.ds.banksys.counter.console;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.common.console.Console;
import ch.sws.ds.banksys.counter.console.screens.menus.CounterMenu;

/**
 * @author kambl1
 *
 * Konsole der Counter Applikation.
 */
public class CounterConsole extends Console {
	/**
	 * Taste welche das Programm beendet (Admin-Funktion).
	 * Funktioniert nicht während der Eingabe von Daten (Form).
	 */
	private static final int EXIT_KEY = KeyEvent.VK_X;
	
	public CounterConsole() {
		setCurrentScreen(new CounterMenu());
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Console#keyPressed(int)
	 */
	@Override
	public void keyPressed(int key) {
		if (key == EXIT_KEY) {
			stop();
		}
	}
}
