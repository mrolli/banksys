package ch.sws.ds.banksys.ebanking.exception;

/**
 * Wrapper-Exception für RemoteExceptions.
 * 
 * @author mrolli
 */
@SuppressWarnings("serial")
public class BackendCommunicationException extends RuntimeException {	
	public BackendCommunicationException(String msg) {
		super(msg);
	}
	
	public BackendCommunicationException(String msg, Throwable e) {
		super(msg, e);
	}
}
