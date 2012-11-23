/**
 * 
 */
package ch.sws.ds.banksys.common.exceptions;

/**
 * @author feuzl1
 * 
 */
public class AmountNotSufficientException extends Exception {

	/** UID. */
	private static final long serialVersionUID = -7694264392459194517L;

	public AmountNotSufficientException() {
		super();
	}

	public AmountNotSufficientException(String msg) {
		super(msg);
	}

	public AmountNotSufficientException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
