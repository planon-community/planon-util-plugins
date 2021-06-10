package edu.planon.lib.client.field.listener;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.datepicker.PnDatePickerPanel;
import edu.planon.lib.client.field.IPnDateField;

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
		PnDatePickerPanel datePickerPanel = new PnDatePickerPanel(this.pnDateField.getPopupWindow().getContentId(), this.editor.getModel());
		//Add support for time   //new PnDateTimePanel(this.pnDateField.getModalWindow().getContentId(), this.pnDateField.getUserLocale());
		
		datePickerPanel.addEventListener(new IAjaxEventListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onEvent(String event, Component sourceComponent, final AjaxRequestTarget target) {
				target.add(PnDateFieldEditorLinkListener.this.editor);
			}
		});
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
