package edu.planon.lib.client.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.table.selection.IPnTableSelectionModel;
import edu.planon.lib.client.table.selection.PnTableSingleSelectionModel;

public class PnDataTableModel extends SortableDataProvider<PnRecordDTO, String> {
	private static final long serialVersionUID = 1L;
	protected static final int DEFAULT_PAGE_SIZE = 50;
	private final boolean isSortable;
	
	private List<PnFieldDefDTO> tableHeaderList;
	private List<PnDataTableColumnModel> columnList;
	private List<PnRecordDTO> dataList = new ArrayList<PnRecordDTO>(0);
	
	private final Map<Integer, PnDataTableRowModel> rowModels = new HashMap<Integer, PnDataTableRowModel>();
	private IPnTableSelectionModel selectionModel;
	
	public PnDataTableModel(boolean isSortable, List<PnFieldDefDTO> headerList, List<PnRecordDTO> dataList) throws PnClientException {
		this(isSortable);
		this.tableHeaderList = headerList;
		this.dataList = dataList;
		this.initModel();
	}
	
	/**
	 * Constructor to use when extending this class. At the end of the sub-classes constructor you MUST
	 * call <code>this.initModel()</code>
	 * 
	 * @param isSortable
	 */
	protected PnDataTableModel(boolean isSortable) {
		this.isSortable = isSortable;
		this.setSelectionModel(new PnTableSingleSelectionModel());
	}
	
	protected final void initModel() throws PnClientException {
		if (this.tableHeaderList == null) {
			this.tableHeaderList = this.getHeaders();
		}
		
		this.createColumns();
		
		//set initial sort
		this.setSort(this.columnList.get(0).getSortProperty(), SortOrder.ASCENDING);
	}
	
	@Override
	public Iterator<PnRecordDTO> iterator(long fromIndex, long count) {
		if (this.isSortable) {
			Collections.sort(this.getDataList(), new RecordComparator(this.tableHeaderList, this.getSort()));
		}
		
		long toIndex = Math.min(fromIndex + count, this.getDataList().size());
		return this.getDataList().subList((int)fromIndex, (int)toIndex).iterator();
	}
	
	@Override
	public IModel<PnRecordDTO> model(PnRecordDTO rowDTO) {
		PnDataTableRowModel rowModel = this.createRowModel(rowDTO);
		this.rowModels.put(rowModel.getRowNumber(), rowModel);
		return rowModel;
	}
	
	@Override
	public long size() {
		return this.getDataList().size();
	}
	
	protected List<PnFieldDefDTO> getHeaders() throws PnClientException {
		return new ArrayList<PnFieldDefDTO>();
	}
	
	public int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}
	
	protected PnDataTableRowModel createRowModel(PnRecordDTO rowDTO) {
		return new PnDataTableRowModel(rowDTO, this.getDataList().indexOf(rowDTO));
	}
	
	/***** COLUMNS *****/
	private void createColumns() {
		this.columnList = new ArrayList<>(this.tableHeaderList.size());
		for (int i = 0; i < this.tableHeaderList.size(); i++) {
			this.columnList.add(new PnDataTableColumnModel(this.tableHeaderList.get(i), i));
		}
	}
	
	public List<PnDataTableColumnModel> getColumns() {
		return this.columnList;
	}
	
	public List<PnFieldDefDTO> getTableHeaderList() {
		return this.tableHeaderList;
	}
	
	/***** Data *****/
	public void setDataList(List<PnRecordDTO> dataList) {
		this.dataList = dataList;
	}
	
	public List<PnRecordDTO> getDataList() {
		return this.dataList;
	}
	
	/***** Selection *****/
	public void setSelectionModel(IPnTableSelectionModel selectionModel) {
		this.selectionModel = selectionModel;
	}
	
	public IPnTableSelectionModel getSelectionModel() {
		return this.selectionModel;
	}
	
	public final ArrayList<PnDataTableRowModel> getSelectedRowModels() {
		if (!this.getSelectionModel().hasSelection()) {
			return new ArrayList<>(0);
		}
		List<Integer> selectedRowIndexes = this.getSelectionModel().getSelectedRows();
		ArrayList<PnDataTableRowModel> selectedRowModels = new ArrayList<>(selectedRowIndexes.size());
		for (int rowIndex : selectedRowIndexes) {
			PnDataTableRowModel rowModel = this.rowModels.get(rowIndex);
			if (rowModel != null) {
				selectedRowModels.add(rowModel);
			}
		}
		return selectedRowModels;
	}
	
	public final List<PnRecordDTO> getSelectedRecords() {
		if (!this.getSelectionModel().hasSelection()) {
			return Collections.emptyList();
		}
		List<Integer> selectedRowIndexes = this.getSelectionModel().getSelectedRows();
		ArrayList<PnRecordDTO> selectedRecords = new ArrayList<>(selectedRowIndexes.size());
		for (int rowIndex : selectedRowIndexes) {
			PnDataTableRowModel rowModel = this.rowModels.get(rowIndex);
			if (rowModel != null) {
				selectedRecords.add(rowModel.getObject());
			}
		}
		return selectedRecords;
	}
	
	public class RecordComparator implements Comparator<PnRecordDTO> {
		private Integer index;
		private int ascending;
		
		public RecordComparator(List<PnFieldDefDTO> tableHeaderList, SortParam<String> sortParam) {
			this.ascending = sortParam.isAscending() ? 1 : -1;
			for (int i = 0; i < tableHeaderList.size(); i++) {
				PnFieldDefDTO fieldDef = tableHeaderList.get(i);
				if (fieldDef != null && fieldDef.getPnName().equals(sortParam.getProperty())) {
					this.index = i;
					break;
				}
			}
		}
		
		@Override
		public int compare(PnRecordDTO tbl1, PnRecordDTO tbl2) {
			if (this.index != null && tbl1.getFields()[this.index] != null && tbl2.getFields()[this.index] != null) {
				return this.ascending * (tbl1.getFields()[this.index].compareToIgnoreCase(tbl2.getFields()[this.index]));
			}
			return 0;
		}
	}
}
