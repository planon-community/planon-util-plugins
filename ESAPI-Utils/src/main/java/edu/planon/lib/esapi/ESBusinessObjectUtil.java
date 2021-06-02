package edu.planon.lib.esapi;

import nl.planon.enterprise.service.api.IPnESActionListManager;
import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;

public final class ESBusinessObjectUtil {
	
	public ESBusinessObjectUtil() {
	}
	
	public static IPnESBusinessObject read(String boTypePnName, Integer primaryKey) throws PnESBusinessException, PnESActionNotFoundException {
		if (primaryKey == null || primaryKey <= 0) {
			return null;
		}
		return ESBusinessObjectUtil.getActionListManager(boTypePnName).executeRead(primaryKey.intValue());
	}
	
	public static IPnESBusinessObject read(String boTypePnName, String lookupValue) throws PnESBusinessException, PnESActionNotFoundException {
		if (lookupValue == null) {
			return null;
		}
		return ESBusinessObjectUtil.getActionListManager(boTypePnName).executeReadByLookup(lookupValue);
	}
	
	public static IPnESActionListManager getActionListManager(String boTypePnName) throws PnESBusinessException {
		return ESContextUtil.getContext().getActionListManager(boTypePnName);
	}
	
}
