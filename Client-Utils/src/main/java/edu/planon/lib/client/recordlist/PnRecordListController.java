package edu.planon.lib.client.recordlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;

import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.recordlist.event.IPnRecordListListener;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import edu.planon.lib.client.table.selection.IPnTableRowSelectionListener;
import nl.planon.util.pnlogging.PnLogger;

public class PnRecordListController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final PnLogger LOGGER = PnLogger.getLogger(PnRecordListController.class);
	private final PnRecordListModel model;
	private PnRecordListPanel view;
	private boolean isMultiSelect;
	private final List<IPnRecordListListener> recordListeners = new ArrayList<IPnRecordListListener>(1);
	private final List<PnRecordDTO> selectedRecords = new ArrayList<>();
	
	public PnRecordListController(PnRecordListModel aModel, boolean sortable) {
		this.model = aModel;
		this.model.setSortable(sortable);
		//LOGGER.setLevel(Level.DEBUG);
	}
	
	public static final PnRecordListController createMVC(String wicketId, PnReferenceFieldDefDTO referenceDTO, boolean isMultiSelect, boolean isSortable)
			throws PnClientException {
		PnRecordListModel model = new PnRecordListModel(referenceDTO, isSortable);
		
		PnRecordListController controller = new PnRecordListController(model, isSortable);
		controller.setMultiSelect(isMultiSelect);
		
		PnRecordListPanel view = new PnRecordListPanel(wicketId, controller);
		
		controller.setView(view);
		return controller;
	}
	
	public void reset(boolean doUpdateRows, AjaxRequestTarget target) {
		this.getModel().reset();
		this.getView().updateView(target);
		this.getView().getDataTable().clearSelection(doUpdateRows, target);
	}
	
	// Selection
	public final List<PnRecordDTO> getAllRecords() {
		return this.getModel().getAllRecords();
	}
	
	public final List<PnRecordDTO> getSelectedRecords() {
		return this.selectedRecords;
	}
	
	public void setSelectedRecords(boolean doHighlightSelected, List<PnRecordDTO> records, AjaxRequestTarget target) {
		assert (records != null);
		this.selectedRecords.clear();
		this.selectedRecords.addAll(records);
		if (doHighlightSelected) {
			this.getView().highlightSelectedRecords(true, target);
			
			if (this.selectedRecords.isEmpty()) {
				this.fireDeselectionEvent(target);
			}
			else {
				this.fireClickedEvent(target);
			}
		}
	}
	
	public void selectAll(AjaxRequestTarget target) {
		this.setSelectedRecords(true, this.getModel().getAllRecords(), target);
	}
	
	public void clearSelection(boolean doUpdateRows, AjaxRequestTarget target) {
		if (!this.selectedRecords.isEmpty()) {
			this.selectedRecords.clear();
			this.getView().getDataTable().clearSelection(doUpdateRows, target);
			this.fireDeselectionEvent(target);
		}
	}
	
	// Actions
	public final void fireDeselectionEvent(AjaxRequestTarget target) {
		LOGGER.debug("fireDeselectionEvent");
		for (IPnRecordListListener listener : this.recordListeners) {
			listener.onDeselection(target);
		}
	}
	
	public final void fireClickedEvent(AjaxRequestTarget target) {
		LOGGER.debug("fireClickedEvent");
		for (IPnRecordListListener listener : this.recordListeners) {
			listener.onClick(target);
		}
	}
	
	// Search Filters
	public void onSearchFilterChange(AjaxRequestTarget target) {
		this.reset(true, target);
		this.setSelectedRecords(true, this.getModel().validateRecords(this.getSelectedRecords()), target);
	}
	
	public void addDefaultSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.getTableModel().addDefaultSearchFilter(searchFilter);
	}
	
	public void addDefaultSearchFilter(PnSearchFilterModel searchFilter) {
		this.getTableModel().addDefaultSearchFilter(searchFilter);
	}
	
	public void setSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.getTableModel().setSearchFilter(searchFilter);
	}
	
	// MVC
	public void setView(PnRecordListPanel view) {
		this.view = view;
	}
	
	public PnRecordListPanel getView() {
		return this.view;
	}
	
	public PnRecordListModel getModel() {
		return this.model;
	}
	
	public final PnRecordListTableModel getTableModel() {
		return this.model.getTableModel();
	}
	
	// SETTINGS
	public boolean isMultipleSelect() {
		return this.isMultiSelect;
	}
	
	public void setMultiSelect(boolean isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
		if (this.getView() != null) {
			this.getView().setMultiSelect(isMultiSelect);
		}
	}
	
	// Listeners
	public void addTableSelectionListener(IPnTableRowSelectionListener selectionListener) {
		this.getView().addTableSelectionListener(selectionListener);
	}
	
	public void addRecordListener(IPnRecordListListener listener) {
		this.recordListeners.add(listener);
	}
}
