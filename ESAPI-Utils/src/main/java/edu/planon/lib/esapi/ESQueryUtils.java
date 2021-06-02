package edu.planon.lib.esapi;

import edu.planon.lib.esapi.exception.ESApiException;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.PnESBusinessException;

public final class ESQueryUtils {
	
	private ESQueryUtils() {
	}
	
	public static IPnESDatabaseQuery getPVDatabaseQuery(String pvName) throws PnESBusinessException, ESApiException {
		IPnESDatabaseQuery query = ESContextUtil.getContext().getPVDatabaseQuery(pvName);
		if (query == null) {
			throw new ESApiException(String.format("PVQuery '%s' not found", pvName));
		}
		return query;
	}
}
