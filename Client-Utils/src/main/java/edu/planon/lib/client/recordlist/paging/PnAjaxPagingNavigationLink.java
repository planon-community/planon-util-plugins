package edu.planon.lib.client.recordlist.paging;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

import edu.planon.lib.client.common.behavior.PnAjaxEventBehavior;
import edu.planon.lib.client.recordlist.PnRecordListPanel;

public class PnAjaxPagingNavigationLink extends AjaxPagingNavigationLink {
	private static final long serialVersionUID = 1L;
	
	public PnAjaxPagingNavigationLink(String wicketId, IPageable pageable, long pageNumber) {
		super(wicketId, pageable, pageNumber);
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		super.onClick(target);
		PnRecordListPanel tablePanel = this.findParent(PnRecordListPanel.class);
		if (tablePanel != null) {
			String tablePanelMarkupID = tablePanel.getMarkupId();
			target.appendJavaScript("if(PlanonWebClient.PnWebProxyPanel!=null)PlanonWebClient.PnWebProxyPanel.scrollToFirstSelection('" + tablePanelMarkupID
					+ "');");
			target.appendJavaScript("if(PlanonWebClient.PnWebProxyPanelMouseHover!=null)PlanonWebClient.PnWebProxyPanelMouseHover.initializeMouseHoverListeners('#"
					+ tablePanelMarkupID + "');");
			target.appendJavaScript("$('.page-link[disabled=disabled]').focus();");
		}
	}
	
	@Override
	protected void onComponentTag(ComponentTag aTag) {
		super.onComponentTag(aTag);
		aTag.put("href", "javascript:;");
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.setChannel(PnAjaxEventBehavior.getBlockingAjaxChannel());
	}
}
