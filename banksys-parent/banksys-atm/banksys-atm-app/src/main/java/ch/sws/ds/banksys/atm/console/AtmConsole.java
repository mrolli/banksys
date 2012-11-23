package ch.sws.ds.banksys.atm.console;

import java.awt.event.KeyEvent;

import ch.sws.ds.banksys.atm.console.screens.forms.WelcomeScreen;
import ch.sws.ds.banksys.common.console.Console;

/**
 * @author kambl1
 *
 * Konsole der Counter Applikation.
 */
public class AtmConsole extends Console {
	/**
	 * Taste welche das Programm beendet (Admin-Funktion).
	 * Funktioniert nicht während der Eingabe von Daten (Form).
	 */
	private static final int EXIT_KEY = KeyEvent.VK_X;
	
	public AtmConsole() {
		setCurrentScreen(new WelcomeScreen());
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
