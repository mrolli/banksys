package ch.sws.ds.banksys.common.console.exceptions;

/**
 * @author kambl1
 *
 * Exception für alle Konvertierungsfehler.
 */
public class ConversionException extends Exception {
	/** UID */
	private static final long serialVersionUID = 5816100308820027926L;

	public ConversionException() {
		super();
	}
	
	public ConversionException(String message) {
		super(message);
	}
}
