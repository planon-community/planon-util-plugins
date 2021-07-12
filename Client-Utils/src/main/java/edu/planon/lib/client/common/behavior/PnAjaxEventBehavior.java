package edu.planon.lib.client.common.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;

public class PnAjaxEventBehavior extends AjaxEventBehavior implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private List<IAjaxEventListener> eventListeners;
	private boolean onBeforeCSS = true;
	private boolean onCompleteCSS = true;
	private boolean preventDefault;
	
	public PnAjaxEventBehavior(String event) {
		this(event, false);
	}
	
	public PnAjaxEventBehavior(String event, boolean preventDefault) {
		super(event);
		this.preventDefault = preventDefault;
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
	
	public static PnAjaxEventBehavior onEvent(String eventName, IAjaxEventListener onEvent) {
		return new PnAjaxEventBehavior(eventName, true) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onEvent.onEvent(eventName, this.getComponent(), target);
			}
		};
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.setChannel(PnAjaxEventBehavior.getBlockingAjaxChannel());
		attributes.getDynamicExtraParameters()
				.add("return {'ctrl' : attrs.event.ctrlKey, 'shift' : attrs.event.shiftKey, 'type' : attrs.event.type, 'detail' : attrs.event.detail}");
		
		String markupId = this.getComponent().getMarkupId();
		
		String beforeComponents = "$('#" + markupId + "').prop('disabled',true).addClass('button-disabled');";
		String completeComponents = "$('#" + markupId + "').prop('disabled',false).removeClass('button-disabled');";
		
		if (this.onBeforeCSS && this.onCompleteCSS) {
			attributes.getAjaxCallListeners().add(new AjaxCallListener().onBefore(beforeComponents).onComplete(completeComponents));
		}
		else if (this.onBeforeCSS) {
			attributes.getAjaxCallListeners().add(new AjaxCallListener().onBefore(beforeComponents));
		}
		else if (this.onCompleteCSS) {
			attributes.getAjaxCallListeners().add(new AjaxCallListener().onComplete(completeComponents));
		}
		
		attributes.setPreventDefault(this.preventDefault);
		attributes.setEventPropagation(AjaxRequestAttributes.EventPropagation.STOP);
	}
	
	public boolean isOnBeforeCSS() {
		return this.onBeforeCSS;
	}
	
	public void setOnBeforeCSS(boolean onBeforeCSS) {
		this.onBeforeCSS = onBeforeCSS;
	}
	
	public boolean isOnCompleteCSS() {
		return this.onCompleteCSS;
	}
	
	public void setOnCompleteCSS(boolean onCompleteCSS) {
		this.onCompleteCSS = onCompleteCSS;
	}
	
	public static AjaxChannel getBlockingAjaxChannel() {
		return new AjaxChannel("blocking", AjaxChannel.Type.ACTIVE);
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
