package edu.planon.lib.esapi.exception;

public class ESApiException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ESApiException(String message) {
		super(message);
	}
	
	public ESApiException(Throwable cause) {
		super(cause);
	}
	
	public ESApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
