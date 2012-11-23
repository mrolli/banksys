/**
 * 
 */
package ch.sws.ds.banksys.common.exceptions;

/**
 * @author feuzl1
 * 
 */
public class InvalidCustomerException extends Exception {
	/** UID. */
	private static final long serialVersionUID = 6499943317403972034L;

	public InvalidCustomerException() {
		super();
	}

	public InvalidCustomerException(String msg) {
		super(msg);
	}

	public InvalidCustomerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
