package edu.planon.lib.client.action;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class CloseModalBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;
	
	public CloseModalBehavior(String event) {
		super(event);
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		ModalWindow.closeCurrent(target);
	}
	
}
