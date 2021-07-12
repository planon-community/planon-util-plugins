package edu.planon.lib.client.common.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;

public class CloseModalBehavior extends AjaxEventBehavior implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners = new ArrayList<IAjaxEventListener>();
	
	public CloseModalBehavior(String triggerEvent) {
		super(triggerEvent);
		
		this.eventListeners.add((event, sourceComponent, target) -> ModalWindow.closeCurrent(target));
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		if (this.eventListeners != null && !this.eventListeners.isEmpty()) {
			String eventName = this.getEvent();
			for (IAjaxEventListener listener : this.eventListeners) {
				listener.onEvent(eventName, this.getComponent(), target);
			}
		}
	}
	
	@Override
	public final void addEventListener(IAjaxEventListener eventListener) {
		this.eventListeners.add(eventListener);
	}
	
	@Override
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			this.eventListeners.addAll(eventListeners);
		}
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return Collections.unmodifiableList(this.eventListeners);
	}
}
