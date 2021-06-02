package edu.planon.lib.client.field;

import java.io.Serializable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.dto.PnFieldDTO;

public class PnTextField<T extends Serializable> extends FieldListItem<T> {
	private static final long serialVersionUID = 1L;
	private TextField<T> textField;
	
	public PnTextField(String id, PnFieldDTO<T> fieldDTO) {
		this(id, fieldDTO, InputType.TEXT);
	}
	
	public PnTextField(String id, PnFieldDTO<T> fieldDTO, InputType inputType) {
		super(id, fieldDTO);
		
		this.setOutputMarkupId(true);
		
		this.textField = new TextField<T>("textField", fieldDTO.getValueModel());
		this.textField.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		
		this.textField.add(new AttributeModifier("maxlength", new PropertyModel<String>(fieldDTO, "inputLength")));
		this.textField.setRequired(fieldDTO.isRequired());
		
		//change the type if needed
		if (!inputType.equals(InputType.TEXT)) {
			this.textField.add(new AttributeModifier("type", inputType.getTypeAttribute()));
		}
		
		// make the component update the model when the users clicks out of the input
		this.textField.add(new AjaxFormComponentUpdatingBehavior("blur") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(textField);
			}
		});
		
		this.textField.setOutputMarkupId(true);
		this.add(this.textField);
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return textField;
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
