package ch.sws.ds.banksys.common.console;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author kambl1
 *
 * Dient der Darstellung von Daten für den Benutzer.
 */
public class Screen {
	/**
	 * Taste '0' um in den vorhergehenden Screen zurückzukehren.
	 */
	public static final int RETURN_KEY = KeyEvent.VK_0;
	
	private String name;
	private SortedMap<Integer, Action> menuItems;
	private ArrayList<String> body;
	private Action returnAction;
	private Action formAction;

	/**
	 * @param name Name des Screens
	 */
	public Screen(String name) {
		this.name = name;
		this.menuItems = new TreeMap<>();
		this.body = new ArrayList<>();
		this.returnAction = null;
		this.formAction = null;
	}
	
	/**
	 * @param name Name des Screen
	 * @param parentScreen Vorhergehender Screen
	 */
	public Screen(String name, Screen parentScreen) {
		this(name);
		this.returnAction = new MenuAction(parentScreen);
	}
	
	/**
	 * Gibt den Namen des Screens zurück.
	 * @return Name des Screen
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt eine sortierte Map aller MenuItems zurèck.
	 * @return Sortierte Map aller MenuItems
	 */
	public SortedMap<Integer, Action> getMenuItems() {
		return menuItems;
	}

	/**
	 * Prüft ob Navigation (Menüpunkte oder ReturnAction) vorhanden ist.
	 * @return True falls Navigation vorhanden ist, sonst false
	 */
	public boolean hasNavigation() {
		return (hasMenuItems() || hasReturnAction());
	}

	/**
	 * Prüft ob Menüpunkte vorhanden sind.
	 * @return True falls Menüpunkte vorhanden sind, sonst false
	 */
	public boolean hasMenuItems() {
		return (menuItems.size() > 0);
	}

	/**
	 * Prüft ob eine ReturnAction vorhanden sind.
	 * @return True falls ReturnAction vorhanden, sonst false
	 */
	public boolean hasReturnAction() {
		return (returnAction != null);
	}
	
	/**
	 * Fügt einen Menüpunkt hinzu.
	 * @param key Key des Menüpunkts
	 * @param action Action für diesen Menüpunkt
	 */
	public void addMenuItem(int key, Action action) {
		if (key != RETURN_KEY) {
			if (menuItems.containsKey(Integer.valueOf(key))) {
				throw new RuntimeException(name + ": <" + (char)key + "> can't be mapped twice!");
			}
			menuItems.put(Integer.valueOf(key), action);
		} else {
			throw new RuntimeException(name + ": <" + (char)RETURN_KEY + "> is not allowed as menu item (reserved for returnAction)!");
		}
	}
	
	/**
	 * Entfernt einen Menüpunkt.
	 * @param key Key des Menüpunkts
	 */
	public void removeMenuItem(int key) {
		menuItems.remove(Integer.valueOf(key));
	}

	/**
	 * Gibt die gesetzte ReturnAction zurück.
	 * @return ReturnAction
	 */
	public Action getReturnAction() {
		return returnAction;
	}

	/**
	 * Setzt eine ReturAction.
	 * @param returnAction ReturnAction
	 */
	public void setReturnAction(Action returnAction) {
		this.returnAction = returnAction;
	}

	/**
	 * Prüft ob ein Body (Text) vorhanden ist.
	 * @return True wenn ein Body vorhanden ist, sonst false
	 */
	public boolean hasBody() {
		return (body.size() > 0);
	}

	/**
	 * Gibt eine Liste mit allen Textzeilen (Body) zurück.
	 * @return Liste mit Textzeilen
	 */
	public ArrayList<String> getBody() {
		return body;
	}
	
	/**
	 * Fügt eine neue Textzeile zum Body hinzu.
	 * @param text Textzeile
	 */
	public void addText(String text) {
		body.add(text);
	}
	
	/**
	 * Löscht den Body.
	 */
	protected void clearBody() {
		body.clear();
	}

	/**
	 * Prüft ob eine FormAction gesetzt wurde.
	 * @return True falls eine FormAction vorhanden ist, sonst false
	 */
	public boolean hasFormAction() {
		return (formAction != null);
	}
	
	/**
	 * Setzt eine FormAction.
	 * @param action FormAction
	 */
	public void setFormAction(FormAction action) {
		formAction = action;
	}

	/**
	 * Führt die gewünschte Aktion aus, entsprechend dem übergebenen Key.
	 * @param key Gedrückte Taste
	 * @return Nächster Screen
	 */
	public Screen doAction(int key) {
		if (key == RETURN_KEY) {
			if (hasReturnAction()) {
				return returnAction.doAction();
			}
			return null;
		}
		Action action = menuItems.get(Integer.valueOf(key));
		if (action != null) {
			return action.doAction();
		}
		return null;
	}
	
	/**
	 * Führt die gewünschte Aktion aus mit dem eingegebenen Parameter.
	 * @param value Eingegebener Parameter
	 * @return Nächster Screen
	 */
	public Screen doAction(String value) {
		if (hasFormAction()) {
			return formAction.doAction(value);
		}
		return null;
	}

	/**
	 * Wird ausgeführt bevor der Screen angezegt wird.
	 * Kann optional in einem Screen implementiert werden.
	 */
	public void onShow() {
		// do nothing
	}
	
	/**
	 * Wird ausgeführt bevor der nächste Screen angezegt wird.
	 * Kann optional in einem Screen implementiert werden.
	 */
	public void onHide() {
		// do nothing
	}
}
