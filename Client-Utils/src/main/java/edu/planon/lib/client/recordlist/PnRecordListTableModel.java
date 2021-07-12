package edu.planon.lib.client.recordlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import edu.planon.lib.client.table.PnDataTableModel;
import edu.planon.lib.client.table.PnDataTableRowModel;

public class PnRecordListTableModel extends PnDataTableModel {
	private static final long serialVersionUID = 1L;
	private final PnRecordListModel recordListModel;
	private List<PnSearchFilterModel> searchFilterList;
	private List<PnSearchFilterModel> defaultSearchFilterList;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	public PnRecordListTableModel(PnRecordListModel recordListModel, boolean isSortable) throws PnClientException {
		super(isSortable);
		this.recordListModel = recordListModel;
		
		this.initModel();
	}
	
	@Override
	public Iterator<PnRecordDTO> iterator(long first, long count) {
		return this.getRecords((int)first, (int)count).iterator();
	}
	
	@Override
	public long size() {
		return this.recordListModel.getTotalRowCount();
	}
	
	@Override
	protected List<PnFieldDefDTO> getHeaders() throws PnClientException {
		return this.recordListModel.doGetHeaders();
	}
	
	@Override
	protected PnDataTableRowModel createRowModel(PnRecordDTO rowDTO) {
		int index = this.recordListModel.getRowNumber(rowDTO);
		return new PnDataTableRowModel(rowDTO, index);
	}
	
	private List<PnRecordDTO> getRecords(int fromRow, int count) {
		try {
			return this.recordListModel.getRecords(fromRow, count);
		}
		catch (PnClientException e) {
			return Collections.emptyList();
		}
	}
	
	/***** Search Filters *****/
	public List<PnSearchFilterModel> getDefaultSearchFilter() {
		return this.defaultSearchFilterList;
	}
	
	public final void addDefaultSearchFilter(List<PnSearchFilterModel> defaultSearchFilterList) {
		if (defaultSearchFilterList == null) {
			return;
		}
		if (this.defaultSearchFilterList == null) {
			this.defaultSearchFilterList = new ArrayList<PnSearchFilterModel>();
		}
		this.defaultSearchFilterList.clear();
		this.defaultSearchFilterList.addAll(defaultSearchFilterList);
	}
	
	public final void addDefaultSearchFilter(PnSearchFilterModel defaultSearchFilter) {
		if (defaultSearchFilter == null) {
			return;
		}
		if (this.defaultSearchFilterList == null) {
			this.defaultSearchFilterList = new ArrayList<PnSearchFilterModel>();
		}
		this.defaultSearchFilterList.add(defaultSearchFilter);
	}
	
	public List<PnSearchFilterModel> getSearchFilters() {
		return this.searchFilterList;
	}
	
	public void setSearchFilter(List<PnSearchFilterModel> searchFilterList) {
		this.searchFilterList = searchFilterList;
	}
	
	public List<PnSearchFilterModel> getCompleteSearchFilter() {
		if (this.defaultSearchFilterList == null || this.defaultSearchFilterList.isEmpty()) {
			return this.searchFilterList;
		}
		if (this.searchFilterList == null || this.searchFilterList.isEmpty()) {
			return this.defaultSearchFilterList;
		}
		ArrayList<PnSearchFilterModel> list = new ArrayList<PnSearchFilterModel>();
		list.addAll(this.defaultSearchFilterList);
		list.addAll(this.searchFilterList);
		return list;
	}
	
	/***** Settings *****/
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public int getPageSize() {
		return this.pageSize;
	}
}
