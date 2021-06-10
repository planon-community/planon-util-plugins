package edu.planon.lib.client.panel;

import java.util.Collections;
import java.util.List;

import edu.planon.lib.client.exception.PnClientException;
import edu.planon.lib.client.exception.PnUserException;
import edu.planon.lib.esapi.ESErrorUtil;

public class ErrorPopup extends InfoPopup {
	private static final long serialVersionUID = 1L;
	
	public ErrorPopup(String id, String message) {
		super(id, message);
	}
	
	public ErrorPopup(String id, String closeLabel, String message) {
		super(id, closeLabel, message);
	}
	
	public ErrorPopup(String id, List<String> messages) {
		super(id, messages);
	}
	
	public ErrorPopup(String id, String closeLabel, List<String> messages) {
		super(id, closeLabel, messages);
	}
	
	public ErrorPopup(String id, Throwable throwable) {
		super(id, getErrorMessage(throwable));
	}
	
	public ErrorPopup(String id, String closeLabel, Throwable throwable) {
		super(id, closeLabel, getErrorMessage(throwable));
	}
	
	private static List<String> getErrorMessage(Throwable throwable) {
		if((throwable instanceof PnClientException || throwable instanceof PnUserException) && throwable.getCause() == null) {
			return Collections.singletonList(throwable.getMessage());
		}
		else {
			return ESErrorUtil.getErrorMessage(throwable);
		}
	}
}
