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

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.common.behavior.IAjaxEventSource;
import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.behavior.PnCSSAttributeModifier;
import edu.planon.lib.client.dto.PnFieldDTO;
import edu.planon.lib.client.field.editor.AbstractPnFieldEditor;
import edu.planon.lib.client.field.editor.PnDateFieldEditor;
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
		initPanel();
	}
	
	private void initPanel() {
		this.container = new WebMarkupContainer("editorContainer");
		this.add(container);
		this.container.setOutputMarkupId(true);
		
		this.editor = createEditor("fieldEditor");
		this.editor.add(new PnCSSAttributeModifier("fieldEditor"));
		this.container.add(this.editor);
		
		this.container.add(new PnCSSAttributeAppender("editorDisabled") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !PnFieldPanel.this.getFieldEditor().isEditorEnabled();
			}
		});
		
		this.container.add(new PnCSSAttributeAppender("fieldValueEditor"));
		
		this.container.add(createLabel("fieldLabel"));
		this.container.add(createMandatorySymbol("mandatorySymbol"));
		
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
		PnESValueType fieldType = fieldDTO.getFieldType();
		Class<?> dataType = fieldDTO.getDataType();
		
		if(dataType.isAssignableFrom(Date.class)) {
			this.editor = new PnDateFieldEditor(wicketId, (PnFieldDTO<Date>)fieldDTO);
		}
		else if(fieldType.equals(PnESValueType.STRING)) {
			if (fieldDTO.isExtended() || fieldDTO.getInputLength() > 300) {
				//this.editor = new PnTextAreaField(wicketId, fieldDTO);
			}
			else {
				this.editor = new PnTextFieldEditor(wicketId, fieldDTO);
			}
		}
		else {
			this.editor = new PnTextFieldEditor(wicketId, fieldDTO);
			
		}
		
		this.editor.setOutputMarkupId(true);
		return this.editor;
		
		
		//Constructor<? extends AbstractPnFieldEditor> fieldValueEditorConstructor;
		//fieldEditorConstructor = aFieldValueEditor.getConstructor(String.class, PnFieldDTO.class);
		//this.editor =  fieldEditorConstructor.newInstance(wicketId, fieldDTO);
		
		
		
		/*if (fieldDTO.isExtended() || fieldDTO.getInputLength() > 300) {
			//this.editor = new PnTextAreaField(wicketId, fieldDTO);
		    } else if (fieldType.equals(PnESValueType.REFERENCE) || fieldType.equals(PnESValueType.STRING_REFERENCE)) {
		     // this.editor = new PnReferenceField(wicketId, fieldDTO);
		    } else if (fieldType.equals(PnESValueType.DATE_NEUTRAL) || fieldType.equals(PnESValueType.DATE_TIME) || fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL)) {
		      //this.editor = new PnDateField(wicketId, fieldDTO);
		    } else if (fieldType.equals(PnESValueType.BOOLEAN)) {
		     // this.editor = new PnBooleanField(wicketId, fieldDTO);
		    } else {
		     // this.editor = new PnTextFieldEditor(wicketId, fieldDTO);
		    }
		    */
		
		
		
		/*Class clazz;
		switch (fieldDTO.getFieldType()) {
			case ATTRIBUTES:
				PnWebAttributeSetFieldValueEditor.class
			case AUTOCAD_FILE:
				PnWebPathCADFieldValueEditor.class
			case BIG_DECIMAL:
				PnWebBigDecimalFieldValueEditor.class
			case BOOLEAN:
				clazz = PnWebBooleanFieldValueEditor.class;
				break;
			case DATABASE_QUERY:
				PnWebSCProxyListListFieldValueEditor.class
			case DATE_NEUTRAL:
				PnWebDateNeutralFieldValueEditor.class
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
				PnWebDateTimeNeutralFieldValueEditor.class
			case DOCUMENT_FILE:
				PnWebPathDocumentFieldValueEditor.class
			case GPS:
				PnWebGPSFieldValueEditor.class
			case IMAGE_FILE:
				PnWebImageFieldValueEditor.class
			case INTEGER:
				PnWebIntegerFieldValueEditor.class
			case PERIOD:
				PnWebPeriodFieldValueEditor.class
			case REFERENCE:
				PnWebAutoCompleteReferenceFieldValueEditor.class
			case SECUREDOCUMENT:
				PnWebSecureDocumentFieldValueEditor.class
			case STRING:
				PnWebStringFieldValueEditor.class
				PnWebTextAreaFieldValueEditor.class
			case STRING_REFERENCE:
				PnWebAutoCompleteStringLookupFieldValueEditor.class
			case TIME_NEUTRAL:
				PnWebTimeNeutralFieldValueEditor.class
			default:
				return null;
			
		}*/
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
		requiredLabel.setVisible(fieldDTO.isRequired());
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
		this.setEnabled(fieldDTO.isEnabled());
	}
	
	public FormComponent<?> getFormComponent() {
		return this.editor.getFormComponent();
	}
}
