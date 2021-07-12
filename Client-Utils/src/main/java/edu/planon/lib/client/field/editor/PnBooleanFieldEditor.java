package edu.planon.lib.client.field.editor;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.common.behavior.PnChoiceComponentUpdatingBehavior;
import edu.planon.lib.client.common.dto.PnFieldDTO;

public class PnBooleanFieldEditor extends AbstractPnFieldEditor {
	private static final long serialVersionUID = 1L;
	private PnChoiceComponentUpdatingBehavior fieldBehavior;
	private RadioGroup<Boolean> editor;
	
	public PnBooleanFieldEditor(String id, PnFieldDTO<Boolean> fieldDTO) {
		super(id, fieldDTO);
		
		this.setOutputMarkupId(true);
		
		this.editor = this.createEditor("radioGroup", fieldDTO);
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		this.editor.setRequired(fieldDTO.isRequired());
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnChoiceComponentUpdatingBehavior();
		this.fieldBehavior.addEventListener((event, sourceComponent, target) -> {
			target.add(this.editor);
		});
		this.editor.add(this.fieldBehavior);
		this.setEventSource(this.fieldBehavior);
		
		this.add(this.editor);
	}
	
	protected RadioGroup<Boolean> createEditor(String wicketId, PnFieldDTO<Boolean> fieldDTO) {
		RadioGroup<Boolean> group = new RadioGroup<Boolean>(wicketId, fieldDTO.getValueModel());
		group.setOutputMarkupId(true);
		group.setRenderBodyOnly(false);
		
		Radio<Boolean> radioYes = new Radio<Boolean>("radioYes", Model.of(Boolean.TRUE));
		radioYes.setOutputMarkupId(true);
		radioYes.setLabel(Model.of("Yes"));
		group.add(new SimpleFormComponentLabel("radioYesLabel", radioYes));
		group.add(radioYes);
		
		Radio<Boolean> radioNo = new Radio<Boolean>("radioNo", Model.of(Boolean.FALSE));
		radioNo.setOutputMarkupId(true);
		radioNo.setLabel(Model.of("No"));
		group.add(new SimpleFormComponentLabel("radioNoLabel", radioNo));
		group.add(radioNo);
		
		return group;
	}
	
	@Override
	public String getClassAttribute() {
		return super.getClassAttribute() + " booleanFieldValueEditor";
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
