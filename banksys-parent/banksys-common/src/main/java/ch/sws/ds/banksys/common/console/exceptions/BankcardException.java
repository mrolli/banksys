package ch.sws.ds.banksys.common.console.exceptions;

/**
 * @author kambl1
 *
 * Exception für alle Fehler im Zusammenhang mit der Bankkarte.
 */
public class BankcardException extends Exception {
	/** UID */
	private static final long serialVersionUID = 5816100308820027926L;

	public BankcardException() {
		super();
	}
	
	public BankcardException(String message) {
		super(message);
	}
}
