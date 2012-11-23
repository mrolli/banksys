package ch.sws.ds.banksys.common.console;

import java.util.ArrayList;

/**
 * @author kambl1
 *
 * Dient der Darstellung von eingegebenen Werten durch eine FormAction.
 */
public class FormScreen extends Screen {
	private int totalSteps;
	private int completedSteps;
	private ArrayList<String> nameList;
	private ArrayList<String> textList;
	private ArrayList<String> valueList;
	
	/**
	 * @param name Name des Screens
	 */
	public FormScreen(String name) {
		super(name);
		this.totalSteps = 0;
		this.completedSteps = 0;
		this.nameList = new ArrayList<>();
		this.textList = new ArrayList<>();
		this.valueList = new ArrayList<>();
	}

	/**
	 * Gibt die Anzahl aller Schritte (einzugebende Werte) zurück.
	 * @return Anzahl aller Schritte
	 */
	public int getTotalFormSteps() {
		return totalSteps;
	}
	
	/**
	 * Prèft ob der eingegebene Wert gültigt ist.
	 * @param text Zu prüfender Text
	 * @return True falls der Text gültig ist, sonst false
	 */
	private static boolean isValid(String text) {
		if (text.isEmpty()) {
			return false;
		}
		if (text.equals("\r")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Fügt einen neuen einzugebenden Wert hinzu.
	 * @param name Name des Werts welcher eingegeben werden soll
	 * @param text Text als Erklärung für den Benutzer
	 */
	public void addFormStep(String name, String text) {
		if (totalSteps == 0) {
			addText(text + "...");
		}
		totalSteps++;
		nameList.add(name);
		textList.add(text);
	}

	/**
	 * Gibt den Namen eines Werts aus der Eingabeliste zurück.
	 * @param index Index der Eingabe
	 * @return Name der Eingabe
	 */
	public String getNameOfValue(int index) {
		return nameList.get(index);
	}
	
	/**
	 * Gibt eine Lister aller eingegebenen Werte zurück.
	 * @return Liste aller eingegebener Werte
	 */
	public ArrayList<String> getValueList() {
		return valueList;
	}
	
	/**
	 * Ersetzt den Body durch die Liste der bereits eingegebenen Werten.
	 * @param text Neuer Text für die Eingabeaufforderung
	 */
	private void replaceBody(String text) {
		clearBody();
		for (int i = 0; i < completedSteps; i++) {
			addText(nameList.get(i) + ": " + valueList.get(i));
		}
		addText(text + "...");
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#doAction(java.lang.String)
	 */
	@Override
	public Screen doAction(String value) {
		if (completedSteps == 0) {
			valueList.clear();
		}
		if (isValid(value)) {
			valueList.add(value);
			completedSteps++;
			if (completedSteps < totalSteps) {
				replaceBody(textList.get(completedSteps));
				return this;
			}
			completedSteps = 0;
			replaceBody(textList.get(completedSteps));
		}
		return super.doAction(value);
	}
}
