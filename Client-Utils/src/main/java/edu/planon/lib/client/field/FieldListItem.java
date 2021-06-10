package edu.planon.lib.client.field;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.dto.PnFieldDTO;
import edu.planon.lib.client.dto.PnFieldDefDTO;
import edu.planon.lib.client.panel.AbstractPanel;

public abstract class FieldListItem<T extends Serializable> extends AbstractPanel {
	private static final long serialVersionUID = 1L;
	protected PnFieldDTO<T> fieldDTO;
	
	public FieldListItem(String id, PnFieldDTO<T> fieldDTO) {
		super(id);
		this.fieldDTO = fieldDTO;
		this.setOutputMarkupId(true);
	}
	
	protected void onInitialize() {
		super.onInitialize();
		
		this.setOutputMarkupId(true);
		
		this.add(new AttributeAppender("class", Model.of("fieldValueEditor fieldValueEditorWithIcon"), " "));
		
		this.add(new AttributeAppender("class", Model.of("editorDisabled"), " ") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		});
		
		Label requiredLabel = new Label("requiredLabel", Model.of("*"));
		requiredLabel.setOutputMarkupId(true);
		requiredLabel.setVisible(this.getFormComponent().isRequired());
		this.add(requiredLabel);
		
		SimpleFormComponentLabel fieldLabel = new SimpleFormComponentLabel("fieldLabel", this.getFormComponent());
		fieldLabel.setOutputMarkupId(true);
		this.add(fieldLabel);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		this.setEnabled(fieldDTO.isEnabled());
	}
	
	public PnFieldDTO<T> getFieldDTO() {
		return this.fieldDTO;
	}
	
	public PnFieldDefDTO getFieldDefDTO() {
		return this.fieldDTO;
	}
	
	public abstract void addEventListener(IAjaxEventListener eventListener);
	
	public abstract void addEventListener(List<IAjaxEventListener> eventListeners);
	
	public abstract List<IAjaxEventListener> getEventListeners();
	
	public abstract FormComponent<?> getFormComponent();
}
