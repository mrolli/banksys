package ch.sws.ds.banksys.common.console;

/**
 * @author kambl1
 *
 * Action welche in den nächsten Screen überführt.
 * Diese ist für die aufbereitung aller Daten für den Screen verantwortlich.
 */
public abstract class Action {
	private String name;
	private Screen nextScreen;
	private ErrorScreen errorScreen;

	/**
	 * @param name Name der Action
	 * @param nextScreen Screen im Erfolgsfall
	 * @param errorScreen Screen im Fehlerfall
	 */
	public Action(String name, Screen nextScreen, ErrorScreen errorScreen) {
		this.name = name;
		this.nextScreen = nextScreen;
		this.errorScreen = errorScreen;
	}
	
	/**
	 * @param name Name der Action
	 * @param nextScreen Nächster Screen
	 */
	public Action(String name, Screen nextScreen) {
		this(name, nextScreen, null);
	}

	/**
	 * Gibt den Namen der Action zurück.
	 * @return Name der Action
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt den nächsten Screen (im Erfolgsfall) zurück.
	 * @return Nächster Screen
	 */
	public Screen getNextScreen() {
		return nextScreen;
	}
	
	/**
	 * Gibt den Screen im Fehlerfall zurück.
	 * @return Screen im Fehlerfall
	 */
	public ErrorScreen getErrorScreen() {
		return errorScreen;
	}
	
	/**
	 * Setzt eine Fehlermeldung auf dem Fehlerfall Screen
	 * @param message Fehlermeldung
	 */
	protected void setErrorMessage(String message) {
		if (errorScreen != null) {
			errorScreen.setMessage(message);
		}
	}
	
	/**
	 * Führt eine Action ohne Parameter aus.
	 * @return Nächster Screen
	 */
	public Screen doAction() {
		return doAction("");
	}
	
	/**
	 * Führt die Aktion mit einem Parameter aus.
	 * @param value Parameter für die Action
	 * @return Nächster Screen
	 */
	public Screen doAction(String value) {
		if (doIt(value)) {
			return nextScreen;
		}
		return errorScreen;
	}

	/**
	 * Zu implementierende Funktion der Action.
	 * @param value Paramter für die Action
	 * @return True falls die Action erfolgreich war, sonst false
	 */
	protected abstract boolean doIt(String value);
}
