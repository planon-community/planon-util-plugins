package edu.planon.lib.client.common.event;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

@FunctionalInterface
public interface IAjaxEventListener extends Serializable {
	public void onEvent(String event, Component sourceComponent, final AjaxRequestTarget target);
}
