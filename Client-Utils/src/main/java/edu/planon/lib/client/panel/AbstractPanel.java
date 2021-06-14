package edu.planon.lib.client.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private boolean closePopup;
	private final ModalWindow popupWindow = new ModalWindow("popupWindow");
	
	public AbstractPanel(String id) {
		super(id);
		
		this.popupWindow.setCloseButtonCallback((target) -> {
			ModalWindow.closeCurrent(target);
			return false;
		});
		this.popupWindow.setWindowClosedCallback((target) -> {
			if (this.isClosePopup()) {
				ModalWindow.closeCurrent(target);
			}
		});
		this.popupWindow.setOutputMarkupId(true);
		
		this.add(this.popupWindow);
	}
	
	public boolean isClosePopup() {
		return this.closePopup;
	}
	
	public void setClosePopup(boolean aClosePopup) {
		this.closePopup = aClosePopup;
	}
	
	public void showError(AjaxRequestTarget target, Exception exception) {
		this.popupWindow.setContent(new ErrorPopup(this.popupWindow.getContentId(), exception));
		this.popupWindow.setTitle("Error");
		this.popupWindow.show(target);
	}
	
	public void showError(AjaxRequestTarget target, String error) {
		this.popupWindow.setContent(new ErrorPopup(this.popupWindow.getContentId(), error));
		this.popupWindow.setTitle("Error");
		this.popupWindow.show(target);
	}
	
	public ModalWindow getPopupWindow() {
		return this.popupWindow;
	}
}
