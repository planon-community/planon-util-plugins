package edu.planon.lib.client.recordlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;

import edu.planon.lib.client.common.behavior.PnAjaxEventBehavior;
import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.util.PnWicketSessionUtils;
import edu.planon.lib.client.recordlist.paging.PnAjaxPagingNavigator;
import edu.planon.lib.client.table.PnDataTable;
import edu.planon.lib.client.table.selection.IPnTableRowSelectionListener;
import nl.planon.util.pnlogging.PnLogger;

public class PnRecordListPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final PnLogger LOGGER = PnLogger.getLogger(PnRecordListPanel.class);
	private final PnRecordListController controller;
	private PnDataTable table;
	private WebMarkupContainer navigationPanel;
	private PnAjaxPagingNavigator pagingNavigator;
	private WebMarkupContainer navButtonContainer;
	private List<IPnTableRowSelectionListener> selectionListeners;
	private List<IAjaxEventListener> listeners = new ArrayList<IAjaxEventListener>();
	
	public PnRecordListPanel(String wicketId, PnRecordListController controller) throws PnClientException {
		super(wicketId);
		this.controller = controller;
		
		this.setOutputMarkupId(true);
		
		this.add(new PnCSSAttributeAppender("PnWebProxyListPanelOuter PnWebProxyListPanel"));
		this.add(new AttributeAppender("style", "display: flex;flex-direction: column;", ";"));
		
		this.addTableSelectionListener(new SelectionListener());
		
		this.initTable(null);
	}
	
	protected void initTable(AjaxRequestTarget target) throws PnClientException {
		if (this.table == null || this.table.getRowCount() != this.controller.getModel().getLoadedRowCount()) {
			List<PnRecordDTO> selection = Collections.emptyList();
			boolean multiSelect = this.controller.isMultipleSelect();
			boolean rowSelectionAllowed = true;
			if (this.table != null) {
				selection = this.table.getModel().getSelectedRecords();
				multiSelect = this.table.isMultipleSelect();
				rowSelectionAllowed = this.table.isRowSelectionAllowed();
			}
			
			this.table = new PnDataTable("proxyList", this.controller.getModel().getTableModel());
			this.table.setMultipleSelect(multiSelect);
			this.table.setRowSelectionAllowed(rowSelectionAllowed);
			this.table.add(new AttributeModifier("cellspacing", "0"));
			this.table.setSelectionListeners(this.selectionListeners);
			
			if (this.get(this.table.getId()) != null) {
				this.table.updateRows(selection, target);
			}
			
			this.addOrReplace(this.table);
			
			this.createNavigationPanel();
		}
		
		if (this.table != null) {
			this.table.setVisible(true);
		}
	}
	
	private void createNavigationPanel() {
		this.navigationPanel = new WebMarkupContainer("navigationPanel");
		this.navigationPanel.setVisible(this.controller.isMultipleSelect() || this.table.getPageCount() > 1L);
		this.addOrReplace(this.navigationPanel);
		
		//navButtonContainer
		this.navButtonContainer = new WebMarkupContainer("navButtonContainer");
		this.navButtonContainer.setOutputMarkupId(true);
		this.navButtonContainer.setVisible(this.table.isMultipleSelect());
		this.navigationPanel.addOrReplace(this.navButtonContainer);
		
		//Select All Button
		Button selectAll = new Button("selectAll");
		PnAjaxEventBehavior selectAllEventBehaviour = new PnAjaxEventBehavior("click");
		selectAllEventBehaviour.addEventListener((event, sourceComponent, target) -> this.controller.selectAll(target));
		selectAllEventBehaviour.addEventListener(this.listeners);
		selectAll.add(selectAllEventBehaviour);
		this.navButtonContainer.addOrReplace(selectAll);
		
		//Select None Button
		Button selectNone = new Button("selectNone");
		PnAjaxEventBehavior selectNoneEventBehaviour = new PnAjaxEventBehavior("click");
		selectNoneEventBehaviour.addEventListener((event, sourceComponent, target) -> this.controller.clearSelection(true, target));
		selectNoneEventBehaviour.addEventListener(this.listeners);
		selectNone.add(selectNoneEventBehaviour);
		this.navButtonContainer.addOrReplace(selectNone);
		
		//Pagination Links
		this.pagingNavigator = new PnAjaxPagingNavigator("mainNavigation", this.table);
		this.navigationPanel.addOrReplace(this.pagingNavigator);
	}
	
	public void updateView(AjaxRequestTarget target) {
		try {
			this.initTable(target);
			if (this.findPage() != null) {
				target.add(this);
			}
		}
		catch (PnClientException e) {
			LOGGER.error(e);
		}
	}
	
	/***** SELECTION METHODS *****/
	void highlightSelectedRecords(boolean setCurrentPage, AjaxRequestTarget target) {
		List<PnRecordDTO> selectedRecords = this.controller.getSelectedRecords();
		if (selectedRecords.isEmpty()) {
			this.controller.clearSelection(true, target);
			if (setCurrentPage) {
				this.pagingNavigator.setCurrentPage(0, false, target);
			}
			return;
		}
		
		for (PnRecordDTO record : selectedRecords) {
			int row = this.controller.getModel().fetchRecord(record);
			if (row != -1) {
				this.table.addSelectedRow(row);
			}
		}
		
		if (setCurrentPage && selectedRecords.size() > 0) {
			this.setCurrentPage(selectedRecords, target);
			this.table.updateRows(selectedRecords, target);
		}
	}
	
	private void setCurrentPage(List<PnRecordDTO> selectedRecords, AjaxRequestTarget target) {
		int rowsPerPage = this.controller.getModel().getPageSize();
		PnRecordDTO selectedRecord = selectedRecords.get(0);
		int rowNumber = this.controller.getModel().getRowNumber(selectedRecord);
		if (rowNumber >= 0) {
			int pageNumber = rowNumber / rowsPerPage;
			this.pagingNavigator.setCurrentPage(pageNumber, false, target);
		}
		else {
			if (!this.controller.getModel().isFetchComplete()) {
				rowNumber = this.controller.getModel().fetchRecord(selectedRecord);
			}
			if (rowNumber >= 0) {
				int pageNumber = rowNumber / rowsPerPage;
				this.pagingNavigator.setCurrentPage(pageNumber, true, target);
			}
		}
	}
	
	/***** SETTINGS *****/
	public void setMultiSelect(boolean enable) {
		this.table.setMultipleSelect(enable);
		this.navButtonContainer.setVisible(enable);
		
		PnWicketSessionUtils.addComponentToAjaxRequestTarget(this);
	}
	
	protected final PnDataTable getDataTable() {
		return this.table;
	}
	
	/***** EVENT HANDLNG *****/
	public void addTableSelectionListener(IPnTableRowSelectionListener selectionListener) {
		if (this.selectionListeners == null) {
			this.selectionListeners = new ArrayList<IPnTableRowSelectionListener>(1);
		}
		this.selectionListeners.add(selectionListener);
	}
	
	protected class SelectionListener implements IPnTableRowSelectionListener {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void orderChanged(AjaxRequestTarget target) {
			PnRecordListPanel.this.controller.reset(true, target);
			PnRecordListPanel.this.highlightSelectedRecords(true, target);
		}
		
		@Override
		public void selectionChanged(int fromRow, int toRow, SelectionEventType eventType, AjaxRequestTarget target) {
			ArrayList<PnRecordDTO> changedRecords = new ArrayList<>(toRow - fromRow + 1);
			try {
				PnRecordListModel model = PnRecordListPanel.this.controller.getModel();
				for (int i = fromRow; i <= toRow; ++i) {
					PnRecordDTO record = model.getRecord(i);
					if (record != null) {
						changedRecords.add(record);
					}
				}
			}
			catch (PnClientException e) {
				LOGGER.error(e);
			}
			
			List<PnRecordDTO> selectedRecords = PnRecordListPanel.this.controller.getSelectedRecords();
			switch (eventType) {
				case ADD: {
					selectedRecords.addAll(changedRecords);
					break;
				}
				case REMOVE: {
					selectedRecords.removeAll(changedRecords);
					break;
				}
				case REPLACE: {
					selectedRecords.clear();
					selectedRecords.addAll(changedRecords);
					break;
				}
			}
			if (selectedRecords.isEmpty()) {
				PnRecordListPanel.this.controller.fireDeselectionEvent(target);
			}
			else {
				PnRecordListPanel.this.controller.fireClickedEvent(target);
			}
		}
	}
}
