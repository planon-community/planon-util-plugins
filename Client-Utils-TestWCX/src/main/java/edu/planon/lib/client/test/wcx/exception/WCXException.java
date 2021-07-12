package edu.planon.lib.client.test.wcx.exception;

public class WCXException extends Exception {
	private static final long serialVersionUID = 1L;
	private String exptionMsg;
	
	public WCXException() {
		this.exptionMsg = "";
	}
	
	public WCXException(String message) {
		super(message);
	}
	
	public WCXException(Throwable cause) {
		super(cause);
	}
	
	public WCXException(String message, Throwable cause) {
		super(message, cause);
		this.exptionMsg = message;
	}
	
	public final String getExptionMsg() {
		return this.exptionMsg;
	}
}
