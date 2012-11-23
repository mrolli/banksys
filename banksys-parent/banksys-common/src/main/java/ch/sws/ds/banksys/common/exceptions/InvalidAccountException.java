/**
 * 
 */
package ch.sws.ds.banksys.common.exceptions;

/**
 * @author feuzl1
 * 
 */
public class InvalidAccountException extends Exception {
	/** UID. */
	private static final long serialVersionUID = 6499943317403972034L;

	public InvalidAccountException() {
		super();
	}

	public InvalidAccountException(String msg) {
		super(msg);
	}

	public InvalidAccountException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
