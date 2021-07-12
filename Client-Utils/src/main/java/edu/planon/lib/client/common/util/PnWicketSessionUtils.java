package edu.planon.lib.client.common.util;

import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;

public class PnWicketSessionUtils {
	public static AjaxRequestTarget getAjaxRequestTarget() {
		Optional<AjaxRequestTarget> ajaxRequestTarget = RequestCycle.get().find(AjaxRequestTarget.class);
		return ajaxRequestTarget.isPresent() ? ajaxRequestTarget.get() : null;
	}
	
	public static void addComponentToAjaxRequestTarget(Component component) {
		if (component == null || component instanceof Page || component.findParent(Page.class) == null) {
			return;
		}
		AjaxRequestTarget requestTarget = getAjaxRequestTarget();
		if (requestTarget != null) {
			requestTarget.add(component);
		}
	}
	
	public static Locale getLocale() {
		return Session.get().getLocale();
	}
}
