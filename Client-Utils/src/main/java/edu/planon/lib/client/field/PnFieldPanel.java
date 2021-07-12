package edu.planon.lib.client.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.behavior.PnCSSAttributeModifier;
import edu.planon.lib.client.common.dto.PnFieldDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;
import edu.planon.lib.client.field.editor.AbstractPnFieldEditor;
import edu.planon.lib.client.field.editor.PnBooleanFieldEditor;
import edu.planon.lib.client.field.editor.PnDateFieldEditor;
import edu.planon.lib.client.field.editor.PnReferenceFieldEditor;
import edu.planon.lib.client.field.editor.PnTextAreaFieldEditor;
import edu.planon.lib.client.field.editor.PnTextFieldEditor;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldPanel extends Panel implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	protected final PnFieldDTO<?> fieldDTO;
	private final List<IAjaxEventListener> eventListeners = new ArrayList<IAjaxEventListener>(1);
	private AbstractPnFieldEditor editor;
	private WebMarkupContainer container;
	
	public PnFieldPanel(String wicketId, final PnFieldDTO<?> fieldDTO) {
		super(wicketId, Model.of(fieldDTO));
		this.fieldDTO = fieldDTO;
		
		this.setOutputMarkupId(true);
		this.initPanel();
	}
	
	private void initPanel() {
		this.container = new WebMarkupContainer("editorContainer");
		this.add(this.container);
		this.container.setOutputMarkupId(true);
		
		this.editor = this.createEditor("fieldEditor");
		this.editor.add(new PnCSSAttributeModifier("fieldEditor"));
		this.container.add(this.editor);
		
		this.container.add(new PnCSSAttributeAppender("editorDisabled") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !PnFieldPanel.this.getFieldEditor().isEditorEnabled();
			}
		});
		
		this.container.add(this.createLabel("fieldLabel"));
		this.container.add(this.createMandatorySymbol("mandatorySymbol"));
		
		this.container.add(new PnCSSAttributeAppender(this.editor.getClassAttribute()));
		
		if (this.getFieldEditor().getInfoButton().isVisible() || this.getFieldEditor().getReferenceButton().isVisible()) {
			this.container.add(new PnCSSAttributeAppender("fieldValueEditorWithIcon"));
		}
	}
	
	public final WebMarkupContainer getContainer() {
		return this.container;
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractPnFieldEditor createEditor(String wicketId) {
		PnESValueType fieldType = this.fieldDTO.getFieldType();
		Class<?> dataType = this.fieldDTO.getDataType();
		
		if (this.fieldDTO instanceof PnReferenceFieldDTO && (fieldType.equals(PnESValueType.REFERENCE) || fieldType.equals(PnESValueType.STRING_REFERENCE))) {
			this.editor = new PnReferenceFieldEditor(wicketId, (PnReferenceFieldDTO)this.fieldDTO);
		}
		else if (dataType.isAssignableFrom(Date.class)) {
			this.editor = new PnDateFieldEditor(wicketId, (PnFieldDTO<Date>)this.fieldDTO);
		}
		else if (dataType.isAssignableFrom(Boolean.class)) {
			this.editor = new PnBooleanFieldEditor(wicketId, (PnFieldDTO<Boolean>)this.fieldDTO);
		}
		else if (dataType.isAssignableFrom(String.class) && this.fieldDTO.isExtendedField()) {
			this.editor = new PnTextAreaFieldEditor(wicketId, (PnFieldDTO<String>)this.fieldDTO);
		}
		else {
			this.editor = new PnTextFieldEditor(wicketId, this.fieldDTO);
		}
		
		this.editor.setOutputMarkupId(true);
		return this.editor;
		
		//TODO add support for a custom implementation class
		//Constructor<? extends AbstractPnFieldEditor> fieldValueEditorConstructor;
		//fieldEditorConstructor = aFieldValueEditor.getConstructor(String.class, PnFieldDTO.class);
		//this.editor =  fieldEditorConstructor.newInstance(wicketId, fieldDTO);
	}
	
	public final AbstractPnFieldEditor getFieldEditor() {
		return this.editor;
	}
	
	protected Component createLabel(String wicketId) {
		SimpleFormComponentLabel fieldLabel = new SimpleFormComponentLabel(wicketId, this.editor.getFormComponent());
		fieldLabel.setOutputMarkupId(true);
		return fieldLabel;
	}
	
	protected Component createMandatorySymbol(String wicketId) {
		Label requiredLabel = new Label(wicketId, Model.of(""));
		requiredLabel.setOutputMarkupId(true);
		requiredLabel.setVisible(this.fieldDTO.isRequired());
		return requiredLabel;
	}
	
	@Override
	public final void addEventListener(IAjaxEventListener eventListener) {
		this.eventListeners.add(eventListener);
	}
	
	@Override
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			this.eventListeners.addAll(eventListeners);
		}
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return Collections.unmodifiableList(this.eventListeners);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.setEnabled(this.fieldDTO.isEnabled());
	}
	
	public FormComponent<?> getFormComponent() {
		return this.editor.getFormComponent();
	}
}
