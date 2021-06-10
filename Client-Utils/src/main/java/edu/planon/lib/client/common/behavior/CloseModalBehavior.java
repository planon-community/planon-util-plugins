package edu.planon.lib.client.common.behavior;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class CloseModalBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners = new ArrayList<IAjaxEventListener>();
	
	public CloseModalBehavior(String event) {
		super(event);
		
		eventListeners.add(new IAjaxEventListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(String event, Component sourceComponent, AjaxRequestTarget target) {
				ModalWindow.closeCurrent(target);
			}
		});
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		if (this.eventListeners != null && !this.eventListeners.isEmpty()) {
			String eventName = getEvent();
			for (IAjaxEventListener listener : this.eventListeners) {
				listener.onEvent(eventName, this.getComponent(), target);
			}
		}
	}
	
	public final void addEventListener(IAjaxEventListener eventListener) {
		this.eventListeners.add(eventListener);
	}
	
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			this.eventListeners.addAll(eventListeners);
		}
	}
	
	public List<IAjaxEventListener> getEventListeners() {
		return this.eventListeners;
	}
}
