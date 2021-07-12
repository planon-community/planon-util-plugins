package edu.planon.lib.client.table.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.util.time.Duration;

import edu.planon.lib.client.common.behavior.PnAjaxEventBehavior;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;

public class PnTableRowAjaxClickBehavior extends AjaxEventBehavior implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners;
	
	public PnTableRowAjaxClickBehavior() {
		super("click");
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
	
	public static PnTableRowAjaxClickBehavior onEvent(IAjaxEventListener onEvent) {
		return new PnTableRowAjaxClickBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onEvent.onEvent(this.getEvent(), this.getComponent(), target);
			}
		};
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes aAttributes) {
		super.updateAjaxAttributes(aAttributes);
		aAttributes.setChannel(PnAjaxEventBehavior.getBlockingAjaxChannel());
		String id = "throttle-" + this.getComponent().getMarkupId();
		ThrottlingSettings throttlingSettings = new ThrottlingSettings(id, Duration.valueOf(200L));
		aAttributes.setThrottlingSettings(throttlingSettings);
		aAttributes.getDynamicExtraParameters().add("return {'ctrlKey' : attrs.event.ctrlKey || attrs.event.metaKey, 'shiftKey' : attrs.event.shiftKey}");
		aAttributes.getAjaxCallListeners().add(new AjaxCallListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public CharSequence getPrecondition(Component aComponent) {
				return "return (!$('#' + attrs.c).hasClass('selected') || attrs.event.ctrlKey || attrs.event.metaKey || attrs.event.shiftKey || $('#' + attrs.c).closest('.proxyList').find('tr.selected').length > 1)";
			}
		});
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
