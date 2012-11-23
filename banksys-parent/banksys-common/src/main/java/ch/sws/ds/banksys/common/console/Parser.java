package ch.sws.ds.banksys.common.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kambl1
 * 
 * Der Parser verarbeitet und analysiert alle Benutzereingaben.
 */
public class Parser {
	private BufferedReader input;
	
	/**
	 * @param inputStream InputStream auf welchem die Benutzereingaben erfolgen
	 */
	public Parser(InputStream inputStream) {
		InputStreamReader streamReader = new InputStreamReader(inputStream);
		this.input = new BufferedReader(streamReader);
	}
	
	/**
	 * Waret auf eine Eingabe eines einzelnen Zeichens (Menü).
	 * @return Eingegebenes Zeichen
	 */
	public int waitForAction() {
		return readLine().charAt(0);
	}

	/**
	 * Wartet auf die Eingabe eines Wertes, bestehend aus mehreren Zeichen (Form).
	 * @return Eingegebener Wert
	 */
	public String waitForValue() {
		return readLine();
	}
	
	/**
	 * Prüft ob die Zeile leer ist.
	 * @param line Zu prüfende Zeile
	 * @return True wenn die Zeile leer ist, sonst false
	 */
	private static boolean isLineEmpty(String line) {
		if (line != null) {
			return line.isEmpty();
		}
		return false;
	}
	
	/**
	 * Liest eine Zeile aus dem InputStream, leere Zeilen werden nicht zurückgegeben.
	 * @return Eingelesene Zeile
	 */
	private String readLine() {
		String line;
		do {
			try {
				line = input.readLine();
			} catch (IOException e) {
				throw new RuntimeException("Can't read from input stream!");
			}
		} while (isLineEmpty(line));
		return line;
	}
}
