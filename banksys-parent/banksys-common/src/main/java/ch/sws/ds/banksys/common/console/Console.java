package ch.sws.ds.banksys.common.console;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 * @author kambl1
 *
 * Die Konsole ist zuständig für die Anzeige sowie das Einlesen von Benutzereingaben.
 * Diese werden dan verwertet und ein entsprechende Änderung der Anzeige wird vorgenommen.
 */
public abstract class Console {
	private static final PrintStream OUTPUT = System.out;
	private static final InputStream INPUT = System.in;
	private static Logger logger = Logger.getLogger(Console.class);
	
	private Printer printer;
	private Parser parser;
	private Screen currentScreen;
	private boolean isRunning;

	protected Console() {
		this.printer = new Printer(OUTPUT);
		this.parser = new Parser(INPUT);
		this.currentScreen = null;
		this.isRunning = false;
	}
	
	/**
	 * Prüft ob bereits ein aktueller Screen gesetzt wurde.
	 * @return True falls ein aktueller Screen gsetzt ist, sonst false
	 */
	private boolean hasCurrentScreen() {
		return (currentScreen != null);
	}
	
	/**
	 * Setzt den aktuellen Screen
	 * @param currentScreen Aktueller Screen
	 */
	protected void setCurrentScreen(Screen currentScreen) {
		this.currentScreen = currentScreen;
	}
	
	/**
	 * Zeigt den übergebenen Screen auf der Konsole.
	 * @param screen Anzuzeigender Screen
	 */
	private void printScreen(Screen screen) {
		if (screen != null) {
			printer.print(screen);
		} else {
			throw new RuntimeException("Screen is null! Your code is wrong.");
		}
	}
	
	/**
	 * Wartet auf eine Benutzereingabe, verarbeitet diese und gibt den nächsten Screen zurück.
	 * @return Nächster Screen
	 */
	private Screen waitForAction() {
		Screen newScreen = null;
		do {
			printScreen(currentScreen);
			if (currentScreen.hasNavigation()) {
				int key = parser.waitForAction();
				keyPressed(key);
				newScreen = currentScreen.doAction(key);
			} else if (currentScreen.hasFormAction()) {
				String value = parser.waitForValue();
				newScreen = currentScreen.doAction(value);
			} else {
				throw new RuntimeException("Incomplete screen <" + currentScreen.getName() + ">! Your code is wrong.");
			}
		} while (newScreen == null && isRunning);
		return newScreen;
	}

	/**
	 * Startet die Konsole und wartet fortlaufend auf neue Eingaben.
	 */
	public void start() {
		if (hasCurrentScreen()) {
			isRunning = true;
			logger.trace("Console started.");
			while (isRunning) {
				Screen newScreen = waitForAction();
				currentScreen = newScreen;
			}
			logger.trace("Console stopped.");
		} else {
			throw new RuntimeException("Console has no currentScreen! Your code is wrong.");
		}
	}

	/**
	 * Stoppt die Konsole.
	 */
	protected void stop() {
		isRunning = false;
	}
	
	/**
	 * Wird bei jedem Tastendruck aufgerufen.
	 * @param key Gedrückte Taste.
	 */
	protected abstract void keyPressed(int key);
}
