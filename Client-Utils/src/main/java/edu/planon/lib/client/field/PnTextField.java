package edu.planon.lib.client.field;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.dto.PnFieldDTO;

public class PnTextField<T extends Serializable> extends FieldListItem<T> {
	private static final long serialVersionUID = 1L;
	private PnComponentUpdatingBehavior fieldBehavior;
	private TextField<T> editor;
	
	public PnTextField(String id, PnFieldDTO<T> fieldDTO) {
		this(id, fieldDTO, InputType.TEXT);
	}
	
	public PnTextField(String id, PnFieldDTO<T> fieldDTO, InputType inputType) {
		super(id, fieldDTO);
		
		this.setOutputMarkupId(true);
		
		this.editor = new TextField<T>("editor", fieldDTO.getValueModel(), fieldDTO.getDataType());
		this.editor.setOutputMarkupId(true);
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		
		this.editor.add(new AttributeModifier("maxlength", new PropertyModel<String>(fieldDTO, "inputLength")));
		this.editor.setRequired(fieldDTO.isRequired());
		
		//change the type if needed
		if (!inputType.equals(InputType.TEXT)) {
			this.editor.add(new AttributeModifier("type", inputType.getTypeAttribute()));
		}
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnComponentUpdatingBehavior("blur");
		this.fieldBehavior.addEventListener(new IAjaxEventListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(String event, Component sourceComponent, AjaxRequestTarget target) {
				target.add(editor);
			}
		});
		this.editor.add(this.fieldBehavior);
		
		this.add(this.editor);
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return editor;
	}
	
	@Override
	public void addEventListener(IAjaxEventListener eventListener) {
		this.fieldBehavior.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.fieldBehavior.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return this.fieldBehavior.getEventListeners();
	}
	
	public enum InputType {
		TEXT("text"), NUMBER("number"), HIDDEN("hidden"), PASSWORD("password");
		
		private final String typeAttribute;
		
		private InputType(String typeAttribute) {
			this.typeAttribute = typeAttribute;
		}
		
		public String getTypeAttribute() {
			return this.typeAttribute;
		}
	}
}
