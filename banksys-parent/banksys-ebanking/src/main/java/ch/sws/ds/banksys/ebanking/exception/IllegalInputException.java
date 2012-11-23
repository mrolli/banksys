package ch.sws.ds.banksys.ebanking.exception;

/**
 * Exceptiontyp für User Input Fehler.
 * 
 * @author mrolli
 */
@SuppressWarnings("serial")
public class IllegalInputException extends RuntimeException {
	public IllegalInputException(String msg) {
		super(msg);
	}
	
	public IllegalInputException(String msg, Throwable e) {
		super(msg, e);
	}
}
