package edu.planon.lib.client.common.behavior;

import java.util.List;

public interface IAjaxEventSource  {
	public void addEventListener(IAjaxEventListener eventListener);
	public void addEventListener(List<IAjaxEventListener> eventListeners);
	public List<IAjaxEventListener> getEventListeners();
}
