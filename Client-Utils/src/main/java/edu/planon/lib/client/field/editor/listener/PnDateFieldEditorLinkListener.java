package edu.planon.lib.client.field.editor.listener;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.model.IModel;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.field.editor.IPnDateField;
import edu.planon.lib.client.panel.datepicker.PnDatePickerPanel;
import edu.planon.lib.client.panel.datepicker.PnDateTimePickerPanel;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnDateFieldEditorLinkListener implements IAjaxEventListener {
	private static final long serialVersionUID = 1L;
	private DateTextField editor;
	private final int initialHeight;
	private final IPnDateField pnDateField;
	
	public PnDateFieldEditorLinkListener(IPnDateField pnDateField, DateTextField editor, int initialHeight) {
		this.editor = editor;
		this.pnDateField = pnDateField;
		this.initialHeight = initialHeight;
	}
	
	public void onEvent(String event, Component sourceComponent, final AjaxRequestTarget target) {
		IModel<Date> dateModel = this.editor.getModel();
		Date initialDate = dateModel.getObject();
		if(initialDate == null) {
			dateModel.setObject(new Date());
		}
		
		PnDatePickerPanel datePickerPanel;
		if (this.pnDateField.getFieldDefDTO().getFieldType().equals(PnESValueType.DATE_NEUTRAL)) {
			datePickerPanel = new PnDatePickerPanel(this.pnDateField.getPopupWindow().getContentId(), dateModel, initialDate);
		}
		else {
			datePickerPanel = new PnDateTimePickerPanel(this.pnDateField.getPopupWindow().getContentId(), dateModel, initialDate);
		}
		
		datePickerPanel.addEventListener((event2, sourceComponent2, target2) -> target2.add(this.editor));
		datePickerPanel.addEventListener(this.pnDateField.getEventListeners());
		
		if (this.pnDateField.getMinDate() != null) {
			datePickerPanel.setMinDate(this.pnDateField.getMinDate());
		}
		if (this.pnDateField.getMaxDate() != null) {
			datePickerPanel.setMaxDate(this.pnDateField.getMaxDate());
		}
		
		this.pnDateField.getPopupWindow().setResizable(false);
		this.pnDateField.getPopupWindow().setContent(datePickerPanel);
		this.pnDateField.getPopupWindow().setInitialWidth(275);
		this.pnDateField.getPopupWindow().setInitialHeight(this.initialHeight);
		this.pnDateField.getPopupWindow().setTitle(this.pnDateField.getFieldDefDTO().getLabel());
		this.pnDateField.getPopupWindow().setVisible(true);
		this.pnDateField.getPopupWindow().setVisibilityAllowed(true);
		this.pnDateField.getPopupWindow().show(target);
	}
}
