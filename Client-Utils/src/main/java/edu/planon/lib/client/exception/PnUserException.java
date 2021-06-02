package edu.planon.lib.client.exception;

public class PnUserException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PnUserException() {
		super("");
	}
	
	public PnUserException(String message) {
		super(message);
	}
	
	public PnUserException(Throwable cause) {
		super(cause);
	}
	
	public PnUserException(String message, Throwable cause) {
		super(message, cause);
	}
}
