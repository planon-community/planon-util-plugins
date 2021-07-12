package edu.planon.lib.client.field.editor;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnFieldDTO;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.field.editor.listener.PnDateFieldEditorLinkListener;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnDateFieldEditor extends AbstractPnFieldEditor implements IPnDateField {
	private static final long serialVersionUID = 1L;
	private PnFieldDTO<Date> fieldDTO;
	private DateTextField editor;
	private PnComponentUpdatingBehavior fieldBehavior;
	
	public PnDateFieldEditor(String wicketId, PnFieldDTO<Date> fieldDTO) {
		super(wicketId, fieldDTO);
		this.fieldDTO = fieldDTO;
		
		this.setOutputMarkupId(true);
		
		String datePattern;
		if (fieldDTO.getFieldType().equals(PnESValueType.DATE_NEUTRAL)) {
			datePattern = "MM/dd/yyyy";
		}
		else if (fieldDTO.getFieldType().equals(PnESValueType.TIME_NEUTRAL)) {
			datePattern = "HH:mm";
		}
		else {
			datePattern = "MM/dd/yyyy HH:mm";
		}
		
		this.editor = new DateTextField("fieldValueEditor", fieldDTO.getValueModel(), datePattern);
		this.editor.setOutputMarkupId(true);
		
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		this.editor.setRequired(fieldDTO.isRequired());
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnComponentUpdatingBehavior("blur");
		this.fieldBehavior.addEventListener((event, sourceComponent, target) -> {
			target.add(this.editor);
		});
		this.editor.add(this.fieldBehavior);
		this.setEventSource(this.fieldBehavior);
		
		AjaxIconLink refButton = this.getReferenceButton();
		refButton.addEventListener(new PnDateFieldEditorLinkListener(this, this.editor, 305));
		
		this.add(this.editor);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.getReferenceButton().setVisible(this.getParent().isEnabled());
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return this.editor;
	}
	
	@Override
	public boolean isEditorEnabled() {
		return this.editor.isEnabled();
	}
	
	@Override
	public Date getMinDate() {
		return this.fieldDTO.getMinValue();
	}
	
	@Override
	public Date getMaxDate() {
		return this.fieldDTO.getMaxValue();
	}
	
	@Override
	public PnFieldDefDTO getFieldDefDTO() {
		return this.fieldDTO;
	}
}
