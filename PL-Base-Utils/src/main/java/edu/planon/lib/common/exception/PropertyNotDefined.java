package edu.planon.lib.common.exception;

public class PropertyNotDefined extends Exception {
	private static final long serialVersionUID = 1L;

	public PropertyNotDefined() {
	}

	public PropertyNotDefined(String message) {
		super(message);
	}

	public PropertyNotDefined(Throwable cause) {
		super(cause);
	}

	public PropertyNotDefined(String message, Throwable cause) {
		super(message, cause);
	}
}
