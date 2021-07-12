package edu.planon.lib.client.common.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;

public class PnChoiceComponentUpdatingBehavior extends AjaxFormChoiceComponentUpdatingBehavior implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners;
	
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		if (this.eventListeners != null && !this.eventListeners.isEmpty()) {
			String eventName = this.getEvent();
			for (IAjaxEventListener listener : this.eventListeners) {
				listener.onEvent(eventName, this.getComponent(), target);
			}
		}
	}
	
	private void initListeners() {
		if (this.eventListeners == null) {
			this.eventListeners = new ArrayList<IAjaxEventListener>();
		}
	}
	
	@Override
	public final void addEventListener(IAjaxEventListener eventListener) {
		this.initListeners();
		this.eventListeners.add(eventListener);
	}
	
	@Override
	public final void addEventListener(List<IAjaxEventListener> eventListeners) {
		if (eventListeners != null && !eventListeners.isEmpty()) {
			this.initListeners();
			this.eventListeners.addAll(eventListeners);
		}
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		this.initListeners();
		return Collections.unmodifiableList(this.eventListeners);
	}
}
