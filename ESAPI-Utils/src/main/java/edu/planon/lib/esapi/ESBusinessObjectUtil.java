package edu.planon.lib.esapi;

import nl.planon.enterprise.service.api.IPnESAction;
import nl.planon.enterprise.service.api.IPnESActionListManager;
import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;

public final class ESBusinessObjectUtil {
	public static IPnESBusinessObject create(String boTypePnName) throws PnESBusinessException, PnESActionNotFoundException {
		IPnESAction bomAddAction = ESBusinessObjectUtil.getActionListManager(boTypePnName).getAction("BomAdd");
		return bomAddAction.execute();
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
