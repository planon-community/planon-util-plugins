package edu.planon.lib.client.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.util.visit.IVisit;

import edu.planon.lib.client.common.behavior.PnAjaxClickOnEnterKeyBehavior;
import edu.planon.lib.client.common.behavior.PnAjaxEventBehavior;
import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.behavior.PnTabIndexAttributeModifier;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.table.behavior.PnTableRowAjaxClickBehavior;
import edu.planon.lib.client.table.selection.IPnTableRowSelectionListener;
import edu.planon.lib.client.table.selection.IPnTableRowSelectionListener.SelectionEventType;
import edu.planon.lib.client.table.selection.IPnTableSelectionModel;
import edu.planon.lib.client.table.selection.PnTableMultiSelectionModel;
import edu.planon.lib.client.table.selection.PnTableSingleSelectionModel;
import edu.planon.lib.client.table.toolbar.PnDataTableHeaders;
import edu.planon.lib.client.table.toolbar.PnDataTableNoRecordsToolbar;
import nl.planon.util.pnlogging.PnLogger;

public class PnDataTable extends DataTable<PnRecordDTO, String> {
	private static final long serialVersionUID = 1L;
	private boolean isRowSelectionAllowed = true;
	private List<IPnTableRowSelectionListener> selectionListeners;
	private static final PnLogger LOGGER = PnLogger.getLogger(PnDataTable.class);
	
	public PnDataTable(String wicketId, PnDataTableModel dataProvider) {
		super(wicketId, dataProvider.getColumns(), dataProvider, dataProvider.getPageSize());
		this.setOutputMarkupId(true);
		//LOGGER.setLevel(Level.DEBUG);
		this.initTable();
	}
	
	protected void initTable() {
		this.setItemReuseStrategy(DefaultItemReuseStrategy.getInstance());
		
		this.add(new PnCSSAttributeAppender("proxyList pnWebTable"));
		this.addTopToolbar(new PnDataTableHeaders(this));
		this.addBottomToolbar(new PnDataTableNoRecordsToolbar(this));
	}
	
	@Override
	protected Item<PnRecordDTO> newRowItem(final String wicketId, final int index, final IModel<PnRecordDTO> model) {
		PnDataTableRowModel tableRowModel = (PnDataTableRowModel)model;
		PnDataTableRowItem rowItem = new PnDataTableRowItem(wicketId, index, tableRowModel);
		
		if (this.isRowSelectionAllowed()) {
			if (index == 0) {
				rowItem.add(new PnTabIndexAttributeModifier());
				rowItem.add(new PnAjaxClickOnEnterKeyBehavior());
			}
			
			rowItem.add(PnTableRowAjaxClickBehavior.onEvent((event, sourceComponent, target) -> {
				LOGGER.debug("row item click");
				IRequestParameters requestParameters = sourceComponent.getRequest().getRequestParameters();
				boolean isCtrlKey = requestParameters.getParameterValue("ctrlKey").toBoolean(false);
				boolean isShiftKey = requestParameters.getParameterValue("shiftKey").toBoolean(false);
				
				this.fireSelectionChangeEvent(sourceComponent, isCtrlKey, isShiftKey, target);
				
				target.appendJavaScript("PlanonWebClient.PnWebProxyPanelFocusOnChange.focusSelectedProxyItem('#" + this.getMarkupId() + "', true);");
			}));
			
			rowItem.add(PnAjaxEventBehavior.onEvent("dblclick", (event, sourceComponent, target) -> {
				LOGGER.debug("row item dblclick");
				PnDataTableRowItem parentRow = PnDataTable.findParentRowItem(sourceComponent);
				this.fireDoubleClickEvent(parentRow.getRowModel().getRowNumber(), target);
			}));
		}
		
		return rowItem;
	}
	
	public static PnDataTableRowItem findParentRowItem(Component child) {
		if (child instanceof PnDataTableRowItem) {
			return (PnDataTableRowItem)child;
		}
		return child.findParent(PnDataTableRowItem.class);
	}
	
	// GETTERS
	public PnDataTableModel getModel() {
		return (PnDataTableModel)this.getDataProvider();
	}
	
	protected IPnTableSelectionModel getSelectionModel() {
		return this.getModel().getSelectionModel();
	}
	
	// SELECTION
	boolean isRowSelected(int row) {
		return this.getSelectionModel().isRowSelected(row);
	}
	
	public void clearSelection(boolean doUpdateRows, AjaxRequestTarget target) {
		ArrayList<PnDataTableRowModel> selectedRowModels = this.getModel().getSelectedRowModels();
		this.getSelectionModel().clear();
		if (doUpdateRows) {
			this.updateRows(selectedRowModels, target);
		}
	}
	
	public void addSelectedRow(int row) {
		assert (row >= 0);
		if (this.getSelectionModel().isMultipleSelect()) {
			((PnTableMultiSelectionModel)this.getSelectionModel()).addSelectedRow(row);
		}
		else {
			this.getSelectionModel().setSelection(row);
		}
	}
	
	private void handleMultipleRowSelection(int rowIndex, PnDataTableRowItem rowItem, AjaxRequestTarget target) {
		PnTableMultiSelectionModel selectionModel = (PnTableMultiSelectionModel)this.getSelectionModel();
		if (!selectionModel.hasSelection()) {
			selectionModel.setSelection(rowIndex);
			this.fireSelectionChangedEvent(rowIndex, rowIndex, IPnTableRowSelectionListener.SelectionEventType.REPLACE, target);
		}
		else {
			int previousClickIndex = selectionModel.getPreviousClick();
			int fromIndex = Math.min(rowIndex, previousClickIndex);
			int toIndex = Math.max(rowIndex, previousClickIndex);
			selectionModel.setSelectedRange(fromIndex, toIndex);
			this.fireSelectionChangedEvent(fromIndex, toIndex, SelectionEventType.ADD, target);
		}
		target.add(this);
	}
	
	private void handleSingleRowSelection(int index, PnDataTableRowItem rowItem, boolean isCtrlKey, AjaxRequestTarget target) {
		IPnTableSelectionModel selectionModel = this.getSelectionModel();
		
		if (!selectionModel.isMultipleSelect() || !isCtrlKey) {
			selectionModel.setSelection(index);
			this.fireSelectionChangedEvent(index, index, SelectionEventType.REPLACE, target);
		}
		else {
			PnTableMultiSelectionModel multiSelectTableModel = (PnTableMultiSelectionModel)selectionModel;
			multiSelectTableModel.toggleRowSelection(index);
			this.fireSelectionChangedEvent(index, index, selectionModel.isRowSelected(index) ? SelectionEventType.ADD : SelectionEventType.REMOVE, target);
		}
		target.add(this);
	}
	
	// Row Updates
	public void updateRows(List<PnRecordDTO> records, AjaxRequestTarget target) {
		if (records == null || records.size() == 0 || target == null) {
			return;
		}
		
		ArrayList<PnDataTableRowModel> rowModels = new ArrayList<>(records.size());
		for (PnRecordDTO rowDTO : records) {
			rowModels.add((PnDataTableRowModel)this.getModel().model(rowDTO));
		}
		
		this.updateRows(rowModels, target);
	}
	
	public void updateRows(ArrayList<PnDataTableRowModel> rowModels, AjaxRequestTarget target) {
		this.visitChildren(PnDataTableRowItem.class, (PnDataTableRowItem rowItem, IVisit<Void> visit) -> {
			int ndx = rowModels.indexOf(rowItem.getRowModel());
			if (ndx >= 0) {
				PnDataTableRowModel rowModel = rowModels.get(ndx);
				rowModels.remove(ndx);
				
				//this could technically update the model since we are comparing on primary key of the record.
				rowItem.getModel().setObject(rowModel.getObject());
				
				if (rowItem.findParent(Page.class) != null) {
					target.add(rowItem);
				}
			}
			
			if (rowModels.isEmpty()) {
				visit.stop();
				return;
			}
			
			visit.dontGoDeeper();
		});
	}
	
	// EVENT HANDLNG
	public void fireDoubleClickEvent(int rowIndex, AjaxRequestTarget target) {
		LOGGER.debug("fireDoubleClickEvent (rowIndex: " + rowIndex + ")");
		if (this.selectionListeners != null) {
			for (IPnTableRowSelectionListener listener : this.selectionListeners) {
				try {
					listener.doubleClicked(rowIndex, target);
				}
				catch (PnClientException e) {
					LOGGER.error(e);
				}
			}
		}
	}
	
	public void fireOrderChangedEvent(AjaxRequestTarget target) {
		LOGGER.debug("fireOrderChangedEvent");
		if (this.selectionListeners != null) {
			for (IPnTableRowSelectionListener listener : this.selectionListeners) {
				listener.orderChanged(target);
			}
		}
	}
	
	protected void fireSelectionChangeEvent(Component component, final boolean isCtrlKey, final boolean isShiftKey, AjaxRequestTarget target) {
		final PnDataTableRowItem rowItem = PnDataTable.findParentRowItem(component);
		PnDataTableRowModel tableRowModel = rowItem.getRowModel();
		final int rowNumber = tableRowModel.getRowNumber();
		
		LOGGER.debug("fireSelectionChangeEvent (rowNumber: " + rowNumber + "; isCtrlKey: " + isCtrlKey + "; isShiftKey: " + isShiftKey + ")");
		
		if (this.isRowSelectionAllowed() && (!isCtrlKey && !isShiftKey || !this.isMultipleSelect())) {
			this.clearSelection(true, target);
		}
		if (isShiftKey && this.isMultipleSelect()) {
			this.handleMultipleRowSelection(rowNumber, rowItem, target);
		}
		else {
			this.handleSingleRowSelection(rowNumber, rowItem, isCtrlKey, target);
		}
		
		target.appendJavaScript("if(PlanonWebClient.PnWebProxyPanelMouseHover!=null)PlanonWebClient.PnWebProxyPanelMouseHover.initializeMouseHoverListeners('#"
				+ this.getMarkupId() + "');");
	}
	
	public void fireSelectionChangedEvent(int fromRow, int toRow, SelectionEventType eventType, AjaxRequestTarget target) {
		LOGGER.debug("fireSelectionChangedEvent [fromRow=" + fromRow + ", toRow=" + toRow + ", eventType=" + eventType + "]");
		if (this.selectionListeners != null) {
			for (IPnTableRowSelectionListener listener : this.selectionListeners) {
				listener.selectionChanged(fromRow, toRow, eventType, target);
			}
		}
	}
	
	public void setSelectionListeners(List<IPnTableRowSelectionListener> selectionListeners) {
		this.selectionListeners = selectionListeners;
	}
	
	// SETTINGS
	public boolean isRowSelectionAllowed() {
		return this.isRowSelectionAllowed;
	}
	
	public void setRowSelectionAllowed(boolean isRowSelectionAllowed) {
		this.isRowSelectionAllowed = isRowSelectionAllowed;
	}
	
	public boolean isMultipleSelect() {
		return this.getSelectionModel().isMultipleSelect();
	}
	
	public void setMultipleSelect(boolean aFlag) {
		if (aFlag != this.isMultipleSelect()) {
			IPnTableSelectionModel oldSelectionModel = this.getSelectionModel();
			IPnTableSelectionModel newSelectionModel = aFlag ? new PnTableMultiSelectionModel() : new PnTableSingleSelectionModel();
			
			this.getModel().setSelectionModel(newSelectionModel);
			
			newSelectionModel.setSelection(oldSelectionModel.getSelectedRow());
		}
	}
}
