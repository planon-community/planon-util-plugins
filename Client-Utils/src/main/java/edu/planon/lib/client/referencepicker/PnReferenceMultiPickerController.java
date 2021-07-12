package edu.planon.lib.client.referencepicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;

import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.recordlist.PnRecordListController;
import edu.planon.lib.client.recordlist.event.IPnRecordListListener;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import edu.planon.lib.client.table.selection.IPnTableRowSelectionListener;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.util.pnlogging.PnLogger;

public class PnReferenceMultiPickerController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final PnReferenceMultiPickerPanel view;
	private final PnRecordListController unselectedProxyController;
	private final PnRecordListController selectedProxyController;
	private final List<Object> selectedCodes = new ArrayList<Object>();
	private Object dummyFilter;
	
	public PnReferenceMultiPickerController(PnReferenceMultiPickerPanel view, PnRecordListController unselectedProxyController,
			PnRecordListController selectedProxyController) {
		this.view = view;
		this.unselectedProxyController = unselectedProxyController;
		this.selectedProxyController = selectedProxyController;
		
		this.initListeners();
		this.initSelectionFilter();
	}
	
	private void initListeners() {
		this.unselectedProxyController.addTableSelectionListener(IPnTableRowSelectionListener.onDoubleClick((rowIndex, target) -> {
			PnRecordDTO record = this.unselectedProxyController.getModel().getRecord(rowIndex);
			this.unselectedProxyController.setSelectedRecords(false, new ArrayList<>(Arrays.asList(record)), target);
			this.handleAdd(target);
		}));
		this.selectedProxyController.addTableSelectionListener(IPnTableRowSelectionListener.onDoubleClick((rowIndex, target) -> {
			PnRecordDTO record = this.selectedProxyController.getModel().getRecord(rowIndex);
			this.selectedProxyController.setSelectedRecords(false, new ArrayList<>(Arrays.asList(record)), target);
			this.handleRemove(target);
		}));
		
		this.unselectedProxyController.addRecordListener(new RecordListener(this.view.getRightArrowButton(), this.selectedProxyController));
		this.selectedProxyController.addRecordListener(new RecordListener(this.view.getLeftArrowButton(), this.unselectedProxyController));
	}
	
	private void initSelectionFilter() {
		String fieldName = this.selectedProxyController.getModel().getFilterFieldName();
		this.dummyFilter = "Syscode".equals(fieldName) ? Integer.valueOf(-1) : "-1";
		this.selectedCodes.add(this.dummyFilter);
		
		PnSearchFilterModel selectedDefaultSearchFilter = new PnSearchFilterModel(fieldName, PnESOperator.IN, this.selectedCodes);
		this.selectedProxyController.addDefaultSearchFilter(selectedDefaultSearchFilter);
		
		PnSearchFilterModel defaultSearchFilter = new PnSearchFilterModel(fieldName, PnESOperator.NOT_IN, this.selectedCodes);
		this.unselectedProxyController.addDefaultSearchFilter(defaultSearchFilter);
	}
	
	public void handleReplace(List<PnRecordDTO> selectedRecords, AjaxRequestTarget target) {
		this.selectedCodes.clear();
		this.selectedCodes.add(this.dummyFilter);
		
		if (selectedRecords != null) {
			for (PnRecordDTO proxyValue : selectedRecords) {
				Object value = this.dummyFilter instanceof String ? proxyValue.getFields()[0] : Integer.valueOf(proxyValue.getFields()[0]);
				if (!this.selectedCodes.contains(value)) {
					this.selectedCodes.add(value);
				}
			}
		}
		
		this.unselectedProxyController.clearSelection(false, target);
		this.unselectedProxyController.reset(false, target);
		
		this.selectedProxyController.clearSelection(false, target);
		this.selectedProxyController.reset(false, target);
	}
	
	public void handleAdd(AjaxRequestTarget target) {
		List<PnRecordDTO> selectedRecords = new ArrayList<>(this.unselectedProxyController.getSelectedRecords());
		for (PnRecordDTO proxyValue : selectedRecords) {
			Object value = this.dummyFilter instanceof String ? proxyValue.getFields()[0] : Integer.valueOf(proxyValue.getFields()[0]);
			if (!this.selectedCodes.contains(value)) {
				this.selectedCodes.add(value);
			}
		}
		
		this.unselectedProxyController.reset(false, target);
		this.unselectedProxyController.clearSelection(false, target);
		
		this.selectedProxyController.reset(false, target);
		this.selectedProxyController.setSelectedRecords(true, selectedRecords, target);
	}
	
	public void handleRemove(AjaxRequestTarget target) {
		List<PnRecordDTO> selectedRecords = new ArrayList<>(this.selectedProxyController.getSelectedRecords());
		for (PnRecordDTO record : selectedRecords) {
			Object value = this.dummyFilter instanceof String ? record.getFields()[0] : Integer.valueOf(record.getFields()[0]);
			this.selectedCodes.remove(value);
		}
		
		this.selectedProxyController.clearSelection(false, target);
		this.selectedProxyController.reset(false, target);
		
		this.unselectedProxyController.setSelectedRecords(true, selectedRecords, target);
		this.unselectedProxyController.reset(false, target);
	}
	
	protected static class RecordListener implements IPnRecordListListener {
		private static final long serialVersionUID = 1L;
		private static final PnLogger LOGGER = PnLogger.getLogger(PnReferenceMultiPickerController.class);
		private final AjaxIconLink button;
		private final PnRecordListController otherListController;
		
		public RecordListener(AjaxIconLink button, PnRecordListController otherListController) {
			this.button = button;
			this.otherListController = otherListController;
			this.enableButton(false, null);
			//LOGGER.setLevel(Level.DEBUG);
		}
		
		@Override
		public void onDeselection(AjaxRequestTarget target) {
			LOGGER.debug("onDeselection");
			this.enableButton(false, target);
		}
		
		@Override
		public void onClick(AjaxRequestTarget target) {
			LOGGER.debug("onClick");
			this.enableButton(true, target);
			//this.otherListController.clearSelection(true, target);
		}
		
		protected void enableButton(boolean enable, AjaxRequestTarget target) {
			if (enable && target != null) {
				target.focusComponent(this.button);
			}
			if (enable != this.button.isEnabled()) {
				this.button.setEnabled(enable);
				if (target != null) {
					target.add(this.button);
				}
			}
		}
		
		protected PnRecordListController getOtherListController() {
			return this.otherListController;
		}
	}
}
