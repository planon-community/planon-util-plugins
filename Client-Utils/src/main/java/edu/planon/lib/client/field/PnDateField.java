package edu.planon.lib.client.field;

import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.ui.AjaxIconLink;
import edu.planon.lib.client.dto.PnFieldDTO;
import edu.planon.lib.client.field.listener.PnDateFieldEditorLinkListener;

public class PnDateField extends FieldListItem<Date> implements IPnDateField {
	private static final long serialVersionUID = 1L;
	private PnComponentUpdatingBehavior fieldBehavior;
	private IModel<Date> dateModel;
	private DateTextField editor;
	private AjaxIconLink editorLink;
	private Date minDate;
	private Date maxDate;
	
	public PnDateField(String id, PnFieldDTO<Date> fieldDTO) {
		super(id, fieldDTO);
		this.dateModel = fieldDTO.getValueModel();
		
		this.setOutputMarkupId(true);
		
		this.editor = new DateTextField("editor", dateModel);
		this.editor.setOutputMarkupId(true);
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		this.editor.setRequired(fieldDTO.isRequired());
		
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
		
		this.editorLink = new AjaxIconLink("editorLink", "pnicon-calendar");
		this.editorLink.addEventListener(new PnDateFieldEditorLinkListener(this, this.editor, 305));
	}
	
	public void setMinDate(Date date) {
		this.minDate = date;
	}
	
	public void setMaxDate(Date date) {
		this.maxDate = date;
	}
	
	@Override
	public Date getMinDate() {
		return this.minDate;
	}
	
	@Override
	public Date getMaxDate() {
		return this.maxDate;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.editorLink.setVisible(this.getParent().isEnabled());
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return editor;
	}
	
	
	@Override
	public final void addEventListener(IAjaxEventListener eventListener) {
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
}
