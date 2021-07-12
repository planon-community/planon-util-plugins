package edu.planon.lib.client.referencepicker;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;

import edu.planon.lib.client.common.behavior.PnCSSAttributeModifier;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.recordlist.PnRecordListController;
import edu.planon.lib.client.search.model.PnSearchFilterModel;

public class PnReferenceSinglePickerPanel extends AbstractPnReferencePickerPanel {
	private static final long serialVersionUID = 1L;
	private PnRecordListController controller;
	
	public PnReferenceSinglePickerPanel(String wicketId, PnReferenceFieldDefDTO referenceDTO) throws PnClientException {
		super(wicketId, referenceDTO);
		
		this.controller = PnRecordListController.createMVC("filterTable", this.getReferenceDTO(), false, true);
		this.addOrReplace(this.controller.getView());
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.add(new PnCSSAttributeModifier("PlanonWebBaseDialog PnWebBaseDialog PnWebFieldValueDialog PnWebProxyRefDialog"));
	}
	
	@Override
	protected void setSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.controller.setSearchFilter(searchFilter);
	}
	
	@Override
	protected void onSearchFilterChange(AjaxRequestTarget target) {
		this.controller.onSearchFilterChange(target);
	}
	
	@Override
	public void setDefaultSearchFilter(List<PnSearchFilterModel> searchFilter) {
		this.controller.addDefaultSearchFilter(searchFilter);
	}
	
	@Override
	public List<PnRecordDTO> getSelectedRecords() {
		return this.controller.getSelectedRecords();
	}
	
	@Override
	public void setSelectedRecords(List<PnRecordDTO> selectedRecords, AjaxRequestTarget target) {
		this.controller.setSelectedRecords(true, selectedRecords, target);
	}
}
