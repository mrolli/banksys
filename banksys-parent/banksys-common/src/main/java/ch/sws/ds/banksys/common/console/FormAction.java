package ch.sws.ds.banksys.common.console;

import java.util.ArrayList;

import ch.sws.ds.banksys.common.console.exceptions.ConversionException;

/**
 * @author kambl1
 *
 * Eine FormAction forderte den User zu Eingaben von Werten auf und speichert diese in einer Liste.
 */
public abstract class FormAction extends Action {
	private final FormScreen parentScreen;
	private ArrayList<String> valueList;
	
	/**
	 * @param name Name der Action
	 * @param parentScreen Vorhergehender Screen
	 * @param nextScreen Screen im Erfolgsfall
	 * @param errorScreen Screen im Fehlerfall
	 * @param valueList Liste mit den abzufüllenden Werten
	 */
	public FormAction(String name, FormScreen parentScreen, Screen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
		super(name, nextScreen, errorScreen);
		this.parentScreen = parentScreen;
		this.valueList = valueList;
	}
	
	/**
	 * Prüft ob alle geforderten Werte eingegeben wurden.
	 * @return True wenn alle geforderten Werte gespeichert wurden, sonst false
	 */
	public boolean dataIsComplete() {
		return (valueList.size() == parentScreen.getTotalFormSteps());
	}

	/**
	 * Gibt die Anzahl aller Schritte (einzugebende Werte) zurück.
	 * @return Anzahl aller Schritte
	 */
	public int getTotalFormSteps() {
		return parentScreen.getTotalFormSteps();
	}
	
	/**
	 * Fügt ein String Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, String value) {
		((MessageScreen)super.getNextScreen()).addResult(key, value);
	}
	
	/**
	 * Fügt ein Integer Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, Integer value) {
		((MessageScreen)super.getNextScreen()).addResult(key, value);
	}
	
	/**
	 * Fügt ein int Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, int value) {
		((MessageScreen)super.getNextScreen()).addResult(key, value);
	}
	
	/**
	 * Löscht die ResulateMap.
	 */
	private void resetValueList() {
		valueList.clear();
	}
	
	/**
	 * Gibt den Namen eines Werts aus der Eingabeliste zurück.
	 * @param index Index der Eingabe
	 * @return Name der Eingabe
	 */
	private String getNameOfValue(int index) {
		return parentScreen.getNameOfValue(index);
	}

	/**
	 * Gibt einen Wert als String aus der Eingabeliste zurück.
	 * @param index Index der Eingabe
	 * @return Wert der Eingabe
	 */
	public String getValue(int index) {
		return valueList.get(index);
	}
	
	/**
	 * Gibt einen Wert als int aus der Eingabeliste zurück.
	 * @param index Index der Eingabe
	 * @return Wert der Eingabe
	 * @throws ConversionException
	 */
	public int getValueAsInt(int index) throws ConversionException {
		try {
			return Integer.parseInt(valueList.get(index));
		} catch (NumberFormatException e) {
			throw new ConversionException("The entered " + getNameOfValue(index) + " is not a number!");
		}
	}
	
	/**
	 * Gibt einen Wert als Integer aus der Eingabeliste zurück.
	 * @param index Index der Eingabe
	 * @return Wert der Eingabe
	 * @throws ConversionException
	 */
	public Integer getValueAsInteger(int index) throws ConversionException {
		try {
			return Integer.valueOf(valueList.get(index));
		} catch (NumberFormatException e) {
			throw new ConversionException("The entered " + getNameOfValue(index) + " is not a number!");
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Action#doIt(java.lang.String)
	 */
	@Override
	final protected boolean doIt(String value) {
		if (doForm(value)) {
			return true;
		}
		resetValueList();
		return false;
	}
	
	/**
	 * Zu implementierende Funktion der FormAction.
	 * @param value Paramter für die FormAction
	 * @return True falls die FormAction erfolgreich war, sonst false
	 */
	protected abstract boolean doForm(String value);
}
