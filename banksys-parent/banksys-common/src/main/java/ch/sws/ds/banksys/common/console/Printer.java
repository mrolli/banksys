package ch.sws.ds.banksys.common.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;

/**
 * @author kambl1
 *
 * Der Printer ist zuständig für die Ausgaben auf die Konsole.
 */
public class Printer {
	private static final String HORIZONTAL_DECORATOR = "=";
	private static final String VERTICAL_DECORATOR = "|";
	private static final String PROMPT = ">";
	private static final int HEIGHT = 15;
	private static final int WIDTH = 80;
	private static Logger logger = Logger.getLogger(Printer.class);
	
	private PrintStream output;
	private Screen previousScreen;
	
	/**
	 * @param output PrintStream auf welchem die Ausgabe erfolgen soll.
	 */
	public Printer(PrintStream output) {
		this.output = output;
		this.previousScreen = null;
	}
	
	/**
	 * Gibt die maximal verwendbare Breite der Konsole aus.
	 * @return Maximale Breite
	 */
	public static int getMaximalWidth() {
		return WIDTH - 4; // -4 = 2x VERTICAL_DECORATOR + 2x SPACE
	}
	
	/**
	 * Gibt einen Screen auf der Konsole aus.
	 * @param screen Anzuzeigender Screen
	 */
	public void print(Screen screen) {
		handleSignals(screen);
		clearScreen();
		printTitel(screen);
		printMenu(screen);
		printBody(screen);
		fillScreen(screen);
		printPrompt();
	}
	
	/**
	 * Löst die Signale beim Neuzeichnen aus.
	 * @param screen Betroffener Screen
	 */
	private void handleSignals(Screen screen) {
		if (previousScreen != null && previousScreen != screen) {
			previousScreen.onHide();
		}
		previousScreen = screen;
		screen.onShow();
	}

	/**
	 * Löscht die Konsole. Die Grösse eines Screens wird leer geschrieben.
	 */
	private void clearScreen() {
		for (int i = 0; i < HEIGHT; i++) {
			output.println('\n');
		}
	}

	/**
	 * Gibt den Titel für einen Screen aus.
	 * @param screen Betroffener Screen
	 */
	private void printTitel(Screen screen) {
		printDecoratorLine();
		printLine(screen.getName());
		printDecoratorLine();
	}

	/**
	 * Gibt das Menü eines Screens aus.
	 * @param screen Betroffener Screen
	 */
	private void printMenu(Screen screen) {
		SortedMap<Integer, Action> menuItems = screen.getMenuItems();
		Set<Integer> keys = menuItems.keySet();
		for (int key: keys) {
			printMenuLine(key, menuItems.get(Integer.valueOf(key)));
		}
		printReturnLine(screen);
	}

	/**
	 * Gibt den Body eines Screens aus.
	 * @param screen Betroffener Screen
	 */
	private void printBody(Screen screen) {
		if (screen.hasBody()) {
			if (screen.hasNavigation()) {
				printEmptyLine();
			}
			ArrayList<String> body = screen.getBody();
			for (int i = 0; i < body.size(); i++) {
				printLine(body.get(i));
			}
		}
	}
	
	/**
	 * Füllt den nicht benutzten Bereich mit leeren Zeilen.
	 * @param screen Betroffener Screen
	 */
	private void fillScreen(Screen screen) {
		int unusedLines = HEIGHT - 4;
		unusedLines -= screen.getMenuItems().size();
		if (screen.hasReturnAction()) {
			if (screen.hasMenuItems()) {
				unusedLines -= 2;
			} else {
				unusedLines -= 1;
			}
		}
		if (screen.hasBody()) {
			if (screen.hasNavigation()) {
				unusedLines -= 1;
			}
			unusedLines -= screen.getBody().size();
		}
		if (unusedLines < 0) {
			logger.warn("printer: screen has too much lines!");
		} else {
			for (int i = 0; i < unusedLines - 1; i++) {
				printEmptyLine();
			}
			printDecoratorLine();
		}
	}

	/**
	 * Gibt die Eingabeaufforderungs-Zeile aus.
	 */
	private void printPrompt() {
		output.print(VERTICAL_DECORATOR + " " + PROMPT + " ");
	}
	
	/**
	 * Gibt einen Text auf der Konsole aus und umrandet diesen mit den Decoratern.
	 * @param text Auszugebender Text.
	 */
	private void printLine(String text) {
		StringBuffer line = new StringBuffer(VERTICAL_DECORATOR + " " + text);
		int missingLength = WIDTH - line.length();
		if (missingLength < 0) {
			logger.warn("printer: screen has too much lines!");
		} else {
			for (int i = 0; i < missingLength - 1; i++) {
				line.append(" ");
			}
			line.append(VERTICAL_DECORATOR);
		}
		output.println(line.delete(WIDTH, line.length()));
	}

	/**
	 * Gibt eine leere Zeile aus.
	 */
	private void printEmptyLine() {
		printLine("");
	}

	/**
	 * Gibt eine Zeile, bestehend aus Decoratern, aus.
	 */
	private void printDecoratorLine() {
		StringBuffer line = new StringBuffer(VERTICAL_DECORATOR);
		for (int i = 0; i < WIDTH - 2; i++) {
			line.append(HORIZONTAL_DECORATOR);
		}
		line.append(VERTICAL_DECORATOR);
		output.println(line);
	}
	
	/**
	 * Gibt eine Menü-Zeile aus.
	 * @param key Taste um in diesen Menüpunkt zu gelangen
	 * @param action Auszuführende Action
	 */
	private void printMenuLine(int key, Action action) {
		printLine("> " + (char)key + ". " + action.getName());
	}

	/**
	 * Gibt einen ReturnAction Menüpunkt aus.
	 * @param screen Screen auf welchen zurückgekehrt wird
	 */
	private void printReturnLine(Screen screen) {
		if (screen.hasReturnAction()) {
			if (screen.hasMenuItems()) {
				printEmptyLine();
			}
			printMenuLine(Screen.RETURN_KEY, screen.getReturnAction());
		}
	}
}
