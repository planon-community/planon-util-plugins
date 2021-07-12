package edu.planon.lib.client.common.exception;

public class PnClientException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PnClientException() {
		super("");
	}
	
	public PnClientException(String message) {
		super(message);
	}
	
	public PnClientException(Throwable cause) {
		super(cause);
	}
	
	public PnClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
