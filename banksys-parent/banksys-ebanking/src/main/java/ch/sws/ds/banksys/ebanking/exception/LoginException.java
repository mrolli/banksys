package ch.sws.ds.banksys.ebanking.exception;

/**
 * Exception für misslunge Loginversuche.
 * 
 * @author mrolli
 */
@SuppressWarnings("serial")
public class LoginException extends RuntimeException {	
	public LoginException(String msg) {
		super(msg);
	}
	
	public LoginException(String msg, Throwable e) {
		super(msg, e);
	}
}
