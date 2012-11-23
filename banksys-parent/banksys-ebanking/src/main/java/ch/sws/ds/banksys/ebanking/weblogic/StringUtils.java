package ch.sws.ds.banksys.ebanking.weblogic;

/**
 * Utility-Klasse mit statischen Stringmethoden.
 * 
 * @author mrolli
 */
public class StringUtils {
	/**
	 * Sanitizing Methode zur Sicherung von Inputtext.
	 * 
	 * Die Methode ersetzt die Zeichen <, >, &, " und ' durch HTML-Entites,
	 * um XSS-Angriffe zu verhindern.
	 * 
	 * @param input Unsicherer Text
	 * @return Sichererer Text
	 */
	public static String sanitizeInput(String input) {
		input.replaceAll("<", "&lt;");
		input.replaceAll(">", "&gt;");
		input.replaceAll("&", "&amp;");
		input.replaceAll("\"", "&quot;");
		input.replaceAll("'", "&apos;");
		return input;
	}
}
