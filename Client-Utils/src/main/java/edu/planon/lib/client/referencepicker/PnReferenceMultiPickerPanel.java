package edu.planon.lib.client.referencepicker;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.PnCSSAttributeModifier;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.recordlist.PnRecordListController;
import edu.planon.lib.client.search.model.PnSearchFilterModel;

public class PnReferenceMultiPickerPanel extends AbstractPnReferencePickerPanel {
	private static final long serialVersionUID = 1L;
	private final PnReferenceMultiPickerController controller;
	private PnRecordListController unselectedProxyController;
	private PnRecordListController selectedProxyController;
	private AjaxIconLink rightButton;
	private AjaxIconLink leftButton;
	
	public PnReferenceMultiPickerPanel(String wicketId, PnReferenceFieldDefDTO referenceDTO) throws PnClientException {
		super(wicketId, referenceDTO);
		
		this.unselectedProxyController = PnRecordListController.createMVC("filterTable", this.getReferenceDTO(), true, true);
		this.selectedProxyController = PnRecordListController.createMVC("selectedTable", this.getReferenceDTO(), true, true);
		
		this.controller = new PnReferenceMultiPickerController(this, this.unselectedProxyController, this.selectedProxyController);
		
		this.addComponents();
	}
	
	private void addComponents() {
		this.add(new Label("available", Model.of("Available")));
		this.addOrReplace(this.unselectedProxyController.getView());
		
		this.addOrReplace(this.getRightArrowButton());
		this.addOrReplace(this.getLeftArrowButton());
		
		this.add(new Label("inUse", Model.of("In use")));
		this.addOrReplace(this.selectedProxyController.getView());
	}
	
	protected AjaxIconLink getLeftArrowButton() {
		if (this.leftButton == null) {
			this.leftButton = AjaxIconLink.onClick("leftButton", "pnicon-circle-arrow-left", (event, sourceComponent, target) -> {
				this.controller.handleRemove(target);
			});
			this.leftButton.setEnabled(false);
		}
		return this.leftButton;
	}
	
	protected AjaxIconLink getRightArrowButton() {
		if (this.rightButton == null) {
			this.rightButton = AjaxIconLink.onClick("rightButton", "pnicon-circle-arrow-right", (event, sourceComponent, target) -> {
				this.controller.handleAdd(target);
			});
			this.rightButton.setEnabled(false);
		}
		return this.rightButton;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.add(new PnCSSAttributeModifier("PlanonWebBaseDialog PnWebBaseDialog PnWebMtoNDialogWithFilter EMULATES PnWebPalettePopup"));
	}
	
	@Override
	protected void setSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.unselectedProxyController.setSearchFilter(searchFilter);
	}
	
	@Override
	protected void onSearchFilterChange(AjaxRequestTarget target) {
		this.unselectedProxyController.onSearchFilterChange(target);
	}
	
	@Override
	public void setDefaultSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.unselectedProxyController.addDefaultSearchFilter(searchFilter);
	}
	
	@Override
	public List<PnRecordDTO> getSelectedRecords() {
		return this.selectedProxyController.getAllRecords();
	}
	
	@Override
	public void setSelectedRecords(List<PnRecordDTO> selectedRecords, AjaxRequestTarget target) {
		this.controller.handleReplace(selectedRecords, target);
	}
}
