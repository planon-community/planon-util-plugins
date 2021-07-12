package edu.planon.lib.client.field.editor;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.dto.PnFieldDTO;

public class PnTextFieldEditor extends AbstractPnFieldEditor {
	private static final long serialVersionUID = 1L;
	private PnComponentUpdatingBehavior fieldBehavior;
	private FormComponent<?> editor;
	
	public PnTextFieldEditor(String id, PnFieldDTO<?> fieldDTO) {
		super(id, fieldDTO);
		
		this.setOutputMarkupId(true);
		
		this.editor = this.createEditor("fieldValueEditor", fieldDTO);
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		this.editor.setRequired(fieldDTO.isRequired());
		
		//change the type if needed
		if (Number.class.isAssignableFrom(fieldDTO.getDataType())) {
			this.editor.add(new AttributeModifier("type", "number"));
		}
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnComponentUpdatingBehavior("blur");
		this.fieldBehavior.addEventListener((event, sourceComponent, target) -> target.add(this.editor));
		this.editor.add(this.fieldBehavior);
		this.setEventSource(this.fieldBehavior);
		
		this.add(this.editor);
	}
	
	protected FormComponent<?> createEditor(String wicketId, PnFieldDTO<?> fieldDTO) {
		TextField<?> editor = new TextField<>(wicketId, fieldDTO.getDataType());
		editor.setDefaultModel(fieldDTO.getValueModel());
		editor.setOutputMarkupId(true);
		
		editor.add(new AttributeModifier("maxlength", new PropertyModel<String>(fieldDTO, "inputLength")));
		
		return editor;
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return this.editor;
	}
	
	@Override
	public boolean isEditorEnabled() {
		return this.editor.isEnabled();
	}
}
