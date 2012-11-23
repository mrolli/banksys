package ch.sws.ds.banksys.common.console;

/**
 * @author kambl1
 *
 * Dient der Darstellung von Fehlermeldungen.
 */
public class ErrorScreen extends Screen {
	private String message;

	/**
	 * @param name Name des Screens
	 */
	public ErrorScreen(String name) {
		super(name);
		this.message = null;
	}
	
	/**
	 * @param name Name des Screens
	 * @param parentScreen Vorhergeheneder Screen
	 */
	public ErrorScreen(String name, Screen parentScreen) {
		super(name, parentScreen);
		this.message = null;
	}
	
	/**
	 * Prüft ob eine Fehlermeldung vorhanden ist.
	 * @return True falls eine Fehlermeldung vorhanden ist, sonst false
	 */
	public boolean hasMessage() {
		return (message != null);
	}
	
	/**
	 * Gibt die Fehlermeldung zurück.
	 * @return Fehlermeldung
	 */
	protected String getMessage() {
		return message;
	}
	
	/**
	 * Setzt eine neue Fehlermeldung.
	 * @param message Fehlermeldung
	 */
	protected void setMessage(String message) {
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		clearBody();
		if (hasMessage()) {
			addText(getMessage());
		} else {
			addText("The entered data is invalid!");
		}
		addText("Try again or return to menu.");
	}
}
