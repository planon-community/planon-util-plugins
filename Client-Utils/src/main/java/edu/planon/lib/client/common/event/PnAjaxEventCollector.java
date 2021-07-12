package edu.planon.lib.client.common.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class PnAjaxEventCollector implements IAjaxEventSource, IAjaxEventListener {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners = new ArrayList<IAjaxEventListener>();
	
	public PnAjaxEventCollector() {}
	
	@Override
	public void onEvent(String event, Component sourceComponent, final AjaxRequestTarget target) {
		for (IAjaxEventListener listener : this.eventListeners) {
			listener.onEvent(event, sourceComponent, target);
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
