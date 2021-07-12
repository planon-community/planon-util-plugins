package edu.planon.lib.client.common.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;

public class AjaxIconLink extends AjaxLink<Void> implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners;
	
	public AjaxIconLink(String id, String iconName) {
		super(id);
		this.add(new PnCSSAttributeAppender(iconName));
		this.add(new PnCSSAttributeAppender("disabled") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		});
		this.setOutputMarkupId(true);
	}
	
	public static AjaxIconLink onClick(String id, String iconName, IAjaxEventListener onEvent) {
		return new AjaxIconLink(id, iconName) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				onEvent.onEvent("click", this, target);
			}
		};
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		if (this.eventListeners != null && !this.eventListeners.isEmpty()) {
			for (IAjaxEventListener listener : this.eventListeners) {
				listener.onEvent("click", this, target);
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
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.initListeners();
		this.eventListeners.addAll(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		this.initListeners();
		return Collections.unmodifiableList(this.eventListeners);
	}
}
