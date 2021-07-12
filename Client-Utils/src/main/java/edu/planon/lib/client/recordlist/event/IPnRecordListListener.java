package edu.planon.lib.client.recordlist.event;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IPnRecordListListener extends Serializable {
	default public void onDeselection(AjaxRequestTarget target) {}
	
	default public void onClick(AjaxRequestTarget target) {}
}
