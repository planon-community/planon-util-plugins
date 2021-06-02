package edu.planon.lib.esapi;

import java.util.ArrayList;
import java.util.List;

import edu.planon.lib.esapi.exception.ESApiException;
import nl.planon.enterprise.service.api.IPnESMessage;
import nl.planon.enterprise.service.api.IPnESMessageHandler;
import nl.planon.enterprise.service.api.PnESBusinessException;

public class ESErrorUtil {
	
	public ESErrorUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<String> getErrorMessage(Throwable throwable) {
		List<String> messageList = new ArrayList<String>();
		
		if (throwable instanceof ESApiException && throwable.getCause() == null) {
			messageList.add(throwable.getMessage());
		}
		else if (throwable instanceof PnESBusinessException) {
			PnESBusinessException businessException = (PnESBusinessException)throwable;
			IPnESMessageHandler messageHandler = businessException.getMessageHandler();
			
			int i;
			int num = messageHandler.getNumberOfErrors();
			IPnESMessage message;
			for (i = 0; i < num; ++i) {
				message = messageHandler.getError(i);
				messageList.add("* " + message.getLabel() + " (" + message.getCode() + ")");
			}
			
			num = messageHandler.getNumberOfSystemErrors();
			for (i = 0; i < num; ++i) {
				message = messageHandler.getSystemError(i);
				messageList.add("* " + message.getLabel() + " (" + message.getCode() + ")");
			}
			
			num = messageHandler.getNumberOfWarnings();
			for (i = 0; i < num; ++i) {
				message = messageHandler.getWarning(i);
				messageList.add("* " + message.getLabel() + " (" + message.getCode() + ")");
			}
			
			num = messageHandler.getNumberOfConfirmations();
			for (i = 0; i < num; ++i) {
				message = messageHandler.getConfirmation(i);
				messageList.add("* " + message.getLabel() + " (" + message.getCode() + ")");
			}
		}
		else {
			messageList.add(throwable.getMessage());
		}
		
		return messageList;
	}
	
}
