package edu.planon.lib.sx.exception;

import nl.planon.hades.userextension.uxinterface.IUXContext;

public class SXException extends Exception {
	private static final long serialVersionUID = 1L;
	private final Integer errorCode;

	public SXException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public SXException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public final int getErrorCode() {
		return this.errorCode;
	}
	
	public final void toError(IUXContext context) {
		context.addError(this.errorCode, this.getMessage());
	}
}
