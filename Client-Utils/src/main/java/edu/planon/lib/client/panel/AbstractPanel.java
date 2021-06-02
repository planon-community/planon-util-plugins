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
		this.add(this.popupWindow);
		this.addResultWindowCloseButtonListner();
	}
	
	private void addResultWindowCloseButtonListner() {
		this.popupWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				ModalWindow.closeCurrent(target);
				if (AbstractPanel.this.isClosePopup()) {
					ModalWindow.closeCurrent(target);
				}
				return false;
			}
		});
		this.popupWindow.setOutputMarkupId(true);
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
