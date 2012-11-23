/**
 * 
 */
package ch.sws.ds.banksys.common.exceptions;

/**
 * @author feuzl1
 * 
 */
public class UnknownClearingException extends Exception {
	/** UID. */
	private static final long serialVersionUID = 6499943317403972034L;

	public UnknownClearingException() {
		super();
	}

	public UnknownClearingException(String msg) {
		super(msg);
	}

	public UnknownClearingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
