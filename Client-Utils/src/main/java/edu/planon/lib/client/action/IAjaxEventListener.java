package edu.planon.lib.client.action;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxEventListener extends Serializable {
	public void onEvent(String event, final AjaxRequestTarget target);
}
