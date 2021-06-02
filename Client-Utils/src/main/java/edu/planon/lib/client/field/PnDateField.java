package edu.planon.lib.client.field;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import edu.planon.lib.client.action.IAjaxEventListener;
import edu.planon.lib.client.datepicker.PnDatePickerPanel;
import edu.planon.lib.client.dto.PnFieldDTO;

public class PnDateField extends FieldListItem<Date> {
	private static final long serialVersionUID = 1L;
	private IModel<Date> dateModel;
	private DateTextField editor;
	private final ModalWindow modalWindow;
	private AjaxLink<?> editorLink;
	
	public PnDateField(String id, PnFieldDTO<Date> fieldDTO) {
		super(id, fieldDTO);
		this.dateModel = fieldDTO.getValueModel();
		this.setOutputMarkupId(true);
		
		this.modalWindow = new ModalWindow("modalWindow");
		this.modalWindow.setOutputMarkupId(true);
		this.add(this.modalWindow);
		
		this.editor = new DateTextField("editor", dateModel);
		this.editor.setLabel(new PropertyModel<String>(fieldDTO, "label"));
		this.editor.setRequired(fieldDTO.isRequired());
		
		// make the component update the model when the users clicks out of the input
		this.editor.add(new AjaxFormComponentUpdatingBehavior("blur") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(editor);
			}
		});
		
		this.editor.setOutputMarkupId(true);
		this.add(this.editor);
		
		this.editorLink = createEditorLink("pnicon-calendar");
	}
	
	private AjaxLink<?> createEditorLink(String iconCssClass) {
		@SuppressWarnings("rawtypes")
		AjaxLink editorLink = new AjaxLink("editorLink") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				openDateEditor(target);
			}
			
			@Override
			public MarkupContainer setDefaultModel(IModel model) {
				return super.setDefaultModel(model);
			}
		};
		editorLink.add(new AttributeAppender("class", iconCssClass, " "));
		editorLink.add(new AttributeAppender("class", "disabled", " ") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		});
		editorLink.setOutputMarkupId(true);
		
		this.add(editorLink);
		return editorLink;
	}
	
	private void openDateEditor(AjaxRequestTarget target) {
		Date tempDate = this.editor.getConvertedInput();
		if (tempDate == null) {
			tempDate = new Date();
		}
		
		PnDatePickerPanel datePickerPanel = new PnDatePickerPanel(this.modalWindow.getContentId(), dateModel);
		datePickerPanel.addUpdateListener(new IAjaxEventListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onEvent(String event, final AjaxRequestTarget target) {
				target.add(PnDateField.this.editor);
			}
		});
		
		//if (this.fieldDTO.getMinValue() != null) {
		//	datePickerPanel.setMinDate(this.fieldDTO.getMinValue());
		//}
		//if (this.fieldDTO.getMaxValue() != null) {
		//	datePickerPanel.setMaxDate(this.fieldDTO.getMaxValue());
		//}
		
		this.modalWindow.setResizable(false);
		this.modalWindow.setContent(datePickerPanel);
		this.modalWindow.setInitialWidth(275);
		this.modalWindow.setInitialHeight(305);
		this.modalWindow.setTitle(new PropertyModel<String>(fieldDTO, "label"));
		this.modalWindow.setVisible(true);
		this.modalWindow.setVisibilityAllowed(true);
		this.modalWindow.show(target);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.editorLink.setVisible(this.getParent().isEnabled());
	}
	
	public ModalWindow getModalWindow() {
		return this.modalWindow;
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return editor;
	}
}
