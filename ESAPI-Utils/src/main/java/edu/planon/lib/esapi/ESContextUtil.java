package edu.planon.lib.esapi;

import nl.planon.enterprise.service.api.IPnESContext;
import nl.planon.enterprise.service.api.IPnESContextCreator;
import nl.planon.enterprise.service.api.IPnESSession;
import nl.planon.enterprise.service.api.factory.PnESContextCreator;

public final class ESContextUtil {
	
	private ESContextUtil() {
	}
	
	public static IPnESContext getContext() {
		IPnESContextCreator contextCreator = PnESContextCreator.getInstance();
		return contextCreator.createContext();
	}
	
	public static IPnESContext getContext(IPnESSession session) {
		IPnESContextCreator contextCreator = PnESContextCreator.getInstance();
		return contextCreator.createContext(session);
	}
}
