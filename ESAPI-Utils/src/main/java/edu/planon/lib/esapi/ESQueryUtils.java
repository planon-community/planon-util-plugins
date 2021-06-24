package edu.planon.lib.esapi;

import edu.planon.lib.esapi.exception.ESApiException;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.PnESBusinessException;

public final class ESQueryUtils {
	public static IPnESDatabaseQuery getBOInQuickSelectionDataBaseQuery(String boName) throws PnESBusinessException, ESApiException {
		IPnESDatabaseQuery query = ESContextUtil.getContext().getBOInQuickSelectionDataBaseQuery(boName);
		if (query == null) {
			throw new ESApiException(String.format("BO '%s' not found", boName));
		}
		return query;
	}
	
	public static IPnESDatabaseQuery getBOFilterDatabaseQuery(String boName) throws PnESBusinessException, ESApiException {
		IPnESDatabaseQuery query = ESContextUtil.getContext().getBOFilterDatabaseQuery(boName);
		if (query == null) {
			throw new ESApiException(String.format("BO '%s' not found", boName));
		}
		return query;
	}
	
	public static IPnESDatabaseQuery getBODatabaseQuery​(String boName) throws PnESBusinessException, ESApiException {
		IPnESDatabaseQuery query = ESContextUtil.getContext().getBODatabaseQuery(boName);
		if (query == null) {
			throw new ESApiException(String.format("BO '%s' not found", boName));
		}
		return query;
	}
	
	public static IPnESDatabaseQuery getPVDatabaseQuery(String pvName) throws PnESBusinessException, ESApiException {
		IPnESDatabaseQuery query = ESContextUtil.getContext().getPVDatabaseQuery(pvName);
		if (query == null) {
			throw new ESApiException(String.format("PVQuery '%s' not found", pvName));
		}
		return query;
	}
}
