package edu.planon.lib.client.common.util;

import java.util.List;

import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.IPnESSearchExpression;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.enterprise.service.api.PnESValueType;

public final class PnQueryUtils {
	public static void applySearchFilter(IPnESDatabaseQuery query, List<PnSearchFilterModel> filterList)
			throws PnESFieldNotFoundException, PnESBusinessException {
		if (filterList == null) {
			return;
		}
		for (PnSearchFilterModel filter : filterList) {
			if (!filter.isActiveSearch()) {
				continue;
			}
			IPnESSearchExpression searchExpression = query.getSearchExpression(filter.getFieldName(), filter.getOperator());
			PnESValueType fieldType = searchExpression.getValueType();
			for (Object value : filter.getSearchValues()) {
				if (value instanceof String) {
					if (fieldType.equals(PnESValueType.INTEGER) || fieldType.equals(PnESValueType.REFERENCE)) {
						value = Integer.valueOf((String)value);
					}
				}
				searchExpression.addValue(value);
			}
		}
	}
}
