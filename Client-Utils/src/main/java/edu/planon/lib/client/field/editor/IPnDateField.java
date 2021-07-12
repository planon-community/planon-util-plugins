package edu.planon.lib.client.field.editor;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;

public interface IPnDateField {
	public ModalWindow getPopupWindow();
	
	public Date getMinDate();
	
	public Date getMaxDate();
	
	public List<IAjaxEventListener> getEventListeners();
	
	public PnFieldDefDTO getFieldDefDTO();
	
	public void showError(AjaxRequestTarget target, String error);
}
