package edu.planon.lib.client.recordlist.paging;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class PnAjaxPagingNavigation extends AjaxPagingNavigation {
	private static final long serialVersionUID = 1L;
	
	public PnAjaxPagingNavigation(String wicketId, IPageable pageable, IPagingLabelProvider labelProvider) {
		super(wicketId, pageable, labelProvider);
	}
	
	@Override
	protected Link<?> newPagingNavigationLink(String wicketId, IPageable pageable, long pageIndex) {
		return new PnAjaxPagingNavigationLink(wicketId, pageable, pageIndex);
	}
}
