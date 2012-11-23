package ch.sws.ds.banksys.ebanking.exception;

/**
 * Exceptiontyp für leere Kontolisten
 * 
 * @author mrolli
 */
@SuppressWarnings("serial")
public class EmptyAccountListException extends RuntimeException {
	public EmptyAccountListException(String msg) {
		super(msg);
	}
	
	public EmptyAccountListException(String msg, Throwable e) {
		super(msg, e);
	}
}
