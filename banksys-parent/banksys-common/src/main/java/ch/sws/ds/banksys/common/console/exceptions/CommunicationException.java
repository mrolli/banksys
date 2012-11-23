package ch.sws.ds.banksys.common.console.exceptions;

import ch.sws.ds.banksys.common.console.utils.Settings;

/**
 * @author kambl1
 *
 * Exception für alle Kommunikationsfehler.
 */
public class CommunicationException extends Exception {
	/** UID */
	private static final long serialVersionUID = 1680423386051747326L;
	private String message;

	public CommunicationException() {
		super();
	}
	
	public CommunicationException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Erstellt einen String mit Kommunikationsdetails zur RMI-Schnittstelle
	 * @return Kommunikationsdetails (RMI)
	 */
	private static String addCommunicationDetails() {
		return Settings.getRmiHost() + ":" + Settings.getRmiPort();
	}
	
	/**
	 * Gibt eine detaillierte Fehlermeldung zurück, mit Angaben über die Verbindung.
	 * @return Dateillierte Fehlermeldung
	 */
	public String getDetailedMessage() {
		return "Communication error: " + message + " on " + addCommunicationDetails() + "!";
	}
}
