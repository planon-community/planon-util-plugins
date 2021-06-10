package edu.planon.lib.client.field;

import java.util.Date;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import edu.planon.lib.client.common.behavior.IAjaxEventListener;
import edu.planon.lib.client.dto.PnFieldDefDTO;

public interface IPnDateField {
	public ModalWindow getPopupWindow();
	
	public Date getMinDate();
	
	public Date getMaxDate();
	
	public List<IAjaxEventListener> getEventListeners(); 
	
	public PnFieldDefDTO getFieldDefDTO();
	
	public void showError(AjaxRequestTarget target, String error);
}
