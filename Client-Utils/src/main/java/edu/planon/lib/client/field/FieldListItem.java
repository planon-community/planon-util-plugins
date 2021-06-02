package edu.planon.lib.client.field;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.dto.PnFieldDTO;

public abstract class FieldListItem<T extends Serializable> extends Panel {
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
		/*
		 * if (this.selectedField.isCommentField() || this.selectedField.getInputLength() > 300) {
		 * this.add(new CssAttributeModifier("fieldValueEditor textAreaFieldValueEditor")); } else {
		 * this.add(new CssAttributeModifier("fieldValueEditor fieldValueEditorWithIcon")); }
		 */
		
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
	
	public abstract FormComponent<?> getFormComponent();
}
