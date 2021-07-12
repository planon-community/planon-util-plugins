package edu.planon.lib.client.recordlist.paging;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

import edu.planon.lib.client.recordlist.PnRecordListPanel;

public class PnAjaxPagingNavigationIncrementLink extends AjaxPagingNavigationIncrementLink {
	private static final long serialVersionUID = 1L;
	
	public PnAjaxPagingNavigationIncrementLink(String wicketId, IPageable pageable, int increment) {
		super(wicketId, pageable, increment);
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		super.onClick(target);
		PnRecordListPanel tablePanel = this.findParent(PnRecordListPanel.class);
		String tablePanelMarkupID = tablePanel != null ? tablePanel.getMarkupId() : "";
		target.appendJavaScript("if(PlanonWebClient.PnWebProxyPanel!=null)PlanonWebClient.PnWebProxyPanel.scrollToFirstSelection('" + tablePanelMarkupID
				+ "');");
		target.focusComponent(this);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("href", "javascript:;");
	}
}
