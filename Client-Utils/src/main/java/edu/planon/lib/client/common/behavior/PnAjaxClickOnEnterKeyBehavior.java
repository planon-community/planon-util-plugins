package edu.planon.lib.client.common.behavior;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

public class PnAjaxClickOnEnterKeyBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;
	private String target;
	
	public PnAjaxClickOnEnterKeyBehavior() {
		super("keydown");
	}
	
	public PnAjaxClickOnEnterKeyBehavior(String aTarget) {
		this();
		this.target = aTarget;
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		String script = "$(this).click();";
		
		if (this.target != null) {
			script = "$(this).find('" + this.target + "').click();";
		}
		
		AjaxCallListener listener = new AjaxCallListener();
		listener.onPrecondition("if (Wicket.Event.keyCode(attrs.event) === 13) {" + script + "} else { return false; } ");
		attributes.getAjaxCallListeners().add(listener);
	}
	
	@Override
	protected final void onEvent(AjaxRequestTarget aTarget) {}
}
