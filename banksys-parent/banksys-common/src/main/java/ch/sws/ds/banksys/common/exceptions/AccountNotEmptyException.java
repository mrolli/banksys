/**
 * 
 */
package ch.sws.ds.banksys.common.exceptions;

/**
 * @author feuzl1
 * 
 */
public class AccountNotEmptyException extends Exception {

	/** UID. */
	private static final long serialVersionUID = -7114848766833134723L;

	public AccountNotEmptyException() {
		super();
	}

	public AccountNotEmptyException(String msg) {
		super(msg);
	}

	public AccountNotEmptyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
