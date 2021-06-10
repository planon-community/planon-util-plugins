package edu.planon.lib.client.common.behavior;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class PnAjaxEventBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> listeners;
	
	public PnAjaxEventBehavior(String event) {
		super(event);
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		if (this.listeners != null && !this.listeners.isEmpty()) {
			String eventName = getEvent();
			for (IAjaxEventListener listener : this.listeners) {
				listener.onEvent(eventName, this.getComponent(), target);
			}
		}
	}
	
	private void initListeners() {
		if (this.listeners == null) {
			this.listeners = new ArrayList<IAjaxEventListener>();
		}
	}
	
	public final void addEventListener(IAjaxEventListener eventListener) {
		this.initListeners();
		this.listeners.add(eventListener);
	}
	
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			this.initListeners();
			this.listeners.addAll(eventListeners);
		}
	}
	
	public List<IAjaxEventListener> getEventListeners() {
		return this.listeners;
	}
}
