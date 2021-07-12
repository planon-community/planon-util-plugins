package edu.planon.lib.client.common.util;

import java.util.ArrayList;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnQueryParamDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import edu.planon.lib.esapi.ESBusinessObjectUtil;
import edu.planon.lib.esapi.ESQueryUtils;
import edu.planon.lib.esapi.exception.ESApiException;
import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.IPnESResultSet;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;

public final class PnQueryBOUtils {
	public static List<PnRecordDTO> getRowsFromBO(String boPnName, PnQueryParamDTO queryParam, List<PnSearchFilterModel> filterList,
			List<PnFieldDefDTO> headerList) throws PnClientException {
		try {
			IPnESDatabaseQuery query = ESQueryUtils.getBOInQuickSelectionDataBaseQuery(boPnName);
			PnQueryUtils.applySearchFilter(query, filterList);
			return executeQuery(queryParam, headerList, query);
		}
		catch (PnESBusinessException | PnESFieldNotFoundException | ESApiException exception) {
			throw new PnClientException(exception.getMessage(), exception);
		}
	}
	
	public static List<PnRecordDTO> getAllRowsFromBO(String boPnName, List<PnSearchFilterModel> filterList, List<PnFieldDefDTO> headerList)
			throws PnClientException {
		try {
			IPnESDatabaseQuery query = ESQueryUtils.getBOInQuickSelectionDataBaseQuery(boPnName);
			PnQueryUtils.applySearchFilter(query, filterList);
			return executeAllQuery(headerList, query);
		}
		catch (PnESBusinessException | PnESFieldNotFoundException | ESApiException exception) {
			throw new PnClientException(exception.getMessage(), exception);
		}
	}
	
	public static int getTotalCountFromBO(String boPnName, List<PnSearchFilterModel> filterList) throws PnClientException {
		try {
			IPnESDatabaseQuery query = ESQueryUtils.getBOInQuickSelectionDataBaseQuery(boPnName);
			PnQueryUtils.applySearchFilter(query, filterList);
			return query.executeCount();
		}
		catch (PnESBusinessException | PnESFieldNotFoundException | ESApiException exception) {
			throw new PnClientException(exception.getMessage(), exception);
		}
	}
	
	private static List<PnRecordDTO> executeQuery(PnQueryParamDTO queryParam, List<PnFieldDefDTO> headerList, IPnESDatabaseQuery query)
			throws PnClientException, IllegalArgumentException, PnESBusinessException {
		ArrayList<PnRecordDTO> tableRowList = new ArrayList<PnRecordDTO>();
		query.setPage((int)queryParam.getPage());
		query.setPageSize((int)queryParam.getPageSize());
		query.setSortColumn(queryParam.getProperty(), queryParam.isAscending());
		
		IPnESResultSet resultSet = query.execute();
		while (resultSet.next()) {
			addTableRow(headerList, tableRowList, resultSet);
		}
		return tableRowList;
	}
	
	private static List<PnRecordDTO> executeAllQuery(List<PnFieldDefDTO> headerList, IPnESDatabaseQuery query) throws PnClientException, PnESBusinessException {
		ArrayList<PnRecordDTO> tableRowList = new ArrayList<PnRecordDTO>();
		query.setPageSize(1000);
		IPnESResultSet resultSet = null;
		int page = 0;
		do {
			query.setPage(page++);
			resultSet = query.execute();
			while (resultSet.next()) {
				addTableRow(headerList, tableRowList, resultSet);
			}
		} while (resultSet.hasNextPage());
		return tableRowList;
	}
	
	private static void addTableRow(List<PnFieldDefDTO> headerList, List<PnRecordDTO> tableRowList, IPnESResultSet resultSet) throws PnClientException {
		try {
			String[] valueArray = new String[headerList.size()];
			
			IPnESBusinessObject bo = null;
			
			for (int index = 0; index < headerList.size(); index++) {
				PnFieldDefDTO header = headerList.get(index);
				if (header.isQueryResultField()) {
					valueArray[index] = PnFieldTypeUtils.getFieldFromQuery(header.getFieldType(), resultSet, header.getPnName());
				}
				else {
					valueArray[index] = "";
					
					if (bo == null) {
						bo = ESBusinessObjectUtil.read(resultSet.getBOType(), resultSet.getPrimaryKey());
					}
					valueArray[index] = bo.getField(header.getPnName()).getDisplayValue();
				}
			}
			PnRecordDTO tableData = new PnRecordDTO(resultSet.getPrimaryKey(), valueArray);
			tableData.setUniqueID(resultSet.getPrimaryKeyAsString());
			tableRowList.add(tableData);
		}
		catch (IllegalStateException | PnESBusinessException | PnESActionNotFoundException | PnESFieldNotFoundException exception) {
			throw new PnClientException(exception);
		}
	}
}
