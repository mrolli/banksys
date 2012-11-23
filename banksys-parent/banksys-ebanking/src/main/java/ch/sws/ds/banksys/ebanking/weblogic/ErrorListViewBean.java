package ch.sws.ds.banksys.ebanking.weblogic;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel-Klasse für Fehler.
 * 
 * Mithilfe dieser Utility-Klasse übergibt der FrontController
 * menschenlesbare Fehlertexte an die View.
 * 
 * @author mrolli
 *
 */
public class ErrorListViewBean {
	/**
	 * Liste mit Fehlern.
	 */
	private List<String> errors;
	
	/**
	 * Der Default-Konstruktor erzeugt eine neue leere Fehlerliste.
	 */
	public ErrorListViewBean() {
		errors = new ArrayList<String>();
	}
	
	/**
	 * Erzeugt eine Fehlerliste mit den übergebenen Fehlermeldungen.
	 * 
	 * @param errors Liste von Fehlernmeldungen.
	 */
	public ErrorListViewBean(List<String> errors) {
		this.errors = errors;
	}
	
	/**
	 * Fügt eine Fehlermeldung zur Liste hinzu.
	 * 
	 * @param error Fehlermeldung
	 */
	public void addError(String error) {
		errors.add(error);
	}
	
	/**
	 * Gibt die Liste der Fehlermeldungen zurück.
	 * 
	 * @return Liste der Fehlermeldungen
	 */
	public List<String> getErrors() {
		return errors;
	}
	
	public int getCount() {
		return errors.size();
	}
}
