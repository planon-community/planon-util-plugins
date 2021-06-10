package edu.planon.lib.client.common.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;

import edu.planon.lib.client.common.behavior.IAjaxEventListener;

public class AjaxIconLink extends AjaxLink<Void> {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> listeners;
	
	public AjaxIconLink(String id, String iconName) {
		super(id);
		this.add(new AttributeAppender("class", iconName, " "));
		this.add(new AttributeAppender("class", "disabled", " ") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		});
		this.setOutputMarkupId(true);
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		if (this.listeners != null && !this.listeners.isEmpty()) {
			for (IAjaxEventListener listener : this.listeners) {
				listener.onEvent("click", this, target);
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
	
	public List<IAjaxEventListener> getEventListeners() {
		return this.listeners;
	}
	
}
