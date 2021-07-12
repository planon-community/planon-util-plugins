package edu.planon.lib.client.common.exception;

public class PnClientRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PnClientRuntimeException() {
		super("");
	}
	
	public PnClientRuntimeException(String message) {
		super(message);
	}
	
	public PnClientRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public PnClientRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
