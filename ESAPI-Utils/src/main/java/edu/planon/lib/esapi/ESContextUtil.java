package edu.planon.lib.esapi;

import java.util.Locale;

import nl.planon.enterprise.service.api.IPnESContext;
import nl.planon.enterprise.service.api.IPnESContextCreator;
import nl.planon.enterprise.service.api.IPnESSession;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.factory.PnESContextCreator;
import nl.planon.util.pnlogging.PnLogger;

public final class ESContextUtil {
	public static IPnESContext getContext() {
		IPnESContextCreator contextCreator = PnESContextCreator.getInstance();
		return contextCreator.createContext();
	}
	
	public static IPnESContext getContext(IPnESSession session) {
		IPnESContextCreator contextCreator = PnESContextCreator.getInstance();
		return contextCreator.createContext(session);
	}
	
	public static Locale getLocale() {
		try {
			return ESContextUtil.getContext().getUserInfo().getLocale();
		}
		catch (PnESBusinessException e) {
			PnLogger.getLogger(ESContextUtil.class).warn(e.getMessage(), e);
			return Locale.getDefault();
		}
	}
}
