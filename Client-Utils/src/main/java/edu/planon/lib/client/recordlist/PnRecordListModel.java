package edu.planon.lib.client.recordlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnQueryParamDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.util.PnQueryBOUtils;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.util.pnlogging.PnLogger;

public class PnRecordListModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final PnLogger LOGGER = PnLogger.getLogger(PnRecordListModel.class);
	private PnReferenceFieldDefDTO referenceDTO;
	private boolean sortable;
	private PnRecordListTableModel tableModel;
	private List<PnRecordDTO> recordsList = new ArrayList<>();
	private static final int NO_FETCH_SIZE = -1;
	private static final int NOT_FOUND = -1;
	private int fetchSize = 100;
	private int lastFetchedRow = NO_FETCH_SIZE;
	private Integer totalRowCount;
	private boolean isFetchComplete;
	private String filterFieldName;
	
	public PnRecordListModel(PnReferenceFieldDefDTO referenceDTO, boolean sortable) {
		this.referenceDTO = referenceDTO;
		this.sortable = sortable;
		//LOGGER.setLevel(Level.DEBUG);
	}
	
	public int getLoadedRowCount() {
		return this.recordsList.size();
	}
	
	public int getTotalRowCount() {
		if (this.totalRowCount == null) {
			try {
				this.totalRowCount = this.doGetDynamicTotalRowCount();
			}
			catch (PnClientException exception) {
				LOGGER.error(exception);
				return -1;
			}
		}
		
		return this.totalRowCount;
	}
	
	public List<PnRecordDTO> getRecords(int fromRow, int count) throws PnClientException {
		if (fromRow < 0 || count < 1) {
			return Collections.emptyList();
		}
		int toRow = fromRow + count;
		this.performCheck(toRow - 1);
		int loadedRowCount = this.getLoadedRowCount();
		if (fromRow > loadedRowCount) {
			return Collections.emptyList();
		}
		return this.recordsList.subList(fromRow, Math.min(toRow, loadedRowCount));
	}
	
	public boolean isFetchComplete() {
		return this.isFetchComplete;
	}
	
	public int getRowNumber(PnRecordDTO rowDTO) {
		return this.recordsList.indexOf(rowDTO);
	}
	
	public int fetchRecord(PnRecordDTO rowDTO) {
		int rowNumber = this.getRowNumber(rowDTO);
		try {
			if (rowNumber == NOT_FOUND) {
				do {
					this.getNextPage();
					rowNumber = this.getRowNumber(rowDTO);
				} while (rowNumber == NOT_FOUND && !this.isFetchComplete());
			}
		}
		catch (PnClientException e) {
			LOGGER.error(e);
		}
		
		return rowNumber;
	}
	
	public PnRecordDTO getRecord(int rowNumber) throws PnClientException {
		if (rowNumber >= 0) {
			this.performCheck(rowNumber);
			if (rowNumber >= this.getLoadedRowCount()) {
				return null;
			}
			return this.recordsList.get(rowNumber);
		}
		return null;
	}
	
	public void reset() {
		this.isFetchComplete = false;
		this.lastFetchedRow = NO_FETCH_SIZE;
		this.recordsList.clear();
		this.totalRowCount = null;
	}
	
	public List<PnRecordDTO> validateRecords(List<PnRecordDTO> records) {
		if (records.isEmpty()) {
			return records;
		}
		
		try {
			List<Object> selectedCodes = new ArrayList<Object>();
			records.forEach(tableDTO -> selectedCodes
					.add("Syscode".equals(this.getFilterFieldName()) ? Integer.valueOf(tableDTO.getFields()[0]) : tableDTO.getFields()[0]));
			
			ArrayList<PnSearchFilterModel> searchFilters = new ArrayList<>(this.getTableModel().getCompleteSearchFilter());
			searchFilters.add(new PnSearchFilterModel(this.getFilterFieldName(), PnESOperator.IN, selectedCodes));
			
			return this.doGetAllRows(searchFilters, this.getTableModel().getTableHeaderList());
		}
		catch (PnClientException e) {
			LOGGER.error(e);
			return Collections.emptyList();
		}
	}
	
	public String getFilterFieldName() {
		if (this.filterFieldName == null) {
			this.filterFieldName = this.getTableModel().getColumns().get(0).getSortProperty();
		}
		return this.filterFieldName;
	}
	
	public List<PnRecordDTO> getAllRecords() {
		try {
			while (!this.isFetchComplete()) {
				this.getNextPage();
			}
		}
		catch (PnClientException e) {
			LOGGER.error(e);
		}
		return this.recordsList;
	}
	
	/***** Internal Methods *****/
	private void performCheck(int rowNumber) throws PnClientException {
		if (rowNumber >= this.getLoadedRowCount()) {
			do {
				this.getNextPage();
			} while (rowNumber >= this.getLoadedRowCount() && !this.isFetchComplete());
		}
	}
	
	private void getNextPage() throws PnClientException {
		try {
			if (!this.isFetchComplete()) {
				long page = this.lastFetchedRow == NO_FETCH_SIZE ? 0L : (long)((this.lastFetchedRow + 1) / this.fetchSize);
				PnQueryParamDTO queryParam = new PnQueryParamDTO(page, this.fetchSize, this.getTableModel().getSort());
				
				List<PnRecordDTO> newRecords = this
						.doGetRows(queryParam, this.getTableModel().getCompleteSearchFilter(), this.getTableModel().getTableHeaderList());
				int size = newRecords.size();
				int fetch = Math.min(size, this.fetchSize);
				this.lastFetchedRow += fetch;
				if (size < this.fetchSize) {
					this.isFetchComplete = true;
				}
				this.recordsList.addAll(newRecords);
			}
		}
		catch (IllegalArgumentException e) {
			this.isFetchComplete = true;
			throw e;
		}
	}
	
	/***** Query Methods *****/
	protected List<PnFieldDefDTO> doGetHeaders() throws PnClientException {
		if (this.referenceDTO != null) {
			List<PnFieldDefDTO> fieldDefs = this.referenceDTO.getFieldsList();
			List<PnFieldDefDTO> headerList = new ArrayList<PnFieldDefDTO>(fieldDefs.size());
			for (PnFieldDefDTO fieldDef : fieldDefs) {
				if (fieldDef.isLookupDisplayField()) {
					headerList.add(fieldDef);
				}
			}
			return headerList;
		}
		return null;
	}
	
	public List<PnRecordDTO> doGetRows(PnQueryParamDTO queryParam, List<PnSearchFilterModel> searchFilters, List<PnFieldDefDTO> tableHeaderList)
			throws PnClientException {
		return PnQueryBOUtils.getRowsFromBO(this.referenceDTO.getBoPnName(), queryParam, searchFilters, tableHeaderList);
	}
	
	public List<PnRecordDTO> doGetAllRows(List<PnSearchFilterModel> searchFilters, List<PnFieldDefDTO> tableHeaderList) throws PnClientException {
		return PnQueryBOUtils.getAllRowsFromBO(this.referenceDTO.getBoPnName(), searchFilters, tableHeaderList);
	}
	
	public int doGetDynamicTotalRowCount() throws PnClientException {
		return PnQueryBOUtils.getTotalCountFromBO(this.referenceDTO.getBoPnName(), this.getTableModel().getCompleteSearchFilter());
	}
	
	/***** Getters *****/
	public final PnRecordListTableModel getTableModel() {
		if (this.tableModel == null) {
			try {
				this.tableModel = new PnRecordListTableModel(this, this.sortable);
			}
			catch (PnClientException e) {
				LOGGER.error(e);
			}
		}
		return this.tableModel;
	}
	
	/***** SETTINGS *****/
	public int getPageSize() {
		return this.getTableModel().getPageSize();
	}
	
	public void setPageSize(int rowsPerPage) {
		this.getTableModel().setPageSize(rowsPerPage);
	}
	
	public final boolean isSortable() {
		return this.sortable;
	}
	
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
}
