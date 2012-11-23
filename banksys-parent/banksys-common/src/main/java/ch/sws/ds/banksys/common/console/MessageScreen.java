package ch.sws.ds.banksys.common.console;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kambl1
 *
 * Dient der Darstellung von erzeugten Daten nach erfolgreicher Bearbeitung.
 */
public class MessageScreen extends Screen {
	private ArrayList<String> valueList;
	private HashMap<ResultKeys, String> resultMap;
	
	/**
	 * @param name Name des Screens
	 * @param parentScreen Vorhergehender Screen
	 * @param valueList Liste mit den eingegebenen Werten
	 */
	public MessageScreen(String name, Screen parentScreen, ArrayList<String> valueList) {
		super(name, parentScreen);
		this.valueList = valueList;
		this.resultMap = new HashMap<>();
	}
	
	/**
	 * Gibt den eingegebenen Wert an der gewünschten Position zurück.
	 * @param index Index des gewünschten Werts
	 * @return Gewünschter eingegebener Wert
	 */
	public String getValue(int index) {
		return valueList.get(index);
	}

	/**
	 * Fügt ein String Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, String value) {
		resultMap.put(key, value);
	}

	/**
	 * Fügt ein Integer Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, Integer value) {
		resultMap.put(key, value.toString());
	}
	
	/**
	 * Fügt ein int Resultat in die ResulateMap ein.
	 * @param key Key des Resultats
	 * @param value Wert des Resultats
	 */
	public void addResult(ResultKeys key, int value) {
		resultMap.put(key, Integer.toString(value));
	}
	
	/**
	 * Gibt das gewünschte Resultat zurück.
	 * @param key Key des Resultats
	 * @return Gewünschtes Resultat
	 */
	public String getResult(ResultKeys key) {
		return resultMap.get(key);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onHide()
	 */
	@Override
	public void onHide() {
		resultMap.clear();
	}
}
