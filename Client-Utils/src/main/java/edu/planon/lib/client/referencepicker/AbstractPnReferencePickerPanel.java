package edu.planon.lib.client.referencepicker;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import edu.planon.lib.client.common.behavior.CloseModalBehavior;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.search.PnSearchPanel;
import edu.planon.lib.client.search.model.PnSearchFilterModel;

public abstract class AbstractPnReferencePickerPanel extends Panel implements Serializable, IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private final CloseModalBehavior okEventBehavior;
	private final PnReferenceFieldDefDTO referenceDTO;
	private PnSearchPanel searchPanel;
	
	public AbstractPnReferencePickerPanel(String wicketId, PnReferenceFieldDefDTO referenceDTO) throws PnClientException {
		super(wicketId);
		this.referenceDTO = referenceDTO;
		
		this.setOutputMarkupId(true);
		
		List<PnFieldDefDTO> fieldDefs = referenceDTO.getFieldsList();
		this.searchPanel = new PnSearchPanel("searchFilter", fieldDefs);
		this.add(this.searchPanel);
		
		Button btnOk = new Button("btnOk", Model.of("Ok"));
		this.okEventBehavior = new CloseModalBehavior("click");
		btnOk.add(this.okEventBehavior);
		this.add(btnOk);
		
		Button btnCancel = new Button("btnCancel", Model.of("Cancel"));
		btnCancel.add(new CloseModalBehavior("click"));
		this.add(btnCancel);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		//connect search to filter table
		this.setSearchFilter(this.searchPanel.getSearchFilter());
		
		this.searchPanel.addEventListener((event, sourceComponent, target) -> {
			this.onSearchFilterChange(target);
		});
	}
	
	protected abstract void setSearchFilter(List<PnSearchFilterModel> searchFilter);
	
	protected abstract void onSearchFilterChange(final AjaxRequestTarget target);
	
	public abstract void setDefaultSearchFilter(List<PnSearchFilterModel> aDefaultSearchFilter);
	
	public abstract List<PnRecordDTO> getSelectedRecords();
	
	public abstract void setSelectedRecords(List<PnRecordDTO> selectedRecords, AjaxRequestTarget editorTarget);
	
	protected final PnReferenceFieldDefDTO getReferenceDTO() {
		return this.referenceDTO;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(this.getClass(), "AbstractPnReferencePickerPanel.css")));
	}
	
	/***** EVENTS *****/
	@Override
	public void addEventListener(IAjaxEventListener eventListener) {
		this.okEventBehavior.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.okEventBehavior.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return this.okEventBehavior.getEventListeners();
	}
}
