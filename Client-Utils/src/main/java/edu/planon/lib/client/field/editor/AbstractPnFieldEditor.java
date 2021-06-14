package edu.planon.lib.client.field.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.common.behavior.IAjaxEventSource;
import edu.planon.lib.client.common.ui.AjaxIconLink;
import edu.planon.lib.client.dto.PnFieldDTO;
import edu.planon.lib.client.exception.PnClientRuntimeException;
import edu.planon.lib.client.field.util.PnFieldTypeUtils;
import edu.planon.lib.client.panel.AbstractPanel;

public abstract class AbstractPnFieldEditor extends AbstractPanel implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private final List<IAjaxEventListener> eventListeners = new ArrayList<IAjaxEventListener>(1);
	private IAjaxEventSource eventSource;
	private AjaxIconLink infoButton;
	private AjaxIconLink referenceButton;
	protected PnFieldDTO<?> fieldDTO;
	
	public AbstractPnFieldEditor(String wicketId, PnFieldDTO<?> fieldDTO) {
		super(wicketId, Model.of(fieldDTO));
		this.fieldDTO = fieldDTO;
		this.setOutputMarkupId(true);
		

		this.add(createInfoButton("infoButton"));
		this.add(createReferenceButton("refButton"));
	}
	
	protected void onInitialize() {
		super.onInitialize();
		
		
		
	}
	
	protected AjaxIconLink createInfoButton(String wicketId) {
		String iconName = PnFieldTypeUtils.getEditorInfoIcon(fieldDTO.getFieldType());
		String tooltip = PnFieldTypeUtils.getEditorInfoTooltip(fieldDTO.getFieldType());
		
		this.infoButton = new AjaxIconLink(wicketId, "editor-link pnicon-"+iconName);
		this.infoButton.add(new AttributeAppender("title", tooltip, " "));
		this.infoButton.setVisible(false);
		return this.infoButton;
	}
	
	public AjaxIconLink getInfoButton() {
		return this.infoButton;
	}
	
	protected AjaxIconLink createReferenceButton(String wicketId) {
		String iconName = PnFieldTypeUtils.getEditorReferenceIcon(fieldDTO.getFieldType());
		String tooltip = PnFieldTypeUtils.getEditorReferenceTooltip(fieldDTO.getFieldType());
		
		this.referenceButton = new AjaxIconLink(wicketId, "editor-link pnicon-"+iconName);
		this.referenceButton.add(new AttributeAppender("title", tooltip, " "));
		this.referenceButton.setVisible(false);
		return this.referenceButton;
	}
	
	public AjaxIconLink getReferenceButton() {
		return this.referenceButton;
	}
	
	public String getClassAttribute() {
		return "fieldValueEditor";
	}
	
	public abstract boolean isEditorEnabled();
	
	public abstract FormComponent<?> getFormComponent();
	
	protected final void setEventSource(IAjaxEventSource eventSource) throws PnClientRuntimeException {
		if(this.eventSource != null) {
			throw new PnClientRuntimeException("EventSource has already been set and it can't be set again.");
		}
		
		if(this.eventListeners.size() > 0) {
			eventSource.addEventListener(this.eventListeners);
		}
		
		this.eventSource = eventSource;
	}
	
	@Override
	public final void addEventListener(IAjaxEventListener eventListener) {
		if(this.eventSource != null) {
			this.eventSource.addEventListener(eventListener);
		}
		else {
			this.eventListeners.add(eventListener);
		}
	}
	
	@Override
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			if(this.eventSource != null) {
				this.eventSource.addEventListener(eventListeners);
			}
			else {
				this.eventListeners.addAll(eventListeners);
			}
		}
	}
	
	@Override
	public final List<IAjaxEventListener> getEventListeners() {
		if(this.eventSource != null) {
			return this.eventSource.getEventListeners();
		}
		return Collections.unmodifiableList(this.eventListeners);
	}
}
